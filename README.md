#Compass

##功能介绍
Compass是搜狗商业平台研发部开发的一套轻量级的分布式数据库访问框架，支持单库、主从库读写分离、分库、分库之后再分表、从库负载均衡和HA等使用场景，并且在框架层面提供了主从反延迟策略。Compass采用Spring配置的方式与应用系统整合，能与目前主流的数据访问层框架Spring JdbcTemplate、MyBatis和Hibernate无缝集成，为应用系统提供透明的分布式数据访问服务。下图是Compass的功能点列表：

![function](https://github.com/sogou-biztech/compass/blob/master/function.jpg "function")


##工程结构
Compass分为4个子模块：
   * compass-core： Compass框架最核心的功能模块，提供指定路由键(RouteKey)的情况下的分布式数据库访问。
   * compass-delayfree：提供主从反延迟的策略的集成，如果不使用该功能可以不依赖。
   * compass-aggregation：提供有限程度的全库扫描、多库聚合和排序支持，如果不使用聚合功能可以不依赖。
   * compass-sample:提供配置和代码编写范例。

   
##整体架构
Compass整体架构图如下所示：

![arch](https://github.com/sogou-biztech/compass/blob/master/arch.jpg "arch")

####数据源切换预处理器
数据源切换预处理器提供了与Spring AOP的集成，通过拦截器的方式收集数据源切换所需的信息并保存到对象RouteContext之中，RouteContext包括分库路由键，主从库读写策略(决定本次访问应该读取主库还是从库)、service方法名、主从库反延时键。RouteContext会保存到当前线程的threadlocal之中，供下层组件使用，并且会在业务调用全部结束之后对当前线程的threadLocal之中的RouteContext进行清理。每个数据源需要配置单独的数据源切换预处理器，同时数据源切换预处理器需要配置在Spring的数据库事务拦截器之前。

####数据聚合层和JDBC封装层
在指定多个路由键以及不指定路由键的情况下提供有限程度的全库扫描、多库聚合和排序支持。

####动态数据源
对JDBC规范的DataSource、Connection、Statement等接口进行代理，分为单库数据源SingleDataSource、主从数据源MasterSlaveDataSource和分库数据源ShardDataSource这3种数据源，其中MasterSlaveDataSource包含一个或多个SingleDataSource，ShardDataSource包含一个或多个MasterSlaveDataSource，MasterSlaveDataSource根据数据源切换预处理器设置的信息实现主从库选择、故障探测和HA等功能。ShardDataSource则根据据数据源切换预处理器设置的信息实现分库和分表路由、Sql解析和表名替换等功能。


##框架工作流程
Compass框架工作流程如下图所示：

![flow](https://github.com/sogou-biztech/compass/blob/master/flow.jpg "flow")

1. 上层应用(比如Action层)对Service层的调用会被数据源切换预处理器所拦截，数据源切换预处理器收集分库路由键，主从库读写策略(决定本次访问应该读取主库还是从库)、service方法名、主从库反延时保存在threadLocal里面的RouteContext对象中，供后续步骤使用。

2. Service层调用Dao层的方法执行实际的数据库操作的时候，会通过Spring JdbcTemplate、MyBatis和Hibernate等数据访问层框架调用到Compass之中实现了JDBC规范的DataSource接口的ShardDataSource。

3. ShardDataSource根据保存在threadLocal中的RouteContext里面的分区路由键和用户配置的路由策略进行路由，定位出当前的操作应该路由到哪个分库的哪个分表上，路由结果的唯一标识在代码中用TableContext表示。分库在Compass之中由实现了JDBC规范的DataSource接口的MasterSlaveDataSource表示。注意如果在分库之后再进一步进行了分表的话，需要同时对这次操作涉及的SQL语句进行解析，根据路由结果标识TableContext将SQL语句中的逻辑表名替换为实际的物理分表的表名，拿用户账号表acount举例，如果用户输入的SQL语句为select * from account，同时定位到这个SQL应该路由到1号库的第8张分表，那么改写之后的SQL语句可能为select * from account_01_08。

4. ShardDataSource路由到某个分库MasterSlaveDataSource之后,MasterSlaveDataSource会进一步根据保存在threadLocal中的RouteContext里面的用户配置的读写策略和主从库反延时策略来进行主从选择。如果选择读取从库，那么需要进一步根据用户配置的负载均衡策略在多个从库之间进行负载均衡，同时如果用从库出现故障还需要及时探测到进行HA，MasterSlaveDataSource最终会选择出一个单库数据源SingleDataSource。

5. SingleDataSource同样实现了JDBC规范的DataSource接口，只是实际的连接池数据源(比如DBCP、C3P0等)的简单封装，SingleDataSource会将操作都委托给的连接池数据源进行实际的数据库访问。


#Compass

##功能介绍
Compass是搜狗商业平台研发部开发的一套轻量级的分布式数据库访问框架，支持单库、分库、分库之后再分表、读写分离、负载均衡和HA，并且在框架层面提供了主从反延迟策略。Compass采用Spring配置的方式与应用系统整合，能与目前主流的数据访问层框架Spring JdbcTemplate、MyBatis和Hibernate无缝集成，为应用系统提供透明的分布式数据访问服务。下图是Compass的功能点列表：

![function](https://github.com/sogou-biztech/compass/blob/master/function.jpg "function")


##工程结构
Compass分为4个子模块：
   * compass-core： Compass框架最核心的功能模块，提供指定路由键(RouteKey)的情况下的分布式数据库访问。
   * compass-delayfree：提供主从反延迟的策略的集成，如果不使用该功能可以不依赖。
   * compass-aggregation：提供有限程度的全库扫描、多库聚合和排序支持，如果不使用聚合功能可以不依赖。
   * compass-sample:提供配置和代码编写范例。

   
##整体架构
Compass整体架构图如下所示：


####数据源切换预处理器
数据源切换预处理器提供了与Spring AOP的集成，通过拦截器的方式收集数据源切换所需的对象RouteContext，RouteContext包括分库路由键，主从库读写模式(决定本次访问应该读取主库还是从库)、service方法名、主从库延时键。RouteContext会保存到当前线程的threadlocal之中，供下层组件使用，并且会在业务调用全部结束之后对当前线程的threadLocal之中的RouteContext进行清理。每个数据源需要配置单独的数据源切换预处理器。

####数据聚合层和JDBC封装层
在指定多个路由键以及不指定路由键的情况下提供有限程度的全库扫描、多库聚合和排序支持。

####动态数据源
对JDBC规范的DataSource、Connection、Statement等接口进行代理，分为单库数据源SingleDataSource、主从数据源MasterSlaveDataSource和分库数据源ShardDataSource这3种数据源，其中MasterSlaveDataSource包含一个或多个SingleDataSource，ShardDataSource包含一个或多个MasterSlaveDataSource，MasterSlaveDataSource根据数据源切换预处理器设置的信息实现主从库选择、故障探测和HA等功能。ShardDataSource则根据据数据源切换预处理器设置的信息实现分库和分表路由、Sql解析和表名替换等功能。






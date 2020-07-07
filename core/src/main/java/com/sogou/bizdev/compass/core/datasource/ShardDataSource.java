package com.sogou.bizdev.compass.core.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;
import com.sogou.bizdev.compass.core.proxyconnection.ShardDataSourceProxyConnection;
import com.sogou.bizdev.compass.core.router.ShardDataSourceRouter;
import com.sogou.bizdev.compass.core.router.TableContext;
import com.sogou.bizdev.compass.core.router.strategy.ShardDataSourceRouteStrategy;
import com.sogou.bizdev.compass.core.sqlinterceptor.DefaultSqlInterceptor;
import com.sogou.bizdev.compass.core.sqlinterceptor.SqlInterceptor;

/**
 * 分库数据源
 *
 * @author gly
 * @since 1.0.0
 */
public class ShardDataSource extends AbstractDataSource implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 分库路由器
     */
    private final ShardDataSourceRouter shardDataSourceRouter = new ShardDataSourceRouter(this);

    /**
     * sql处理拦截器(含SQL parser+SQL替换),默认值为DefaultSqlInterceptor
     */
    private SqlInterceptor sqlInterceptor = new DefaultSqlInterceptor();

    /**
     * 分库数据源id(shard01~shard0x),与用户配置的顺序一致
     */
    private List<String> masterSlaveDataSourceIds;

    /**
     * 分库数据源<shard0x,MasterSlaveDataSource>
     */
    private Map<String, MasterSlaveDataSource> idToMasterSlaveDataSource;

    private final ShardDataSourceContext shardDataSourceContext = new ShardDataSourceContext() {
        @Override
        public String getShardDataSourceId() {
            return ShardDataSource.this.getId();
        }

        @Override
        public List<String> getMasterSlaveDataSourceIds() {
            return ShardDataSource.this.getMasterSlaveDataSourceIds();
        }

        @Override
        public Map<String, MasterSlaveDataSource> getIdToMasterSlaveDataSource() {
            return ShardDataSource.this.getIdToMasterSlaveDataSource();
        }
    };

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(this.getId())) {
            throw new IllegalArgumentException("id is null");
        }

        if (CollectionUtils.isEmpty(this.masterSlaveDataSourceIds)) {
            throw new IllegalArgumentException("masterSlaveDataSourceIds is null");
        }

        if (CollectionUtils.isEmpty(this.idToMasterSlaveDataSource)) {
            throw new IllegalArgumentException("idToMasterSlaveDataSource is null");
        }

        if (this.idToMasterSlaveDataSource.size() != this.masterSlaveDataSourceIds.size()) {
            throw new IllegalArgumentException("masterSlaveDataSourceIds and idToMasterSlaveDataSource size are not equal");
        }

        if (this.shardDataSourceRouter.getShardDataSourceRouteStrategy() == null) {
            throw new IllegalArgumentException("shardDataSourceRouteStrategy is null");
        }
    }

    /**
     * 获取分库代理数据库连接
     *
     * @see javax.sql.DataSource#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = new ShardDataSourceProxyConnection(this);
        if (logger.isDebugEnabled()) {
            logger.debug("get ShardProxyConnection from ShardDataSource: [{}], return: [{}]", this.getId(), connection);
        }
        return connection;
    }

    /**
     * 获取分库代理数据库连接(用户名,密码)
     *
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     */
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        Connection connection = new ShardDataSourceProxyConnection(this, username, password);
        if (logger.isDebugEnabled()) {
            logger.debug("get ShardProxyConnection from ShardDataSource: [{}], username: [{}], password: [{}], " +
                "return: [{}]", this.getId(), username, password, connection);
        }
        return connection;
    }

    /**
     * 根据masterSlaveDataSourceId获取分库的数据源
     *
     * @param masterSlaveDataSourceId
     * @return
     */
    public MasterSlaveDataSource getMasterSlaveDataSourceById(String masterSlaveDataSourceId) {
        return this.idToMasterSlaveDataSource.get(masterSlaveDataSourceId);
    }

    /**
     * 从threadLocal里面取得routeKey进行路由,主要被proxyConnection使用,获取分库数据源(主从库或者单库),
     * 如果取不到routeKey的话就返回null
     */
    public TableContext route() {
        return this.shardDataSourceRouter.route();
    }

    /**
     * 指定routeKey的路由,与threadLocal无关,主要被聚合框架使用,获取分库数据源
     *
     * @param routeKey
     * @return 如果路由的TableContext为null或者TableContext的masterSlaveDataSourceId不存在, 会抛出BadRouteException
     */
    public TableContext route(Object routeKey) {
        return this.shardDataSourceRouter.route(routeKey);
    }

    /**
     * 获取当前主从库的线程池统计信息
     *
     * @return
     */
    @Override
    public List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics() {
        List<DataSourceConnectionStatistic> connectionStatistics = new ArrayList<DataSourceConnectionStatistic>();
        try {
            for (Map.Entry<String, MasterSlaveDataSource> entry : idToMasterSlaveDataSource.entrySet()) {
                MasterSlaveDataSource masterSlaveDataSource = entry.getValue();
                // 分库监控
                connectionStatistics.addAll(masterSlaveDataSource.getDataSourceConnectionStatistics());

            }
        } catch (Exception e) {
            logger.error("can't get DataSource Connection Statistics from shardDataSource={}", this.getId(), e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("get DataSource Connection Statistics from shardDataSource={}, result={}",
                this.getId(), connectionStatistics);
        }
        return connectionStatistics;
    }

    /**
     * 列举出所有的库表id，用于数据聚合层查询,
     *
     * @return 如果用户返回的TableContexts列表为空或者某个TableContext为null, 会抛出异常
     */
    public Map<String, List<TableContext>> listTableContext() {
        return getShardDataSourceRouter().listTableContext();
    }

    /**
     * 获得ShardDataSourceContext供用户使用
     *
     * @return
     */
    public ShardDataSourceContext getShardDataSourceContext() {
        return this.shardDataSourceContext;
    }

    public Map<String, MasterSlaveDataSource> getIdToMasterSlaveDataSource() {
        return idToMasterSlaveDataSource;
    }

    public static final String MASTER_SLAVE_DATA_SOURCES_PROPERTY_NAME = "masterSlaveDataSources";

    /**
     * 用户必须要设置
     *
     * @param masterSlaveDataSources 不能为空
     */
    public void setMasterSlaveDataSources(List<MasterSlaveDataSource> masterSlaveDataSources) {
        if (CollectionUtils.isEmpty(masterSlaveDataSources)) {
            throw new IllegalArgumentException("masterSlaveDataSources is null");
        }

        this.idToMasterSlaveDataSource = new HashMap<String, MasterSlaveDataSource>();
        this.masterSlaveDataSourceIds = new ArrayList<String>(masterSlaveDataSources.size());
        for (MasterSlaveDataSource masterSlaveDataSource : masterSlaveDataSources) {
            String masterSlaveDataSourceId = masterSlaveDataSource.getId();
            if (!StringUtils.hasText(masterSlaveDataSourceId)) {
                throw new IllegalArgumentException("shardDataSourceId: [" + this.getId()
                    + "], has masterSlaveDataSourceId null");
            }
            if (this.idToMasterSlaveDataSource.containsKey(masterSlaveDataSourceId)) {
                throw new IllegalArgumentException("shardDataSourceId: [" + this.getId()
                    + "], masterSlaveDataSourceId: [" + masterSlaveDataSourceId + "] repeat");
            }
            this.idToMasterSlaveDataSource.put(masterSlaveDataSourceId, masterSlaveDataSource);
            this.masterSlaveDataSourceIds.add(masterSlaveDataSourceId);
        }
    }

    public List<String> getMasterSlaveDataSourceIds() {
        return masterSlaveDataSourceIds;
    }

    public ShardDataSourceRouter getShardDataSourceRouter() {
        return shardDataSourceRouter;
    }

    public static final String SHARD_DATA_SOURCE_ROUTE_STRATEGY_PROPERTY_NAME = "shardDataSourceRouteStrategy";

    /**
     * 用户必须设置
     *
     * @param shardDataSourceRouterStrategy
     */
    public void setShardDataSourceRouteStrategy(ShardDataSourceRouteStrategy shardDataSourceRouterStrategy) {
        if (shardDataSourceRouterStrategy == null) {
            throw new IllegalArgumentException("shardDataSourceRouterStrategy is null");
        }
        if (this.shardDataSourceRouter.getShardDataSourceRouteStrategy() != null) {
            throw new IllegalArgumentException("ShardDataSource: [" + this.getId()
                + "], shardDataSourceRouterStrategy can not set repeat");
        }
        this.shardDataSourceRouter.setShardDataSourceRouteStrategy(shardDataSourceRouterStrategy);
    }

    public SqlInterceptor getSqlInterceptor() {
        return sqlInterceptor;
    }

    public static final String SQL_INTERCEPTOR_PROPERTY_NAME = "sqlInterceptor";

    /**
     * 允许用户不设置，默认为DefaultSqlInterceptor
     *
     * @param sqlInterceptor
     */
    public void setSqlInterceptor(SqlInterceptor sqlInterceptor) {
        if (sqlInterceptor == null) {
            throw new IllegalArgumentException("sqlInterceptor is null");
        }
        this.sqlInterceptor = sqlInterceptor;
    }

    public String intercept(String sql, TableContext tableContext) {
        return this.sqlInterceptor.intercept(sql, tableContext);
    }

    @Override
    public String toString() {
        return "ShardDataSource [id=" + this.getId()
            + ", shardDataSourceRouter=" + shardDataSourceRouter
            + ", sqlInterceptor=" + sqlInterceptor
            + ", masterSlaveDataSourceIds=" + masterSlaveDataSourceIds
            + ", idToMasterSlaveDataSource=" + idToMasterSlaveDataSource + "]";
    }

}

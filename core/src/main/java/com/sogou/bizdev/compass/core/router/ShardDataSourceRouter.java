package com.sogou.bizdev.compass.core.router;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSourceContext;
import com.sogou.bizdev.compass.core.exception.BadRouteException;
import com.sogou.bizdev.compass.core.preprocessor.RouteContext;
import com.sogou.bizdev.compass.core.router.strategy.ShardDataSourceRouteStrategy;

/**
 * 每一个ShardDataSource对应于一个ShardDataSourceRouter
 *
 * @author cl
 */
public class ShardDataSourceRouter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ShardDataSourceRouteStrategy shardDataSourceRouteStrategy;

    private final ShardDataSource shardDataSource;

    public ShardDataSourceRouter(ShardDataSource shardDataSource) {
        this.shardDataSource = shardDataSource;
    }

    public ShardDataSourceRouteStrategy getShardDataSourceRouteStrategy() {
        return shardDataSourceRouteStrategy;
    }

    public void setShardDataSourceRouteStrategy(ShardDataSourceRouteStrategy shardDataSourceRouterStrategy) {
        this.shardDataSourceRouteStrategy = shardDataSourceRouterStrategy;
    }

    /**
     * 从threadLocal里面取得routeKey进行路由,主要被proxyConnection使用,获取分库数据源(主从库或者单库),
     * 如果取不到routeKey的话就返回null
     */
    public TableContext route() {
        Object routeKey = this.getRouteKey();
        if (routeKey == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("get routeKey of {} null, route return null DataSource", shardDataSource.getId());
            }
            return null;
        }
        return route(routeKey);
    }

    /**
     * 指定routeKey的路由,与threadLocal无关,主要被聚合框架使用,获取分库数据源
     *
     * @param routeKey
     * @return 如果路由的TableContext为null或者TableContext的masterSlaveDataSourceId不存在, 会抛出BadRouteException
     */
    public TableContext route(Object routeKey) {
        if (routeKey == null) {
            throw new IllegalArgumentException("null routeKey");
        }

        String shardDataSourceId = shardDataSource.getId();
        TableContext tableContext = shardDataSourceRouteStrategy.route(routeKey, this.shardDataSource.getShardDataSourceContext());
        if (tableContext == null) {
            throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], routeKey: [" + routeKey
                + "], route tableContext is null");
        }

        String routedMasterSlaveDataSourceId = tableContext.getMasterSlaveDataSourceId();
        if (!StringUtils.hasText(routedMasterSlaveDataSourceId)) {
            throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], routeKey: [" + routeKey
                + "], routedMasterSlaveDataSourceId is null");
        }

        if (this.shardDataSource.getMasterSlaveDataSourceById(routedMasterSlaveDataSourceId) == null) {
            throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], routeKey: [" + routeKey
                + "], routedMasterSlaveDataSourceId: [" + routedMasterSlaveDataSourceId + "] is not exist");
        }
        return tableContext;
    }

    protected Object getRouteKey() {
        String shardDataSourceId = shardDataSource.getId();
        // 取预处理器的标识
        RouteContext routingContext = RouteContext.getCurrentRouteContext(shardDataSourceId);
        // 为了处理初始化时获取连接的情况，这里返回null，作为特殊情况外部调用者处理
        if (routingContext == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("get routingContext of ShardDataSource [{}] null", shardDataSourceId);
            }
            return null;
        }
        Object routeKey = routingContext.getRouteKey();
        if (routeKey == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("get routeKey of ShardDataSource [{}] null", shardDataSourceId);
            }
            return null;
        }

        return routeKey;
    }

    public Map<String, List<TableContext>> listTableContext() {
        Map<String, List<TableContext>> masterSlaveDataSourceIdToTableContexts = new HashMap<String, List<TableContext>>();

        List<String> masterSlaveDataSourceIds = this.shardDataSource.getMasterSlaveDataSourceIds();

        for (String masterSlaveDataSourceId : masterSlaveDataSourceIds) {
            List<TableContext> tableContexts = this.shardDataSourceRouteStrategy.getMasterSlaveDataSourceTableContexts(
                masterSlaveDataSourceId, this.getShardDataSourceContext());
            this.validateTableContexts(masterSlaveDataSourceId, tableContexts);
            masterSlaveDataSourceIdToTableContexts.put(masterSlaveDataSourceId, tableContexts);
        }

        return masterSlaveDataSourceIdToTableContexts;
    }

    private void validateTableContexts(String masterSlaveDataSourceId, List<TableContext> tableContexts) {
        String shardDataSourceId = this.shardDataSource.getId();
        if (CollectionUtils.isEmpty(tableContexts)) {
            throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], masterSlaveDataSourceId: ["
                + masterSlaveDataSourceId + "], getMasterSlaveDataSourceTableContexts return tableContexts is null");
        }

        for (TableContext tableContext : tableContexts) {
            if (tableContext == null) {
                throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], masterSlaveDataSourceId: ["
                    + masterSlaveDataSourceId + "], getMasterSlaveDataSourceTableContexts return tableContext is null");
            }
            String tableMasterSlaveDataSourceId = tableContext.getMasterSlaveDataSourceId();
            if (!StringUtils.hasText(tableMasterSlaveDataSourceId)) {
                throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], masterSlaveDataSourceId: ["
                    + masterSlaveDataSourceId + "], getMasterSlaveDataSourceTableContexts return " +
                    "tableMasterSlaveDataSourceId is null");
            }
            if (!tableMasterSlaveDataSourceId.equals(masterSlaveDataSourceId)) {
                throw new BadRouteException("ShardDataSource: [" + shardDataSourceId + "], masterSlaveDataSourceId: ["
                    + masterSlaveDataSourceId + "], getMasterSlaveDataSourceTableContexts return " +
                    "tableMasterSlaveDataSourceId: [" + tableMasterSlaveDataSourceId + "] is not equal masterSlaveDataSourceId");
            }
        }
    }

    protected ShardDataSourceContext getShardDataSourceContext() {
        return this.shardDataSource.getShardDataSourceContext();
    }

}

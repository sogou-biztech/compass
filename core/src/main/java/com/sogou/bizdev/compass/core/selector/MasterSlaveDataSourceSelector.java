package com.sogou.bizdev.compass.core.selector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.SingleDataSource;
import com.sogou.bizdev.compass.core.preprocessor.RouteContext;
import com.sogou.bizdev.compass.core.selector.loadbalance.LoadBalance;
import com.sogou.bizdev.compass.core.selector.loadbalance.WeightedRandom;

/**
 * 主从库选择器,与一个MasterSlaveDataSource绑定
 *
 * @author xr
 * @since 1.0.0
 */
public class MasterSlaveDataSourceSelector {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 负载均衡策略，注入方式初始化，用户可以不设置,默认随机
     */
    private LoadBalance loadbalance = new WeightedRandom();

    private volatile List<SingleDataSource> aliveSlaveDataSources;

    private final MasterSlaveDataSource masterSlaveDataSource;

    public MasterSlaveDataSourceSelector(MasterSlaveDataSource masterSlaveDataSource) {
        this.masterSlaveDataSource = masterSlaveDataSource;
    }

    public LoadBalance getLoadBalance() {
        return loadbalance;
    }

    public void setLoadBalance(LoadBalance loadbalance) {
        this.loadbalance = loadbalance;
    }

    public void setAliveSlaves(Map<String, SingleDataSource> aliveIdToSlaveDataSource) {
        if (aliveIdToSlaveDataSource == null) {
            return;
        }

        List<SingleDataSource> aliveSlaveDataSourcesToUse = new ArrayList<SingleDataSource>();
        for (String slaveDataSourceId : this.masterSlaveDataSource.getSlaveDataSourceIds()) {
            SingleDataSource aliveSlaveDataSource = aliveIdToSlaveDataSource.get(slaveDataSourceId);
            if (aliveSlaveDataSource != null) {
                aliveSlaveDataSourcesToUse.add(aliveSlaveDataSource);
            }
        }

        this.aliveSlaveDataSources = aliveSlaveDataSourcesToUse;
    }

    /**
     * 有可能返回空
     *
     * @return
     */
    public SingleDataSource select() {
        // 两种情况下给默认的主库：1、在只有分库，没有主从的情况下  2、如果没有可选从库
        List<SingleDataSource> aliveSlaveDataSourcesToUse = this.aliveSlaveDataSources;
        if (CollectionUtils.isEmpty(aliveSlaveDataSourcesToUse)) {
            if (logger.isInfoEnabled()) {
                logger.info("masterSlaveDataSource: [{}] no alive slave dataSource, select master dataSource: [{}]",
                    masterSlaveDataSource.getId(), this.getMasterDataSource().getId());
            }
            return this.getMasterDataSource();
        }
        // 取预处理器的标识
        RouteContext routeContext = RouteContext.getCurrentRouteContext(masterSlaveDataSource.getShardDataSourceId());
        // 为了处理初始化时获取连接的情况，这里返回null，作为特殊情况外部调用者处理
        if (routeContext == null) {
            if (logger.isWarnEnabled()) {
                logger.warn("masterSlaveDataSource: [{}], get RouteContext of shardDataSourceId: [{}] null",
                    masterSlaveDataSource.getId(), masterSlaveDataSource.getShardDataSourceId());
            }
            return null;
        }
        boolean masterMode = routeContext.isMasterMode();
        if (masterMode) {
            if (logger.isDebugEnabled()) {
                logger.debug("masterSlaveDataSource: [{}], select master dataSource: [{}]",
                    masterSlaveDataSource.getId(), this.getMasterDataSource().getId());
            }
            return this.getMasterDataSource();
        }
        return doSelect(aliveSlaveDataSourcesToUse);
    }

    protected SingleDataSource doSelect(List<SingleDataSource> aliveSlaveDataSourcesToUse) {
        SingleDataSource singleDataSource = loadbalance.select(aliveSlaveDataSourcesToUse);
        if (logger.isDebugEnabled()) {
            logger.debug("masterSlaveDataSource: [{}], select slave datasource: [{}]",
                masterSlaveDataSource.getId(), singleDataSource.getId());
        }
        return singleDataSource;
    }

    protected SingleDataSource getMasterDataSource() {
        return this.masterSlaveDataSource.getMasterDataSource();
    }

}

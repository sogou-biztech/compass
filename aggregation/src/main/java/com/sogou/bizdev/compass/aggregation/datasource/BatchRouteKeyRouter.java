package com.sogou.bizdev.compass.aggregation.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * 基于多个routeKey进行路由的Router
 * @author yk
 * @since 1.0.0
 */
public class BatchRouteKeyRouter 
{

    private ShardDataSource shardDataSource;

    public BatchRouteKeyRouter(ShardDataSource shardDataSource)
    {
        this.shardDataSource = shardDataSource;
    }

    /**
     * 
     * @param routeKeys
     * @return
     */
    public  Map<TableContext,Shard> route(List<Object> routeKeys)
    {
    	 Map<TableContext,Shard>  tableContextToShard=new HashMap<TableContext,Shard>();
    	 for (Object routeKey : routeKeys)
         {
         	 TableContext tableContext = shardDataSource.route(routeKey);
         	 Shard shard=tableContextToShard.get(tableContext);
         	 if(shard==null)
         	 {
         		DataSource targetDataSource=getTargetDataSource(tableContext);
         		shard=new Shard(targetDataSource,tableContext);
         		tableContextToShard.put(tableContext,shard);
         	 }
         	 shard.addRouteKey(routeKey);
             
         }
    	 
    	 return tableContextToShard;
    }
    
    
    
    protected DataSource getTargetDataSource(TableContext tableContext)
    {
    	String masterSlaveDataSourceId=tableContext.getMasterSlaveDataSourceId();
    	MasterSlaveDataSource masterSlaveDataSource=this.shardDataSource.getMasterSlaveDataSourceById(masterSlaveDataSourceId);
    	return this.selectTargetDataSource(masterSlaveDataSource);
    }

    public List<Shard> listAllShards() 
    {
    	List<Shard> shards = new ArrayList<Shard>();
        Map<String, List<TableContext>> masterSlaveDataSourceIdToTableContexts =  shardDataSource.listTableContext();

        for (Map.Entry<String, List<TableContext>> entry : masterSlaveDataSourceIdToTableContexts.entrySet()) 
        {
            String masterSlaveDataSourceId = entry.getKey();
            List<TableContext> tableContexts = entry.getValue();
            
            MasterSlaveDataSource masterSlaveDataSource = shardDataSource.getMasterSlaveDataSourceById(masterSlaveDataSourceId);
            DataSource targetDataSource = this.selectTargetDataSource(masterSlaveDataSource);

            for (TableContext tableContext : tableContexts)
            {
                Shard shard = new Shard(targetDataSource, tableContext);
                shards.add(shard);
            }
        }
        
        return shards;
    }
    
    /**
     * 从主从数据源中获取物理数据源，供ShardJdbcTemplate中进行多线程并发查询时使用
     * 
     * 由于ShardJdbcTemplate中会起新线程，在新起的线程中是取不到RoutingContext的
     * 因此在这里(主线程)先做取物理数据源这件事
     * 
     * @param masterSlaveDataSource
     * @return
     */
    private DataSource selectTargetDataSource(MasterSlaveDataSource masterSlaveDataSource)
    {
    	// 根据RoutingContext中的主从信息选择对应的数据源
    	DataSource target = masterSlaveDataSource.select();
    	
    	if (target == null) 
    	{
    		// 如果没有配置主从信息，那么强制走主数据源
    		target = masterSlaveDataSource.getMasterDataSource();
    	}
    	
    	return target;
    }


}

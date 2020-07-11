package com.sogou.bizdev.compass.core.proxyconnection;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;

import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.exception.BadRouteException;
import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * 分库代理Connection
 *
 * @author gly
 * @since 1.0.0
 */
public class ShardDataSourceProxyConnection extends AbstractProxyConnection {

    private final Random random = new Random();

    private ShardDataSource shardDataSource;

    private TableContext tableContext;

    public ShardDataSourceProxyConnection(ShardDataSource shardDataSource) {
        this.shardDataSource = shardDataSource;
    }

    public ShardDataSourceProxyConnection(ShardDataSource shardDataSource, String username, String password) {
        super(username, password);
        this.shardDataSource = shardDataSource;
    }

    /**
     * 获取真实数据源,可能返回null
     */
    @Override
    protected DataSource getPhysicalDataSource() {
        TableContext tableContextToUse = this.getOrCreateTableContext();
        if (tableContextToUse == null) {
            return null;
        }
        String masterSlaveDataSourceId = tableContextToUse.getMasterSlaveDataSourceId();
        if (!StringUtils.hasText(masterSlaveDataSourceId)) {
            throw new BadRouteException("ShardDataSource: [" + this.shardDataSource.getId()
                + "], TableContext masterSlaveDataSourceId is null");
        }
        return this.shardDataSource.getMasterSlaveDataSourceById(masterSlaveDataSourceId);
    }

    @Override
    protected String getTargetDataSourceNotFoundExceptionErrorMsg() {
        return "caused by shardDataSource: [" + this.shardDataSource.getId() + "], maybe shard route evidence is lost";
    }

    /**
     * 获取随机数据源(默认随机选择主从库，取其主库)
     */
    @Override
    protected DataSource getRandomDataSource() {
        Map<String, MasterSlaveDataSource> dataSourceMap = shardDataSource.getIdToMasterSlaveDataSource();
        return selectRandomMasterSlaveDataSource(dataSourceMap);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return super.prepareCall(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return super.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement stmt = super.createStatement();
        return new ShardDataSourceProxyStatement(stmt, this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        Statement stmt = super.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        return new ShardDataSourceProxyStatement(stmt, this);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement stmt = super.createStatement(resultSetType, resultSetConcurrency);
        return new ShardDataSourceProxyStatement(stmt, this);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
        int resultSetHoldability) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        sql = this.interceptSql(sql);
        return super.prepareStatement(sql, columnNames);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        sql = this.interceptSql(sql);
        return super.nativeSQL(sql);
    }

    public MasterSlaveDataSource selectRandomMasterSlaveDataSource(Map<String, MasterSlaveDataSource> targets) {
        if (targets == null || targets.isEmpty()) {
            return null;
        }
        Collection<MasterSlaveDataSource> dataSources = targets.values();
        MasterSlaveDataSource[] dataSourcesArray = dataSources.toArray(new MasterSlaveDataSource[dataSources.size()]);
        return dataSourcesArray[random.nextInt(dataSourcesArray.length)];
    }

    public String interceptSql(String sql) {
        TableContext tableContextToUse = this.getOrCreateTableContext();
        return this.shardDataSource.intercept(sql, tableContextToUse);
    }

    protected TableContext getOrCreateTableContext() {
        if (tableContext != null) { return tableContext; }
        return tableContext = shardDataSource.route();
    }

}

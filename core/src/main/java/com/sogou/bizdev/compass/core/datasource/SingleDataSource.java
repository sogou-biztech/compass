package com.sogou.bizdev.compass.core.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.datasource.retriever.DatabasePropertiesRetriever;
import com.sogou.bizdev.compass.core.datasource.retriever.DatabasePropertiesRetrieverRegistry;
import com.sogou.bizdev.compass.core.datasource.statistic.DataSourceConnectionStatistic;
import com.sogou.bizdev.compass.core.selector.loadbalance.Selectable;

public class SingleDataSource extends AbstractDataSource implements Selectable, InitializingBean {

    public static final int DEFAULT_WEIGHT = 1;

    private DataSource targetDataSource;
    private int weight = DEFAULT_WEIGHT;

    public DataSource getTargetDataSource() {
        return targetDataSource;
    }

    public static final String TARGET_DATA_SOURCE_PROPERTY_NAME = "targetDataSource";

    /**
     * 必须要设置
     *
     * @param targetDataSource
     */
    public void setTargetDataSource(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    public static final String WEIGHT_PROPERTY_NAME = "weight";

    public void setWeight(int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must > 0");
        }
        this.weight = weight;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return this.targetDataSource.getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.targetDataSource.getConnection(username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.targetDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.targetDataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.targetDataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.targetDataSource.getLoginTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.targetDataSource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.targetDataSource.isWrapperFor(iface);
    }

    @Override
    public List<DataSourceConnectionStatistic> getDataSourceConnectionStatistics() {
        DatabasePropertiesRetriever dbRetriever = this.getDatabasePropertiesRetriever();
        return Collections.singletonList(dbRetriever.getDataSourceConnectionStatistic(this.getId(), this.targetDataSource));
    }

    public DatabasePropertiesRetriever getDatabasePropertiesRetriever() {
        DatabasePropertiesRetriever dbRetriever = DatabasePropertiesRetrieverRegistry.get(this.targetDataSource.getClass());
        if (dbRetriever == null) {
            throw new IllegalArgumentException("Unable to find DatabasePropertiesRetriever according to class: ["
                + this.targetDataSource.getClass() + "]");
        }
        return dbRetriever;
    }

    public DatabaseType getDatabaseType() {
        DatabasePropertiesRetriever dbRetriever = this.getDatabasePropertiesRetriever();
        return dbRetriever.getDatabaseType(this.targetDataSource);
    }

    public String getDatabaseUrl() {
        DatabasePropertiesRetriever dbRetriever = this.getDatabasePropertiesRetriever();
        return dbRetriever.getDatabaseUrl(this.targetDataSource);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (!StringUtils.hasText(this.getId())) {
            throw new IllegalArgumentException("id is null");
        }

        if (this.targetDataSource == null) {
            throw new IllegalArgumentException("targetDataSource is null");
        }
    }

}

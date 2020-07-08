package com.sogou.bizdev.compass.aggregation.datasource;

import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * 分表信息，保存了主从数据源、目标物理数据源、分库分表信息（TableContext）
 *
 * @author yk
 * @since 1.0.0
 */
public class Shard {

    private DataSource targetDataSource;
    private TableContext tableContext;
    private List<Object> routeKeys = null;

    public Shard(DataSource targetDataSource, TableContext tableContext) {
        this.targetDataSource = targetDataSource;
        this.tableContext = tableContext;
    }

    public DataSource getTargetDataSource() {
        return targetDataSource;
    }

    public void setTargetDataSource(DataSource targetDataSource) {
        this.targetDataSource = targetDataSource;
    }

    public TableContext getTableContext() {
        return tableContext;
    }

    public void setTableContext(TableContext tableContext) {
        this.tableContext = tableContext;
    }

    public List<Object> getRouteKeys() {
        return routeKeys;
    }

    public void addRouteKey(Object routeKey) {
        if (CollectionUtils.isEmpty(this.routeKeys)) {
            this.routeKeys = new LinkedList<Object>();
        }
        this.routeKeys.add(routeKey);
    }

}

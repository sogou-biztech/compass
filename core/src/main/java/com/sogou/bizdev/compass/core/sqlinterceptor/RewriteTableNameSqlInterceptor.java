/**
 *
 */
package com.sogou.bizdev.compass.core.sqlinterceptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.sogou.bizdev.compass.core.router.TableContext;
import com.sogou.bizdev.compass.core.util.TableRenameUtil;
import com.sogou.bizdev.compass.core.util.TableRenameUtil.TableRenamer;

/**
 * @Description: 基于sql解析的sql拦截器，提供rewrite扩展点，子类可以根据自己的表名替换规则替换表名
 * @author zjc
 * @since 1.0.0
 */
public abstract class RewriteTableNameSqlInterceptor implements SqlInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    protected Set<String> excludedTables = Collections.<String>emptySet();

    @Override
    public String intercept(String sql, TableContext tableContext) {
        if (!StringUtils.hasText(sql)) {
            return sql;
        }

        String newSql = TableRenameUtil.modifyTableNames(sql, this.createTableRenamer(tableContext));
        if (logger.isDebugEnabled()) {
            logger.debug("sql intercept, {}, sql before: {}, after: {}", tableContext, sql, newSql);
        }
        return newSql;
    }

    protected TableRenamer createTableRenamer(final TableContext tableContext) {
        return new TableRenamer() {
            @Override
            public String rename(String oldTableName) {
                if (!StringUtils.hasText(oldTableName)) {
                    return oldTableName;
                }
                if (excludedTables.contains(oldTableName.toLowerCase())) {
                    return oldTableName;
                }
                return rewrite(oldTableName, tableContext);
            }
        };
    }

    /**
     * 重写rewrite方法，根据自定制规则对sql中的表名重写
     *
     * @param oldTableName
     * @param tc
     * @return
     * @see ModSqlInterceptor
     */
    public abstract String rewrite(String oldTableName, TableContext tc);

    public Set<String> getExcludedTables() {
        return excludedTables;
    }

    public void setExcludedTables(Set<String> originalExcludedTables) {
        if (CollectionUtils.isEmpty(originalExcludedTables)) {
            throw new IllegalArgumentException("excludedTables is null");
        }
        this.excludedTables = new HashSet<String>();
        for (String excludeTable : originalExcludedTables) {
            if (!StringUtils.hasText(excludeTable)) {
                throw new IllegalArgumentException("excludedTables must not has null table");
            }
            this.excludedTables.add(excludeTable.trim().toLowerCase());
        }
    }

}

/**
 *
 */
package com.sogou.bizdev.compass.core.sqlinterceptor;

import com.sogou.bizdev.compass.core.router.TableContext;

/**
 * @Description: 默认sql拦截器，原样返回
 * @author zjc
 * @since 1.0.0
 */
public class DefaultSqlInterceptor implements SqlInterceptor {
    @Override
    public String intercept(String sql, TableContext tc) {
        return sql;
    }
}

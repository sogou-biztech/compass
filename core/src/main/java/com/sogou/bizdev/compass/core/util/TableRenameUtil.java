package com.sogou.bizdev.compass.core.util;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * @Description: 基于JsqlParser的sql解析替换工具类
 * @author zjc
 * @since 1.0.0
 */
public class TableRenameUtil {
    public interface TableRenamer {
        public String rename(String oldTableName);
    }

    public static String modifyTableNames(String sql, TableRenamer tableRenamer) {
        if (sql == null) {
            throw new IllegalArgumentException("sql is null");
        }

        Statement statement = null;
        try {
            statement = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            throw new IllegalArgumentException("Error when parsing sql: [" + sql + "]", e);
        }

        TableRenameVisitor tableRenameVisitor = new TableRenameVisitor(tableRenamer);
        statement.accept(tableRenameVisitor);
        return statement.toString();
    }
}

package com.sogou.bizdev.compass.core.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.sogou.bizdev.compass.core.util.TableRenameUtil.TableRenamer;

public class TableRenameVisitorTest {

    private TableRenamer tableRenamer;
    private String suffix = "_01_01";

    @Before
    public void setUp() {
        this.tableRenamer = new TableRenamer() {
            @Override
            public String rename(String oldTableName) {
                return oldTableName + suffix;
            }
        };
    }

    @Test
    public void testUnionAll() {
        String sql = "(select * from cpcplan) union all (select * from cpc)";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals("(SELECT * FROM cpcplan_01_01) UNION ALL (SELECT * FROM cpc_01_01)", newSql);
    }

    /**
     * 测试insert select语句
     */
    @Test
    public void testInsertWithSelect() {
        String sql = "insert into cpcplan(id,name) select id,name from cpcplan";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "INSERT INTO cpcplan" + suffix + "(id,name) SELECT id,name FROM cpcplan" + suffix);
    }

    /**
     * 测试:name 参数注入
     */
    @Test
    public void testParameterSet() {
        String sql = "select * from cpcplan where name = :name";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT * FROM cpcplan" + suffix + " WHERE name = :name");
    }

    /**
     * 测试@dblink,测试结果是能识别，但是表名改不对
     */
    @Test
    public void testDBlink() {
        String sql = "select * from cpcplan@dblink where name = '12'";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT * FROM cpcplan@dblink" + suffix + " WHERE name = '12'");
    }

    /**
     * 测试test case语句
     */
    @Test
    public void testCaseElse() {
        String sql = "select CASE WHEN salary <= 500 THEN '1' WHEN salary > 500 AND salary <= 600 THEN '2' ELSE NULL " +
            "END salary_class from cpcplan ";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT CASE WHEN salary <= 500 THEN '1' WHEN salary > 500 AND salary <= 600 " +
            "THEN '2' ELSE NULL END salary_class FROM cpcplan" + suffix);
    }

    /**
     * 测试update as别名语句
     */
    @Test
    public void testUpdateTableWithAlia() {
        String sql = "update cpcplan as cp set cp.id=1";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "UPDATE cpcplan" + this.suffix + " AS cp SET cp.id = 1");
    }

    /**
     * 测试join (+)语句
     */
    @Test
    public void testJoin() {
        String sql = "select * from cpcplan t1, cpc t2 where t1.a<t2.a(+)";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT * FROM cpcplan" + suffix + " t1, cpc" + suffix + " t2 WHERE t1.a < t2.a(+)");
    }

    /**
     * 测试多个group字段语句
     */
    @Test
    public void testMultiGroupBy() {
        String sql = "select t.name,sum(t.price) from cpcplan t group by t.name,t.id";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT t.name, sum(t.price) FROM cpcplan" + suffix + " t GROUP BY t.name, t.id");
    }

    /**
     * 测试多列in语句
     */
    @Test
    public void testMultiIn() {
        String sql = "select t.name,sum(t.price) from cpcplan t where t.id in(1,2,3,4)";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT t.name, sum(t.price) FROM cpcplan" + suffix + " t WHERE t.id IN (1, 2, 3, 4)");
    }

    /**
     * 测试like escape语句
     */
    @Test
    public void testLikeEscape() {
        String sql = "select t.name  from cpcplan t where t.name like  'gs_'  ESCAPE   'S'";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT t.name FROM cpcplan" + suffix + " t WHERE t.name LIKE 'gs_' ESCAPE 'S'");
    }

    /**
     * 测试insert values多个语句
     */
    @Test
    public void testMultiValues() {
        String sql = "insert into cpcplan(id,name) values(1,'zhangsan1'),(2,'zhangsan2') ,(3,'zhangsan3')  ";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "INSERT INTO cpcplan" + suffix + " (id, name) VALUES (1, 'zhangsan1'), " +
            "(2, 'zhangsan2'), (3, 'zhangsan3')");
    }

    /**
     * 测试update多张表连接语句
     */
    @Test
    public void testMultiUpdate() {
        String sql = "update cpcplan t,cpc m set t.id=1 where t.id=m.id";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "UPDATE cpcplan" + suffix + " t, cpc" + suffix + " m SET t.id = 1 WHERE t.id = m.id");
    }

    /**
     * 测试左连接
     */
    @Test
    public void testLeftJoin() {
        String sql = "select t.id from cpcplan t left join cpc m on t.id=m.id";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT t.id FROM cpcplan" + suffix + " t LEFT JOIN cpc" + suffix + " m " +
            "ON t.id = m.id");
    }

    /**
     * 测试右连接
     */
    @Test
    public void testRightJoin() {
        String sql = "select t.id from cpcplan t right join cpc m on t.id=m.id";
        String newSql = TableRenameUtil.modifyTableNames(sql, this.tableRenamer);
        System.out.println(newSql);
        Assert.assertEquals(newSql, "SELECT t.id FROM cpcplan" + suffix + " t RIGHT JOIN cpc" + suffix + " m " +
            "ON t.id = m.id");
    }

    @Test
    public void testFormat() {
        String pattern = "_%02S%02S";
        System.out.println(String.format(pattern, "1", "5"));
    }

}

package com.sogou.bizdev.compass.aggregation.aggregator;

import java.util.*;

/**
 * 聚合描述器, 提供对SUM、COUNT、AVG、MAX、MIN函数的支持，以及对GROUP BY、ORDER BY和LIMIT操作的支持
 *
 * 使用方法：
 * 1. 添加一个聚合列
 *    使用sum(field, targetField)添加一个sum(field) as targetField列。sum的结果会被放置在结果行的targetField字段中
 *    其他聚合函数使用方法与sum函数相同
 *    
 * 2. 使用groupBy(String...)方法添加若干group by列
 *    查询结果会被按照groupBy列的添加顺序依次进行分组
 * 3. 使用orderBy(String, boolean)方法添加一个order by列, 第二个参数为true/false分别代表升序/降序
 *    查询结果会被按照orderBy列的添加顺序依次进行排序
 * 4. 使用limit(int, int)方法添加范围参数
 *    第一个参数为范围的起始偏移量，第二个参数为所取范围的行数
 *
 * @author yk
 * @since 1.0.0
 */
public class AggregationDescriptor {

    public static enum Function {
        SUM,
        COUNT,
        AVG,
        MAX,
        MIN
    }

    private Map<Function, Map<String, String>> aggregationFields = new HashMap<Function, Map<String, String>>();
    private List<String> groupByFields = new ArrayList<String>();
    private LinkedHashMap<String, Boolean> orderByFields = new LinkedHashMap<String, Boolean>();
    private int[] limitDescriptor = null;	// [0] - offset, [1] - rows


    public AggregationDescriptor sum(String field, String targetField) {
        this.add(Function.SUM, field, targetField);
        return this;
    }

    public AggregationDescriptor count(String field, String targetField) {
        this.add(Function.COUNT, field, targetField);
        return this;
    }

    public AggregationDescriptor avg(String field, String targetField) {
        this.add(Function.AVG, field, targetField);
        return this;
    }

    public AggregationDescriptor max(String field, String targetField) {
        this.add(Function.MAX, field, targetField);
        return this;
    }

    public AggregationDescriptor min(String field, String targetField) {
        this.add(Function.MIN, field, targetField);
        return this;
    }

    public AggregationDescriptor groupBy(String... field) {
        groupByFields.addAll(Arrays.asList(field));
        return this;
    }

    public AggregationDescriptor orderBy(String field, boolean isAsc) {
        orderByFields.put(field, Boolean.valueOf(isAsc));
        return this;
    }
    
    public AggregationDescriptor limit(int offset, int rows) {
    	this.limitDescriptor = new int[] { offset, rows };
    	return this;
    }
    
    public AggregationDescriptor limit(int rows) {
    	return this.limit(0, rows);
    }

    public Map<String, String> listAggregationFields(Function function) {
        return aggregationFields.get(function);
    }

    public List<String> listGroupByFields() {
        return groupByFields;
    }

    public LinkedHashMap<String, Boolean> listOrderByFields() {
        return orderByFields;
    }
    
    /**
     * [0] - offset,
     * [1] - rows
     * @return
     */
    public int[] getLimitDescriptor() {
    	return limitDescriptor;
    }

    public boolean needAggregation() {
        return aggregationFields.size() > 0;
    }

    public boolean needGroupBy() {
        return groupByFields.size() > 0;
    }

    public boolean needOrderBy() {
        return orderByFields.size() > 0;
    }
    
    public boolean needLimit() {
    	return limitDescriptor != null;
    }

    private void add(Function function, String field, String targetField) {
        if (!aggregationFields.containsKey(function)) {
            aggregationFields.put(function, new LinkedHashMap<String, String>());
        }

        aggregationFields.get(function).put(field, targetField);
    }

}

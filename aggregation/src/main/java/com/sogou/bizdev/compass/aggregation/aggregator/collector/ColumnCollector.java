package com.sogou.bizdev.compass.aggregation.aggregator.collector;

/**
 * 列收集器，用于从一组查询结果行中收集指定列的值来进行函数计算
 * collect()方法会被持续地调用
 * 实现类中需要在每一次调用中对参数的指定列进行收集（并计算）
 * <p>
 * 最终getCollectedValue()方法会被调用
 * 实现类需要在此方法中将收集（计算）结果返回给调用方
 *
 * @author yk
 * @since 1.0.0
 */
public interface ColumnCollector {

    public void collect(RowAccessor row);

    /**
     * 与另一个同类Collector的数据进行合并
     *
     * @param collector
     */
    public void merge(ColumnCollector collector);

    public String getCollectingField();

    public Object getCollectedValue();

}
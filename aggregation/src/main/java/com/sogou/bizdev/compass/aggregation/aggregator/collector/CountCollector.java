package com.sogou.bizdev.compass.aggregation.aggregator.collector;

/**
 * Count收集器
 * 提供对查询结果的指定field进行计数的功能
 *
 * @author yk
 * @since 1.0.0
 */
public class CountCollector extends AbstractColumnCollector {

    private int count = 0;

    public CountCollector(String collectingField) {
        super(collectingField);
    }

    @Override
    public void collect(RowAccessor row) {
        Object value = row.getFieldValue(getCollectingField());

        if (value != null) {
            count++;
        }
    }

    @Override
    public void merge(ColumnCollector collector) {
        CountCollector countCollector = super.transform(collector);
        count += countCollector.count;
    }

    @Override
    public Integer getCollectedValue() {
        return Integer.valueOf(count);
    }

}

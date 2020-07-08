package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import com.sogou.bizdev.compass.aggregation.util.ObjectComparator;

/**
 * 最大值收集器，用于从一组查询结果行中收集值最大的一行
 *
 * @author yk
 * @since 1.0.0
 */
public class MaxCollector extends AbstractColumnCollector {

    private static ObjectComparator comparator = new ObjectComparator();
    private Object max;

    public MaxCollector(String collectingField) {
        super(collectingField);
    }

    private void compareAndSet(Object value) {
        if (value == null) {
            return;
        }

        if (max == null || comparator.compare(max, value) < 0) {
            max = value;
        }

    }

    @Override
    public void collect(RowAccessor row) {
        Object value = row.getFieldValue(getCollectingField());
        this.compareAndSet(value);
    }

    @Override
    public void merge(ColumnCollector collector) {
        MaxCollector maxCollector = super.transform(collector);
        this.compareAndSet(maxCollector.max);
    }

    @Override
    public Object getCollectedValue() {
        return max;
    }

}

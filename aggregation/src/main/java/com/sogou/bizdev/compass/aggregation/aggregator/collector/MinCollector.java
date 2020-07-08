package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import com.sogou.bizdev.compass.aggregation.util.ObjectComparator;

/**
 * 最小值收集器，用于从一组查询结果行中收集值最小的一行
 *
 * @author yk
 * @since 1.0.0
 */
public class MinCollector extends AbstractColumnCollector {

    private static ObjectComparator comparator = new ObjectComparator();
    private Object min;

    public MinCollector(String collectingField) {
        super(collectingField);
    }

    private void compareAndSet(Object value) {
        if (value != null) {
            // 关于 min == null 的注释：
            // 在sql中，null并不参与min的计算，min值一定是非null值
            // 因此min为null时也为其设置value
            if (min == null || comparator.compare(min, value) > 0) {
                min = value;
            }
        }
    }

    @Override
    public void collect(RowAccessor row) {
        Object value = row.getFieldValue(getCollectingField());
        this.compareAndSet(value);
    }

    @Override
    public void merge(ColumnCollector collector) {
        MinCollector minCollector = super.transform(collector);
        this.compareAndSet(minCollector.min);
    }

    @Override
    public Object getCollectedValue() {
        return min;
    }

}

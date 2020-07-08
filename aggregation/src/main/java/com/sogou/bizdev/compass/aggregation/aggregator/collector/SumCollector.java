package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import com.sogou.bizdev.compass.aggregation.util.NumberUtil;

/**
 * SUM收集器
 * 提供对查询结果的指定field计算和的功能
 *
 * @author yk
 * @since 1.0.0
 */
public class SumCollector extends AbstractColumnCollector {

    private Number sum;

    public SumCollector(String collectingField) {
        super(collectingField);
    }

    @Override
    public void collect(RowAccessor row) {
        Object value = row.getFieldValue(getCollectingField());

        if (value == null) {
            return;
        }

        if (!(value instanceof Number)) {
            throw new IllegalArgumentException();
        }

        Number num = (Number) value;

        if (sum == null) {
            sum = num;
            return;
        }

        sum = NumberUtil.add(sum, num);
    }

    @Override
    public void merge(ColumnCollector collector) {
        SumCollector sumCollector = super.transform(collector);
        sum = NumberUtil.add(sum, sumCollector.sum);
    }

    @Override
    public Number getCollectedValue() {
        return sum;
    }

}

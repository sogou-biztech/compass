package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;

/**
 * 列收集器工厂
 *
 * @author yk
 * @since 1.0.0
 */
public class ColumnCollectorFactory {

    public static ColumnCollector createColumnCollector(AggregationDescriptor.Function function, String field) {
        switch (function) {
            case SUM:
                return new SumCollector(field);
            case AVG:
                return new AvgCollector(field);
            case COUNT:
                return new CountCollector(field);
            case MIN:
                return new MinCollector(field);
            case MAX:
                return new MaxCollector(field);
            default:
                throw new IllegalArgumentException("unsupported function=" + function);
        }
    }

}

package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import java.util.HashMap;
import java.util.Map;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;

/**
 * 行收集器
 * 每个GroupedRowCollector代表聚合结果中的一行（即针对原始结果行进行groupBy后的一个group）
 *
 * @author yk
 * @since 1.0.0
 */
public class GroupedRowCollector {

    private AggregationDescriptor descriptor;
    private Map<String, ColumnCollector> collectors;    // key:targetField, value:columnCollectorEntity

    private Map<String, Object> firstRow = null;

    public GroupedRowCollector(AggregationDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public void collect(RowAccessor row) {
        this.setFirstRow(row);

        for (ColumnCollector collector : this.getColumnCollectors().values()) {
            collector.collect(row);
        }
    }
    
    public void merge(GroupedRowCollector group) {
    	for (String key : collectors.keySet()) {
    		ColumnCollector baseCollector = collectors.get(key);
    		ColumnCollector toBeMerged = group.collectors.get(key);
    		
    		baseCollector.merge(toBeMerged);
    	}
    }

    private Map<String, ColumnCollector> getColumnCollectors() {
        if (collectors == null) {
            collectors = new HashMap<String, ColumnCollector>();

            for (AggregationDescriptor.Function function : AggregationDescriptor.Function.values()) {
                Map<String, String> aggregationFields = descriptor.listAggregationFields(function);

                if (aggregationFields != null) {
                    for (Map.Entry<String, String> entry : aggregationFields.entrySet()) {
                        String field = entry.getKey();
                        String targetField = entry.getValue();

                        ColumnCollector collector = ColumnCollectorFactory.createColumnCollector(function, field);
                        collectors.put(targetField, collector);
                    }
                }
            }
        }

        return collectors;
    }

    private void setFirstRow(RowAccessor row) {
        if (firstRow == null) {
            firstRow = row.toFieldValueMap();
        }
    }

    public Map<String, Object> getCollectedValue() {
    	Map<String, Object> collected = firstRow;

        for (Map.Entry<String, ColumnCollector> entry : collectors.entrySet()) {
            String targetField = entry.getKey();
            Object targetFieldValue = entry.getValue().getCollectedValue();

            collected.put(targetField, targetFieldValue);
        }

        return collected;
    }

}
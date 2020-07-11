package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;

/**
 * 聚合分组收集器
 * 用于对多个结果行进行收集、并根据AggregationDescriptor进行分组、聚合
 * <p>
 * 支持通过merge()方法与其它收集器的分组结果进行合并
 *
 * @author yk
 * @since 1.0.0
 */
public class GroupListCollector {

    private AggregationDescriptor descriptor;
    private GroupKeyBuilder groupKeyBuilder;
    private LinkedHashMap<Object, GroupedRowCollector> groups = new LinkedHashMap<Object, GroupedRowCollector>();

    public GroupListCollector(AggregationDescriptor descriptor) {
        this.descriptor = descriptor;
        this.groupKeyBuilder = new GroupKeyBuilder(descriptor);
    }

    public void collectRow(RowAccessor row) {
        Object key = groupKeyBuilder.buildKey(row);

        if (!groups.containsKey(key)) {
            GroupedRowCollector group = new GroupedRowCollector(descriptor);
            groups.put(key, group);
        }

        groups.get(key).collect(row);
    }

    public void merge(GroupListCollector groupList) {
        for (Entry<Object, GroupedRowCollector> entry : groupList.groups.entrySet()) {
            Object key = entry.getKey();
            GroupedRowCollector group = entry.getValue();

            if (groups.containsKey(key)) {
                groups.get(key).merge(group);
            } else {
                groups.put(key, group);
            }
        }
    }

    public List<Map<String, Object>> getCollectedGroupList() {
        List<Map<String, Object>> collected = new ArrayList<Map<String, Object>>();

        for (GroupedRowCollector group : groups.values()) {
            collected.add(group.getCollectedValue());
        }

        return collected;
    }

}

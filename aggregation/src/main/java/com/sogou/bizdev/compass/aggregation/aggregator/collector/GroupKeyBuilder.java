package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import java.util.HashMap;
import java.util.Map;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;

/**
 * {@link GroupListCollector}在收集一个row时，使用此类来判断该类属于哪个分组
 *
 * @author yk
 * @since 1.0.0
 */
public class GroupKeyBuilder {

    private AggregationDescriptor descriptor;

    public GroupKeyBuilder(AggregationDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public Object buildKey(RowAccessor row) {
        if (descriptor.needGroupBy()) {		// 需要groupBy的情况
            // 使用groupByFields及其values构造一个HashMap作为该row的group key
            // 如果结果集列表中的某两行的groupByFields的values完全相等
            // 那么说明他们在同一个group中，此处会相应地构造出相同的group key
        	Map<String, Object> key = new HashMap<String, Object>();

            for (String f : descriptor.listGroupByFields()) {
                Object value = row.getFieldValue(f);
                key.put(f, value);
            }

            return key;
        } 
        else if (descriptor.needAggregation()) {		// 不需要groupBy但是需要聚合的情况
                // 返回一个固定的key
                // 意味着结果集列表的所有行在一个group中
                return String.valueOf("uniqueSingleGroup");
        }
        
        
        throw new IllegalArgumentException("neither needGroupBy nor needAggregation was specified");
    }

}

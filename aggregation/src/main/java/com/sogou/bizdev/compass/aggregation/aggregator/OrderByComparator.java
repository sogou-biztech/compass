package com.sogou.bizdev.compass.aggregation.aggregator;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sogou.bizdev.compass.aggregation.util.ObjectComparator;

/**
 * 用于对结果集按照多个order by field进行排序
 *
 * @author yk
 * @since 1.0.0
 *
 */
public class OrderByComparator implements Comparator<Map<String, Object>> {

    private static ObjectComparator objectComparator = new ObjectComparator();

    private LinkedHashMap<String, Boolean> orderByFields;

    public OrderByComparator(LinkedHashMap<String, Boolean> orderByFields) {
        this.orderByFields = orderByFields;
    }

    @Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        for (Map.Entry<String, Boolean> field : orderByFields.entrySet()) {
            String fieldName = field.getKey();
            Boolean isAsc = field.getValue();

            int result = compare(o1, o2, fieldName);

            if (result != 0) {
                if (isAsc.booleanValue()) {
                    return result;
                } else {
                    return -result;
                }
            }
        }

        return 0;
    }

    private int compare(Map<String, Object> o1, Map<String, Object> o2, String field) {
        Object value1 = o1.get(field);
        Object value2 = o2.get(field);
        
        return objectComparator.compare(value1, value2);
    }

}

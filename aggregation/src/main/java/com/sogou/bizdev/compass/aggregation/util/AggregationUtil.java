package com.sogou.bizdev.compass.aggregation.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;
import com.sogou.bizdev.compass.aggregation.aggregator.OrderByComparator;
import com.sogou.bizdev.compass.aggregation.aggregator.collector.GroupListCollector;

public class AggregationUtil {
	
    /**
     * 基于Integer的聚合工具，通常用于update方法后计算总的affectedRows
     * @param intList
     * @return
     */
    public static Integer aggregateInteger(List<Integer> intList) {
        int result = 0;

        for (Integer i : intList) {
            result += i.intValue();
        }

        return Integer.valueOf(result);
    }
    
    /**
     * 基于任意类型的聚合工具，仅仅将多个List的内容合并至一个List中
     * 通常用于无AggregationDescriptor的查询结果的聚合
     * @param paramList
     * @return
     */
    @SuppressWarnings({ "unchecked" })
	public static <T> List<T> aggregateObjectList(List<List<T>> paramList) {
        List<T> resultList = new ArrayList<T>();

        for (List<T> list : paramList) {
            resultList.addAll(list);
        }

        return resultList;
    }


    /**
     * 基于int[]的聚合工具，通常用于batchUpdate后计算affectedRows
     * @param intsList
     * @return
     */
    public static int[] aggregateIntegerArray(List<int[]> intsList) {
        if (CollectionUtils.isEmpty(intsList)) {
            return new int[0];
        } else {
            int[] result = new int[intsList.get(0).length];

            for (int[] ints : intsList) {
                if (ints.length != result.length) {
                    throw new IllegalArgumentException();
                }
                for (int i = 0; i < ints.length; i++) {
                   result[i] += ints[i];
                }
            }

            return result;
        }
    }
    
    /**
     * 基于GroupListCollector的聚合工具
     * 通常用于针对单表进行聚合查询后对聚合结果进行合并
     * 
     * @param groupList
     * @return
     */
    public static List<Map<String, Object>> aggregateGroupList(List<GroupListCollector> groupList) {
		GroupListCollector baseList = groupList.get(0);
		
		for (int i = 1; i < groupList.size(); i++) {
			GroupListCollector toBeMerged = groupList.get(i);
			baseList.merge(toBeMerged);
		}
		
		return baseList.getCollectedGroupList();
    }
    
    /**
     * 排序工具，提供基于AggregationDescriptor.listOrderByFields()的排序功能
     * @param descriptor
     * @param rowList
     */
    public static void sortIfNecessary(AggregationDescriptor descriptor, List<Map<String, Object>> rowList) {
        if (descriptor.needOrderBy()) {
            Collections.sort(rowList, new OrderByComparator(descriptor.listOrderByFields()));
        }
    }
    
    /**
     * limit工具，提供基于AggregationDescriptor.getLimitDescriptor()的limit功能
     * @param descriptor
     * @param rowList
     * @return
     */
    public static List<Map<String, Object>> limitIfNecessary(AggregationDescriptor descriptor, List<Map<String, Object>> rowList) {
    	List<Map<String, Object>> result = rowList;

    	if (descriptor.needLimit()) {
    		int size = rowList.size();
    		int offset = descriptor.getLimitDescriptor()[0];
    		int rows = descriptor.getLimitDescriptor()[1];
    		
    		if (offset >= size) {
    			result = new ArrayList<Map<String,Object>>();
    		} else {
    			int toIndex = Math.min(size, offset + rows);
    			result = new ArrayList<Map<String,Object>>(rowList.subList(offset, toIndex));
    		}
    	} 

   		return result;
    }

}

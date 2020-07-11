package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import java.util.Map;

/**
 * 定义了GroupListCollector、GrouppedRowCollector、ColumnCollector操作的基本单位
 *
 * @author yk
 * @since 1.0.0
 */
public interface RowAccessor {

    public Object getFieldValue(String field);

    public Map<String, Object> toFieldValueMap();

}

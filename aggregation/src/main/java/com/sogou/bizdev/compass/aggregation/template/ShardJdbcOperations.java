package com.sogou.bizdev.compass.aggregation.template;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import com.sogou.bizdev.compass.aggregation.aggregator.AggregatedRowMapper;
import com.sogou.bizdev.compass.aggregation.aggregator.AggregationDescriptor;

/**
 * 此处定义了ShardJdbcTemplate支持的基本jdbc操作
 *
 * @author yk
 * @since 1.0.0
 */
public interface ShardJdbcOperations {

    /**
     * 使用sql与args绑定进行更新
     * 返回被更新的行数
     *
     * @param sql
     * @param args
     * @return
     */
    public int update(String sql, Object[] args);

    /**
     * 使用sql与一组args进行批量更新
     * 然后返回被更新的行数
     *
     * @param sql
     * @param args
     * @return
     */
    public int[] batchUpdate(String sql, List<Object[]> args);

    /**
     * 使用sql与args进行查询
     * 查询结果以List返回，结果的每一行以field-value的形式存在Map中
     *
     * @param sql
     * @param args
     * @return
     */
    public List<Map<String, Object>> query(String sql, Object[] args);

    /**
     * 使用sql与args进行查询
     * 对查询结果集的每一行使用rowMapper进行映射
     * 查询结果以List返回
     *
     * @param sql
     * @param args
     * @param rowMapper
     * @param <T>
     * @return
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper);

    /**
     * 使用sql与args进行查询
     * 然后根据descriptor中的描述对查询结果进行分组、聚合和排序
     * 最终结果以List返回，结果的每一行以field-value的形式存在Map中
     *
     * @param sql
     * @param args
     * @param descriptor
     * @return
     */
    public List<Map<String, Object>> query(String sql, Object[] args, AggregationDescriptor descriptor);

    /**
     * 使用sql与args进行查询
     * 然后根据descriptor中的描述对映射结果进行分组、聚合和排序
     * 对聚合结果的每一行使用rowMapper进行映射
     * 最终结果以List返回
     *
     * @param sql
     * @param args
     * @param rowMapper
     * @param descriptor
     * @return
     */
    public <T> List<T> query(String sql, Object[] args, AggregatedRowMapper<T> rowMapper,
        AggregationDescriptor descriptor);

}
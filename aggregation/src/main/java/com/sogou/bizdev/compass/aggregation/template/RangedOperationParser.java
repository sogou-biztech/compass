package com.sogou.bizdev.compass.aggregation.template;


import java.util.Arrays;
import java.util.List;

import com.sogou.bizdev.compass.aggregation.util.RangedSqlUtil;

/**
 * 指定范围操作的解析器
 * 用于判断一个sql是否为指定范围的操作
 * 以及从sql的args中抽取出范围参数
 *
 * @author yk
 * @since 1.0.0
 */
public class RangedOperationParser {

    public boolean isRangedOperation(String sql) {
        return sql.contains(ShardJdbcTemplate.RANGE_PLACEHOLDER);
    }

    public SqlParameterList parseParameter(String sql, Object[] args) {
        String[] splitted = RangedSqlUtil.split(sql, ShardJdbcTemplate.RANGE_PLACEHOLDER);

        int leftCount = RangedSqlUtil.count(splitted[0], '?');
        int rightCount = RangedSqlUtil.count(splitted[1], '?');

        if (args.length <= leftCount + rightCount) {
        	StringBuilder builder = new StringBuilder();
        	builder.append("insufficient amount of arguments: ")
        		.append("required(at least) amount=")
        		.append(leftCount + rightCount + 1)
        		.append(", actual amount=")
        		.append(args.length);
            throw new IllegalArgumentException(builder.toString());
        }

        SqlParameterList param = new SqlParameterList();
        
        this.addLeftParameter(param, Arrays.copyOfRange(args, 0, leftCount));
        this.addRangeParameter(param, Arrays.copyOfRange(args, leftCount, args.length - rightCount));
        this.addRightParameter(param, Arrays.copyOfRange(args, args.length - rightCount, args.length));

        return param;
    }
    
    private void addLeftParameter(SqlParameterList param, Object[] leftArgs) {
    	for (Object p : leftArgs) {
    		param.addParameter(p);
    	}
    }
    
    private void addRangeParameter(SqlParameterList param, Object[] rangeArgs) {
    	for (Object p : rangeArgs) {
    		// 一组范围参数可能会以Object[]或List<?>的形式提供
    		// 此处对这两种形式提供支持
    		if (p instanceof Object[]) {
    			param.addRangeParameter((Object[]) p);
    		} else if (p instanceof List<?>) {
    			List<?> list = (List<?>) p;
    			param.addRangeParameter(list.toArray(new Object[list.size()]));
    		} else {
    			param.addRangeParameter(p);
    		}
    	}
    }
    
    private void addRightParameter(SqlParameterList param, Object[] rightArgs) {
    	for (Object p : rightArgs) {
    		param.addParameter(p);
    	}
    }

}

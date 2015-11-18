package com.sogou.bizdev.compass.aggregation.template;

import java.util.ArrayList;
import java.util.List;

/**
 * 对于需要使用in (id1, id2, ...)方式的操作，请使用此类构建sql的参数
 * @author yk
 * @since 1.0.0
 */
public class SqlParameterList 
{

    private List<Object> leftParameterList = new ArrayList<Object>();
    private List<Object> rangeParameterList = new ArrayList<Object>();
    private List<Object> rightParameterList = new ArrayList<Object>();

    /**
     * 添加一个常规参数（非in语句中的参数）
     * @param object
     * @return
     */
    public SqlParameterList addParameter(Object object) {
        if (rangeParameterList.size() == 0) {
            leftParameterList.add(object);
        } else {
            rightParameterList.add(object);
        }

        return this;
    }

    /**
     * 添加in (?, ?, ....)语句中需要绑定的参数
     * @param param
     * @return
     */
    public SqlParameterList addRangeParameter(Object... param) {
        if (rightParameterList.size() > 0) {
            throw new IllegalStateException("Range parameters must be added sequentially");
        }
        if (param != null && param.length != 0) {
            for (Object p : param) {
                rangeParameterList.add(p);
            }
        }

        return this;
    }

    /**
     * 获取in (?, ?, ....)语句中需要绑定的参数
     * @return
     */
    public List<Object> getRangeParameterList() {
        return rangeParameterList;
    }

    /**
     * 在根据路由规则对rangeParameterList进行分组后
     * 使用此方法构建基于一个分组的完整参数列表
     * @param subRangeParam
     * @return
     */
    public Object[] buildFullParameterList(List<Object> subRangeParam) {
        List<Object> fullParameterList = new ArrayList<Object>();

        fullParameterList.addAll(leftParameterList);
        if (subRangeParam != null) 
        {
        	fullParameterList.addAll(subRangeParam); 
        }
        fullParameterList.addAll(rightParameterList);

        return fullParameterList.toArray(new Object[fullParameterList.size()]);
    }
    
    public Object[] buildFullParameterList() {
    	List<Object> rangeParamList = getRangeParameterList();
    	return this.buildFullParameterList(rangeParamList);
    }

}

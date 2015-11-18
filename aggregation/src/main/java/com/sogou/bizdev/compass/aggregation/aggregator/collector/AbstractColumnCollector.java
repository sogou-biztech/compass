package com.sogou.bizdev.compass.aggregation.aggregator.collector;


/**
 * 抽象列收集器
 * 提供了对collectingField的支持
 *
 * @author yk
 * @since 1.0.0
 * @see com.sogou.bizdev.compass.aggregation.aggregator.collector.ColumnCollector
 */
public abstract class AbstractColumnCollector implements ColumnCollector {

    private String collectingField;

    public AbstractColumnCollector(String collectingField) {
        this.collectingField = collectingField;
    }

    @Override
    public String getCollectingField() {
        return collectingField;
    }
    
    /**
     * 校验一个ColumnCollector的类型是否与此类相同
     * 主要用于merge(ColumnCollector)方法
     * @param collector
     * @return
     */
	@SuppressWarnings("unchecked")
	protected <T extends ColumnCollector> T transform(ColumnCollector collector) {
    	if (!this.getClass().equals(collector.getClass())) {
    		throw new IllegalArgumentException("invalid collecor type=" + collector.getClass());
    	}
    	
    	return (T) collector;
    }

}

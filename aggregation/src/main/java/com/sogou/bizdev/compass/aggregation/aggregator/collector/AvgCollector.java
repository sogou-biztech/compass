package com.sogou.bizdev.compass.aggregation.aggregator.collector;

import java.math.BigDecimal;

import com.sogou.bizdev.compass.aggregation.util.NumberUtil;

/**
 * AVG收集器
 * 提供对查询结果的指定field计算平均值的功能
 *
 * @author yk
 * @since 1.0.0
 */
public class AvgCollector extends AbstractColumnCollector {
	
    private SumCollector sumCollector = new SumCollector(getCollectingField());
    private CountCollector countCollector = new CountCollector(getCollectingField());

    public AvgCollector(String collectingField) {
        super(collectingField);
    }

    @Override
    public void collect(RowAccessor row) {
    	sumCollector.collect(row);
    	countCollector.collect(row);
    }
    
	@Override
	public void merge(ColumnCollector collector) {
		AvgCollector avgCollector = super.transform(collector);
		
		sumCollector.merge(avgCollector.sumCollector);
		countCollector.merge(avgCollector.countCollector);
	}

    @Override
    public Number getCollectedValue() {
    	Integer count = countCollector.getCollectedValue();
        if (count == 0) {
            return null;
        } else {
            Number sum = sumCollector.getCollectedValue();

            if (sum == null) {
                return BigDecimal.valueOf(0);
            } else {
                BigDecimal n1 = NumberUtil.convert(BigDecimal.class, sum);
                BigDecimal n2 = NumberUtil.convert(BigDecimal.class, Integer.valueOf(count));

                BigDecimal avg = n1.divide(n2, BigDecimal.ROUND_HALF_UP);
               	return avg;
            }
        }
    }

}

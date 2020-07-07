package com.sogou.bizdev.compass.core.selector.loadbalance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.CollectionUtils;

/**
 * 带权轮询，需要一个轮询序号，所以是带状态的
 *
 * @author xr
 * @since 1.0.0
 */
public class WeightedRoundRobin implements LoadBalance {

    private final AtomicInteger position = new AtomicInteger(0);

    public AtomicInteger getPosition() {
        return position;
    }

    @Override
    public <T extends Selectable> T select(List<T> targets) {
        if (CollectionUtils.isEmpty(targets)) {
            throw new IllegalArgumentException("targets is empty");
        }

        int sum = this.getSumWeight(targets);
        int index = position.getAndIncrement();
        if (index >= sum - 1) {
            position.set(0);
        }
        List<T> filteredTarget = this.getWeightedRoundRobinList(targets, sum);
        return filteredTarget.get(index % sum);
    }

    private <T extends Selectable> List<T> getWeightedRoundRobinList(List<T> targets, int sum) {
        List<T> result = new ArrayList<T>(sum);
        int maxWeight = getMaxWeight(targets);
        for (int i = 0; i < maxWeight; i++) {
            for (T selectable : targets) {
                if (selectable.getWeight() > i) {
                    result.add(selectable);
                }
            }
        }
        return result;
    }

    private <T extends Selectable> int getSumWeight(List<T> targets) {
        int sum = 0;
        for (T target : targets) {
            sum += target.getWeight();
        }
        return sum;
    }

    private <T extends Selectable> int getMaxWeight(List<T> targets) {
        int max = 0;
        for (T target : targets) {
            if (max < target.getWeight()) {
                max = target.getWeight();
            }
        }
        return max;
    }

}

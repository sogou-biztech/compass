package com.sogou.bizdev.compass.delayfree;

import org.junit.Assert;
import org.junit.Test;

public class LocalMemBasedDelayFreeTest {

    @Test
    public void test() {
        LocalMemBasedDelayFree localMemBasedDelayFree = new LocalMemBasedDelayFree();
        localMemBasedDelayFree.setPeriod(10L);
        Long accountId = 274280L;

        boolean isNeed = localMemBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertFalse(isNeed);
        localMemBasedDelayFree.markNeedDelayFree(String.valueOf(accountId));
        isNeed = localMemBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertTrue(isNeed);
        try {
            Thread.sleep(12 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isNeed = localMemBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertFalse(isNeed);
    }

}

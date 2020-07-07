package com.sogou.bizdev.compass.delayfree;

import org.junit.Assert;
import org.junit.Test;

public class MemcacheBasedDelayFreeTest {

    @Test
    public void test() {
        MemcacheBasedDelayFree memcacheBasedDelayFree = new MemcacheBasedDelayFree();
        memcacheBasedDelayFree.setPeriod(10L);
        Long accountId = 274280L;
        boolean isNeed = memcacheBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertFalse(isNeed);
        memcacheBasedDelayFree.markNeedDelayFree(String.valueOf(accountId));
        isNeed = memcacheBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertTrue(isNeed);
        try {
            Thread.sleep(11 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        isNeed = memcacheBasedDelayFree.isInDelay(String.valueOf(accountId));
        Assert.assertFalse(isNeed);
    }

}

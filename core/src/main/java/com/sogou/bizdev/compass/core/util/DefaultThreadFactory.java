package com.sogou.bizdev.compass.core.util;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ThreadFactory;

/**
 * @Description: 线程池工厂，支持自定义线程池线程名称
 * @author zjc
 * @since 1.0.0
 */
public class DefaultThreadFactory implements ThreadFactory {
    static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public DefaultThreadFactory(String poolName) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" + poolName + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) { t.setDaemon(false); }
        if (t.getPriority() != Thread.NORM_PRIORITY) { t.setPriority(Thread.NORM_PRIORITY); }
        return t;
    }
}
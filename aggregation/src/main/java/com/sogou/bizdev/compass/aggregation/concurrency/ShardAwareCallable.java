package com.sogou.bizdev.compass.aggregation.concurrency;

import com.sogou.bizdev.compass.aggregation.datasource.Shard;

import java.util.concurrent.Callable;

/**
 * @author yk
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ShardAwareCallable<V> extends Callable<V> {

    public Shard getShard();

}

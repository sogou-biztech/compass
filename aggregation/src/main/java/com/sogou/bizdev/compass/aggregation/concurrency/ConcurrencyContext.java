package com.sogou.bizdev.compass.aggregation.concurrency;


/**
 * 并发操作上下文, 作用范围为针对ShardAwareCallableExecutor.execute()方法的一次调用
 * 当某一个Callable的submit操作或者Callable内部的sql操作发生异常时，其它Callable均不再执行了
 *
 * @author yk
 * @since 1.0.0
 */
public class ConcurrencyContext {

    private volatile Throwable ex;

    public void setException(Throwable ex) {
        this.ex = ex;   
    }

    public Throwable getException() {
        return ex;
    }

    public boolean hasException() {
        return ex != null;
    }
    
    public void throwExceptionIfHasAny() throws Throwable {
    	if (ex != null) {
    		throw ex;
    	}
    }

}

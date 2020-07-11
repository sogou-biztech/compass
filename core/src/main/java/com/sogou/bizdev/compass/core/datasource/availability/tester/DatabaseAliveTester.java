package com.sogou.bizdev.compass.core.datasource.availability.tester;

import com.sogou.bizdev.compass.core.datasource.availability.DatabaseDetectingEvent;

/**
 * 数据库探测器
 *
 * @author gly
 * @since 1.0.0
 */
public interface DatabaseAliveTester {

    /**
     * 根据探测事件判断数据源是否存活
     *
     * @param event 心跳探测时间
     * @return true 存活，false 失效
     */
    boolean isDatabaseAlive(DatabaseDetectingEvent event);

}

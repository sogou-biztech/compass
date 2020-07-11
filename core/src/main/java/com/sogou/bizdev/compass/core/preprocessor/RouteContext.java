/**
 *
 */
package com.sogou.bizdev.compass.core.preprocessor;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 路由上下文，存放当前线程中调用栈的路由信息
 *
 * @author zjc
 * @since 1.0.0
 *
 */
public class RouteContext {

    private static final ThreadLocal<Map<String, RouteContext>> ROUTE_CONTEXT_HOLDER = new ThreadLocal<Map<String, RouteContext>>() {
        @Override
        protected Map<String, RouteContext> initialValue() {
            return new HashMap<String, RouteContext>();
        }
    };

    /**
     * 当前调用的方法
     */
    private Method method;

    /**
     * 当前方法的路由key
     */
    private Object routeKey;

    /**
     * 当前方法是去主库还是从库
     */
    private boolean masterMode;

    private int invokeCount = 0;

    public RouteContext(Method method, Object routingKey, boolean masterMode) {
        this.method = method;
        this.routeKey = routingKey;
        this.masterMode = masterMode;
    }

    public Method getMethod() {
        return method;
    }

    public Object getRouteKey() {
        return routeKey;
    }

    public boolean isMasterMode() {
        return masterMode;
    }

    public void setMasterMode(boolean masterMode) {
        this.masterMode = masterMode;
    }

    public void bindToThread(String dataSourceId) {
        setRoutingContext(dataSourceId, this);
    }

    private void setRoutingContext(String dataSourceId, RouteContext context) {
        Map<String, RouteContext> resource = ROUTE_CONTEXT_HOLDER.get();
        resource.put(dataSourceId, context);
    }

    public static RouteContext getCurrentRouteContext(String dataSourceId) {
        Map<String, RouteContext> resource = ROUTE_CONTEXT_HOLDER.get();
        return resource.get(dataSourceId);
    }

    public static void removeRoutingContext(String dataSourceId) {
        Map<String, RouteContext> resource = ROUTE_CONTEXT_HOLDER.get();
        resource.remove(dataSourceId);
        if (resource.isEmpty()) {
            remove();
        }
    }

    public static void remove() {
        ROUTE_CONTEXT_HOLDER.remove();
    }

    public void invoked() {
        this.invokeCount++;
    }

    /**
     *
     * @return 如果是最外层方法，返回true，否则返回false
     */
    public boolean released() {
        this.invokeCount--;
        return invokeCount == 0;
    }

    /**
     *
     * @return 如果是最外层方法，返回true，否则返回false
     */
    public boolean isOutermost() {
        return (this.invokeCount == 1);
    }

    @Override
    public String toString() {
        return String.format("RoutingContext [method=%s, routingKey=%s, masterMode=%s]",
            method, routeKey, masterMode);
    }

}

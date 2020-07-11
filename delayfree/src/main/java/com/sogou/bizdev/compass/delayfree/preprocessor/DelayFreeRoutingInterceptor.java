/**
 *
 */
package com.sogou.bizdev.compass.delayfree.preprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;

import com.sogou.bizdev.compass.core.preprocessor.RouteContext;
import com.sogou.bizdev.compass.core.preprocessor.RoutingInterceptor;
import com.sogou.bizdev.compass.delayfree.DelayFree;
import com.sogou.bizdev.compass.delayfree.annotation.AntiDelayKey;

/**
 * @Description: 路由拦截器，应该配置在事务拦截器前
 * @author zjc
 * @since 1.0.0
 */
public class DelayFreeRoutingInterceptor extends RoutingInterceptor implements InitializingBean {

    private DelayFree delayFree;

    private boolean useRouteKeyAsAntiDelayKey = false;

    public boolean isUseRouteKeyAsAntiDelayKey() {
        return useRouteKeyAsAntiDelayKey;
    }

    public void setUseRouteKeyAsAntiDelayKey(boolean useRouteKeyAsAntiDelayKey) {
        this.useRouteKeyAsAntiDelayKey = useRouteKeyAsAntiDelayKey;
    }

    public DelayFree getDelayFree() {
        return delayFree;
    }

    public void setDelayFree(DelayFree delayFree) {
        this.delayFree = delayFree;
    }

    @Override
    protected RouteContext buildRoutingContext(MethodInvocation mi) {
        RouteContext context = super.buildRoutingContext(mi);

        Object routeKey = context.getRouteKey();
        Object antiDelayKey = null;
        // 如果useRouteKeyAsAntiDelayKey=true, 那么以路由标识作为反延时标识
        if (useRouteKeyAsAntiDelayKey) {
            antiDelayKey = routeKey;
        } else {
            antiDelayKey = extractAntiDelayKey(mi);
        }
        return new DelayFreeRoutingContext(context.getMethod(), context.getRouteKey(), context.isMasterMode(), antiDelayKey);
    }

    protected Object extractAntiDelayKey(MethodInvocation mi) {
        Method method = mi.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = mi.getArguments();
        Object antiDelayKey = null;

        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            Object parameter = args[i++];
            for (Annotation annotation : annotations) {
                if (null == antiDelayKey && annotation instanceof AntiDelayKey) {
                    antiDelayKey = parameter;
                }
            }
        }
        return antiDelayKey;
    }

    @Override
    protected void onInitialEnterMethod(RouteContext context) {
        boolean isMaster = context.isMasterMode();
        DelayFreeRoutingContext contextToUse = (DelayFreeRoutingContext) context;
        if (!isMaster && delayFree != null && contextToUse.getAntiDelayKey() != null) {
            boolean inDelay = delayFree.isInDelay(contextToUse.getAntiDelayKey());
            if (inDelay) {
                context.setMasterMode(true);
            }
        }
    }

    @Override
    protected void onLastExitMethod(RouteContext context) {
        DelayFreeRoutingContext contextToUse = (DelayFreeRoutingContext) context;
        if (delayFree != null && contextToUse.getAntiDelayKey() != null && isMasterMode(context.getMethod())) {
            delayFree.markNeedDelayFree(contextToUse.getAntiDelayKey());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getDelayFree() == null) {
            throw new IllegalArgumentException("Property 'delayFree' required");
        }
        super.afterPropertiesSet();
    }

}

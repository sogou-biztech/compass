/**
 *
 */
package com.sogou.bizdev.compass.core.preprocessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ClassUtils;

import com.sogou.bizdev.compass.core.anotation.Master;
import com.sogou.bizdev.compass.core.anotation.RouteKey;
import com.sogou.bizdev.compass.core.anotation.Slave;
import com.sogou.bizdev.compass.core.exception.MasterSlaveConfigInconsistencyException;
import com.sogou.bizdev.compass.core.exception.MasterSlaveConfigNotFoundException;
import com.sogou.bizdev.compass.core.exception.RouteKeyInconsistencyException;

/**
 * @Description: 路由拦截器，应该配置在事务拦截器前
 * @author zjc
 * @since 1.0.0
 */
public class RoutingInterceptor implements MethodInterceptor, InitializingBean {

    private String dataSourceId;

    /**
     * 使用方法的第一个参数作为routingKey，这样可以不用在参数上添加{@link RouteKey}
     */
    private boolean useFirstArgumentAsRouteKey = false;

    private MasterSlaveAttributeSource masterSlaveAttributeSource;

    public String getDataSourceId() {
        return dataSourceId;
    }

    /**
     * 路由拦截器配置在哪个数据源上，dataSourceId需要与数据源中的dataSourceId保持一致
     *
     * @param dataSourceId
     */
    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public boolean isUseFirstArgumentAsRouteKey() {
        return useFirstArgumentAsRouteKey;
    }

    /**
     * 如果想以service的第一个参数作为默认的路由key，可以设置此参数为true,默认为false
     *
     * @param useFirstArgumentAsRoutingKey
     */
    public void setUseFirstArgumentAsRouteKey(boolean useFirstArgumentAsRoutingKey) {
        this.useFirstArgumentAsRouteKey = useFirstArgumentAsRoutingKey;
    }

    public void setMasterSlaveAttributes(Properties attributes) {
        NameMatchMasterSlaveAttributeSource source = new NameMatchMasterSlaveAttributeSource();
        source.setProperties(attributes);
        this.masterSlaveAttributeSource = source;
    }

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        RouteContext context = RouteContext.getCurrentRouteContext(getDataSourceId());
        if (context == null) {
            context = buildRoutingContext(mi);
            context.bindToThread(getDataSourceId());
            onInitialEnterMethod(context);
        }

        Object routeKey = extractRouteKey(mi);
        Method method = mi.getMethod();
        boolean isMaster = isMasterMode(method);
        try {
            context.invoked();
            if (!context.isOutermost()) {
                checkRoutingKeyConsistence(context, routeKey);
                checkMasterSlaveConsistence(context, method, isMaster);
            }
            return mi.proceed();
        } finally {
            if (context.released()) {
                RouteContext.removeRoutingContext(getDataSourceId());
                onLastExitMethod(context);
            }
        }
    }

    protected RouteContext buildRoutingContext(MethodInvocation mi) {
        Object routeKey = extractRouteKey(mi);
        Method method = mi.getMethod();
        boolean isMaster = isMasterMode(method);
        return new RouteContext(method, routeKey, isMaster);
    }

    protected Object extractRouteKey(MethodInvocation mi) {
        Method method = mi.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = mi.getArguments();
        Object routeKey = null;

        // 没有设置路由标识，如果useFirstArgumentAsRoutingKey=true,那么以第一个参数作为路由标识
        if (useFirstArgumentAsRouteKey) {
            if (args.length < 1) {
                throw new IllegalArgumentException("use firstArgumentAsRoutingKey, but no arguments for method: "
                    + ClassUtils.getQualifiedMethodName(method));
            }
            routeKey = args[0];
            return routeKey;
        }
        int i = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            Object parameter = args[i++];
            for (Annotation annotation : annotations) {
                if (null == routeKey && annotation instanceof RouteKey) {
                    routeKey = parameter;
                }
            }
        }
        return routeKey;
    }

    /**
     * 扩展点，子类可以在进入最外层service时作一些初始化操作
     *
     * @param context
     */
    protected void onInitialEnterMethod(RouteContext context) {
    }

    /**
     * 扩展点，子类可以在离开最外层service时作一些清理操作
     *
     * @param context
     */
    protected void onLastExitMethod(RouteContext context) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getDataSourceId() == null) {
            throw new IllegalArgumentException("Property 'dataSourceId' is required");
        }
    }

    protected boolean isMasterMode(Method method) {
        // 先检查配置项，再检查注解项
        if (masterSlaveAttributeSource != null) {
            MasterSlaveAttribute attr = masterSlaveAttributeSource.getMasterSlaveAttribute(method, method.getClass());
            if (attr == null) {
                throw new MasterSlaveConfigNotFoundException(ClassUtils.getQualifiedMethodName(method) + "can not match MASTER/SLAVE");
            }
            return attr == MasterSlaveAttribute.MASTER;
        }
        if (method.getAnnotation(Slave.class) != null) { return false; }
        if (method.getAnnotation(Master.class) != null) { return true; }
        if (method.getDeclaringClass().getAnnotation(Slave.class) != null) { return false; }
        if (method.getDeclaringClass().getAnnotation(Master.class) != null) { return true; }

        throw new MasterSlaveConfigNotFoundException(ClassUtils.getQualifiedMethodName(method) + "cannot match MASTER/SLAVE");
    }

    protected void checkRoutingKeyConsistence(RouteContext oldRoutingContext, Object currentRoutingKey) {
        if (oldRoutingContext == null) { return; }
        if (oldRoutingContext.getRouteKey() != null) {
            if (!oldRoutingContext.getRouteKey().equals(currentRoutingKey)) {
                throw new RouteKeyInconsistencyException("current routingKey '" + currentRoutingKey
                    + "' mismatch with outer routingKey '" + oldRoutingContext + "'");
            }
        } else {
            if (currentRoutingKey != null) {
                throw new RouteKeyInconsistencyException("current routingKey '" + currentRoutingKey
                    + "' mismatch with outer routingKey '" + oldRoutingContext + "'");
            }
        }
    }

    protected void checkMasterSlaveConsistence(RouteContext oldRoutingContext, Method method, boolean isMasterMode) {
        if (oldRoutingContext == null) { return; }
        // 对于嵌套调用的情形，检查读写的一致性，同一个数据源，外层service是读，里层是写，直接抛异常
        if (!oldRoutingContext.isMasterMode() && isMasterMode) {
            throw new MasterSlaveConfigInconsistencyException("current write method '"
                + ClassUtils.getQualifiedMethodName(method) + "' mismatch with outer read method '"
                + ClassUtils.getQualifiedMethodName(oldRoutingContext.getMethod()) + "'");
        }
    }

}

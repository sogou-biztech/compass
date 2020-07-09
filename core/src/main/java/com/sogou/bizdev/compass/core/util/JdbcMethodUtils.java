package com.sogou.bizdev.compass.core.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * @author cl
 */
public abstract class JdbcMethodUtils {
    /**
     * 通过反射来执行JDBC接口的方法
     *
     * @param interfaceClass
     * @param methodName
     * @param argTypes
     * @param target
     * @param args
     * @return
     * @throws SQLException
     */
    public static Object invokeJdbcMethod(Class<?> interfaceClass, String methodName, Class<?>[] argTypes,
        Object target, Object[] args) throws SQLException {
        Method method = ClassUtils.getMethodIfAvailable(interfaceClass, methodName, argTypes);
        if (method == null) {
            return null;
        }
        return invokeJdbcMethod(method, target, args);
    }

    private static Object invokeJdbcMethod(Method method, @Nullable Object target, @Nullable Object... args)
        throws SQLException {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException ex) {
            ReflectionUtils.handleReflectionException(ex);
        } catch (InvocationTargetException ex) {
            if (ex.getTargetException() instanceof SQLException) {
                throw (SQLException) ex.getTargetException();
            }
            ReflectionUtils.handleInvocationTargetException(ex);
        }
        throw new IllegalStateException("Should never get here");
    }
}

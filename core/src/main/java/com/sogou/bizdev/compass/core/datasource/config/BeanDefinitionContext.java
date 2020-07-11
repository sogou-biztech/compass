package com.sogou.bizdev.compass.core.datasource.config;

/**
 * ref或class形式的bean
 *
 * @author gly
 * @since 2.1.3
 */
public class BeanDefinitionContext {
    /**
     * 默认为非引用类型
     */
    private boolean ref = false;
    /**
     * 引用的bean名称或者直接设置的class名
     */
    private String attributeValue;

    public BeanDefinitionContext(boolean ref, String attributeValue) {
        this.ref = ref;
        this.attributeValue = attributeValue;
    }

    public BeanDefinitionContext() {
    }

    public boolean isRef() {
        return ref;
    }

    public void setRef(boolean ref) {
        this.ref = ref;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String beanDefinitionClass) {
        this.attributeValue = beanDefinitionClass;
    }

    @Override
    public String toString() {
        return "BeanDefinitionContext [ref=" + ref
            + ", beanDefinitionClass=" + attributeValue
            + "]";
    }

} 
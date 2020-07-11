package com.sogou.bizdev.compass.core.datasource.config;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.core.Conventions;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.sogou.bizdev.compass.core.datasource.MasterSlaveDataSource;
import com.sogou.bizdev.compass.core.datasource.ShardDataSource;
import com.sogou.bizdev.compass.core.datasource.SingleDataSource;
import com.sogou.bizdev.compass.core.datasource.availability.DatabaseAvailabilityDetector;
import com.sogou.bizdev.compass.core.datasource.config.enums.DataSourceType;
import com.sogou.bizdev.compass.core.datasource.config.enums.LoadBalanceStrategyType;
import com.sogou.bizdev.compass.core.selector.loadbalance.WeightedRandom;
import com.sogou.bizdev.compass.core.selector.loadbalance.WeightedRoundRobin;

/**
 * 读取datasource XML配置，构建ShardDataSource和MasterSlaveDataSource
 *
 * @author gly
 * @since 1.0.0
 */
public class DatasourceBeanDefinitionParser extends AbstractBeanDefinitionParser implements DataSourceTag {

    /**
     * 解析datasource节点，翻译为封装数据源类型
     *
     * @see org.springframework.beans.factory.xml.AbstractBeanDefinitionParser#parseInternal(org.w3c.dom.Element, org.springframework.beans.factory.xml.ParserContext)
     */
    @Override
    protected AbstractBeanDefinition parseInternal(Element dataSourceElement, ParserContext parserContext) {
        List<Element> groupElements = DomUtils.getChildElementsByTagName(dataSourceElement, GROUP);
        if (CollectionUtils.isEmpty(groupElements)) {
            throw new IllegalArgumentException(GROUP + " ChildElements is null");
        }

        DataSourceType dataSourceType = getDataSourceType(dataSourceElement, groupElements);
        switch (dataSourceType) {
            case SHARD_DATASOURCE:
                return parseShardDataSource(dataSourceElement, groupElements, parserContext);
            case MASTER_SLAVE_DATASOURCE:
                return parseMasterSlaveDataSource(dataSourceElement, groupElements.get(0), parserContext);
            default:
                throw new IllegalArgumentException("Unsupported data source type from the configuration");
        }
    }

    private String getShardDataSourceId(Element dataSourceElement) {
        String shardDataSourceId = dataSourceElement.getAttribute(ID);
        if (!StringUtils.hasText(shardDataSourceId)) {
            throw new IllegalArgumentException("Property '" + ID + "' of element '" + DATASOURCE + "' must be set");
        }
        return shardDataSourceId.trim();
    }

    /**
     * 分库数据源解析(主从库分表/分库不分表/分库分表)
     *
     * @param dataSourceElement   主节点
     * @param masterSlaveElements 数据源组节点
     * @param parserContext
     * @return
     */
    private AbstractBeanDefinition parseShardDataSource(Element dataSourceElement, List<Element> masterSlaveElements,
        ParserContext parserContext) {
        String shardDataSourceId = this.getShardDataSourceId(dataSourceElement);

        BeanDefinitionBuilder shardDataSourceBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ShardDataSource.class);
        shardDataSourceBeanDefinitionBuilder.addPropertyValue(ID, shardDataSourceId);

        List<AbstractBeanDefinition> masterSlaveDataSourceBeanDefinitions = this.createMasterSlaveBeanDefinitions(
            dataSourceElement,
            shardDataSourceId,
            masterSlaveElements,
            parserContext);
        shardDataSourceBeanDefinitionBuilder.addPropertyValue(ShardDataSource.MASTER_SLAVE_DATA_SOURCES_PROPERTY_NAME,
            masterSlaveDataSourceBeanDefinitions);

        // 数据库SQL解析器, 默认解析器不替换
        Element sqlInterceptorElement = DomUtils.getChildElementByTagName(dataSourceElement, SQL_INTERCEPTOR);
        //允许用户不设置sqlInterceptor, 默认为DefaultSqlInterceptor
        if (sqlInterceptorElement != null) {
            this.parseSqlInterceptor(sqlInterceptorElement, shardDataSourceBeanDefinitionBuilder, parserContext);
        }

        // 数据库路由选择器,不能为空
        Element routerElement = DomUtils.getChildElementByTagName(dataSourceElement, ROUTER);
        if (routerElement == null) {
            throw new IllegalArgumentException("if ShardDataSource, Property '" + ROUTER + "' of element '" + DATASOURCE + "' must be set");
        }
        this.parseRouteStrategy(routerElement, shardDataSourceBeanDefinitionBuilder, parserContext);

        return shardDataSourceBeanDefinitionBuilder.getBeanDefinition();
    }

    private void parseSqlInterceptor(Element sqlInterceptorElement,
        BeanDefinitionBuilder shardDataSourceBeanDefinitionBuilder, ParserContext parserContext) {
        BeanDefinitionContext beanDefinitionContext = getBeanDefinitionContext(sqlInterceptorElement, CLASS, REF, true);
        String sqlInterceptorValue = beanDefinitionContext.getAttributeValue();

        if (beanDefinitionContext.isRef()) {
            shardDataSourceBeanDefinitionBuilder.addPropertyReference(ShardDataSource.SQL_INTERCEPTOR_PROPERTY_NAME, sqlInterceptorValue);
        } else {
            AbstractBeanDefinition sqlInterceptorBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(sqlInterceptorValue).getBeanDefinition();
            // 可能在此处会进行property的注入
            shardDataSourceBeanDefinitionBuilder.addPropertyValue(ShardDataSource.SQL_INTERCEPTOR_PROPERTY_NAME,
                parsePropertyElement(sqlInterceptorElement, sqlInterceptorBeanDefinition, parserContext));
        }
    }

    private void parseRouteStrategy(Element routerElement, BeanDefinitionBuilder shardDataSourceBeanDefinitionBuilder,
        ParserContext parserContext) {
        BeanDefinitionContext beanDefinitionContext = getBeanDefinitionContext(routerElement, CLASS, REF, true);
        String shardDataSourceRouteStrategyValue = beanDefinitionContext.getAttributeValue();

        // 分库情况下必须设置路由选择器
        if (beanDefinitionContext.isRef()) {
            shardDataSourceBeanDefinitionBuilder.addPropertyReference(ShardDataSource.SHARD_DATA_SOURCE_ROUTE_STRATEGY_PROPERTY_NAME,
                shardDataSourceRouteStrategyValue);
        } else {
            BeanDefinition shardDataSourceRouteStrategyBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(shardDataSourceRouteStrategyValue).getBeanDefinition();
            // 可能在此处会进行property的注入
            shardDataSourceBeanDefinitionBuilder.addPropertyValue(ShardDataSource.SHARD_DATA_SOURCE_ROUTE_STRATEGY_PROPERTY_NAME,
                parsePropertyElement(routerElement, shardDataSourceRouteStrategyBeanDefinition, parserContext));
        }
    }


    /**
     * 主从库数据源解析
     *
     * @param dataSourceElement
     * @param masterSlaveElement
     * @param parserContext
     * @return
     */
    private AbstractBeanDefinition parseMasterSlaveDataSource(Element dataSourceElement, Element masterSlaveElement,
        ParserContext parserContext) {
        String shardDataSourceId = this.getShardDataSourceId(dataSourceElement);
        List<AbstractBeanDefinition> beanDefinitions = this.createMasterSlaveBeanDefinitions(dataSourceElement,
            shardDataSourceId, Collections.singletonList(masterSlaveElement), parserContext);
        return beanDefinitions.get(0);
    }

    /**
     * 主从库数据源解析
     *
     * @param dataSourceElement
     * @param shardDataSourceId
     * @param masterSlaveElements
     * @param parserContext
     * @return
     */
    private List<AbstractBeanDefinition> createMasterSlaveBeanDefinitions(Element dataSourceElement,
        String shardDataSourceId, List<Element> masterSlaveElements, ParserContext parserContext) {
        // 1.心跳探测器availabilityDetector
        Element availabilityDetectorElement = DomUtils.getChildElementByTagName(dataSourceElement, AVAILABILITY_DETECTOR);
        // 2.数据库负载均衡策略loadBalanceStrategy
        Element loadBalanceElement = DomUtils.getChildElementByTagName(dataSourceElement, LOAD_BALANCE);
        // 3.pingStatement
        Element pingStatementElement = DomUtils.getChildElementByTagName(dataSourceElement, PING_STATEMENT);
        // dataSourcePrototypeAttributeValue可以为空
        String dataSourcePrototypeAttributeValue = dataSourceElement.getAttribute(DATASOURCE_PROTOTYPE);

        List<AbstractBeanDefinition> masterSlaveDataSourceBeanDefinitions = new ManagedList<AbstractBeanDefinition>();

        for (Element masterSlaveElement : masterSlaveElements) {
            AbstractBeanDefinition masterSlaveBeanDefinition = doCreateMasterSlaveDataSourceBeanDefinition(
                shardDataSourceId,
                masterSlaveElement,
                parserContext,
                dataSourcePrototypeAttributeValue,
                availabilityDetectorElement,
                loadBalanceElement,
                pingStatementElement);
            masterSlaveDataSourceBeanDefinitions.add(masterSlaveBeanDefinition);
        }
        return masterSlaveDataSourceBeanDefinitions;
    }

    /**
     * 解析主从库xml定义，构建主从库datasource
     *
     * @param shardDataSourceId                 数据源id
     * @param masterSlaveElement                数据源组节点
     * @param parserContext
     * @param dataSourcePrototypeAttributeValue 数据源原型
     * @param availabilityDetectorElement       心跳探测器节点
     * @param loadBalanceElement                负载均衡器节点
     * @param pingStatementElement              心跳探测sql语句
     * @return
     */
    private AbstractBeanDefinition doCreateMasterSlaveDataSourceBeanDefinition(
        String shardDataSourceId,
        Element masterSlaveElement,
        ParserContext parserContext,
        String dataSourcePrototypeAttributeValue,
        Element availabilityDetectorElement,
        Element loadBalanceElement,
        Element pingStatementElement) {
        String masterSlaveDataSourceId = masterSlaveElement.getAttribute(NAME);
        if (!StringUtils.hasText(masterSlaveDataSourceId)) {
            throw new IllegalArgumentException("shardDataSourceId: [" + shardDataSourceId + "], attribute '" + NAME +
                "' of element '" + GROUP + "' must be set");
        }
        masterSlaveDataSourceId = masterSlaveDataSourceId.trim();
        BeanDefinitionBuilder masterSlaveDataSourceBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(MasterSlaveDataSource.class);
        masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(ID, masterSlaveDataSourceId);
        masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.SHARD_DATA_SOURCE_ID_PROPERTY_NAME, shardDataSourceId);

        // master必须设置
        Element masterElement = DomUtils.getChildElementByTagName(masterSlaveElement, MASTER);
        if (masterElement == null) {
            throw new IllegalArgumentException("shardDataSourceId: [" + shardDataSourceId + "], Property '" + MASTER +
                "' of element '" + GROUP + "' must be set");
        }

        this.parseMasterDataSource(
            masterElement,
            masterSlaveDataSourceBeanDefinitionBuilder,
            parserContext,
            masterSlaveDataSourceId,
            dataSourcePrototypeAttributeValue);

        // slave可以为空
        List<Element> slaveElements = DomUtils.getChildElementsByTagName(masterSlaveElement, SLAVE);
        if (!CollectionUtils.isEmpty(slaveElements)) {
            this.parseSlaveDataSources(
                slaveElements,
                masterSlaveDataSourceBeanDefinitionBuilder,
                parserContext,
                masterSlaveDataSourceId,
                dataSourcePrototypeAttributeValue);
        }

        // pingStatementElement可以为空
        if (pingStatementElement != null) {
            String pingStatement = pingStatementElement.getAttribute(PING_STATEMENT_VALUE);
            if (StringUtils.hasText(pingStatement)) {
                masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.PING_STATEMENT_PROPERTY_NAME, pingStatement.trim());
            }
        }

        // 心跳探测器availabilityDetector可以为空,为空的时候不进行检测
        if (availabilityDetectorElement != null) {
            this.parseAvailabilityDetector(availabilityDetectorElement, masterSlaveDataSourceBeanDefinitionBuilder, parserContext);
        }

        // 数据库负载均衡策略loadBalanceStrategy可以为空，为空的时候默认为WeightedRandom
        if (loadBalanceElement != null) {
            this.parseLoadBalance(loadBalanceElement, masterSlaveDataSourceBeanDefinitionBuilder, parserContext);
        }

        return masterSlaveDataSourceBeanDefinitionBuilder.getBeanDefinition();
    }

    private void parseAvailabilityDetector(Element availabilityDetectorElement,
        BeanDefinitionBuilder masterSlaveDataSourceBeanDefinitionBuilder, ParserContext parserContext) {
        // ref和class可以都为空，为空的时候为DatabaseAvailabilityDetector
        BeanDefinitionContext availabilityDetectorBeanDefinitionContext = getBeanDefinitionContext(availabilityDetectorElement, CLASS, REF, false);
        String availabilityDetectorValue = availabilityDetectorBeanDefinitionContext.getAttributeValue();

        if (availabilityDetectorBeanDefinitionContext.isRef()) {
            masterSlaveDataSourceBeanDefinitionBuilder.addPropertyReference(MasterSlaveDataSource.AVAILABILITY_DETECTOR_PROPERTY_NAME, availabilityDetectorValue);
        } else {
            AbstractBeanDefinition availabilityDetectorBeanDefinition = createAvailabilityDetectorBeanDefinition(availabilityDetectorElement, availabilityDetectorValue);
            masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.AVAILABILITY_DETECTOR_PROPERTY_NAME,
                parsePropertyElement(availabilityDetectorElement, availabilityDetectorBeanDefinition, parserContext));
        }
    }

    private void parseLoadBalance(Element loadBalanceElement,
        BeanDefinitionBuilder masterSlaveDataSourceBeanDefinitionBuilder, ParserContext parserContext) {
        BeanDefinitionContext loadBalanceBeanDefinitionContext = this.getLoadBalanceBeanDefinitionContext(loadBalanceElement);
        String loadBalanceValue = loadBalanceBeanDefinitionContext.getAttributeValue();
        if (loadBalanceBeanDefinitionContext.isRef()) {
            masterSlaveDataSourceBeanDefinitionBuilder.addPropertyReference(MasterSlaveDataSource.LOAD_BALANCE_PROPERTY_NAME, loadBalanceValue);
        } else {
            AbstractBeanDefinition loadBalanceBeanDefinition = BeanDefinitionBuilder.rootBeanDefinition(loadBalanceValue).getBeanDefinition();
            masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.LOAD_BALANCE_PROPERTY_NAME,
                parsePropertyElement(loadBalanceElement, loadBalanceBeanDefinition, parserContext));
        }
    }

    private void parseMasterDataSource(
        Element masterElement,
        BeanDefinitionBuilder masterSlaveDataSourceBeanDefinitionBuilder,
        ParserContext parserContext,
        String masterSlaveDataSourceId,
        String dataSourcePrototypeAttributeValue) {
        BeanDefinitionBuilder masterBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(SingleDataSource.class);
        masterBeanDefinitionBuilder.addPropertyValue(ID, masterSlaveDataSourceId + ".master");
        masterBeanDefinitionBuilder.addPropertyValue(SingleDataSource.TARGET_DATA_SOURCE_PROPERTY_NAME,
            parseSingleTargetDatasourceBeanDefinition(masterElement, dataSourcePrototypeAttributeValue, parserContext));
        AbstractBeanDefinition masterBeanDefinition = masterBeanDefinitionBuilder.getBeanDefinition();
        masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.MASTER_DATA_SOURCE_PROPERTY_NAME, masterBeanDefinition);
    }

    private AbstractBeanDefinition createSlaveBeanDefinition(
        int index,
        Element slaveElement,
        String masterSlaveDataSourceId,
        String dataSourcePrototypeAttributeValue,
        ParserContext parserContext) {
        BeanDefinitionBuilder slaveBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(SingleDataSource.class);
        slaveBeanDefinitionBuilder.addPropertyValue(ID, masterSlaveDataSourceId + ".slave" + index);
        slaveBeanDefinitionBuilder.addPropertyValue(SingleDataSource.TARGET_DATA_SOURCE_PROPERTY_NAME,
            parseSingleTargetDatasourceBeanDefinition(slaveElement, dataSourcePrototypeAttributeValue, parserContext));

        String strWeight = slaveElement.getAttribute(WEIGHT);

        if (StringUtils.hasText(strWeight)) {
            strWeight = strWeight.trim();
            int weight;
            try {
                weight = Integer.parseInt(strWeight);
            } catch (Throwable ex) {
                throw new IllegalArgumentException("masterSlaveDataSourceId: [" + masterSlaveDataSourceId
                    + "], slave: [" + index + "] '" + WEIGHT + "': [" + strWeight + "] must be int");
            }
            slaveBeanDefinitionBuilder.addPropertyValue(SingleDataSource.WEIGHT_PROPERTY_NAME, weight);
        }

        return slaveBeanDefinitionBuilder.getBeanDefinition();
    }

    private void parseSlaveDataSources(
        List<Element> slaveElements,
        BeanDefinitionBuilder masterSlaveDataSourceBeanDefinitionBuilder,
        ParserContext parserContext,
        String masterSlaveDataSourceId,
        String dataSourcePrototypeAttributeValue) {
        ManagedList<AbstractBeanDefinition> slaveBeanDefinitions = new ManagedList<AbstractBeanDefinition>(slaveElements.size());
        int index = 0;
        for (Element slaveElement : slaveElements) {
            index++;
            AbstractBeanDefinition slaveBeanDefinition = this.createSlaveBeanDefinition(
                index,
                slaveElement,
                masterSlaveDataSourceId,
                dataSourcePrototypeAttributeValue,
                parserContext);
            slaveBeanDefinitions.add(slaveBeanDefinition);
        }
        masterSlaveDataSourceBeanDefinitionBuilder.addPropertyValue(MasterSlaveDataSource.SLAVE_DATA_SOURCES_PROPERTY_NAME, slaveBeanDefinitions);
    }

    /**
     * 解析心跳探测器
     *
     * @param availabilityDetectorElement
     * @return
     */
    private AbstractBeanDefinition createAvailabilityDetectorBeanDefinition(Element availabilityDetectorElement,
        String availabilityDetectorClassName) {
        BeanDefinitionBuilder availabilityDetectorBeanDefinitionBuilder = getAvailabilityDetectorBeanDefinitionBuilder(availabilityDetectorClassName);

        String enableDetector = availabilityDetectorElement.getAttribute(ENABLED);
        String interval = availabilityDetectorElement.getAttribute(INTERVAL);
        String detectPoolSize = availabilityDetectorElement.getAttribute(DETECT_POOL_SIZE);

        if (StringUtils.hasText(enableDetector)) {
            // 是否开启心跳探测
            availabilityDetectorBeanDefinitionBuilder.addPropertyValue(DatabaseAvailabilityDetector.ENABLED_PROPERTY_NAME,
                "true".equals(enableDetector.trim().toLowerCase()));
        }

        if (StringUtils.hasText(interval)) {
            try {
                // 心跳探测间隔
                availabilityDetectorBeanDefinitionBuilder.addPropertyValue(DatabaseAvailabilityDetector.INTERVAL_PROPERTY_NAME,
                    Integer.valueOf(interval.trim()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Property '" + INTERVAL + "' of '" + AVAILABILITY_DETECTOR
                    + "' is not Number format");
            }
        }

        if (StringUtils.hasText(detectPoolSize)) {
            try {
                // 心跳探测线程数
                availabilityDetectorBeanDefinitionBuilder.addPropertyValue(DatabaseAvailabilityDetector.DETECT_POOL_SIZE_PROPERTY_NAME,
                    Integer.valueOf(detectPoolSize.trim()));
            } catch (Exception e) {
                throw new IllegalArgumentException("Property '" + DETECT_POOL_SIZE + "' of '" + AVAILABILITY_DETECTOR
                    + "' is not Number format");
            }
        }

        return availabilityDetectorBeanDefinitionBuilder.getBeanDefinition();
    }

    private BeanDefinitionContext getLoadBalanceBeanDefinitionContext(Element loadBalanceElement) {
        String loadBalanceStrategyType = loadBalanceElement.getAttribute(LOAD_BALANCE_STRATEGY);

        if (StringUtils.hasText(loadBalanceStrategyType)) {
            Class<?> loadBalanceClass = getLoadBalanceClassByType(loadBalanceStrategyType.trim());
            return new BeanDefinitionContext(false, loadBalanceClass.getName());
        }

        return getBeanDefinitionContext(loadBalanceElement, CLASS, REF, true);
    }

    /**
     * 查询对应的负载均衡策略
     *
     * @param loadBalanceStrategyType
     * @return
     */
    private Class<?> getLoadBalanceClassByType(String loadBalanceStrategyType) {
        LoadBalanceStrategyType lbStrategyType = LoadBalanceStrategyType.parse(loadBalanceStrategyType);
        if (null == lbStrategyType) {
            throw new IllegalArgumentException("Property '" + LOAD_BALANCE_STRATEGY + "' of element '"
                + LOAD_BALANCE + "', value: [" + loadBalanceStrategyType + "] can't find LoadBalance Class");
        }

        switch (lbStrategyType) {
            case WEIGHTED_RANDOM:
                return WeightedRandom.class;
            case WEIGHTED_ROUND_ROBIN:
                return WeightedRoundRobin.class;
            default:
                throw new IllegalArgumentException("Property '" + LOAD_BALANCE_STRATEGY + "' of element '"
                    + LOAD_BALANCE + "', value: [" + loadBalanceStrategyType + "] can't find LoadBalance Class");
        }
    }

    /**
     * 根据className和classRefName获取该节点的Class定义以及是否为引用注入
     *
     * @param element
     * @param classAttributeName
     * @param refAttributeName
     * @return
     */
    private BeanDefinitionContext getBeanDefinitionContext(Element element, String classAttributeName,
        String refAttributeName, boolean disallowBothNull) {
        BeanDefinitionContext beanDefinitionContext = new BeanDefinitionContext();
        if (element == null) {
            return beanDefinitionContext;
        }

        String classValue = element.getAttribute(classAttributeName);
        if (!StringUtils.hasText(classValue)) {
            classValue = null;
        } else {
            classValue = classValue.trim();
        }

        String refValue = element.getAttribute(refAttributeName);
        if (!StringUtils.hasText(refValue)) {
            refValue = null;
        } else {
            refValue = refValue.trim();
        }

        if (classValue != null && refValue != null) {
            throw new IllegalArgumentException("Property '" + classAttributeName + "' and '" + refAttributeName
                + "' of element '" + element.getTagName() + "' can not be both set");
        }

        if (classValue == null && refValue == null) {
            if (disallowBothNull) {
                throw new IllegalArgumentException("Property '" + classAttributeName + "' and '" + refAttributeName
                    + "' of element '" + element.getTagName() + "' can not be both null");
            }
            beanDefinitionContext.setAttributeValue(null);
            beanDefinitionContext.setRef(false);
        } else if (classValue == null) {
            beanDefinitionContext.setAttributeValue(refValue);
            beanDefinitionContext.setRef(true);
        } else {
            beanDefinitionContext.setAttributeValue(classValue);
            beanDefinitionContext.setRef(false);
        }

        return beanDefinitionContext;
    }


    /**
     * 根据availabilityDetectorClass获取数据库连接池监控实现，默认DatabaseAvailabilityDetector
     *
     * @param availabilityDetectorClassName
     * @return
     */
    private BeanDefinitionBuilder getAvailabilityDetectorBeanDefinitionBuilder(String availabilityDetectorClassName) {
        if (!StringUtils.hasText(availabilityDetectorClassName)) {
            return BeanDefinitionBuilder.rootBeanDefinition(DatabaseAvailabilityDetector.class);
        }

        // MUST be an instance of AvailabilityDetector
        return BeanDefinitionBuilder.rootBeanDefinition(availabilityDetectorClassName);
    }

    /**
     * 继承prototype基类的属性配置
     *
     * @param targetDataSourceElement
     * @param dataSourcePrototypeAttributeValue
     * @param parserContext
     * @return
     */
    private AbstractBeanDefinition parseSingleTargetDatasourceBeanDefinition(
        Element targetDataSourceElement,
        String dataSourcePrototypeAttributeValue,
        ParserContext parserContext) {
        BeanDefinitionBuilder targetDataSourceBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition();
        if (StringUtils.hasText(dataSourcePrototypeAttributeValue)) {
            targetDataSourceBeanDefinitionBuilder.setParentName(dataSourcePrototypeAttributeValue.trim());
        }

        AbstractBeanDefinition targetDataSourceBeanDefinition = targetDataSourceBeanDefinitionBuilder.getBeanDefinition();

        List<Element> propertyElements = DomUtils.getChildElementsByTagName(targetDataSourceElement, PROPERTY);
        NamedNodeMap attributes = targetDataSourceElement.getAttributes();

        if (!CollectionUtils.isEmpty(propertyElements)) {
            for (Element propertyElement : propertyElements) {
                parserContext.getDelegate().parsePropertyElement(propertyElement, targetDataSourceBeanDefinition);
            }
        }

        if (attributes != null && attributes.getLength() > 0) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node node = attributes.item(i);
                if (!(node instanceof Attr)) {
                    continue;
                }

                Attr attr = (Attr) node;
                String attributeName = attr.getLocalName();
                String attributeValue = attr.getValue();
                MutablePropertyValues pvs = targetDataSourceBeanDefinition.getPropertyValues();
                if (pvs.contains(attributeName)) {
                    parserContext.getReaderContext().error("Property '" + attributeName
                        + "' is already defined using both <property> and inline syntax. "
                        + "Only one approach may be used per property.", attr);
                    continue;
                }
                if (attributeName.endsWith(REF_SUFFIX)) {
                    attributeName = attributeName.substring(0, attributeName.length() - REF_SUFFIX.length());
                    pvs.addPropertyValue(Conventions.attributeNameToPropertyName(attributeName), new RuntimeBeanReference(attributeValue));
                } else {
                    pvs.addPropertyValue(Conventions.attributeNameToPropertyName(attributeName), attributeValue);
                }
            }
        }
        return targetDataSourceBeanDefinition;
    }

    /**
     * 对设置的bean进行properties属性注入
     *
     * @param beanElement
     * @param beanDefinition
     * @param parserContext
     * @return
     */
    private BeanDefinition parsePropertyElement(Element beanElement, BeanDefinition beanDefinition,
        ParserContext parserContext) {
        if (beanElement == null) {
            return beanDefinition;
        }
        List<Element> propertyElements = DomUtils.getChildElementsByTagName(beanElement, PROPERTY);
        if (CollectionUtils.isEmpty(propertyElements)) {
            return beanDefinition;
        }

        for (Element propertyElement : propertyElements) {
            parserContext.getDelegate().parsePropertyElement(propertyElement, beanDefinition);
        }

        return beanDefinition;
    }

    /**
     * 获取数据源类型（分库：含分库不分表/分库分表/主从库分表，主从库：主从库不分表）
     *
     * @param groupElements
     * @return
     */
    private DataSourceType getDataSourceType(Element dataSourceElement, List<Element> groupElements) {
        Assert.isTrue(!CollectionUtils.isEmpty(groupElements));

        if (groupElements.size() > 1) {
            return DataSourceType.SHARD_DATASOURCE;
        }

        if (groupElements.size() == 1) {
            Element dataSourceRouterElement = DomUtils.getChildElementByTagName(dataSourceElement, ROUTER);
            // 主从库设置了router的也认为是分库, 而且必须自定义实现
            if (dataSourceRouterElement != null) {
                return DataSourceType.SHARD_DATASOURCE;
            } else {
                return DataSourceType.MASTER_SLAVE_DATASOURCE;
            }
        }
        return DataSourceType.NONE;
    }
}
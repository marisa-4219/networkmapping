package io.github.zhangxh.networkmapping.factory;

import io.github.zhangxh.networkmapping.annotation.NetworkMapping;
import io.github.zhangxh.networkmapping.utils.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.util.ArrayList;

/**
 * @author zhangxh
 * Network Mapping 代理工厂
 */
public class NetworkMappingRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, BeanFactoryAware {

    private final Logger logger = LoggerFactory.getLogger(NetworkMappingRegistryPostProcessor.class);


//    private final NetworkConfigurationProperties properties;

    private final String mappingPackage;

    private BeanFactory beanFactory;

    public NetworkMappingRegistryPostProcessor(String mappingPackage) {
        this.mappingPackage = mappingPackage;
        logger.info("实例化 networkmapping bean 注册处理器 完成...");
    }

    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        logger.info("扫描注册" + mappingPackage + "下的networkmapping...");
        ArrayList<Class<?>> classes = new ArrayList<>(ClassScanner.scan(mappingPackage, NetworkMapping.class));
        for (Class<?> mappingClass : classes) {
            RootBeanDefinition definition = new RootBeanDefinition();
            definition.setLazyInit(true);
            definition.setBeanClass(NetworkMappingProxyFactoryBean.class);
            definition.getPropertyValues().add("proxyTarget", mappingClass).add("beanFactory", beanFactory);
            String name = BeanDefinitionReaderUtils.generateBeanName(definition, beanDefinitionRegistry);
            beanDefinitionRegistry.registerBeanDefinition(name, definition);
            logger.info("注册 networkmapping -> " + mappingClass.getName());
        }
        logger.info("共 " + classes.size() + " 个...");

    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }


//
//
//
//
//

//
//
//    private Object newInstance(Class<?> target) {
//        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{target}, new NetworkMappingProxy(properties, storage, restTemplate, target));
//    }
//
//
//    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
//
//
////        restTemplate = context.getBean(RestTemplate.class);
////        storage = context.getBean(properties.getStorage());
//        try {
//            List<Class<?>> classes = handleScanMappingInterface();
//            for (Class<?> mapping : classes) {
//                // 通过代理生成实例
//                Object instance = newInstance(mapping);
//                // 生成bean名称
//                String simpleName = mapping.getSimpleName();
//                simpleName = simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
//                // 注册bean
//
//                RootBeanDefinition beanDefinition = new RootBeanDefinition(mapping);
//                beanDefinition.setTargetType(mapping);
//                beanDefinition.setFactoryBeanName();
//                beanDefinition.setFac
//
//
//                registry.registerBeanDefinition();
//
////                registry.(simpleName, instance);
//            }
//
//        } catch (Exception e) {
//            throw new ScanMappingInterfaceException(e.getMessage(), e);
//        }
//
//
//    }
//

}

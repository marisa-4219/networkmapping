package io.github.zhangxh.networkmapping.config;

import io.github.zhangxh.networkmapping.factory.NetworkMappingRegistryPostProcessor;
import io.github.zhangxh.networkmapping.properties.NetworkConfigurationProperties;
import io.github.zhangxh.networkmapping.storage.IApplicationStorage;
import io.github.zhangxh.networkmapping.storage.impl.DefaultApplicationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * 网络接口映射配置类
 *
 * @author zhangxh
 */
@ComponentScan("io.github.zhangxh.networkmapping")
@Configuration
@EnableConfigurationProperties({NetworkConfigurationProperties.class})
public class NetworkConfiguration {
    private final Logger logger = LoggerFactory.getLogger(NetworkConfiguration.class);


    /**
     * 默认仓库
     */
    @Bean
    @Lazy
    @ConditionalOnMissingBean(IApplicationStorage.class)
    public DefaultApplicationStorage initDefaultApplicationStorage() {
        logger.info("实例化 networkmapping 默认 storage -> DefaultApplicationStorage");
        return new DefaultApplicationStorage();
    }

    @Bean
    public BeanDefinitionRegistryPostProcessor initNetworkMappingRegistryPostProcessor(Environment env) {
        logger.info("初始化 networkmapping...");
        String mappingPackage = env.getProperty("spring.networkmapping.mapping-package");
        if (StringUtils.isEmpty(mappingPackage)) {
            mappingPackage = env.getProperty("spring.networkmapping.mappingPackage");
        }
        if (StringUtils.isEmpty(mappingPackage)) {
            throw new IllegalArgumentException("初始化networkmapping失败，mapping-package不能为空！");
        }
        logger.info("实例化 networkmapping bean 注册处理器...");
        return new NetworkMappingRegistryPostProcessor(mappingPackage);
    }


}


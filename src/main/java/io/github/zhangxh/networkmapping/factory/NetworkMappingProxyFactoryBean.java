package io.github.zhangxh.networkmapping.factory;

import io.github.zhangxh.networkmapping.handler.IGlobalResponseHandler;
import io.github.zhangxh.networkmapping.handler.impl.DefaultGlobalResponseHandler;
import io.github.zhangxh.networkmapping.properties.NetworkConfigurationProperties;
import io.github.zhangxh.networkmapping.proxy.NetworkMappingProxy;
import io.github.zhangxh.networkmapping.storage.IApplicationStorage;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;


/**
 * 代理工厂
 */
public class NetworkMappingProxyFactoryBean implements FactoryBean<Object> {

    private NetworkConfigurationProperties properties;

    private IGlobalResponseHandler responseHandler;

    private IApplicationStorage storage;

    private RestTemplate restTemplate;

    private BeanFactory beanFactory;

    private Class<?> proxyTarget;

    public NetworkConfigurationProperties getProperties() {
        if (properties == null) properties = beanFactory.getBean(NetworkConfigurationProperties.class);
        return properties;
    }

    public void setProperties(NetworkConfigurationProperties properties) {
        this.properties = properties;
    }

    public IApplicationStorage getStorage() {
        if (storage == null) storage = beanFactory.getBean(getProperties().getStorage());
        return storage;
    }


    public IGlobalResponseHandler getResponseHandler() {
        try {
            if (responseHandler == null) responseHandler = beanFactory.getBean(IGlobalResponseHandler.class);
        } catch (BeansException ignored) {
            responseHandler = new DefaultGlobalResponseHandler();
        }
        return responseHandler;
    }

    public void setResponseHandler(IGlobalResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setStorage(IApplicationStorage storage) {
        this.storage = storage;
    }

    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            factory.setConnectTimeout(Long.valueOf(getProperties().getRequestWaitTimeout()).intValue());
            factory.setReadTimeout(Long.valueOf(getProperties().getResponseWaitTimeout()).intValue());
            restTemplate = new RestTemplate(factory);
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            restTemplate.setErrorHandler(new ResponseErrorHandler() {
                @Override
                public boolean hasError(ClientHttpResponse response) throws IOException {
                    return false;
                }

                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                }
            });
        }
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Class<?> getProxyTarget() {
        return proxyTarget;
    }

    public void setProxyTarget(Class<?> proxyTarget) {
        this.proxyTarget = proxyTarget;
    }

    private Object newInstance(Class<?> target) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{target}, new NetworkMappingProxy(getProperties(), getStorage(),getResponseHandler(), getRestTemplate(), target));
    }

    @Override
    public Object getObject() throws Exception {
        return newInstance(proxyTarget);
    }

    @Override
    public Class<?> getObjectType() {
        return proxyTarget;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

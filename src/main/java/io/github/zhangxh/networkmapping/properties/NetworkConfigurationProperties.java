package io.github.zhangxh.networkmapping.properties;


import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;
import io.github.zhangxh.networkmapping.formatter.impl.DefaultResponseFormatter;
import io.github.zhangxh.networkmapping.storage.IApplicationStorage;
import io.github.zhangxh.networkmapping.storage.impl.DefaultApplicationStorage;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网络接口映射配置信息
 *
 * @author zhangxh
 */
@ConfigurationProperties(prefix = "spring.networkmapping")
public class NetworkConfigurationProperties {

    /**
     * 服务端地址
     */
    private String serverHost;
    /**
     * mapping文件扫描起始路径
     */
    private String mappingPackage;
    /**
     * storage默认实现
     */
    private Class<? extends IApplicationStorage> storage = DefaultApplicationStorage.class;
    /**
     * 响应结果格式化默认实现
     */
    private Class<? extends INetworkMappingResponseFormatter> formatter = DefaultResponseFormatter.class;
    /**
     * 请求等待时间（连接等待）
     */
    private long requestWaitTimeout = 1000L;
    /**
     * 响应的等待时间
     */
    private long responseWaitTimeout = 1000L;
    /**
     * 认证请求头名称
     */
    private String headerAuthorizationProperty = "Authorization";
    /**
     * 认证请求头前缀
     */
    private String headerAuthorizationPrefix = "Bearer ";
    /**
     * 异步处理线程池大小
     */
    private int asyncMaxThreadPoolSize = 100;

    /**
     * 开启debug
     */
    private boolean debugEnabled = false;

    /**
     * debug时，响应结果截断位置
     */
    private int debugResponseBodyTruncationLength = 180;

    public NetworkConfigurationProperties() {
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getMappingPackage() {
        return mappingPackage;
    }

    public void setMappingPackage(String mappingPackage) {
        this.mappingPackage = mappingPackage;
    }

    public Class<? extends IApplicationStorage> getStorage() {
        return storage;
    }

    public void setStorage(Class<? extends IApplicationStorage> storage) {
        this.storage = storage;
    }

    public Class<? extends INetworkMappingResponseFormatter> getFormatter() {
        return formatter;
    }

    public void setFormatter(Class<? extends INetworkMappingResponseFormatter> formatter) {
        this.formatter = formatter;
    }

    public long getRequestWaitTimeout() {
        return requestWaitTimeout;
    }

    public void setRequestWaitTimeout(long requestWaitTimeout) {
        this.requestWaitTimeout = requestWaitTimeout;
    }

    public long getResponseWaitTimeout() {
        return responseWaitTimeout;
    }

    public void setResponseWaitTimeout(long responseWaitTimeout) {
        this.responseWaitTimeout = responseWaitTimeout;
    }

    public String getHeaderAuthorizationProperty() {
        return headerAuthorizationProperty;
    }

    public void setHeaderAuthorizationProperty(String headerAuthorizationProperty) {
        this.headerAuthorizationProperty = headerAuthorizationProperty;
    }

    public String getHeaderAuthorizationPrefix() {
        return headerAuthorizationPrefix;
    }

    public void setHeaderAuthorizationPrefix(String headerAuthorizationPrefix) {
        this.headerAuthorizationPrefix = headerAuthorizationPrefix;
    }

    public int getAsyncMaxThreadPoolSize() {
        return asyncMaxThreadPoolSize;
    }

    public void setAsyncMaxThreadPoolSize(int asyncMaxThreadPoolSize) {
        this.asyncMaxThreadPoolSize = asyncMaxThreadPoolSize;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public int getDebugResponseBodyTruncationLength() {
        return debugResponseBodyTruncationLength;
    }

    public void setDebugResponseBodyTruncationLength(int debugResponseBodyTruncationLength) {
        this.debugResponseBodyTruncationLength = debugResponseBodyTruncationLength;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}

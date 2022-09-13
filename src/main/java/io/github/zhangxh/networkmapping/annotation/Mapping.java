package io.github.zhangxh.networkmapping.annotation;

import io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;
import io.github.zhangxh.networkmapping.formatter.impl.InvalidResponseFormatter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 映射接口定义
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Mapping {

    String value();

    /**
     * @return 请求方式
     */
    HttpMethod method() default HttpMethod.POST;

    /**
     * @return 数据格式
     */
    String mediaType() default MediaType.APPLICATION_JSON_VALUE;

    /**
     * @return token认证
     */
    boolean auth() default true;

    /**
     * @return 异步请求
     */
    boolean async() default false;

    /**
     * 响应数据格式化处理器
     */
    Class<? extends INetworkMappingResponseFormatter> formatter() default InvalidResponseFormatter.class;

    /**
     * 服务端地址
     */
    String serverHost() default "";
}



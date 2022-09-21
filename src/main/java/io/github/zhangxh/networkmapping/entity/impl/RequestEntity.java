package io.github.zhangxh.networkmapping.entity.impl;

import io.github.zhangxh.networkmapping.entity.IBody;
import io.github.zhangxh.networkmapping.entity.IParam;
import  io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;
import  io.github.zhangxh.networkmapping.handler.IAsyncResponseHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.lang.reflect.Type;

public class RequestEntity {

    private String requestUrl;

    private IBody body;

    private IParam param;
    private MediaType mediaType;

    private HttpMethod httpMethod;

    private HttpHeaders httpHeaders;

    private boolean auth;

    private boolean async;

    private IAsyncResponseHandler<Object> asyncHandlerImpl;

    private Class<? extends INetworkMappingResponseFormatter> formatter;

    private Class<?> resultType;

    private Type resultGenericType;

    private boolean quiet;

    public RequestEntity() {
    }

    public RequestEntity(String requestUrl, IBody body,IParam param, MediaType mediaType, HttpMethod httpMethod, HttpHeaders httpHeaders, boolean auth, boolean async, IAsyncResponseHandler<Object> asyncHandlerImpl, Class<? extends INetworkMappingResponseFormatter> formatter, Class<?> resultType, Type resultGenericType) {
        this.requestUrl = requestUrl;
        this.body = body;
        this.param =param;
        this.mediaType = mediaType;
        this.httpMethod = httpMethod;
        this.httpHeaders = httpHeaders;
        this.auth = auth;
        this.async = async;
        this.asyncHandlerImpl = asyncHandlerImpl;
        this.formatter = formatter;
        this.resultType = resultType;
        this.resultGenericType = resultGenericType;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public IBody getBody() {
        return body;
    }

    public void setBody(IBody body) {
        this.body = body;
    }

    public IParam getParam() {
        return param;
    }

    public RequestEntity setParam(IParam param) {
        this.param = param;
        return this;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public IAsyncResponseHandler<Object> getAsyncHandlerImpl() {
        return asyncHandlerImpl;
    }

    public void setAsyncHandlerImpl(IAsyncResponseHandler<Object> asyncHandlerImpl) {
        this.asyncHandlerImpl = asyncHandlerImpl;
    }

    public Class<? extends INetworkMappingResponseFormatter> getFormatter() {
        return formatter;
    }

    public void setFormatter(Class<? extends INetworkMappingResponseFormatter> formatter) {
        this.formatter = formatter;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public Type getResultGenericType() {
        return resultGenericType;
    }

    public void setResultGenericType(Type resultGenericType) {
        this.resultGenericType = resultGenericType;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public RequestEntity setQuiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }
}



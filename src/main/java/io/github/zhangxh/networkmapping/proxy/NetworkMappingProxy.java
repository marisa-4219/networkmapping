package io.github.zhangxh.networkmapping.proxy;

import io.github.zhangxh.networkmapping.annotation.MakeQuiet;
import io.github.zhangxh.networkmapping.annotation.Mapping;
import io.github.zhangxh.networkmapping.annotation.MappingPathVariable;
import io.github.zhangxh.networkmapping.annotation.NetworkMapping;
import io.github.zhangxh.networkmapping.entity.RequestEntity;
import io.github.zhangxh.networkmapping.entity.request.*;
import io.github.zhangxh.networkmapping.exception.RequestException;
import io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;
import io.github.zhangxh.networkmapping.formatter.impl.InvalidResponseFormatter;
import io.github.zhangxh.networkmapping.handler.IAsyncResponseHandler;
import io.github.zhangxh.networkmapping.handler.IGlobalResponseHandler;
import io.github.zhangxh.networkmapping.properties.NetworkConfigurationProperties;
import io.github.zhangxh.networkmapping.storage.IApplicationStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkMappingProxy implements InvocationHandler {

    private final NetworkConfigurationProperties properties;

    private final IApplicationStorage storage;

    private final RestTemplate restTemplate;

    private final IGlobalResponseHandler responseHandler;

    private final Class<?> proxyTarget;

    private final ExecutorService threadPool;

    public NetworkMappingProxy(NetworkConfigurationProperties properties, IApplicationStorage storage, IGlobalResponseHandler responseHandler, RestTemplate restTemplate, Class<?> proxyTarget) {
        this.properties = properties;
        this.storage = storage;
        this.restTemplate = restTemplate;
        this.responseHandler = responseHandler;
        this.proxyTarget = proxyTarget;
        this.threadPool = Executors.newFixedThreadPool(properties.getAsyncMaxThreadPoolSize());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 获取类注解
        NetworkMapping classAnnotation = proxyTarget.getAnnotation(NetworkMapping.class);
        if (classAnnotation == null) return null;
        // 获取方法注解
        Mapping methodAnnotation = method.getAnnotation(Mapping.class);
        MakeQuiet makeQuietAnnotation = method.getAnnotation(MakeQuiet.class);
        if (methodAnnotation == null) return null;
        // 如果定义了host
        IHost host = getInterfaceImplForArgs(args, IHost.class);
        // 如果定义了路径
        IPath customURL = getInterfaceImplForArgs(args, IPath.class);
        // 获取请求地址
        String requestUrl = getRequestUrl(classAnnotation, methodAnnotation, host, customURL);

        // 获取方法参数定义
        Parameter[] parameters = method.getParameters();
        if (parameters != null) {
            // 判断是否存在PathVariable
            for (int i = 0; i < parameters.length; i++) {
                Parameter define = parameters[i];
                MappingPathVariable annotation = define.getAnnotation(MappingPathVariable.class);
                if (annotation != null) {
                    String variableName = annotation.value();
                    if (!StringUtils.hasText(variableName))
                        throw new IllegalArgumentException("variable name is not empty!");
                    String regEx = "{" + variableName + "}";
                    requestUrl = requestUrl.replace(regEx, String.valueOf(args[i]));
                }
            }
        }
        // 获取参数
        IQuery query = getInterfaceImplForArgs(args, IQuery.class);
        IBody body = getInterfaceImplForArgs(args, IBody.class);
        IForm form = getInterfaceImplForArgs(args, IForm.class);


        // 获取异步请求响应处理对象
        //noinspection unchecked
        final IAsyncResponseHandler<Object> asyncHandlerImpl = getInterfaceImplForArgs(args, IAsyncResponseHandler.class);
        // 获取返回值类型
        final Class<?> resultClass = method.getReturnType();
        // 获取返回值泛型
        final Type resultGenericType = getGenericReturnType(method);
        // 请求方式
        final HttpMethod requestMethod = methodAnnotation.method();
        // 数据格式
        final MediaType mediaType = MediaType.parseMediaType(methodAnnotation.mediaType());
        // 是否认证
        final boolean auth = methodAnnotation.auth();
        // 异步请求
        final boolean async = methodAnnotation.async();

        // 请求头
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(mediaType);
        if (auth) {
            requestHeaders.set(properties.getHeaderAuthorizationProperty(), properties.getHeaderAuthorizationPrefix() + storage.getToken());
        }

        IHead headers = getInterfaceImplForArgs(args, IHead.class);
        if (headers != null) {
            Map<String, String> map = headers.toMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                requestHeaders.set(entry.getKey(), entry.getValue());
            }
        }

        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setRequestUrl(requestUrl);
        requestEntity.setMediaType(mediaType);
        requestEntity.setHttpMethod(requestMethod);
        requestEntity.setHttpHeaders(requestHeaders);
        requestEntity.setAuth(auth);
        requestEntity.setAsync(async);
        requestEntity.setQuery(query);
        requestEntity.setAsyncHandlerImpl(asyncHandlerImpl);
        requestEntity.setFormatter(properties.getFormatter());
        requestEntity.setResultType(resultClass);
        requestEntity.setResultGenericType(resultGenericType);
        requestEntity.setQuiet(makeQuietAnnotation != null);

        // 填充参数
        fullParameter(requestEntity, query, form, body);

        if (methodAnnotation.formatter() != null && !methodAnnotation.formatter().equals(InvalidResponseFormatter.class)) {
            requestEntity.setFormatter(methodAnnotation.formatter());
        }
        // 请求
        return request(method, requestEntity);
    }

    private void fullParameter(RequestEntity requestEntity, IQuery query, IForm form, IBody body) {
        boolean isSpliced = false;
        String queryContent = null;
        String formContent = null;
        String bodyContent = null;

        if (query != null && StringUtils.hasText(query.getContent())) queryContent = query.getContent();
        if (form != null && StringUtils.hasText(form.getContent())) formContent = form.getContent();
        if (body != null && StringUtils.hasText(body.getContent())) bodyContent = body.getContent();

        // query parameter
        if (StringUtils.hasText(queryContent)) {
            requestEntity.setRequestUrl(requestEntity.getRequestUrl() + queryContent);
            isSpliced = true;
        }
        // body parameter
        if (StringUtils.hasText(bodyContent)) {
            requestEntity.setParameter(body);
            // 如果body和form同时存在 让form以query方式传输
            if (StringUtils.hasText(formContent)) {
                requestEntity.setRequestUrl(requestEntity.getRequestUrl() + (isSpliced ? "" : "?") + formContent);
            }
        } else if (StringUtils.hasText(formContent)) {
            requestEntity.setParameter(form);
        }
    }

    public Object request(Method method, RequestEntity requestEntity) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(method.getDeclaringClass().getName() + "." + method.getName());
        // 发出请求
        if (logger.isDebugEnabled() && properties.isDebugEnabled()) {
            logger.debug("请求 ->\n\n url：\t\t\t\t" + requestEntity.getRequestUrl()
                    + "\n headers：\t\t\t\t" + requestEntity.getHttpHeaders()
                    + "\n args：\t\t\t\t" + requestEntity.getParameter()
                    + "\n method：\t\t\t" + requestEntity.getHttpMethod()
                    + "\n contentType：\t\t" + requestEntity.getMediaType() + "\n");
        }

        HttpEntity<String> httpEntity = new HttpEntity<>(requestEntity.getParameter() != null ? requestEntity.getParameter().getContent() : null, requestEntity.getHttpHeaders());

        if (requestEntity.isAsync()) {
            asyncRequest(method, requestEntity, httpEntity);
            return null;
        } else {
            return awaitRequest(method, requestEntity, httpEntity);
        }
    }


    public void asyncRequest(Method method, RequestEntity requestEntity, HttpEntity<String> httpEntity) {
        threadPool.execute(() -> {
            IAsyncResponseHandler<Object> asyncHandlerImpl = requestEntity.getAsyncHandlerImpl();
            try {
                asyncHandlerImpl.success(awaitRequest(method, requestEntity, httpEntity));
            } catch (Throwable e) {
                asyncHandlerImpl.error(e);
            }

        });
    }


    public Object awaitRequest(Method method, RequestEntity requestEntity, HttpEntity<String> httpEntity) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(method.getDeclaringClass().getName() + "." + method.getName());

        long startTime = System.currentTimeMillis();

        Class<? extends INetworkMappingResponseFormatter> formatterClass = requestEntity.getFormatter();

        INetworkMappingResponseFormatter formatter;

        try {
            formatter = formatterClass.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RequestException(e);
        }

        responseHandler.setRequestEntity(requestEntity);

        ResponseEntity<byte[]> response;
        Charset responseCharset = StandardCharsets.UTF_8;

        try {
            switch (requestEntity.getHttpMethod()) {
                case POST:
                    response = restTemplate.postForEntity(requestEntity.getRequestUrl(), httpEntity, byte[].class);
                    break;
                case GET:
                    response = restTemplate.exchange(requestEntity.getRequestUrl(), HttpMethod.GET, httpEntity, byte[].class);
                    break;
                default:
                    throw new IllegalArgumentException("不支持的请求方式 -> " + requestEntity.getHttpMethod());
            }

            MediaType mediaType = response.getHeaders().getContentType();
            if (mediaType != null && mediaType.getCharset() != null) {
                responseCharset = mediaType.getCharset();
            }

            if (logger.isDebugEnabled() && properties.isDebugEnabled()) {
                String responseStr = null;
                if (response.getBody() != null) {
                    responseStr = shortenLongText(new String(response.getBody(), responseCharset), properties.getDebugResponseBodyTruncationLength());
                }
                logger.debug("响应 - >\n\n url：\t\t\t\t" + requestEntity.getRequestUrl() + "\n response：\t\t\t" + responseStr + "\n time:\t\t\t\t" + (System.currentTimeMillis() - startTime) + "ms\n");
            }

            return responseHandler.response(formatter.format(response.getBody(), requestEntity, response.getHeaders()));
        } catch (HttpServerErrorException e) {
            byte[] errorMsgByteArray = null;
            if (e.getMessage() != null) {
                errorMsgByteArray = e.getMessage().getBytes(StandardCharsets.UTF_8);
            }
            return responseHandler.response(formatter.format(errorMsgByteArray, requestEntity, null));
        } catch (Throwable e) {
            responseHandler.exception(e);
            return null;
        }

    }


    public String getRequestUrl(NetworkMapping classAnnotation, Mapping methodAnnotation, IHost host, IPath customURL) {
        if (customURL != null && customURL.isOverwriteHost()) {
            return customURL.getURL();
        }
        // 请求地址
        String url = methodAnnotation.value();
        if (customURL != null) {
            url = customURL.getURL();
        }

        if (StringUtils.hasText(classAnnotation.value())) {
            if (classAnnotation.value().endsWith("/") && url.startsWith("/")) {
                url = classAnnotation.value() + url.substring(1);
            } else if (!classAnnotation.value().endsWith("/") && !url.startsWith("/")) {
                url = classAnnotation.value() + "/" + url;
            } else {
                url = classAnnotation.value() + url;
            }
        }
        String serverHost = properties.getServerHost();
        if (StringUtils.hasText(classAnnotation.serverHost())) {
            serverHost = classAnnotation.serverHost();
        }
        if (StringUtils.hasText(methodAnnotation.serverHost())) {
            serverHost = methodAnnotation.serverHost();
        }
        if (host != null && StringUtils.hasText(host.getHost())) {
            serverHost = host.getHost();
        }
        return serverHost + url;
    }


    public <T> T getInterfaceImplForArgs(Object[] args, Class<T> interfaceClass) {
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && (arg.getClass().equals(interfaceClass) || interfaceClass.isAssignableFrom(arg.getClass()))) {
                    //noinspection unchecked
                    return (T) arg;
                }
            }
        }
        return null;
    }


    public Type getGenericReturnType(Method method) {
        if (method.getGenericReturnType() instanceof ParameterizedType) {
            return ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        }
        return null;
    }


    private static String shortenLongText(String str, int index) {
        if (str == null) return null;
        return str.length() - 1 >= index ? str.substring(0, index) + " ...(省略" + (str.length() - 1 - index) + "个字符)" : str;
    }

}
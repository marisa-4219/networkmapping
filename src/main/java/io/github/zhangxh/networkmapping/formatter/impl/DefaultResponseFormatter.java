package io.github.zhangxh.networkmapping.formatter.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import io.github.zhangxh.networkmapping.entity.RequestEntity;
import io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;
import org.springframework.http.HttpHeaders;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class DefaultResponseFormatter implements INetworkMappingResponseFormatter {
    @Override
    public Object format(byte[] response, RequestEntity requestEntity, HttpHeaders headers) {
        String json = null;
        if (response != null) {
            json = new String(response, StandardCharsets.UTF_8);
        }
        return JSONObject.parseObject(json, new ParameterizedTypeImpl(new Type[]{requestEntity.getResultGenericType()}, null, requestEntity.getResultType()));
    }
}

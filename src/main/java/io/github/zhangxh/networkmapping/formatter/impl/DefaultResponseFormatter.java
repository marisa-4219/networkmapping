package io.github.zhangxh.networkmapping.formatter.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import io.github.zhangxh.networkmapping.entity.impl.RequestEntity;
import io.github.zhangxh.networkmapping.formatter.INetworkMappingResponseFormatter;

import java.lang.reflect.Type;

public class DefaultResponseFormatter implements INetworkMappingResponseFormatter {
    @Override
    public Object format(String response, RequestEntity requestEntity) {
        return JSONObject.parseObject(response, new ParameterizedTypeImpl(new Type[]{requestEntity.getResultGenericType()}, null, requestEntity.getResultType()));
    }
}

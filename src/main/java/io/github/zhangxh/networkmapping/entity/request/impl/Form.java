package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IForm;
import org.springframework.core.io.AbstractResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Form implements IForm {
    private final Map<String, Object> variable = new ConcurrentHashMap<>();
    private final MultiValueMap<String, Object> multiValue = new LinkedMultiValueMap<>();

    public Form set(String k, Object v) {
        variable.put(k, v);
        multiValue.set(k, v);
        return this;
    }

    @Override
    public Object getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : variable.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }


    @Override
    public Object getRaw() {
        return multiValue;
    }

    public String toString() {
        Map<String, Object> map = new HashMap<>();
        variable.forEach((k, v) -> {
            if (v instanceof AbstractResource) {
                map.put(k, "[Binary data]");
            }else{
                map.put(k, v);
            }
        });
        return JSON.toJSONString(map);
    }
}

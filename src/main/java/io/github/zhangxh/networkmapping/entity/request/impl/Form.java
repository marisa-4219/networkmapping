package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IForm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Form implements IForm {
    private Map<String, String> variable = new ConcurrentHashMap<>();

    public Form set(String k, String v) {
        variable.put(k, v);
        return this;
    }

    @Override
    public String getContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : variable.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

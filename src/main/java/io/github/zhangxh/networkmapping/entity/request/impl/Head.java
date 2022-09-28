package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IHead;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Head implements IHead {

    public final Map<String, String> container = new ConcurrentHashMap<>();

    public Head append(String key, String value) {
        container.put(key, value);
        return this;
    }

    public String get(String key) {
        return container.get(key);
    }

    @Override
    public Map<String, String> toMap() {
        return container;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

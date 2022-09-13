package io.github.zhangxh.networkmapping.entity.impl;

import  io.github.zhangxh.networkmapping.entity.IHeader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Header implements IHeader {

    public final Map<String, String> container = new ConcurrentHashMap<>();

    public void append(String key, String value) {
        container.put(key, value);
    }

    public String get(String key) {
        return container.get(key);
    }

    @Override
    public Map<String, String> toMap() {
        return container;
    }
}

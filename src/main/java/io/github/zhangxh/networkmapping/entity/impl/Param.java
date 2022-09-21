package io.github.zhangxh.networkmapping.entity.impl;

import io.github.zhangxh.networkmapping.entity.IParam;

import java.util.HashMap;

public class Param extends HashMap<String, Object> implements IParam {

    public Param set(String property, Object val) {
        this.put(property, val);
        return this;
    }
}

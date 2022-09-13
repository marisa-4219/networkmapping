package io.github.zhangxh.networkmapping.entity.impl;

import  io.github.zhangxh.networkmapping.entity.IFormData;

import java.util.HashMap;

public class FormData extends HashMap<String, Object> implements IFormData {

    public FormData set(String property, Object val) {
        this.put(property, val);
        return this;
    }
}

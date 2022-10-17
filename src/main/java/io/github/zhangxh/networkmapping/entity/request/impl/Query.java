package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IQuery;

import java.util.HashMap;

public class Query extends HashMap<String, String> implements IQuery {

    public Query set(String property, Object val) {
        this.put(property, val == null ? null : val.toString());
        return this;
    }

    @Override
    public String getContent() {
        if (!this.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("?");
            this.forEach((key, value) -> builder.append(key).append("=").append(value).append("&"));
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        } else {
            return "";
        }
    }

    @Override
    public Object getRaw() {
        return this;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

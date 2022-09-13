package io.github.zhangxh.networkmapping.storage.impl;

import io.github.zhangxh.networkmapping.storage.IApplicationStorage;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的应用数据仓库实现
 */
public class DefaultApplicationStorage extends ConcurrentHashMap<String, Object> implements IApplicationStorage {

    public static final String TOKEN = "token";

    /**
     * 写入仓库
     */
    public <T> void write(String key, T val) {
        this.put(key, val);
    }


    /**
     * 读取仓库
     */
    @SuppressWarnings("unchecked")
    public <T> T read(String key) {
        if (!this.containsKey(key)) return null;
        Object val = this.get(key);
        return (T) val;
    }

    /**
     * 设置token
     */
    public void setToken(String token, Date expireTime) {
        this.write("token", token);
        this.write("expireTime", expireTime);
    }


    /**
     * 读取token
     */
    @Override
    public String getToken() {
        return read(TOKEN);
    }

}

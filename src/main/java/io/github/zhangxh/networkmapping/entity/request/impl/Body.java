package io.github.zhangxh.networkmapping.entity.request.impl;

import com.alibaba.fastjson.JSON;
import io.github.zhangxh.networkmapping.entity.request.IBody;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Body extends HashMap<String, Object> implements IBody {

    public Body set(String property, Object val) {
        this.put(property, val);
        return this;
    }

    public static Body format(Object o) {
        Body param = new Body();
        param.putAll(format2Map(o));
        return param;
    }

    private static Map<String, Object> format2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())) continue;
                if (Modifier.isStatic(field.getModifiers())) continue;
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Object getContent() {
        return JSON.toJSONString(this);
    }


    @Override
    public Object getRaw() {
        return this;
    }

    public String toString(){
        return JSON.toJSONString(this);
    }
}

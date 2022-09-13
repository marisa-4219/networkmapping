package io.github.zhangxh.networkmapping.entity.impl;

import  io.github.zhangxh.networkmapping.entity.IParam;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class Param extends HashMap<String, Object> implements IParam {

    public Param set(String property, Object val) {
        this.put(property, val);
        return this;
    }

    public static Param format(Object o) {
        Param param = new Param();
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

}

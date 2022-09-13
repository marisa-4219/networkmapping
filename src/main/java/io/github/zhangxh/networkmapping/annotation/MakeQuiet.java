package io.github.zhangxh.networkmapping.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 静默处理返回异常（体现在响应处理中requestEntity的isQuiet为True）
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MakeQuiet {
}

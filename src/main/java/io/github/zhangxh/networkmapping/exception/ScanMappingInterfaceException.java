package io.github.zhangxh.networkmapping.exception;

import org.springframework.beans.BeansException;

/**
 * 包扫描异常定义
 *
 * @author zhangxh
 */
public class ScanMappingInterfaceException extends BeansException {

    public ScanMappingInterfaceException(String msg) {
        super(msg);
    }

    public ScanMappingInterfaceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}

package com.atguigu.crowd.exception;
/**
 * 用户未登录访问收保护资源时抛出的异常
 * @author hjp
 */
public class AccessForbidden extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public AccessForbidden() {
    }

    public AccessForbidden(String message) {
        super(message);
    }

    public AccessForbidden(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbidden(Throwable cause) {
        super(cause);
    }

    public AccessForbidden(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
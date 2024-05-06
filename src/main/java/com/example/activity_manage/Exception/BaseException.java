package com.example.activity_manage.Exception;

/**
 * 业务异常——全局处理
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}

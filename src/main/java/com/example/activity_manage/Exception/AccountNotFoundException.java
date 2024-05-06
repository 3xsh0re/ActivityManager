package com.example.activity_manage.Exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {

    public AccountNotFoundException() {
    }
    public AccountNotFoundException(String msg) {
        super(msg);
    }
}

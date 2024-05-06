package com.example.activity_manage.Exception;

public class DuplicatePhoneException extends BaseException{
    public DuplicatePhoneException(){};

    public DuplicatePhoneException(String msg)
    {
        super(msg);
    }
}

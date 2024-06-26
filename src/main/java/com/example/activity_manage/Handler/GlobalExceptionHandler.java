package com.example.activity_manage.Handler;

import com.example.activity_manage.Constant.MessageConstant;
import com.example.activity_manage.Exception.BaseException;
import com.example.activity_manage.Result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获业务异常
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        return Result.error(ex.getMessage());
    }
}
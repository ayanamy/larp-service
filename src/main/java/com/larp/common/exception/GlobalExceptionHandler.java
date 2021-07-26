package com.larp.common.exception;


import com.larp.common.lang.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(CommonException.class)
    public Object handlerException(CommonException e) {
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Object handlerException(Exception e) {
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

}

package org.example.interceptor;


import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一异常处理类
 */
@RestControllerAdvice
@Order(99)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseResult validationExceptionHandler(Exception e){
        return ResponseResult.fail(CommonStatusEnum.FAIL.getCode(),CommonStatusEnum.FAIL.getValue());
    }

}

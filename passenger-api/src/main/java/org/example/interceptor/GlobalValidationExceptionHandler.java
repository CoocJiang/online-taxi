package org.example.interceptor;


import org.example.constant.CommonStatusEnum;
import org.example.dto.ResponseResult;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class GlobalValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult validationExceptionHandler(MethodArgumentNotValidException e){
        e.printStackTrace();
        return ResponseResult.fail(CommonStatusEnum.VALIDATION_EXCEPTION.getCode(),e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}

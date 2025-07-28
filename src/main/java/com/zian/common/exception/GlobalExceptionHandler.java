package com.zian.common.exception;

import com.zian.common.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler (BusinessException e) {
        log.info("BusinessException: " + e.getMsg(), e);
        return new BaseResponse(e.getCode(), null, e.getMsg());
    }
}

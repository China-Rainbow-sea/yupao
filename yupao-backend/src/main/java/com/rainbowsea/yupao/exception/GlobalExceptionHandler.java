package com.rainbowsea.yupao.exception;


import com.rainbowsea.yupao.common.BaseResponse;
import com.rainbowsea.yupao.common.ErrorCode;
import com.rainbowsea.yupao.utils.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局异常处理器
 * 捕获全局的异常，从而返回给前端。
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 当发生  BusinessException.class 这个方法处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e) {
        log.error("BusinessException" + e.getMessage(), e);
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }


    /**
     * 当发生  RuntimeException.class 异常时，这个方法处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(BusinessException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}

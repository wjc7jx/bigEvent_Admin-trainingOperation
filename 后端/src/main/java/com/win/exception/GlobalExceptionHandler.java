package com.win.exception;

import com.win.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.win.exception.BusinessException; // Add import statement for BusinessException

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.error("操作失败，请联系管理员");
    }

    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        return Result.error(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public Result handleValidationException(Exception e) {
        String message = null;
        if (e instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) e)
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("参数验证失败");
        } else if (e instanceof BindException) {
            message = ((BindException) e)
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("参数验证失败");
        }
        return Result.error(StringUtils.hasLength(message) ? message : "参数验证失败");
    }
}

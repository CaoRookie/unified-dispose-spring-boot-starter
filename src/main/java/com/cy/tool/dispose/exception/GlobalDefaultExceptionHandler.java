package com.cy.tool.dispose.exception;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cy.tool.dispose.exception.category.BusinessException;
import com.cy.tool.dispose.exception.category.FeignException;
import com.cy.tool.dispose.exception.error.ResultEnum;
import com.cy.tool.dispose.exception.error.base.BaseEnum;
import com.cy.tool.dispose.result.ResponseData;

import lombok.extern.slf4j.Slf4j;

/**
 * 全局异常处理类
 * 
 * @author cy
 * @date 2019-11-13 11:28
 */
@Slf4j
@RestControllerAdvice
public final class GlobalDefaultExceptionHandler {
    /**
     * NoHandlerFoundException 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseData<?> handlerNoHandlerFoundException(NoHandlerFoundException exception) {
        outPutErrorWarn(NoHandlerFoundException.class, ResultEnum.NOT_FOUND, exception);
        return ResponseData.result(ResultEnum.NOT_FOUND, null);
    }

    /**
     * HttpRequestMethodNotSupportedException 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData<?>
        handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        outPutErrorWarn(HttpRequestMethodNotSupportedException.class, ResultEnum.METHOD_NOT_ALLOWED, exception);
        return ResponseData.result(ResultEnum.METHOD_NOT_ALLOWED, null);
    }

    /**
     * HttpMediaTypeNotSupportedException 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseData<?> handlerHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class, ResultEnum.UNSUPPORTED_MEDIA_TYPE, exception);
        return ResponseData.result(ResultEnum.UNSUPPORTED_MEDIA_TYPE, null);
    }

    /**
     * Exception 类捕获 500 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData<?> handlerException(Exception e, HttpServletRequest request) {
        log.info("Exception message  : {}{}", e.getMessage(), e);
        return ifDepthExceptionType(e, request);
    }

    /**
     * 二次深度检查错误类型
     */
    private ResponseData<?> ifDepthExceptionType(Throwable throwable, HttpServletRequest request) {
        Throwable cause = throwable.getCause();

        if (cause instanceof FeignException) {
            return handlerFeignException((FeignException)cause);
        }
        outPutError(Exception.class, ResultEnum.EXCEPTION, throwable);
        return ResponseData.result(ResultEnum.EXCEPTION.getCode(), ResultEnum.EXCEPTION.getMessage(), null,
            String.valueOf(request.getAttribute("request_id")), request.getRequestURI());
    }

    /**
     * FeignException 类捕获
     */
    @ExceptionHandler(value = FeignException.class)
    public ResponseData<?> handlerFeignException(FeignException e) {
        outPutError(FeignException.class, ResultEnum.RPC_ERROR, e);
        return ResponseData.result(ResultEnum.RPC_ERROR, null);
    }

    /**
     * BusinessException 类捕获
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseData<?> handlerBusinessException(BusinessException e) {
        outPutError(BusinessException.class, ResultEnum.BUSINESS_ERROR, e);
        return ResponseData.error(e.getCode(), e.getMessage(), null);
    }

    /**
     * HttpMessageNotReadableException 参数错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseData<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        outPutError(HttpMessageNotReadableException.class, ResultEnum.PARAM_ERROR, e);
        String msg = String.format("{} : 错误详情( {} )", ResultEnum.PARAM_ERROR.getMessage(),
            Objects.requireNonNull(e.getRootCause()).getMessage());
        return ResponseData.error(ResultEnum.PARAM_ERROR.getCode(), msg, null);
    }

    /**
     * 绑定失败，如表单对象参数违反约束
     */
    @ExceptionHandler(BindException.class)
    public ResponseData<?> handleBindException(BindException e) {
        outPutError(BindException.class, ResultEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResultDTO(bindingResult);
    }

    /**
     * 参数无效，如JSON请求参数违反约束
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        outPutError(BindException.class, ResultEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResultDTO(bindingResult);
    }

    /**
     * 违反约束，javax扩展定义
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseData<?> handleConstraintViolationException(ConstraintViolationException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.result(ResultEnum.PARAM_ERROR, null);
    }

    /**
     * 参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseData<?> handleConstraintViolationException(MissingServletRequestParameterException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.result(ResultEnum.PARAM_ERROR, null);
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseData<?> handleConstraintViolationException(TypeMismatchException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.result(ResultEnum.PARAM_ERROR, null);
    }

    private ResponseData<?> getBindResultDTO(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (log.isDebugEnabled()) {
            for (FieldError error : fieldErrors) {
                log.error("{} -> {}", error.getDefaultMessage(), error.getDefaultMessage());
            }
        }
        if (fieldErrors.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ResponseData.result(ResultEnum.BUSINESS_ERROR.getCode(), "", null);
        }
        return ResponseData.result(ResultEnum.PARAM_ERROR.getCode(), fieldErrors.get(0).getDefaultMessage(), null);
    }

    private void outPutError(Class<?> errorType, BaseEnum secondaryErrorType, Throwable throwable) {
        log.error("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(), throwable);
    }

    private void outPutErrorWarn(Class<?> errorType, BaseEnum secondaryErrorType, Throwable throwable) {
        log.warn("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}

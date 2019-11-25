package com.cebon.tool.dispose.exception;

import com.cebon.tool.dispose.exception.category.BusinessException;
import com.cebon.tool.dispose.exception.category.FeignException;
import com.cebon.tool.dispose.result.ResponseData;
import com.cebon.tool.dispose.exception.error.ResultEnum;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

/**
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
    public ResponseData handlerNoHandlerFoundException(NoHandlerFoundException exception) {
        outPutErrorWarn(NoHandlerFoundException.class, ResultEnum.NOT_FOUND, exception);
        return ResponseData.ofError(ResultEnum.NOT_FOUND);
    }
    /**
     * HttpRequestMethodNotSupportedException 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseData handlerHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        outPutErrorWarn(HttpRequestMethodNotSupportedException.class,
                ResultEnum.METHOD_NOT_ALLOWED, exception);
        return ResponseData.ofError(ResultEnum.METHOD_NOT_ALLOWED);
    }
    /**
     * HttpMediaTypeNotSupportedException 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseData handlerHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException exception) {
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class,
                ResultEnum.UNSUPPORTED_MEDIA_TYPE, exception);
        return ResponseData.ofError(ResultEnum.UNSUPPORTED_MEDIA_TYPE);
    }
    /**
     * Exception 类捕获 500 异常处理
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseData handlerException(Exception e) {
        return ifDepthExceptionType(e);
    }
    /**
     * 二次深度检查错误类型
     */
    private ResponseData ifDepthExceptionType(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause instanceof FeignException) {
            return handlerFeignException((FeignException) cause);
        }
        outPutError(Exception.class, ResultEnum.EXCEPTION, throwable);
        return ResponseData.ofError(ResultEnum.EXCEPTION);
    }
    /**
     * FeignException 类捕获
     */
    @ExceptionHandler(value = FeignException.class)
    public ResponseData handlerFeignException(FeignException e) {
        outPutError(FeignException.class, ResultEnum.RPC_ERROR, e);
        return ResponseData.ofError(ResultEnum.RPC_ERROR);
    }
    /**
     * BusinessException 类捕获
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseData handlerBusinessException(BusinessException e) {
        outPutError(BusinessException.class, ResultEnum.BUSINESS_ERROR, e);
        return ResponseData.ofError(e.getCode(), e.getMessage());
    }
    /**
     * HttpMessageNotReadableException 参数错误异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseData handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        outPutError(HttpMessageNotReadableException.class, ResultEnum.PARAM_ERROR, e);
        String msg = String.format("{} : 错误详情( {} )", ResultEnum.PARAM_ERROR.getMessage(), Objects.requireNonNull(e.getRootCause()).getMessage());
        return ResponseData.ofError(ResultEnum.PARAM_ERROR.getCode(), msg);
    }
    /**
     * 绑定失败，如表单对象参数违反约束
     */
    @ExceptionHandler(BindException.class)
    public ResponseData handleBindException(BindException e) {
        outPutError(BindException.class, ResultEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResultDTO(bindingResult);
    }

    /**
     * 	参数无效，如JSON请求参数违反约束
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseData handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        outPutError(BindException.class, ResultEnum.PARAM_ERROR, e);
        BindingResult bindingResult = e.getBindingResult();
        return getBindResultDTO(bindingResult);
    }

    /**
     * 违反约束，javax扩展定义
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseData handleConstraintViolationException(ConstraintViolationException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.ofError(ResultEnum.PARAM_ERROR);
    }
    /**
     * 参数缺失
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseData handleConstraintViolationException(MissingServletRequestParameterException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.ofError(ResultEnum.PARAM_ERROR);
    }
    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseData handleConstraintViolationException(TypeMismatchException e) {
        outPutError(ConstraintViolationException.class, ResultEnum.PARAM_ERROR, e);
        return ResponseData.ofError(ResultEnum.PARAM_ERROR);
    }

    private ResponseData getBindResultDTO(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (log.isDebugEnabled()) {
            for (FieldError error : fieldErrors) {
                log.error("{} -> {}", error.getDefaultMessage(), error.getDefaultMessage());
            }
        }
        if (fieldErrors.isEmpty()) {
            log.error("validExceptionHandler error fieldErrors is empty");
            ResponseData.ofError(ResultEnum.BUSINESS_ERROR.getCode(), "");
        }
        return ResponseData.ofError(ResultEnum.PARAM_ERROR.getCode(), fieldErrors.get(0).getDefaultMessage());
    }

    private void outPutError(Class errorType, Enum secondaryErrorType, Throwable throwable) {
        log.error("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(), throwable);
    }

    private void outPutErrorWarn(Class errorType, Enum secondaryErrorType, Throwable throwable) {
        log.warn("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage());
    }
}

package com.cebon.tool.dispose.advice;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.cebon.tool.dispose.annotation.IgnoreResponseAdvice;
import com.cebon.tool.dispose.properties.GlobalDefaultProperties;
import com.cebon.tool.dispose.result.ResponseData;

/**
 * 全局统一返回处理增强
 * 
 * @author cy
 * @date 2019-11-13 12:36
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice {

    private GlobalDefaultProperties properties;

    public CommonResponseDataAdvice(GlobalDefaultProperties properties) {
        this.properties = properties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return filter(methodParameter);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass,
        ServerHttpRequest serverHttpReqest, ServerHttpResponse serverHttpResponse) {
        // o is null -> return response
        if (o == null) {
            return ResponseData.success(null);
        }
        // o is instanceof ConmmonResponse -> return o
        if (o instanceof ResponseData) {
            return o;
        }
        // string 特殊处理
        if (o instanceof String) {
            return JSON.toJSON(ResponseData.success(o)).toString();
        }
        return ResponseData.success(o);
    }

    private Boolean filter(MethodParameter methodParameter) {
        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 方法上存在注解，则直接忽略
        if (Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 类上存在注解，直接忽略
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        // 在扫描包外，直接忽略
        long count = properties.getBasePackages().stream()
            .filter(className -> declaringClass.getName().contains(className)).count();
        return count > 0;
    }
}

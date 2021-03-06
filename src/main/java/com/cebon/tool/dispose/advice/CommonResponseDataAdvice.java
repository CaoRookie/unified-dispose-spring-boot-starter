package com.cebon.tool.dispose.advice;

import com.alibaba.fastjson.JSON;
import com.cebon.tool.dispose.annotation.IgnoreResponseAdvice;
import com.cebon.tool.dispose.properties.GlobalDefaultProperties;
import com.cebon.tool.dispose.result.ResponseData;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author cy
 * @date 2019-11-13 12:36
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice {

    private GlobalDefaultProperties properties;

    public CommonResponseDataAdvice(GlobalDefaultProperties properties){
        this.properties = properties;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return filter(methodParameter);
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        // o is null -> return response
        if (o == null) {
            return ResponseData.ofSuccess();
        }
        // o is instanceof ConmmonResponse -> return o
        if (o instanceof ResponseData) {
            return o;
        }
        // string 特殊处理
        if (o instanceof String) {
            return JSON.toJSON(ResponseData.ofSuccess(o)).toString();
        }
        return ResponseData.ofSuccess(o);
    }

    private Boolean filter(MethodParameter methodParameter) {
        Class<?> declaringClass = methodParameter.getDeclaringClass();
        // 检查过滤包路径
        long count = properties.getAdviceFilterPackage().stream()
                .filter(className -> declaringClass.getName().contains(className)).count();
        if (count > 0) {
            return false;
        }
        // 检查<类>过滤列表
        if (properties.getAdviceFilterClass().contains(declaringClass.getName())) {
            return false;
        }
        // 检查<方法>过滤列表
        if (properties.getAdviceFilterMethod().contains(Objects.requireNonNull(methodParameter.getMethod()).getName())) {
            return false;
        }
        // 检查注解是否存在
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)) {
            return false;
        }
        return !Objects.requireNonNull(methodParameter.getMethod()).isAnnotationPresent(IgnoreResponseAdvice.class);
    }
}

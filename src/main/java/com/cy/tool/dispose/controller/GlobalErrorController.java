package com.cy.tool.dispose.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.cy.tool.dispose.exception.error.ResultEnum;
import com.cy.tool.dispose.result.ResponseData;

/**
 * /** 全局错误处理 SpringBoot默认会将异常映射到/error路径，从而根据请求方式返回html或json 在这个控制器中处理/error路径的请求，将所有异常的返回值进行统一处理
 * <p>
 * Author GreedyStar Date 2018/7/19
 * 
 * @author CaoYuan
 * @date 2020/3/2 23:15
 */
@RestController
public class GlobalErrorController implements ErrorController {
    private final String PATH = "/error";
    private final ErrorAttributes errorAttributes;

    public GlobalErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

    @RequestMapping(value = PATH, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseData handleError(HttpServletRequest request) {
        Map<String, Object> attributesMap = getErrorAttributes(request, true);
        return ResponseData.result(ResultEnum.EXCEPTION, null);
    }

    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, includeStackTrace);
    }
}

package com.cy.tool.dispose.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cy.tool.dispose.advice.CommonResponseDataAdvice;
import com.cy.tool.dispose.exception.GlobalDefaultExceptionHandler;
import com.cy.tool.dispose.properties.GlobalDefaultProperties;

/**
 * 全局统一返回Bean,全局异常处理Bean注册
 * 
 * @author cy
 * @date 2019-11-13 12:35
 */
@Configuration
@EnableConfigurationProperties(GlobalDefaultProperties.class)
@ConditionalOnProperty(prefix = "spring.global", value = "enabled", matchIfMissing = true)
public class GlobalDefaultConfiguration {

    private GlobalDefaultProperties globalDefaultProperties;

    public GlobalDefaultConfiguration(GlobalDefaultProperties globalDefaultProperties) {
        this.globalDefaultProperties = globalDefaultProperties;
    }

    @Bean
    public GlobalDefaultExceptionHandler globalDefaultExceptionHandler() {
        return new GlobalDefaultExceptionHandler();
    }

    @Bean
    public CommonResponseDataAdvice commonResponseDataAdvice() {
        return new CommonResponseDataAdvice(globalDefaultProperties);
    }
}

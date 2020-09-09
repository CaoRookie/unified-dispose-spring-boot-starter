package com.cebon.tool.dispose.config;

import com.cebon.tool.dispose.advice.CommonResponseDataAdvice;
import com.cebon.tool.dispose.exception.GlobalDefaultExceptionHandler;
import com.cebon.tool.dispose.properties.GlobalDefaultProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author cy
 * @date 2019-11-13 12:35
 */
@Configuration
@EnableConfigurationProperties(GlobalDefaultProperties.class)
@ConditionalOnProperty(prefix = "spring.igore", value = "enabled",matchIfMissing = true)
public class GlobalDefaultConfiguration {

    private GlobalDefaultProperties globalDefaultProperties;

    public GlobalDefaultConfiguration(GlobalDefaultProperties globalDefaultProperties){
        this.globalDefaultProperties = globalDefaultProperties;
    }

    @Bean
    public GlobalDefaultExceptionHandler globalDefaultExceptionHandler() {
        return new GlobalDefaultExceptionHandler();
    }

    @Bean
    public CommonResponseDataAdvice commonResponseDataAdvice(){
        return new CommonResponseDataAdvice(globalDefaultProperties);
    }
}

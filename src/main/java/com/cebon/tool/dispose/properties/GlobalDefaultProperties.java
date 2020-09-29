package com.cebon.tool.dispose.properties;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * 全局统一返回配置映射文件
 * 
 * @author cy
 * @date 2019-11-13 11:28
 */

@Data
@ConfigurationProperties(prefix = "spring.global")
public class GlobalDefaultProperties {

    /**
     * 统一返回扫描包
     */
    private List<String> basePackages = new ArrayList<>();
}

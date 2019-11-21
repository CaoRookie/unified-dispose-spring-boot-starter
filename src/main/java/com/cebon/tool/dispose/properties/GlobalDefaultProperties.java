package com.cebon.tool.dispose.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cy
 * @date 2019-11-13 11:28
 */

@Data
@ConfigurationProperties(prefix = "spring.igore")
public class GlobalDefaultProperties {

    /**
     * 统一返回过滤包
     */
    private List<String> adviceFilterPackage = new ArrayList<>();

    /**
     * 统一返回过滤类
     */
    private List<String> adviceFilterClass = new ArrayList<>();

    /**
     * 统一返回过滤方法
     */
    private List<String> adviceFilterMethod = new ArrayList<>();
}

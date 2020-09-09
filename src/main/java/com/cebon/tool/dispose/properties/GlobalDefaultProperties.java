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
     * 统一返回扫描包
     */
    private List<String> basePackageScan = new ArrayList<>();
}

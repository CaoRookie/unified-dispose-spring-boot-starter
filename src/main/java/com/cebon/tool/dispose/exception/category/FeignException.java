package com.cebon.tool.dispose.exception.category;

import com.cebon.tool.dispose.exception.error.base.BaseEnum;
import lombok.Getter;

/**
 * @author cy
 * @date 2019-11-13 12:24
 */
@Getter
public class FeignException extends RuntimeException {


    private Integer code;
    private boolean isShowMsg = true;
    /**
     * 使用枚举传参
     *
     * @param errorCode 异常枚举
     */
    public FeignException(BaseEnum errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }
    /**
     * 使用自定义消息
     *
     * @param code 值
     * @param msg 详情
     */
    public FeignException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}

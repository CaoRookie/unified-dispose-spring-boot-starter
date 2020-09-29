package com.cebon.tool.dispose.result;

import java.io.Serializable;

import com.cebon.tool.dispose.exception.error.ResultEnum;
import com.cebon.tool.dispose.exception.error.base.BaseEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author cy
 * @date 2019-11-13 10:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private String requestId;

    private Integer code;

    private String message;

    private String requestPath;

    private T data;

    /**
     * 返回结果
     * 
     * @param resultEnum 自定义返回异常 {@link BaseEnum}
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> result(BaseEnum resultEnum, M data) {
        return ResponseData.<M>builder().code(resultEnum.getCode()).message(resultEnum.getMessage()).data(data).build();
    }

    /**
     * 返回结果
     * 
     * @param code 状态码
     * @param message 状态信息
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> result(Integer code, String message, M data) {
        return ResponseData.<M>builder().code(code).message(message).data(data).build();
    }

    /**
     * 返回结果
     * 
     * @param code 状态码
     * @param message 状态信息
     * @param data 数据
     * @param requestId 请求ID
     * @param path 请求路径
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> result(Integer code, String message, M data, String requestId, String path) {
        return ResponseData.<M>builder().code(code).requestId(requestId).requestPath(path).message(message).data(data)
            .build();
    }

    /**
     * 请求成功
     * 
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> success(M data) {
        return ResponseData.result(ResultEnum.SUCCESS, data);
    }

    /**
     * 请求成功
     * 
     * @param code 状态码
     * @param message 成功描述信息
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> success(Integer code, String message, M data) {
        return ResponseData.result(code, message, data);
    }

    /**
     * 请求失败
     *
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> error(M data) {
        return ResponseData.result(ResultEnum.BUSINESS_ERROR, data);
    }

    /**
     * 请求成功
     *
     * @param code 状态码
     * @param message 成功描述信息
     * @param data 数据
     * @return 泛型统一封装数据
     */
    public static <M> ResponseData<M> error(Integer code, String message, M data) {
        return ResponseData.result(code, message, data);
    }

}

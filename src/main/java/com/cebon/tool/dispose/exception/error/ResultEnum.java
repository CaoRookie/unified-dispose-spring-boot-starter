package com.cebon.tool.dispose.exception.error;

import com.cebon.tool.dispose.exception.error.base.BaseEnum;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author cy
 * @date 2019-11-13 10:28
 */
@Getter
public enum ResultEnum implements BaseEnum {

    /***  * 404 Web 服务器找不到您所请求的文件或脚本。请检查URL 以确保路径正确。*/
    NOT_FOUND(404, String.format("哎呀，无法找到这个资源啦(%s)", HttpStatus.NOT_FOUND.getReasonPhrase())),

    /** * 405 对于请求所标识的资源，不允许使用请求行中所指定的方法。请确保为所请求的资源设置了正确的 MIME 类型。*/
    METHOD_NOT_ALLOWED(405,String.format("请换个姿势操作试试(%s)", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())),

    /**
     * 415 Unsupported Media Type
     */
    UNSUPPORTED_MEDIA_TYPE(415,String.format("呀，不支持该媒体类型(%s)", HttpStatus.UNSUPPORTED_MEDIA_TYPE.getReasonPhrase())),
    /**
     * 系统异常 500 服务器的内部错误
     */
    EXCEPTION(500 ,"服务器开小差，请稍后再试"),
    /**
     * 系统限流
     */
    TRAFFIC_LIMITING(429, "哎呀，网络拥挤请稍后再试试"),
    /**
     * 服务调用异常
     */
    API_GATEWAY_ERROR(9999, "网络繁忙，请稍后再试"),
    /**
     * 参数错误
     */
    PARAM_ERROR(100, "参数错误"),
    /**
     * 业务异常
     */
    BUSINESS_ERROR(400, "业务异常"),
    /**
     * rpc调用异常
     */
    RPC_ERROR(510, "呀，网络出问题啦！"),

    SUCCESS(200, "请求成功");

    private Integer code;
    private String message;

    ResultEnum(Integer code, String message){
        this.code = code;
        this.message = message;
    }

}

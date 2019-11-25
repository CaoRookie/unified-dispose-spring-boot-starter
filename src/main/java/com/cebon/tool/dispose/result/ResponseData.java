package com.cebon.tool.dispose.result;

import com.cebon.tool.dispose.exception.error.ResultEnum;
import com.cebon.tool.dispose.exception.error.base.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author cy
 * @date 2019-11-13 10:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

    private Integer code;

    private String message;

    private T data;

    public static ResponseData reslut(BaseEnum resultEnum, Object data){
        return  ResponseData.builder()
                .code(resultEnum.getCode())
                .message(resultEnum.getMessage())
                .data(data)
                .build();
    }
    public static ResponseData reslut(Integer code,String message,Object data){
        return  ResponseData.builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }

    public static ResponseData reslut(BaseEnum resultEnum){
        return ResponseData.reslut(resultEnum,null);
    }

    public static ResponseData ofSuccess(){
        return ResponseData.reslut(ResultEnum.SUCCESS,null);
    }

    public static ResponseData ofSuccess(String message,Object data){
        return ResponseData.reslut(ResultEnum.SUCCESS.getCode(),message,data);
    }
    public static ResponseData ofSuccess(String message){
        return ResponseData.reslut(ResultEnum.SUCCESS.getCode(),message,null);
    }

    public static ResponseData ofSuccess(Integer code,String message){
        return  ResponseData.reslut(code,message,null);
    }


    public static ResponseData ofSuccess(Object data){
        return ResponseData.reslut(ResultEnum.SUCCESS,data);
    }

    public static ResponseData ofError(BaseEnum resultEnum){
        return  ResponseData.reslut(resultEnum);
    }

    public static ResponseData ofError(String message){
        return  ResponseData.reslut(ResultEnum.EXCEPTION.getCode(),message,null);
    }

    public static ResponseData ofError(Integer code,String message){
        return  ResponseData.reslut(code,message,null);
    }


}

package com.oa.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @PackageName:com.oa.common
 * @ClassName:ResponseData
 * @Description:
 * @Author: zqc
 * @date 2021/2/20 15:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class RespBean {

    private Integer code;
    private String msg;
    private Object data;

    public RespBean(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    };

    /**
     * 成功返回结果
     * @param msg
     * @param data
     * @return
     */
    public static RespBean success(String msg, Object data){
        return new RespBean(200, msg, data);
    }
    /**
     * 成功返回结果
     * @param msg
     * @return
     */
    public static RespBean success(String msg){
        return new RespBean(200, msg, null);
    }
    /**
     * 失败返回结果
     * @param msg
     * @param data
     * @return
     */
    public static RespBean error(String msg, Object data){
        return new RespBean(500, msg, data);
    }
    /**
     * 失败返回结果
     * @param msg
     * @return
     */
    public static RespBean error(String msg){
        return new RespBean(500, msg, null);
    }
}

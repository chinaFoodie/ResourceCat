package com.cn.clound.bean;

import java.io.Serializable;

/****
 * 返回前端json的基类
 *
 * @author onion
 */
public class BaseModel implements Serializable {

    private Integer status;// 通信状态

    private String message;// 提示信息

    private String time;//当前系统时间

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

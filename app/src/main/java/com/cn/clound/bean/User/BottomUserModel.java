package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;

/**
 * 底部群成员model
 *
 * @author chunfaLee(ly09219@gamil.com)
 * @date 2016-4-29 16:49:24
 */
public class BottomUserModel extends BaseModel implements Serializable {
    private String userId;
    private String userHead;
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
}

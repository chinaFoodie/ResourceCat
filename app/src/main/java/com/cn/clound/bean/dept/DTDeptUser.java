package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseBean;

/**
 * 部门人员信息
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-18 17:45:09
 */
public class DTDeptUser extends BaseBean {
    private String userName;
    private String userAvatar;

    private String userPhone;
    //人员等级（局长or教师）
    private String userLevel;
    //人员角色
    private String userRole;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "DTDeptUser{" +
                "userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userLevel='" + userLevel + '\'' +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}

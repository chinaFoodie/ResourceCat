package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseBean;

/**
 * 部门管理
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-20 11:37:45
 */
public class DeptManagerBean extends BaseBean {
    private String deptAvatar;
    private String deptName;
    private String deptNumber;
    private String deptDesc;
    private String deptChargePhone;

    public String getDeptAvatar() {
        return deptAvatar;
    }

    public void setDeptAvatar(String deptAvatar) {
        this.deptAvatar = deptAvatar;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getDeptChargePhone() {
        return deptChargePhone;
    }

    public void setDeptChargePhone(String deptChargePhone) {
        this.deptChargePhone = deptChargePhone;
    }

    @Override
    public String toString() {
        return "DeptManagerBean{" +
                "deptAvatar='" + deptAvatar + '\'' +
                ", deptName='" + deptName + '\'' +
                ", deptNumber='" + deptNumber + '\'' +
                ", deptDesc='" + deptDesc + '\'' +
                ", deptChargePhone='" + deptChargePhone + '\'' +
                '}';
    }
}

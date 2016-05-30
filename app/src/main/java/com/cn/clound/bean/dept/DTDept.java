package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseBean;

/**
 * Class Of DTDept
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 17:04:03
 */
public class DTDept extends BaseBean {

    private String deptName;
    private String deptAvatar;
    private String deptDesc;
    private String deptNumber;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptAvatar() {
        return deptAvatar;
    }

    public void setDeptAvatar(String deptAvatar) {
        this.deptAvatar = deptAvatar;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    public void setDeptNumber(String deptNumber) {
        this.deptNumber = deptNumber;
    }

    @Override
    public String toString() {
        return "DTDept{" +
                "deptName='" + deptName + '\'' +
                ", deptAvatar='" + deptAvatar + '\'' +
                ", deptDesc='" + deptDesc + '\'' +
                ", deptNumber='" + deptNumber + '\'' +
                '}';
    }
}

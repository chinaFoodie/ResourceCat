package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FindDepUserListModel extends BaseModel {
    private DepUserData data;


    public DepUserData getData() {
        return data;
    }

    public void setData(DepUserData data) {
        this.data = data;
    }

    public static class DepUserData {
        List<DepUser> depUser = new ArrayList<DepUser>();

        public List<DepUser> getDepUser() {
            return depUser;
        }

        public void setDepUser(List<DepUser> depUser) {
            this.depUser = depUser;
        }

    }

    public static class DepUser implements Serializable {
        private String name;//姓名
        private String userNo;//用户编号
        private String userHead;//头像
        private String userPhone;//手机号
        private String imUser;//环信账号
        private String dutyName;//职务名称
        private String isDepManger;//是否是部门管理
        private String unitId;//单位Id
        private String isAdmin;//是否超级管理员
        private String depId;//部门编号
        private String isSetting;//是否激活

        public String getIsSetting() {
            return isSetting;
        }

        public void setIsSetting(String isSetting) {
            this.isSetting = isSetting;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getUserHead() {
            return userHead;
        }

        public void setUserHead(String userHead) {
            this.userHead = userHead;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getImUser() {
            return imUser;
        }

        public void setImUser(String imUser) {
            this.imUser = imUser;
        }

        public String getDutyName() {
            return dutyName;
        }

        public void setDutyName(String dutyName) {
            this.dutyName = dutyName;
        }

        public String getIsDepManger() {
            return isDepManger;
        }

        public void setIsDepManger(String isDepManger) {
            this.isDepManger = isDepManger;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getDepId() {
            return depId;
        }

        public void setDepId(String depId) {
            this.depId = depId;
        }
    }
}

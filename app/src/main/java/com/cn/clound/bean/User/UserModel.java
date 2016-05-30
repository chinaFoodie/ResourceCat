package com.cn.clound.bean.User;


import com.cn.clound.bean.BaseModel;

public class UserModel extends BaseModel {

    private UserData data;

    public UserData getData() {
        return data;
    }

    public void setData(UserData data) {
        this.data = data;
    }

    public static class UserData {
        private UserInfoModel userInfo;

        public UserInfoModel getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoModel userInfo) {
            this.userInfo = userInfo;
        }

    }

    public static class UserInfoModel {
        private String userNo;//用户号
        private String phone;//用户手机号
        private String name;//用户名字
        private String imUser;//环信账号
        private String imPass;//环信密码
        private String head;//用户头像
        private String isAdmin;//是否超级管理员 -1=不是超级管理员 1=是超级管理员
        private String role;//角色  1=教育局 2=教辅站 3=教师  -1=无所属机构
        private String isPatriarch;// 是否为家长 -1=否 1=是
        private String depNo;//部门编码
        private String depName;//部门名称
        private String duty;// 职务 -1 普通 1=部门主管  2=单位负责人
        private String dutyName;//部门自己设定
        private String unitNo;//单位编码
        private String unitName;//单位名称
        private String cityName;//所属区域
        private String cityId;//城市Id
        private String isSetting;//是否初始化 -1 未初始化 1=已经初始化
        private String isTop;// 是否是常用联系人

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImUser() {
            return imUser;
        }

        public void setImUser(String imUser) {
            this.imUser = imUser;
        }

        public String getImPass() {
            return imPass;
        }

        public void setImPass(String imPass) {
            this.imPass = imPass;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getIsAdmin() {
            return isAdmin;
        }

        public void setIsAdmin(String isAdmin) {
            this.isAdmin = isAdmin;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getIsPatriarch() {
            return isPatriarch;
        }

        public void setIsPatriarch(String isPatriarch) {
            this.isPatriarch = isPatriarch;
        }

        public String getDepNo() {
            return depNo;
        }

        public void setDepNo(String depNo) {
            this.depNo = depNo;
        }

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public String getDutyName() {
            return dutyName;
        }

        public void setDutyName(String dutyName) {
            this.dutyName = dutyName;
        }

        public String getUnitNo() {
            return unitNo;
        }

        public void setUnitNo(String unitNo) {
            this.unitNo = unitNo;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getIsSetting() {
            return isSetting;
        }

        public void setIsSetting(String isSetting) {
            this.isSetting = isSetting;
        }

        public String getIsTop() {
            return isTop;
        }

        public void setIsTop(String isTop) {
            this.isTop = isTop;
        }
    }
}

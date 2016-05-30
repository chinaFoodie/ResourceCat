package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class FindUnitMangerListModel extends BaseModel {

    private FindUnitMangerListData data;

    public FindUnitMangerListData getData() {
        return data;
    }

    public void setData(FindUnitMangerListData data) {
        this.data = data;
    }

    public static class FindUnitMangerListData {
        List<UserListModel> user = new ArrayList<UserListModel>();

        public List<UserListModel> getUser() {
            return user;
        }

        public void setUser(List<UserListModel> user) {
            this.user = user;
        }
    }

    public static class UserListModel {
        private String name;
        private String userNo;
        private String userHead;
        private String userPhone;
        private String imUser;
        private String dutyName;
        private String isSetting;

        public String getIsSetting() {
            return isSetting;
        }

        public void setIsSetting(String isSetting) {
            this.isSetting = isSetting;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}

package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-16 15:01:50
 */
public class WipedApprovalModel extends BaseModel {
    private WipedApproval data;

    public WipedApproval getData() {
        return data;
    }

    public void setData(WipedApproval data) {
        this.data = data;
    }

    public class WipedApproval {
        private Form form;
        private List<User> users;

        public Form getForm() {
            return form;
        }

        public void setForm(Form form) {
            this.form = form;
        }

        public List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        public class Form {
            private List<Approval> details;

            public List<Approval> getDetails() {
                return details;
            }

            public void setDetails(List<Approval> details) {
                this.details = details;
            }
        }

        public class Approval {
            private String chargesDetail;
            private String money;
            private String name;
            private String type;

            public String getChargesDetail() {
                return chargesDetail;
            }

            public void setChargesDetail(String chargesDetail) {
                this.chargesDetail = chargesDetail;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public class User {
            private String deptId;
            private String userId;

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getDeptId() {
                return deptId;
            }

            public void setDeptId(String deptId) {
                this.deptId = deptId;
            }
        }
    }
}

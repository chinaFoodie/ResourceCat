package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 通用审批model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-16 18:42:25
 */
public class CommonApprovalModel extends BaseModel {
    private CommonApproval data;

    public CommonApproval getData() {
        return data;
    }

    public void setData(CommonApproval data) {
        this.data = data;
    }

    public class CommonApproval {
        private Form form;
        private List<WipedApprovalModel.WipedApproval.User> users;

        public Form getForm() {
            return form;
        }

        public void setForm(Form form) {
            this.form = form;
        }

        public List<WipedApprovalModel.WipedApproval.User> getUsers() {
            return users;
        }

        public void setUsers(List<WipedApprovalModel.WipedApproval.User> users) {
            this.users = users;
        }
    }

    public class Form {
        private String sName;
        private String sText;

        public void setsName(String sName) {
            this.sName = sName;
        }

        public void setsText(String sText) {
            this.sText = sText;
        }
    }
}

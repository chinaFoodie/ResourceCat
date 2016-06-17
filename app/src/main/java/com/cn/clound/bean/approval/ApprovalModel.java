package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 审批消息model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-17 10:41:56
 */
public class ApprovalModel extends BaseModel {
    private ApprovalMessage data;

    public ApprovalMessage getData() {
        return data;
    }

    public void setData(ApprovalMessage data) {
        this.data = data;
    }

    public class ApprovalMessage {
        private List<ApprovalInfo> result;
        private String totalCount;
        private String totalPage;

        public List<ApprovalInfo> getResult() {
            return result;
        }

        public void setResult(List<ApprovalInfo> result) {
            this.result = result;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }
    }

    public class ApprovalInfo {
        private String beginAt;
        private String id;
        private String name;
        private String stateStr;
        private String unitName;
        private String userHead;
        private String userId;

        public String getBeginAt() {
            return beginAt;
        }

        public void setBeginAt(String beginAt) {
            this.beginAt = beginAt;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStateStr() {
            return stateStr;
        }

        public void setStateStr(String stateStr) {
            this.stateStr = stateStr;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUserHead() {
            return userHead;
        }

        public void setUserHead(String userHead) {
            this.userHead = userHead;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

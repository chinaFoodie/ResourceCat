package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 我发出的审批列表model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-13 16:21:34
 */
public class DeliveredApprovalsModel extends BaseModel {
    private DeliveredApprovals data;

    public DeliveredApprovals getData() {
        return data;
    }

    public void setData(DeliveredApprovals data) {
        this.data = data;
    }

    public class DeliveredApprovals {
        private String totalCount;
        private String totalPage;
        private List<DeliveredApproval> result;

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

        public List<DeliveredApproval> getResult() {
            return result;
        }

        public void setResult(List<DeliveredApproval> result) {
            this.result = result;
        }

        public class DeliveredApproval {
            private String beginAt;
            private String id;
            private String name;
            private String stateStr;
            private String uniteName;
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

            public String getUniteName() {
                return uniteName;
            }

            public void setUniteName(String uniteName) {
                this.uniteName = uniteName;
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
}

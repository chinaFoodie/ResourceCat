package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-14 11:45:38
 */
public class BuyerApprovalDetailModel extends BaseModel {
    private BuyerApprovalDetail form;
    private List<BuyerUser> users;

    public List<BuyerUser> getUsers() {
        return users;
    }

    public void setUsers(List<BuyerUser> users) {
        this.users = users;
    }

    public BuyerApprovalDetail getForm() {
        return form;
    }

    public void setForm(BuyerApprovalDetail form) {
        this.form = form;
    }

    public class BuyerApprovalDetail {
        private List<BuyerApproval> details;
        private Purchase purchase;

        public List<BuyerApproval> getDetails() {
            return details;
        }

        public void setDetails(List<BuyerApproval> details) {
            this.details = details;
        }

        public Purchase getPurchase() {
            return purchase;
        }

        public void setPurchase(Purchase purchase) {
            this.purchase = purchase;
        }


        public class BuyerApproval {
            private String pName;
            private String pNum;
            private String pPrice;
            private String pSepc;
            private String pUnit;

            public String getpName() {
                return pName;
            }

            public void setpName(String pName) {
                this.pName = pName;
            }

            public String getpNum() {
                return pNum;
            }

            public void setpNum(String pNum) {
                this.pNum = pNum;
            }

            public String getpPrice() {
                return pPrice;
            }

            public void setpPrice(String pPrice) {
                this.pPrice = pPrice;
            }

            public String getpSepc() {
                return pSepc;
            }

            public void setpSepc(String pSepc) {
                this.pSepc = pSepc;
            }

            public String getpUnit() {
                return pUnit;
            }

            public void setpUnit(String pUnit) {
                this.pUnit = pUnit;
            }
        }

        public class Purchase {
            private String pDate;
            private String payType;
            private String reason;
            private String remark;

            public String getpDate() {
                return pDate;
            }

            public void setpDate(String pDate) {
                this.pDate = pDate;
            }

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }
    }

    public class BuyerUser {
        private String deptId;
        private String userId;

        public String getDeptId() {
            return deptId;
        }

        public void setDeptId(String deptId) {
            this.deptId = deptId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

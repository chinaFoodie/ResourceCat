package com.cn.clound.bean.approval;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 审批详情model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-17 12:20:09
 */
public class ApprovalDetailsModel extends BaseModel implements Serializable {
    private ApprovalDetail data;

    public ApprovalDetail getData() {
        return data;
    }

    public void setData(ApprovalDetail data) {
        this.data = data;
    }

    public class ApprovalDetail implements Serializable {
        private String type;
        private String typeStr;
        private String unitName;
        private String userHead;
        private String userName;
        private CommExamine commExamine;
        private ExpExamine expExamine;
        private PurExamine purExamine;
        private List<Nodes> nodes;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeStr() {
            return typeStr;
        }

        public void setTypeStr(String typeStr) {
            this.typeStr = typeStr;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public CommExamine getCommExamine() {
            return commExamine;
        }

        public void setCommExamine(CommExamine commExamine) {
            this.commExamine = commExamine;
        }

        public ExpExamine getExpExamine() {
            return expExamine;
        }

        public void setExpExamine(ExpExamine expExamine) {
            this.expExamine = expExamine;
        }

        public PurExamine getPurExamine() {
            return purExamine;
        }

        public void setPurExamine(PurExamine purExamine) {
            this.purExamine = purExamine;
        }

        public List<Nodes> getNodes() {
            return nodes;
        }

        public void setNodes(List<Nodes> nodes) {
            this.nodes = nodes;
        }
    }

    //通用审批
    public class CommExamine implements Serializable {
        private String content;//申请详情
        private String name;//申请名称

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    //报销审批
    public class ExpExamine implements Serializable {
        private List<Exp> details;
        private String totalMoney;

        public List<Exp> getDetails() {
            return details;
        }

        public void setDetails(List<Exp> details) {
            this.details = details;
        }

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public class Exp implements Serializable {
            private String charges_detail;//费用明细
            private String money;//报销金额
            private String name;//报销名称
            private String type;//报销类别

            public String getCharges_detail() {
                return charges_detail;
            }

            public void setCharges_detail(String charges_detail) {
                this.charges_detail = charges_detail;
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
    }

    //采购审批
    public class PurExamine implements Serializable {
        private List<Pur> details;
        private String totalMoney;

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }

        public List<Pur> getDetails() {
            return details;
        }

        public void setDetails(List<Pur> details) {
            this.details = details;
        }

        public class Pur implements Serializable {
            private String pName;//采购名称
            private String pNum;//采购数量
            private String pPrice;//价格
            private String pSepc;//规格
            private String pUnit;//单位

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
    }

    //审批节点
    public class Nodes implements Serializable {
        private String deptName;//部门名称
        private String processingTime;//处理时间
        private String stateStr;//审批状态
        private String userHead;//审批人头像
        private String userId;//审批人id
        private String userName;//审批人名称

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public String getProcessingTime() {
            return processingTime;
        }

        public void setProcessingTime(String processingTime) {
            this.processingTime = processingTime;
        }

        public String getStateStr() {
            return stateStr;
        }

        public void setStateStr(String stateStr) {
            this.stateStr = stateStr;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}

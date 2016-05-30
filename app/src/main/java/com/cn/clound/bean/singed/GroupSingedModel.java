package com.cn.clound.bean.singed;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 团队签到模型
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-13 21:34:46
 */
public class GroupSingedModel extends BaseModel {
    private GroupData data;

    public GroupData getData() {
        return data;
    }

    public void setData(GroupData data) {
        this.data = data;
    }

    public class GroupData {
        private String pageNo;
        private String totalCount;
        private String totalPage;
        private List<GroupModel> result;

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
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

        public List<GroupModel> getResult() {
            return result;
        }

        public void setResult(List<GroupModel> result) {
            this.result = result;
        }

        public class GroupModel {
            private String firstSignAddress;
            private String fistSignInTime;
            private String lastSignAddress;
            private String lastSignInTime;
            private String userName;

            public String getFirstSignAddress() {
                return firstSignAddress;
            }

            public void setFirstSignAddress(String firstSignAddress) {
                this.firstSignAddress = firstSignAddress;
            }

            public String getFistSignInTime() {
                return fistSignInTime;
            }

            public void setFistSignInTime(String fistSignInTime) {
                this.fistSignInTime = fistSignInTime;
            }

            public String getLastSignAddress() {
                return lastSignAddress;
            }

            public void setLastSignAddress(String lastSignAddress) {
                this.lastSignAddress = lastSignAddress;
            }

            public String getLastSignInTime() {
                return lastSignInTime;
            }

            public void setLastSignInTime(String lastSignInTime) {
                this.lastSignInTime = lastSignInTime;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}

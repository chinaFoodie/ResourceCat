package com.cn.clound.bean.dept;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 未分配成员模型
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-10 16:15:25
 */
public class UnCallocateMenberModel extends BaseModel implements Serializable {
    private CallocateMenberModel data;

    public CallocateMenberModel getData() {
        return data;
    }

    public void setData(CallocateMenberModel data) {
        this.data = data;
    }

    public class CallocateMenberModel implements Serializable {
        private String pageNo;
        private String totalCount;
        private String totalPage;
        private List<UnCallocateMenber> result;

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

        public List<UnCallocateMenber> getResult() {
            return result;
        }

        public void setResult(List<UnCallocateMenber> result) {
            this.result = result;
        }

        public class UnCallocateMenber implements Serializable {
            private String isSetting;
            private String name;
            private String phone;
            private String userNo;

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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getUserNo() {
                return userNo;
            }

            public void setUserNo(String userNo) {
                this.userNo = userNo;
            }
        }
    }
}

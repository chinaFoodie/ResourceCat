package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 所有权限Model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-7 15:25:15
 */
public class AllRole extends BaseModel {
    private Role data;

    public Role getData() {
        return data;
    }

    public void setData(Role data) {
        this.data = data;
    }

    public class Role {
        private String pageNo;
        private String totalCount;
        private String totalPage;
        private List<String> result;

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

        public List<String> getResult() {
            return result;
        }

        public void setResult(List<String> result) {
            this.result = result;
        }
    }
}

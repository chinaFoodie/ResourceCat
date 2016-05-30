package com.cn.clound.bean.singed;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 用户签到模型
 *
 * @author ChunfaLee(ly09219@gamil.com).
 * @date 2016-5-9 19:46:23
 */
public class UserSingedModel extends BaseModel {
    private SingedData data;

    public SingedData getData() {
        return data;
    }

    public void setData(SingedData data) {
        this.data = data;
    }

    public class SingedData {
        private String pageNo;
        private String totalCount;
        private String totalPage;
        private List<SingedModel> result;

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

        public List<SingedModel> getResult() {
            return result;
        }

        public void setResult(List<SingedModel> result) {
            this.result = result;
        }

        public class SingedModel {
            private String signAddress;
            private String signDatetime;
            private String signLat;
            private String signLon;
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getSignAddress() {
                return signAddress;
            }

            public void setSignAddress(String signAddress) {
                this.signAddress = signAddress;
            }

            public String getSignDatetime() {
                return signDatetime;
            }

            public void setSignDatetime(String signDatetime) {
                this.signDatetime = signDatetime;
            }

            public String getSignLat() {
                return signLat;
            }

            public void setSignLat(String signLat) {
                this.signLat = signLat;
            }

            public String getSignLon() {
                return signLon;
            }

            public void setSignLon(String signLon) {
                this.signLon = signLon;
            }
        }
    }
}

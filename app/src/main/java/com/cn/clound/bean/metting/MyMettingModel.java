package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 我的会议模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月23日 16:46:26
 */
public class MyMettingModel extends BaseModel implements Serializable {

    public MeetingData data;

    public MeetingData getDate() {
        return data;
    }

    public void setDate(MeetingData date) {
        this.data = date;
    }

    public class MeetingData implements Serializable {
        public String totalCount;
        public String totalPage;
        public String pageNo;
        public List<MineMetting> result;

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

        public List<MineMetting> getResult() {
            return result;
        }

        public void setResult(List<MineMetting> result) {
            this.result = result;
        }

        public class MineMetting implements Serializable {
            private String topTimeId;
            private String type;
            private String beginAt;
            private String endAt;
            private String meetingId;
            private String name;
            private String topState;
            private String typeStr;

            public String getTopTimeId() {
                return topTimeId;
            }

            public void setTopTimeId(String topTimeId) {
                this.topTimeId = topTimeId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getBeginAt() {
                return beginAt;
            }

            public void setBeginAt(String beginAt) {
                this.beginAt = beginAt;
            }

            public String getEndAt() {
                return endAt;
            }

            public void setEndAt(String endAt) {
                this.endAt = endAt;
            }

            public String getMeetingId() {
                return meetingId;
            }

            public void setMeetingId(String meetingId) {
                this.meetingId = meetingId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getTopState() {
                return topState;
            }

            public void setTopState(String topState) {
                this.topState = topState;
            }

            public String getTypeStr() {
                return typeStr;
            }

            public void setTypeStr(String typeStr) {
                this.typeStr = typeStr;
            }
        }
    }
}

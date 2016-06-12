package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 历史会议模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-30 15:14:26
 */
public class HistoryMeetingModel extends BaseModel {
    public HistoryMeeting data;

    public HistoryMeeting getData() {
        return data;
    }

    public void setData(HistoryMeeting data) {
        this.data = data;
    }

    public class HistoryMeeting {
        private String totalCount;
        private String totalPage;
        private List<MeetingModel> result;

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

        public List<MeetingModel> getResult() {
            return result;
        }

        public void setResult(List<MeetingModel> result) {
            this.result = result;
        }

        public class MeetingModel {
            private String beginAt;
            private String endAt;
            private String meetingId;
            private String groupId;
            private String name;
            private String typeStr;

            public String getGruopId() {
                return groupId;
            }

            public void setGruopId(String groupId) {
                this.groupId = groupId;
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

            public String getTypeStr() {
                return typeStr;
            }

            public void setTypeStr(String typeStr) {
                this.typeStr = typeStr;
            }
        }
    }
}

package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 会议考勤model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-1 19:42:59
 */
public class MeetingAttendanceModel extends BaseModel {
    private MeetingAttendance data;

    public MeetingAttendance getData() {
        return data;
    }

    public void setData(MeetingAttendance data) {
        this.data = data;
    }

    public class MeetingAttendance {
        private List<Absent> absent;
        private List<Absent> late;

        public List<Absent> getAbsent() {
            return absent;
        }

        public void setAbsent(List<Absent> absent) {
            this.absent = absent;
        }

        public List<Absent> getLate() {
            return late;
        }

        public void setLate(List<Absent> late) {
            this.late = late;
        }

        public class Absent {
            private String headImg;
            private String name;
            private String userId;

            public String getHeadImg() {
                return headImg;
            }

            public void setHeadImg(String headImg) {
                this.headImg = headImg;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 发布会议模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-27 16:01:13
 */
public class IssuedMeetingModel extends BaseBean implements Serializable {
    private IssuedMeeting data;

    public IssuedMeeting getData() {
        return data;
    }

    public void setData(IssuedMeeting data) {
        this.data = data;
    }

    public class IssuedMeeting implements Serializable {
        private Meeting meeting;
        private List<MeetingTime> times;
        private List<MeetingUser> users;

        public Meeting getMeeting() {
            return meeting;
        }

        public void setMeeting(Meeting meeting) {
            this.meeting = meeting;
        }

        public List<MeetingTime> getTimes() {
            return times;
        }

        public void setTimes(List<MeetingTime> times) {
            this.times = times;
        }

        public List<MeetingUser> getUsers() {
            return users;
        }

        public void setUsers(List<MeetingUser> users) {
            this.users = users;
        }

        public class Meeting implements Serializable {
            private String id;
            private String mDescription;
            private String mIsSpeak;
            private String mName;
            private String mType;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getmDescription() {
                return mDescription;
            }

            public void setmDescription(String mDescription) {
                this.mDescription = mDescription;
            }

            public String getmIsSpeak() {
                return mIsSpeak;
            }

            public void setmIsSpeak(String mIsSpeak) {
                this.mIsSpeak = mIsSpeak;
            }

            public String getmName() {
                return mName;
            }

            public void setmName(String mName) {
                this.mName = mName;
            }

            public String getmType() {
                return mType;
            }

            public void setmType(String mType) {
                this.mType = mType;
            }
        }

        public class MeetingTime implements Serializable {
            private String beginAt;
            private String endAt;

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
        }

        public class MeetingUser implements Serializable {
            private String mRole;
            private String userId;
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getmRole() {
                return mRole;
            }

            public void setmRole(String mRole) {
                this.mRole = mRole;
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

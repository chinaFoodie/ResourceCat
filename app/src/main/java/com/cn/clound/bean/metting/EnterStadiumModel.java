package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 会场模型
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-30 17:10:04
 */
public class EnterStadiumModel extends BaseModel {
    public Stadium data;

    public Stadium getData() {
        return data;
    }

    public void setData(Stadium data) {
        this.data = data;
    }

    public class Stadium {
        private String beginAt;
        private String endAt;
        private String groupId;
        private String id;
        private String isSign;
        private String meetingType;
        private String signType;
        private String state;
        private List<MeetingUser> users;

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

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsSign() {
            return isSign;
        }

        public void setIsSign(String isSign) {
            this.isSign = isSign;
        }

        public String getMeetingType() {
            return meetingType;
        }

        public void setMeetingType(String meetingType) {
            this.meetingType = meetingType;
        }

        public String getSignType() {
            return signType;
        }

        public void setSignType(String signType) {
            this.signType = signType;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<MeetingUser> getUsers() {
            return users;
        }

        public void setUsers(List<MeetingUser> users) {
            this.users = users;
        }

        public class MeetingUser {
            private String attendState;
            private String headImg;
            private String name;
            private String role;
            private String userId;

            public String getAttendState() {
                return attendState;
            }

            public void setAttendState(String attendState) {
                this.attendState = attendState;
            }

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

            public String getRole() {
                return role;
            }

            public void setRole(String role) {
                this.role = role;
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

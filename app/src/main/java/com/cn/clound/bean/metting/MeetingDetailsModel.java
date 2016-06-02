package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 会议详情模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月31日 19:21:00
 */
public class MeetingDetailsModel extends BaseModel {
    private MeetingDetails data;

    public MeetingDetails getData() {
        return data;
    }

    public void setData(MeetingDetails data) {
        this.data = data;
    }

    public class MeetingDetails {
        private String canTalk;
        private String name;
        private String signTypeStr;
        private String state;
        private List<DetailTime> times;
        private String typeStr;
        private String userSize;
        private List<DetailUser> users;

        public String getCanTalk() {
            return canTalk;
        }

        public void setCanTalk(String canTalk) {
            this.canTalk = canTalk;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSignTypeStr() {
            return signTypeStr;
        }

        public void setSignTypeStr(String signTypeStr) {
            this.signTypeStr = signTypeStr;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<DetailTime> getTimes() {
            return times;
        }

        public void setTimes(List<DetailTime> times) {
            this.times = times;
        }

        public String getTypeStr() {
            return typeStr;
        }

        public void setTypeStr(String typeStr) {
            this.typeStr = typeStr;
        }

        public String getUserSize() {
            return userSize;
        }

        public void setUserSize(String userSize) {
            this.userSize = userSize;
        }

        public List<DetailUser> getUsers() {
            return users;
        }

        public void setUsers(List<DetailUser> users) {
            this.users = users;
        }

        public class DetailTime {
            private String beginAt;
            private String detailId;
            private String endAt;

            public String getBeginAt() {
                return beginAt;
            }

            public void setBeginAt(String beginAt) {
                this.beginAt = beginAt;
            }

            public String getDetailId() {
                return detailId;
            }

            public void setDetailId(String detailId) {
                this.detailId = detailId;
            }

            public String getEndAt() {
                return endAt;
            }

            public void setEndAt(String endAt) {
                this.endAt = endAt;
            }
        }

        public class DetailUser {
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

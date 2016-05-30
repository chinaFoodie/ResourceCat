package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-26 11:05:41
 */
public class MeetingTimeExpandModel extends BaseModel implements Serializable {
    public String meetingDate;
    public String meetingTime;
    public String meetingUpdate;
    public List<MeetingTime> timeList;

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingUpdate() {
        return meetingUpdate;
    }

    public void setMeetingUpdate(String meetingUpdate) {
        this.meetingUpdate = meetingUpdate;
    }

    public List<MeetingTime> getTimeList() {
        return timeList;
    }

    public void setTimeList(List<MeetingTime> timeList) {
        this.timeList = timeList;
    }

    public class MeetingTime implements Serializable {
        private String date;
        private String time;
        private String update;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getUpdate() {
            return update;
        }

        public void setUpdate(String update) {
            this.update = update;
        }
    }
}

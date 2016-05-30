package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

/**
 * 会议时间适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-25 17:04:34
 */
public class MeetingTimeModel extends BaseModel {
    private String meetingDate;
    private String meetingTime;
    private String meetingUpdate;

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
}

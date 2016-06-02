package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseBean;

/**
 * 会议管理->会议详情头部时间model
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-2 10:30:20
 */
public class MeetingDetailsTitleTimeModel extends BaseBean {
    private String beginAt;
    private String detailId;
    private String endAt;
    private boolean isChecked;

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

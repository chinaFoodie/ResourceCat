package com.cn.clound.database;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-19 11:30:00
 */
public class SignTipModel extends DataSupport {
    @Column(nullable = false)
    private int signTipId;
    @Column(nullable = false)
    private String signTipTime;//时间格式 HH:mm
    private String signTipState;//闹铃是否唤醒
    private String signTipUserNo;//签到用户No

    public int getSignTipId() {
        return signTipId;
    }

    public void setSignTipId(int signTipId) {
        this.signTipId = signTipId;
    }

    public String getSignTipTime() {
        return signTipTime;
    }

    public void setSignTipTime(String signTipTime) {
        this.signTipTime = signTipTime;
    }

    public String getSignTipState() {
        return signTipState;
    }

    public void setSignTipState(String signTipState) {
        this.signTipState = signTipState;
    }

    public String getSignTipUserNo() {
        return signTipUserNo;
    }

    public void setSignTipUserNo(String signTipUserNo) {
        this.signTipUserNo = signTipUserNo;
    }
}

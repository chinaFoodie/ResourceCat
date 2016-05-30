package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * 聊天扩展字段
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-9 10:33:26
 */
public class ExtendedChatModel implements Serializable {
    private String toUserNo;
    private String toUserAvatar;
    private String toUSerNick;
    private String fromUserNo;
    private String fromUserAvatar;
    private String fromUserNick;
    private String msgType;//信息类型
    private String sessionName;//会话名称

    public String getToUserNo() {
        return toUserNo;
    }

    public void setToUserNo(String toUserNo) {
        this.toUserNo = toUserNo;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public String getFromUserNo() {
        return fromUserNo;
    }

    public String getToUSerNick() {
        return toUSerNick;
    }

    public void setToUSerNick(String toUSerNick) {
        this.toUSerNick = toUSerNick;
    }

    public String getFromUserNick() {
        return fromUserNick;
    }

    public void setFromUserNick(String fromUserNick) {
        this.fromUserNick = fromUserNick;
    }

    public void setFromUserNo(String fromUserNo) {
        this.fromUserNo = fromUserNo;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}

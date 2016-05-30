package com.cn.clound.bean.chat;

import com.cn.clound.bean.BaseModel;

/**
 * 聊天信息群组信息
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-5 10:03:03
 */
public class ChatRoomInfoModel extends BaseModel {
    private ChatRoomInfo data;

    public ChatRoomInfo getData() {
        return data;
    }

    public void setData(ChatRoomInfo data) {
        this.data = data;
    }

    public class ChatRoomInfo {
        private String content;
        private String groupNo;
        private String imGroupId;
        private String name;
        private String offTongxunlu;
        private String isCreate;
        private String number;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getGroupNo() {
            return groupNo;
        }

        public void setGroupNo(String groupNo) {
            this.groupNo = groupNo;
        }

        public String getImGroupId() {
            return imGroupId;
        }

        public void setImGroupId(String imGroupId) {
            this.imGroupId = imGroupId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOffTongxunlu() {
            return offTongxunlu;
        }

        public void setOffTongxunlu(String offTongxunlu) {
            this.offTongxunlu = offTongxunlu;
        }

        public String getIsCreate() {
            return isCreate;
        }

        public void setIsCreate(String isCreate) {
            this.isCreate = isCreate;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return "ChatRoomInfo{" +
                    "content='" + content + '\'' +
                    ", groupNo='" + groupNo + '\'' +
                    ", imGroupId='" + imGroupId + '\'' +
                    ", name='" + name + '\'' +
                    ", offTongxunlu='" + offTongxunlu + '\'' +
                    ", isCreate='" + isCreate + '\'' +
                    ", number='" + number + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ChatRoomInfoModel{" +
                "data=" + data +
                '}';
    }
}

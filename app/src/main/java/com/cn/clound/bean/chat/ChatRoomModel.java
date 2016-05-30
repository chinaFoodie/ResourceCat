package com.cn.clound.bean.chat;

import com.cn.clound.bean.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 群聊model
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 09:51:13
 */
public class ChatRoomModel extends BaseModel {
    private ChatRoomData data;

    public ChatRoomData getData() {
        return data;
    }

    public void setData(ChatRoomData data) {
        this.data = data;
    }

    public static class ChatRoomData {
        private String pageNo;
        private String totalCount;
        private String totalPage;
        private List<ChatRoom> result = new ArrayList<ChatRoom>();

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public List<ChatRoom> getResult() {
            return result;
        }

        public void setResult(List<ChatRoom> result) {
            this.result = result;
        }
    }

    public static class ChatRoom {
        private String content;
        private String groupNo;
        private String imGroupId;
        private String name;
        private String offTongxunlu;
        private String number;
        private String isCreate;

        public String getOffTongxunlu() {
            return offTongxunlu;
        }

        public void setOffTongxunlu(String offTongxunlu) {
            this.offTongxunlu = offTongxunlu;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getIsCreate() {
            return isCreate;
        }

        public void setIsCreate(String isCreate) {
            this.isCreate = isCreate;
        }

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
    }
}

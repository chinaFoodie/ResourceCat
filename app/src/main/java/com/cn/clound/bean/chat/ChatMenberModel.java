package com.cn.clound.bean.chat;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 成员model
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-5 10:28:28
 */
public class ChatMenberModel extends BaseModel {
    private ChatMenberData data;

    public ChatMenberData getData() {
        return data;
    }

    public void setData(ChatMenberData data) {
        this.data = data;
    }

    public class ChatMenberData {
        private String pageNo;
        private String totalPage;
        private String totalCount;
        private List<ChatMenber> result;

        public String getPageNo() {
            return pageNo;
        }

        public void setPageNo(String pageNo) {
            this.pageNo = pageNo;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        public List<ChatMenber> getResult() {
            return result;
        }

        public void setResult(List<ChatMenber> result) {
            this.result = result;
        }

        public class ChatMenber {
            private String name;
            private String head;
            private String userNo;
            private String isCreate;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getHead() {
                return head;
            }

            public void setHead(String head) {
                this.head = head;
            }

            public String getUserNo() {
                return userNo;
            }

            public void setUserNo(String userNo) {
                this.userNo = userNo;
            }

            public String getIsCreate() {
                return isCreate;
            }

            public void setIsCreate(String isCreate) {
                this.isCreate = isCreate;
            }
        }
    }
}

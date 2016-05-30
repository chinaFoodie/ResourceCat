package com.cn.clound.bean.singed;

import com.cn.clound.bean.BaseModel;

/**
 * 设置签到模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-16 16:38:59
 */
public class SetSignModel extends BaseModel {
    private SetSign data;

    public SetSign getData() {
        return data;
    }

    public void setData(SetSign data) {
        this.data = data;
    }

    public class SetSign {
        private String id;
        private String userId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}

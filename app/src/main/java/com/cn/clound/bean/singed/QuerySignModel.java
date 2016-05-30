package com.cn.clound.bean.singed;

import com.cn.clound.bean.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * 查询签到模型
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-16 16:51:42
 */
public class QuerySignModel extends BaseModel implements Serializable {
    private List<QuerySign> data;

    public List<QuerySign> getData() {
        return data;
    }

    public void setData(List<QuerySign> data) {
        this.data = data;
    }

    public class QuerySign implements Serializable {
        private String id;
        private String remindTime;
        private String repeatStr;
        private String state;
        private String type;
        private String updatedOn;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRemindTime() {
            return remindTime;
        }

        public void setRemindTime(String remindTime) {
            this.remindTime = remindTime;
        }

        public String getRepeatStr() {
            return repeatStr;
        }

        public void setRepeatStr(String repeatStr) {
            this.repeatStr = repeatStr;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }
    }
}

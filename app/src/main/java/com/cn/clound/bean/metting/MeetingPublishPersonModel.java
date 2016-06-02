package com.cn.clound.bean.metting;

import com.cn.clound.bean.BaseModel;

import java.util.List;

/**
 * 会议发布人模型
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-6-1 15:55:11
 */
public class MeetingPublishPersonModel extends BaseModel {
    private List<MeetingPublishPerson> data;

    public List<MeetingPublishPerson> getData() {
        return data;
    }

    public void setData(List<MeetingPublishPerson> data) {
        this.data = data;
    }

    public class MeetingPublishPerson {
        private String depName;
        private String head;
        private String name;
        private String unitName;
        private String userNo;

        public String getDepName() {
            return depName;
        }

        public void setDepName(String depName) {
            this.depName = depName;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }
    }
}

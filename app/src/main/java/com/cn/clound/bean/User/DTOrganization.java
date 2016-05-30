package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseBean;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-14 18:18:11
 */
public class DTOrganization extends BaseBean {
    private String orgName;
    private String orgAvatar;
    private String num;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgAvatar() {
        return orgAvatar;
    }

    public void setOrgAvatar(String orgAvatar) {
        this.orgAvatar = orgAvatar;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "DTOrganization{" +
                "orgName='" + orgName + '\'' +
                ", orgAvatar='" + orgAvatar + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}

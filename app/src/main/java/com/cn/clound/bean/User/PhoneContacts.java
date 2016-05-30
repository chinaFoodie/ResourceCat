package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseBean;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-15 10:28:46
 */
public class PhoneContacts extends BaseBean {
    private String filter;
    private String sortLetter;
    private String name;
    private String phoneNo;
    /***
     * 是否被邀请
     **/
    private String joined;

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getJoined() {
        return joined;
    }

    public void setJoined(String joined) {
        this.joined = joined;
    }

    @Override
    public String toString() {
        return "PhoneContacts{" +
                "filter='" + filter + '\'' +
                ", sortLetter='" + sortLetter + '\'' +
                ", name='" + name + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", joined='" + joined + '\'' +
                '}';
    }
}

package com.cn.clound.base.common.contacts;

/**
 * 联系人的模型类.有联系人姓名和电话两个字段.
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:49:03
 */
public class ContactModel {

    private String contactName;

    private String phoneNumber;

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

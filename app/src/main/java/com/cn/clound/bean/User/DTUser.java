package com.cn.clound.bean.User;

import com.cn.clound.bean.BaseBean;

/**
 * 用户类
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-14 10:54:30
 */
public class DTUser extends BaseBean {
    private FindTopContactsModel.TopContactsModel cm;
    private String SortLetters;
    private String headKey;

    private boolean isCheckedBox;

    public FindTopContactsModel.TopContactsModel getCm() {
        return cm;
    }

    public void setCm(FindTopContactsModel.TopContactsModel cm) {
        this.cm = cm;
    }

    public String getSortLetters() {
        return SortLetters;
    }

    public void setSortLetters(String sortLetters) {
        SortLetters = sortLetters;
    }

    public String getHeadKey() {
        return headKey;
    }

    public void setHeadKey(String headKey) {
        this.headKey = headKey;
    }

    public boolean isCheckedBox() {
        return isCheckedBox;
    }

    public void setCheckedBox(boolean checkedBox) {
        isCheckedBox = checkedBox;
    }

    @Override
    public String toString() {
        return "DTUser{" +
                "cm=" + cm +
                ", SortLetters='" + SortLetters + '\'' +
                ", headKey='" + headKey + '\'' +
                ", isCheckedBox=" + isCheckedBox +
                '}';
    }
}

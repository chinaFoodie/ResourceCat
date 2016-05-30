package com.cn.clound.utils;

import java.util.Comparator;

import com.cn.clound.bean.User.DTUser;

/**
 * @author ChunfaLee(ly90219@gmail.com)
 * @date 2016年4月14日 15:32:15
 */
public class PinyinComparator implements Comparator<DTUser> {

    public int compare(DTUser o1, DTUser o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}

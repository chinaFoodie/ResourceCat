package com.cn.clound.utils;

import com.cn.clound.bean.User.PhoneContacts;

import java.util.Comparator;

/**
 * @author ChunfaLee(ly90219@gmail.com)
 * @date 2016年4月14日 15:32:15
 */
public class PhoneContactsComparator implements Comparator<PhoneContacts> {

    public int compare(PhoneContacts o1, PhoneContacts o2) {
        if (o1.getSortLetter().equals("@")
                || o2.getSortLetter().equals("#")) {
            return 1;
        } else if (o1.getSortLetter().equals("#")
                || o2.getSortLetter().equals("@")) {
            return -1;
        } else {
            return o1.getSortLetter().compareTo(o2.getSortLetter());
        }
    }
}

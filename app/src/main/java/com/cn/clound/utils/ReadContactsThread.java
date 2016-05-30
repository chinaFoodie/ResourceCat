package com.cn.clound.utils;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.cn.clound.base.common.contacts.ContactModel;
import com.cn.clound.base.common.contacts.ContactsHelper;
import com.cn.clound.base.common.utils.PinyinUtils;
import com.cn.clound.bean.User.PhoneContacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 异步线程读取通讯录
 *
 * @author ChunfaLee(ly09219@gami.com)
 * @date 2016-5-6 11:57:20
 */
public class ReadContactsThread extends AsyncQueryHandler {
    private Context context;
    private ContactsHelper contactsHelper;
    private List<PhoneContacts> listPc = new ArrayList<PhoneContacts>();
    private PhoneContactsComparator pinyinComparator;

    public ReadContactsThread(ContentResolver cr, Context context) {
        super(cr);
        this.context = context;
        contactsHelper = new ContactsHelper(context);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        PhoneContacts pc;
        List<ContactModel> listModel = contactsHelper.getContactsList();
        for (int i = 0; i < listModel.size(); i++) {
            pc = new PhoneContacts();
            String name = listModel.get(i).getContactName();
            pc.setName(name);
            pc.setPhoneNo(listModel.get(i).getPhoneNumber());
            String temp = PinyinUtils.getPinYin(listModel.get(i).getContactName()).toUpperCase();
            pc.setSortLetter(temp);
//            pc.setFilter(PinyinUtils.getPinYinHeadChar(listModel.get(i).getContactName()).substring(0, 1).toUpperCase());
            listPc.add(pc);
        }
        Collections.sort(listPc, pinyinComparator);
        super.onQueryComplete(token, cookie, cursor);
    }
}

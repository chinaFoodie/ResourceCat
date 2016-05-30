package com.cn.clound.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.User.PhoneContacts;
import com.cn.clound.view.AlertDialog;
import com.hyphenate.easeui.widget.CircleImageView;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-4-15 10:27:05
 */
public class PhoneContactsAdapter extends BaseAdapter {
    private Context context;
    private List<PhoneContacts> phoneList;

    int[] imgResource = new int[]{R.mipmap.img_join_header_wechat, R.mipmap.img_join_header_qq, R.mipmap.img_join_header_tel};

    public PhoneContactsAdapter(Context context, List<PhoneContacts> phoneList) {
        this.context = context;
        this.phoneList = phoneList;
    }

    @Override
    public int getCount() {
        return phoneList.size();
    }

    @Override
    public Object getItem(int position) {
        return phoneList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {//定义不同位置的convertView类型
        if (position < 3) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {//convertView总共类型。默认为1
        return 2;//返回2,表示有2种。你目前只用了2种
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (getItemViewType(position) == 0) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.dt_main_contacts_header_menu, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_contact_header);
                holder.avatat = (CircleImageView) convertView.findViewById(R.id.img_contact_header);
                holder.tvName.setText(phoneList.get(position).getName());
                holder.avatat.setImageResource(imgResource[position]);
            }
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dt_phone_contact_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.phone_contact_name);
            holder.tvPhone = (TextView) convertView.findViewById(R.id.contact_phone_value);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.phone_contact_catalog);
            holder.tvJoin = (TextView) convertView.findViewById(R.id.is_joined);

            int section = getSectionForPosition(position);
            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvLetter.setVisibility(View.VISIBLE);
                String temp = phoneList.get(position).getSortLetter().toUpperCase();
                holder.tvLetter.setText(temp.substring(0, 1));
            } else {
                holder.tvLetter.setVisibility(View.GONE);
            }
            holder.tvName.setText(phoneList.get(position).getName());
            holder.tvPhone.setText(phoneList.get(position).getPhoneNo());
            holder.tvJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog(context).builder()
                            .setMsg("发送短信到" + phoneList.get(position).getName() + ":" + phoneList.get(position).getPhoneNo())
                            .setNegativeButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    doSendSMSTo(phoneList.get(position).getPhoneNo(), "我在使用云在家教实时掌握孩子的学习情况，点击连接下载www.baidu.com!");
                                }
                            }).show();
                }
            });
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvLetter;
        TextView tvJoin;
        CircleImageView avatat;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return phoneList.get(position).getSortLetter().toUpperCase().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = phoneList.get(i).getSortLetter();
            if (sortStr != null & !"".equals(sortStr)) {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 调起系统发短信功能
     *
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            context.startActivity(intent);
        }
    }
}

package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.User.DTUser;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @dae 2016-4-14 11:26:27
 */
public class ContactsMainAdapter extends BaseAdapter {

    private Context context;
    private List<DTUser> listUser;

    public ContactsMainAdapter(Context context, List<DTUser> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public void updateListView(List<DTUser> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int position) {
        return listUser.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getViewTypeCount() {//convertView总共类型。默认为1
        return 2;//返回2,表示有2种。你目前只用了2种
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dt_main_contacts_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.tvLevel = (TextView) convertView.findViewById(R.id.contact_phone);
            holder.imgAvatar = (CircleImageView) convertView.findViewById(R.id.contact_circleimage);
            holder.tvLetter = (TextView) convertView.findViewById(R.id.catalog);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        int section = getSectionForPosition(position);
        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            String temp = listUser.get(position).getSortLetters().toUpperCase();
            holder.tvLetter.setText(temp.substring(0, 1));
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }
        if ("".equals(listUser.get(position).getCm().getDepName())) {
            holder.tvName.setText(listUser.get(position).getCm().getName());
        } else {
            holder.tvName.setText(listUser.get(position).getCm().getName() + " (" + listUser.get(position).getCm().getDepName() + ")");
        }
        holder.tvLevel.setText(listUser.get(position).getCm().getUnitName());
        ImageLoader.getInstance().displayImage(listUser.get(position).getCm().getHead(), holder.imgAvatar, options);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvLevel;
        TextView tvLetter;
        CircleImageView imgAvatar;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return listUser.get(position).getSortLetters().toUpperCase().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = listUser.get(i).getSortLetters();
            if (sortStr != null & !"".equals(sortStr)) {
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
        }
        return -1;
    }
}

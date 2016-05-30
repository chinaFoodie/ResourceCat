package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Class Fragment Message Adapter
 *
 * @author ChunFaLee(ly09219@gamil.com)
 * @date 2016年4月7日17:15:19
 */
public class DeptDetailsListViewAdapter extends BaseAdapter {
    private List<FindDepUserListModel.DepUser> listString;
    private Context context;

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

    public DeptDetailsListViewAdapter(Context context, List<FindDepUserListModel.DepUser> listString) {
        this.context = context;
        this.listString = listString;
    }

    @Override
    public int getCount() {
        return listString.size();
    }

    @Override
    public Object getItem(int position) {
        return listString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dt_list_dept_user_item, parent, false);
            holder.tvDeptName = (TextView) convertView.findViewById(R.id.tv_dept_name);
            holder.imgAvatar = (CircleImageView) convertView.findViewById(R.id.dept_avatar);
            holder.tvDeptPhone = (TextView) convertView.findViewById(R.id.tv_dept_phone);
            holder.tvDeptLevle = (ImageView) convertView.findViewById(R.id.is_joined);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (listString.get(position).getIsSetting().equals("1")) {
            if ("1".equals(listString.get(position).getIsDepManger())) {
                holder.tvDeptLevle.setVisibility(View.VISIBLE);
                holder.tvDeptLevle.setImageResource(R.mipmap.img_dept_details_charge);
            } else {
                holder.tvDeptLevle.setVisibility(View.GONE);
            }
        } else {
            holder.tvDeptLevle.setVisibility(View.VISIBLE);
            holder.tvDeptLevle.setImageResource(R.mipmap.img_menber_is_settings);
        }
        holder.tvDeptPhone.setText(listString.get(position).getUserPhone());
        holder.tvDeptName.setText(listString.get(position).getName());
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", holder.imgAvatar, options);
        return convertView;
    }

    private class ViewHolder {
        TextView tvDeptName;
        TextView tvDeptPhone;
        ImageView tvDeptLevle;
        CircleImageView imgAvatar;
    }
}

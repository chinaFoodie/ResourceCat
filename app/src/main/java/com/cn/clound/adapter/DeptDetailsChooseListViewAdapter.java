package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.choose.RadioOrMultDeptUser;
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
public class DeptDetailsChooseListViewAdapter extends BaseAdapter {
    private List<RadioOrMultDeptUser> listString;
    private Context context;
    private List<BottomUserModel> listHasCunzai;
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

    public DeptDetailsChooseListViewAdapter(Context context, List<RadioOrMultDeptUser> listString, List<BottomUserModel> listHasCunzai) {
        this.context = context;
        this.listString = listString;
        this.listHasCunzai = listHasCunzai;
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
                    R.layout.dt_list_dept_choose_user_item, parent, false);
            holder.tvDeptName = (TextView) convertView.findViewById(R.id.tv_dept_name);
            holder.imgAvatar = (CircleImageView) convertView.findViewById(R.id.dept_avatar);
            holder.tvDeptPhone = (TextView) convertView.findViewById(R.id.tv_dept_phone);
            holder.tvDeptLevle = (CheckBox) convertView.findViewById(R.id.is_joined);
            holder.imgNotChoose = (ImageView) convertView.findViewById(R.id.img_joined);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (PublicDataUtil.isContacs(listHasCunzai, listString.get(position).getUser().getUserNo())) {
            holder.tvDeptLevle.setVisibility(View.GONE);
            holder.imgNotChoose.setVisibility(View.VISIBLE);
            convertView.setClickable(true);
        } else {
            holder.tvDeptLevle.setVisibility(View.VISIBLE);
            holder.imgNotChoose.setVisibility(View.GONE);
            convertView.setClickable(false);
            if (listString.get(position).isHadChecked()) {
                holder.tvDeptLevle.setChecked(true);
            } else {
                holder.tvDeptLevle.setChecked(false);
            }
        }
        holder.tvDeptPhone.setText(listString.get(position).getUser().getUserPhone());
        holder.tvDeptName.setText(listString.get(position).getUser().getName());
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", holder.imgAvatar, options);
        return convertView;
    }

    private class ViewHolder {
        TextView tvDeptName;
        TextView tvDeptPhone;
        CheckBox tvDeptLevle;
        CircleImageView imgAvatar;
        ImageView imgNotChoose;
    }
}

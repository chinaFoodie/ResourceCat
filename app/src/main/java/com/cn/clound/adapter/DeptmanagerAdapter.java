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
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.appconfig.PublicDataUtil;
import com.cn.clound.bean.User.BottomUserModel;
import com.cn.clound.bean.dept.DeptManager;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-20 11:34:37
 */
public class DeptmanagerAdapter extends BaseAdapter {
    private Context context;
    private List<DeptManager> list;
    private int jump;
    private List<BottomUserModel> listHasCunzai;

    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageForEmptyUri(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageOnFail(R.mipmap.img_empty_defalt) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public DeptmanagerAdapter(Context context, List<DeptManager> list, int jump, List<BottomUserModel> listHasCunzai) {
        this.context = context;
        this.list = list;
        this.jump = jump;
        this.listHasCunzai = listHasCunzai;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {//定义不同位置的convertView类型
        if (position < jump) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.dt_dept_manager_list_item, parent, false);
                holder.tvName = (TextView) convertView.findViewById(R.id.dept_manager_name);
                holder.tvPhone = (TextView) convertView.findViewById(R.id.dept_manager_phone);
                holder.avatat = (CircleImageView) convertView.findViewById(R.id.dept_manager_circleimage);
                holder.tvlevle = (TextView) convertView.findViewById(R.id.tv_dept_level);
                holder.imgIsChick = (CheckBox) convertView.findViewById(R.id.ishx_or_isfriends);
                holder.imgIsSettings = (ImageView) convertView.findViewById(R.id.img_is_settings);
                holder.imgNoChoose = (ImageView) convertView.findViewById(R.id.img_or_isfriends);
            } else {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.dt_organization_recycler_item, parent, false);
                holder.avatat = (CircleImageView) convertView.findViewById(R.id.img_org_avatar);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_org_name);
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_org_num);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == 0) {
            holder.tvName.setText(list.get(position).getUm().getName());
            holder.tvPhone.setText(list.get(position).getUm().getUserPhone());
            holder.tvlevle.setText("(" + list.get(position).getUm().getDutyName() + ")");
            ImageLoader.getInstance().displayImage(list.get(position).getUm().getUserHead(), holder.avatat, options);
            if (AppConfig.DEPT_MENBER_MUTIL) {
                if (PublicDataUtil.isContacs(listHasCunzai, list.get(position).getUm().getUserNo())) {
                    holder.imgIsChick.setVisibility(View.GONE);
                    holder.imgNoChoose.setVisibility(View.VISIBLE);
                    convertView.setClickable(true);
                } else {
                    holder.imgIsChick.setVisibility(View.VISIBLE);
                    holder.imgNoChoose.setVisibility(View.GONE);
                    convertView.setClickable(false);
                    if (list.get(position).isCheckedHas()) {
                        holder.imgIsChick.setChecked(true);
                    } else {
                        holder.imgIsChick.setChecked(false);
                    }
                }
            } else {
                if (!"1".equals(list.get(position).getUm().getIsSetting())) {
                    holder.imgIsSettings.setVisibility(View.VISIBLE);
                } else {
                    holder.imgIsSettings.setVisibility(View.GONE);
                }
                holder.imgIsChick.setVisibility(View.GONE);
            }
        } else {
            holder.tvName.setText(list.get(position).getDm().getDepName());
            holder.tvNumber.setText("(" + list.get(position).getDm().getDepUserCount() + ")");
            ImageLoader.getInstance().displayImage("", holder.avatat, options);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvNumber;
        TextView tvlevle;
        CheckBox imgIsChick;
        ImageView imgIsSettings;
        CircleImageView avatat;
        ImageView imgNoChoose;
    }
}

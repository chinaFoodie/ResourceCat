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
public class DeptChooseAdapter extends BaseAdapter {
    private Context context;
    private List<DeptManager> list;
    private int index;

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

    public DeptChooseAdapter(Context context, List<DeptManager> list, int jump) {
        this.context = context;
        this.list = list;
        this.index = jump;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.dt_organization_choose_recycler_item, parent, false);
            holder.avatat = (CircleImageView) convertView.findViewById(R.id.img_org_avatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_org_name);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_org_num);
            holder.tvlevle = (ImageView) convertView.findViewById(R.id.img_is_choosed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (index != -1) {
            if (index == position) {
                holder.tvlevle.setImageResource(R.mipmap.img_no_choose);
            } else {
                holder.tvlevle.setImageResource(R.mipmap.img_choosed);
            }
        } else {
            holder.tvlevle.setImageResource(R.mipmap.img_choosed);
        }
        holder.tvName.setText(list.get(position).getDm().getDepName());
        holder.tvNumber.setText("(" + list.get(position).getDm().getDepUserCount() + ")");
        ImageLoader.getInstance().displayImage("", holder.avatat, options);
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvNumber;
        ImageView tvlevle;
        CircleImageView avatat;
    }
}

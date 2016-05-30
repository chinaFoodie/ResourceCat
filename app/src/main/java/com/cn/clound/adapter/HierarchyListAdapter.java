package com.cn.clound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.hierarchy.FindUtilListModel;

import java.util.List;

/**
 * 上下级单位adapter
 *
 * @ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 17:01:57
 * @phone 18607149219
 */
public class HierarchyListAdapter extends BaseAdapter {
    private Context context;
    private List<FindUtilListModel.Unit> listDept;

    public HierarchyListAdapter(Context context, List<FindUtilListModel.Unit> listDept) {
        this.listDept = listDept;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listDept.size();
    }

    @Override
    public Object getItem(int position) {
        return listDept.get(position);
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
                    R.layout.dt_hierarcher_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_hierarchy_header);
            holder.imgAvatar = (ImageView) convertView.findViewById(R.id.img_hierarchy_header);
            holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_hierarchy_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(listDept.get(position).getUnitName());
        holder.tvNumber.setText("(" + listDept.get(position).getDepCount() + ")");
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
        TextView tvNumber;
        ImageView imgAvatar;
    }
}
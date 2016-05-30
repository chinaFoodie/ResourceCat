package com.cn.clound.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.clound.R;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年4月14日 18:13:40
 */
public class OrganizationAdapter extends BaseAdapter {

    private Context context;
    private List<String> listOrg;

    public OrganizationAdapter(Context context, List<String> listOrg) {
        this.context = context;
        this.listOrg = listOrg;
    }


    @Override
    public int getCount() {
        return listOrg.size();
    }

    @Override
    public Object getItem(int position) {
        return listOrg.get(position);
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
                    R.layout.dt_organization_list_item, parent, false);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_hierarchy_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(listOrg.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView tvName;
    }
}

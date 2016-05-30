package com.cn.clound.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MeetingTimeExpandModel;

import java.util.List;

/**
 * 二级列表时间选择
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-26 11:03:34
 */
public class MeetingTimeExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MeetingTimeExpandModel> listExpand;
    private Handler handler;
    private ExpandableListView expandableListView;

    public MeetingTimeExpandAdapter(Context context, List<MeetingTimeExpandModel> listExpand, Handler handler, ExpandableListView expandableListView) {
        this.context = context;
        this.listExpand = listExpand;
        this.handler = handler;
        this.expandableListView = expandableListView;
    }

    @Override
    public int getGroupCount() {
        return listExpand == null ? 0 : listExpand.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listExpand.get(groupPosition).getTimeList() == null ? 0 : listExpand.get(groupPosition).getTimeList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listExpand.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listExpand.get(groupPosition).getTimeList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder = null;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dt_recycler_meeting_time_item, parent, false);
            holder.tvMeetingDate = (TextView) convertView.findViewById(R.id.tv_meeting_date);
            holder.tvMeetingTime = (TextView) convertView.findViewById(R.id.tv_meeting_time);
            holder.tvMeetingUpdate = (TextView) convertView.findViewById(R.id.tv_meeting_update);
            holder.imgTimeFlag = (ImageView) convertView.findViewById(R.id.img_meeting_time);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.imgTimeFlag.setVisibility(View.VISIBLE);
        holder.tvMeetingUpdate.setText(listExpand.get(groupPosition).getMeetingUpdate());
        if (handler != null) {
            holder.tvMeetingUpdate.setVisibility(View.VISIBLE);
            holder.tvMeetingUpdate.setTextColor(context.getResources().getColor(R.color.colorBlueMainTab));
        } else {
            holder.tvMeetingUpdate.setVisibility(View.GONE);
        }
        holder.tvMeetingTime.setText(listExpand.get(groupPosition).getMeetingTime());
        holder.tvMeetingDate.setText(listExpand.get(groupPosition).getMeetingDate());
        holder.tvMeetingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 101;
                msg.obj = groupPosition;
                handler.sendMessage(msg);
            }
        });
        expandableListView.expandGroup(groupPosition);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder = null;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.dt_recycler_meeting_time_item, parent, false);
            holder.tvMeetingDate = (TextView) convertView.findViewById(R.id.tv_meeting_date);
            holder.tvMeetingTime = (TextView) convertView.findViewById(R.id.tv_meeting_time);
            holder.tvMeetingUpdate = (TextView) convertView.findViewById(R.id.tv_meeting_update);
            holder.imgTimeFlag = (ImageView) convertView.findViewById(R.id.img_meeting_time);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.tvMeetingDate.setText(listExpand.get(groupPosition).getTimeList().get(childPosition).getDate());
        holder.tvMeetingUpdate.setTextColor(context.getResources().getColor(R.color.actionsheet_red));
        holder.tvMeetingTime.setText(listExpand.get(groupPosition).getTimeList().get(childPosition).getTime());
        holder.tvMeetingUpdate.setText(listExpand.get(groupPosition).getTimeList().get(childPosition).getUpdate());
        holder.imgTimeFlag.setVisibility(View.GONE);
        holder.tvMeetingUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 102;
                msg.obj = groupPosition * 1000 + childPosition;
                if (handler != null) {
                    handler.sendMessage(msg);
                }
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        TextView tvMeetingDate;
        TextView tvMeetingTime;
        TextView tvMeetingUpdate;
        ImageView imgTimeFlag;
    }

    private class ChildViewHolder {
        TextView tvMeetingDate;
        TextView tvMeetingTime;
        TextView tvMeetingUpdate;
        ImageView imgTimeFlag;
    }
}

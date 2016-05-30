package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.HistoryMeetingModel;

import java.util.List;

/**
 * 我的会议适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-23 16:37:55
 */
public class HistoryMettingRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HistoryMeetingModel.HistoryMeeting.MeetingModel> listMetting;

    public HistoryMettingRecyclerAdapter(Context context, List<HistoryMeetingModel.HistoryMeeting.MeetingModel> listMetting) {
        this.context = context;
        this.listMetting = listMetting;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_history_metting_swipe_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).tvMeetingName.setText(listMetting.get(position).getName());
        ((MyViewHolder) holder).tvMeetingTime.setText(listMetting.get(position).getBeginAt() + "-" + listMetting.get(position).getEndAt().substring(listMetting.get(position).getEndAt().indexOf(" ") + 1));
        if (listMetting.get(position).getTypeStr().equals("1")) {
            ((MyViewHolder) holder).tvMeetingType.setText("网络会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_net);
        } else {
            ((MyViewHolder) holder).tvMeetingType.setText("实体会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_off);
        }
    }

    @Override
    public int getItemCount() {
        return listMetting != null ? listMetting.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingType;
        TextView tvMeetingTime;
        TextView tvMeetingName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_mine_meeting_name);
            tvMeetingType = (TextView) itemView.findViewById(R.id.tv_mine_meeting_type);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_mine_meeting_time);
        }
    }
}

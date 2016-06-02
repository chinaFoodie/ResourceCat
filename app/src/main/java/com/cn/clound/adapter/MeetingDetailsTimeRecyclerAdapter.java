package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MeetingDetailsModel;

import java.util.List;

/**
 * 会议详情时间适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-1 11:59:01
 */
public class MeetingDetailsTimeRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MeetingDetailsModel.MeetingDetails.DetailTime> listTimes;

    public MeetingDetailsTimeRecyclerAdapter(Context context, List<MeetingDetailsModel.MeetingDetails.DetailTime> listTimes) {
        this.context = context;
        this.listTimes = listTimes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_meeting_details_time_recycler_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String date = listTimes.get(position).getBeginAt().substring(0, listTimes.get(position).getBeginAt().indexOf(" "));
        ((MyViewHolder) holder).tvMeetingDate.setText(date);
        String time = listTimes.get(position).getBeginAt().substring(listTimes.get(position).getBeginAt().indexOf(" ") + 1) + "-" + listTimes.get(position).getEndAt().substring(listTimes.get(position).getEndAt().indexOf(" ") + 1);
        ((MyViewHolder) holder).tvMeetingTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return listTimes != null ? listTimes.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingDate;
        TextView tvMeetingTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMeetingDate = (TextView) itemView.findViewById(R.id.tv_meeting_details_date);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_meeting_details_time);
        }
    }
}

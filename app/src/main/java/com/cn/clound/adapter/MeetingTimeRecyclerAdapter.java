package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MeetingTimeModel;

import java.util.List;

/**
 * 选择时间适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月25日 15:56:19
 */
public class MeetingTimeRecyclerAdapter extends RecyclerView.Adapter {
    private List<MeetingTimeModel> listDate;
    private Context context;

    public MeetingTimeRecyclerAdapter(Context context, List<MeetingTimeModel> listDate) {
        this.context = context;
        this.listDate = listDate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_meeting_time_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tvMeetingDate.setText(listDate.get(position).getMeetingDate());
        ((MyViewHolder) holder).tvMeetingTime.setText(listDate.get(position).getMeetingTime());
        if (listDate.get(position).getMeetingUpdate().equals("添加")) {
            ((MyViewHolder) holder).imgMeetingTime.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).tvMeetingUpdate.setVisibility(View.GONE);
            ((MyViewHolder) holder).tvMeetingUpdate.setText(listDate.get(position).getMeetingUpdate());
            ((MyViewHolder) holder).tvMeetingUpdate.setTextColor(context.getResources().getColor(R.color.colorBlueMainTab));
        } else {
            ((MyViewHolder) holder).tvMeetingUpdate.setVisibility(View.GONE);
            ((MyViewHolder) holder).imgMeetingTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return listDate == null ? 0 : listDate.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingDate;
        TextView tvMeetingTime;
        TextView tvMeetingUpdate;
        ImageView imgMeetingTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMeetingUpdate = (TextView) itemView.findViewById(R.id.tv_meeting_update);
            tvMeetingDate = (TextView) itemView.findViewById(R.id.tv_meeting_date);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_meeting_time);
            imgMeetingTime = (ImageView) itemView.findViewById(R.id.img_meeting_time);
        }
    }
}

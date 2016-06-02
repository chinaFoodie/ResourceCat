package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MyMettingModel;

import java.util.List;

/**
 * 会议->历史会议
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-31 15:39:31
 */
public class HistoryManagerRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MyMettingModel.MeetingData.MineMetting> listMetting;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public HistoryManagerRecyclerAdapter(Context context, List<MyMettingModel.MeetingData.MineMetting> listMetting) {
        this.context = context;
        this.listMetting = listMetting;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_manager_meeting_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).tvMeetingName.setText(listMetting.get(position).getName());
        ((MyViewHolder) holder).tvMeetingDate.setText(listMetting.get(position).getBeginAt().substring(0, listMetting.get(position).getBeginAt().indexOf(" ")));
        ((MyViewHolder) holder).tvMeetingTime.setText(listMetting.get(position).getBeginAt().substring(listMetting.get(position).getBeginAt().indexOf(" ") + 1) + "-" + listMetting.get(position).getEndAt().substring(listMetting.get(position).getEndAt().indexOf(" ") + 1));
        if (listMetting.get(position).getTypeStr().equals("1")) {
            ((MyViewHolder) holder).tvMeetingType.setText("网络会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_net);
        } else {
            ((MyViewHolder) holder).tvMeetingType.setText("实体会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_off);
        }
        if (onItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMetting != null ? listMetting.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingType;
        TextView tvMeetingTime;
        TextView tvMeetingDate;
        TextView tvMeetingName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_history_meeting_name);
            tvMeetingType = (TextView) itemView.findViewById(R.id.tv_history_meeting_type);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_history_meeting_time);
            tvMeetingDate = (TextView) itemView.findViewById(R.id.tv_history_meeting_date);
        }
    }
}

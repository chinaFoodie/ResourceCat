package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MeetingDetailsTitleTimeModel;
import com.cn.clound.interfaces.OnItemClickListener;

import java.util.List;

/**
 * 会议详情头部时间适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-6-1 19:31:01
 */
public class MeetingDetailsTimeTitleRecyclerAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MeetingDetailsTitleTimeModel> list;
    private OnItemClickListener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public MeetingDetailsTimeTitleRecyclerAdapter(Context context, List<MeetingDetailsTitleTimeModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_meeting_details_title_time_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).tvMeetingDate.setText(list.get(position).getBeginAt().substring(0, list.get(position).getBeginAt().indexOf(" ")));
        String tempTime = list.get(position).getBeginAt().substring(list.get(position).getBeginAt().indexOf(" ") + 1) + "-" + list.get(position).getEndAt().substring(list.get(position).getEndAt().indexOf(" ") + 1);
        ((MyViewHolder) holder).tvMeetingTime.setText(tempTime);
        if (list.get(position).isChecked()) {
            ((MyViewHolder) holder).tvMeetingDate.setTextColor(context.getResources().getColor(R.color.colorBlueMainTab));
            ((MyViewHolder) holder).tvMeetingTime.setTextColor(context.getResources().getColor(R.color.colorBlueMainTab));
            ((MyViewHolder) holder).imgBottom.setBackgroundResource(R.color.colorBlueMainTab);
        } else {
            ((MyViewHolder) holder).tvMeetingDate.setTextColor(context.getResources().getColor(R.color.colorGrayAddDept));
            ((MyViewHolder) holder).tvMeetingTime.setTextColor(context.getResources().getColor(R.color.colorGrayAddDept));
            ((MyViewHolder) holder).imgBottom.setBackgroundResource(R.color.colorGrayAddDept);
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
        return list != null ? list.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingDate;
        TextView tvMeetingTime;
        ImageView imgBottom;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMeetingDate = (TextView) itemView.findViewById(R.id.tv_meeting_details_title_date);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_meeting_details_title_time);
            imgBottom = (ImageView) itemView.findViewById(R.id.img_meeting_title_time_choosed);
        }
    }
}

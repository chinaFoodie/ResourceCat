package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.bean.metting.MyMettingModel;

import java.util.Date;
import java.util.List;

/**
 * 我的会议适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-23 16:37:55
 */
public class MineMettingRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MyMettingModel.MeetingData.MineMetting> listMetting;
    private long[] count;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public MineMettingRecyclerAdapter(Context context, List<MyMettingModel.MeetingData.MineMetting> listMetting) {
        this.context = context;
        this.listMetting = listMetting;
        count = new long[listMetting.size()];
        for (int i = 0; i < listMetting.size(); i++) {
            Date temp = DateUtil.string2Date(listMetting.get(i).getBeginAt() + ":00", "yyyy-MM-dd HH:mm:ss");
            count[i] = (temp.getTime() - new Date().getTime()) / 1000;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_mine_metting_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).midView.setVisibility(View.VISIBLE);
        ((MyViewHolder) holder).llMettingDisCount.setVisibility(View.VISIBLE);
        ((MyViewHolder) holder).tvMeetingName.setText(listMetting.get(position).getName());
        ((MyViewHolder) holder).tvMeetingTime.setText(listMetting.get(position).getBeginAt() + "-" + listMetting.get(position).getEndAt().substring(listMetting.get(position).getEndAt().indexOf(" ") + 1));
        if (listMetting.get(position).getType().equals("1")) {
            ((MyViewHolder) holder).tvMeetingType.setText("网络会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_net);
        } else {
            ((MyViewHolder) holder).tvMeetingType.setText("实体会议");
            ((MyViewHolder) holder).tvMeetingType.setBackgroundResource(R.color.color_meeting_off);
        }
//        final long[] secondCount = new long[listMetting.size()];//{1080000, 108003, 1080054, 1340800, 104800, 108030, 108200, 10800, 108040, 1082300};
//        for (int i = 0; i < listMetting.size(); i++) {
//            secondCount[i] = temp - System.currentTimeMillis();
//        }
        ((MyViewHolder) holder).tvHour.post(new Runnable() {
            @Override
            public void run() {
                if (count[position] > 0) {
                    count[position]--;
                    long d = count[position] / 86400;
                    long h = count[position] / 3600 % 24;
                    long m = count[position] / 60 % 60;
                    long s = count[position] % 60;
                    StringBuffer day = new StringBuffer();
                    StringBuffer hour = new StringBuffer();
                    StringBuffer minute = new StringBuffer();
                    StringBuffer second = new StringBuffer();
                    if (h < 10) {
                        hour.append(0);
                    }
                    if (m < 10) {
                        minute.append(0);
                    }
                    if (s < 10) {
                        second.append(0);
                    }
                    day.append(d);
                    hour.append(h);
                    minute.append(m);
                    second.append(s);
                    ((MyViewHolder) holder).tvDay.setText(day);
                    ((MyViewHolder) holder).tvHour.setText(hour);
                    ((MyViewHolder) holder).tvMinute.setText(minute);
                    ((MyViewHolder) holder).tvSecond.setText(second);
                    ((MyViewHolder) holder).tvHour.postDelayed(this, 1000);
                } else {
                    ((MyViewHolder) holder).tvDay.setText("0");
                    ((MyViewHolder) holder).tvHour.setText("00");
                    ((MyViewHolder) holder).tvMinute.setText("00");
                    ((MyViewHolder) holder).tvSecond.setText("00");
                }
            }
        });
        if (onItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listMetting != null ? listMetting.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay;
        TextView tvHour;
        TextView tvMinute;
        TextView tvSecond;
        View midView;
        LinearLayout llMettingDisCount;
        TextView tvMeetingType;
        TextView tvMeetingTime;
        TextView tvMeetingName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDay = (TextView) itemView.findViewById(R.id.tv_has_metting_day);
            tvHour = (TextView) itemView.findViewById(R.id.tv_discount_hour);
            tvMinute = (TextView) itemView.findViewById(R.id.tv_discount_minute);
            tvSecond = (TextView) itemView.findViewById(R.id.tv_discount_second);
            midView = itemView.findViewById(R.id.view_mid_mine_metting);
            llMettingDisCount = (LinearLayout) itemView.findViewById(R.id.ll_mine_metting_time_count);
            tvMeetingName = (TextView) itemView.findViewById(R.id.tv_mine_meeting_name);
            tvMeetingType = (TextView) itemView.findViewById(R.id.tv_mine_meeting_type);
            tvMeetingTime = (TextView) itemView.findViewById(R.id.tv_mine_meeting_time);
        }
    }
}

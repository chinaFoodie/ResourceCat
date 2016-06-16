package com.cn.clound.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.view.SwipeMenuLayout;
import com.cn.clound.bean.singed.QuerySignModel;
import com.cn.clound.interfaces.OnItemClickListener;

import java.util.List;

/**
 * 签到适配器
 *
 * @author ChunfaLee(ly09219@gamil.com).
 * @date 2016年5月9日 19:26:47
 */
public class SignTipRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<QuerySignModel.QuerySign> listSign;
    private Handler handler;
    private OnItemClickListener onItemClickLitener;

    public OnItemClickListener getOnItemClickLitener() {
        return onItemClickLitener;
    }

    public void setOnItemClickLitener(OnItemClickListener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public SignTipRecyclerAdapter(Context context, List<QuerySignModel.QuerySign> listSign, Handler handler) {
        this.context = context;
        this.listSign = listSign;
        this.handler = handler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_sign_tip_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final SwipeMenuLayout item = (SwipeMenuLayout) holder.itemView;
        String temp = listSign.get(position).getRemindTime().substring(0, listSign.get(position).getRemindTime().indexOf(":"));
        if (Integer.parseInt(temp) <= 12) {
            ((MyViewHolder) holder).tvSignTipMorAft.setText("上午");
        } else {
            ((MyViewHolder) holder).tvSignTipMorAft.setText("下午");
        }
        ((MyViewHolder) holder).tvSignTipTime.setText(listSign.get(position).getRemindTime());
        ((MyViewHolder) holder).tvSignTipType.setText(replase(listSign.get(position).getRepeatStr()));
        if (listSign.get(position).getState().equals("1")) {
            ((MyViewHolder) holder).imgSignTipOpen.setImageResource(R.mipmap.img_switch_open);
        } else {
            ((MyViewHolder) holder).imgSignTipOpen.setImageResource(R.mipmap.img_switch_off);
        }
        if (onItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
        ((MyViewHolder) holder).imgSignTipOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 100;
                msg.obj = position;
                if (listSign.get(position).getState().equals("1")) {
                    msg.arg2 = 2;
                } else {
                    msg.arg2 = 1;
                }
                handler.sendMessage(msg);
            }
        });
        ((MyViewHolder) holder).tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 101;
                msg.obj = listSign.get(position);
                item.smoothCloseMenu();
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSign == null ? 1 : listSign.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSignTipMorAft;
        TextView tvSignTipTime;
        TextView tvSignTipType;
        ImageView imgSignTipOpen;
        TextView tvDel;

        public MyViewHolder(View view) {
            super(view);
            tvSignTipMorAft = (TextView) view.findViewById(R.id.tv_sign_tip_mor_aft);
            tvSignTipTime = (TextView) view.findViewById(R.id.tv_sign_tip_time);
            tvSignTipType = (TextView) view.findViewById(R.id.tv_sign_tip_type);
            imgSignTipOpen = (ImageView) view.findViewById(R.id.img_sign_tip_open);
            tvDel = (TextView) view.findViewById(R.id.btDelete);
        }
    }

    private String replase(String replase) {
        if (replase.contains("1") && replase.contains("2") && replase.contains("3") && replase.contains("4") &&
                replase.contains("5") && replase.contains("6") && replase.contains("7")) {
            replase = "每天";
        } else {
            replase = replase.replace("1", "每周一").replace("2", "每周二").replace("3", "每周三")
                    .replace("4", "每周四").replace("5", "每周五").replace("6", "每周六").replace("7", "每周日");
        }
        return replase;
    }
}

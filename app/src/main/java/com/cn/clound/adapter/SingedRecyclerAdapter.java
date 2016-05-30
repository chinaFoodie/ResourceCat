package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.singed.UserSingedModel;

import java.util.List;

/**
 * 签到适配器
 *
 * @author ChunfaLee(ly09219@gamil.com).
 * @date 2016年5月9日 19:26:47
 */
public class SingedRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<UserSingedModel.SingedData.SingedModel> listSinge;

    public SingedRecyclerAdapter(Context context, List<UserSingedModel.SingedData.SingedModel> listSinge) {
        this.context = context;
        this.listSinge = listSinge;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_singed_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (listSinge == null) {
            ((MyViewHolder) holder).tvSignTime.setText("今天还未签到！");
            ((MyViewHolder) holder).tvSignAddress.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).tvSignTime.setText(listSinge.get(position).getSignDatetime());
            ((MyViewHolder) holder).tvSignAddress.setVisibility(View.VISIBLE);
            ((MyViewHolder) holder).tvSignAddress.setText(listSinge.get(position).getSignAddress());
            if (position == 0) {
                ((MyViewHolder) holder).rlLastBg.setBackgroundResource(R.mipmap.img_singed_time_line_item_last);
            } else {
                ((MyViewHolder) holder).rlLastBg.setBackgroundResource(R.mipmap.img_singed_time_line_item);
            }
        }
    }

    @Override
    public int getItemCount() {
        return listSinge == null ? 1 : listSinge.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSignTime;
        TextView tvSignAddress;
        RelativeLayout rlLastBg;

        public MyViewHolder(View view) {
            super(view);
            tvSignTime = (TextView) view.findViewById(R.id.tv_sign_date);
            tvSignAddress = (TextView) view.findViewById(R.id.tv_sign_address);
            rlLastBg = (RelativeLayout) view.findViewById(R.id.tl_last_sign);
        }
    }
}

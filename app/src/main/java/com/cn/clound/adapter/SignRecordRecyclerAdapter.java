package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.singed.UserSingedModel;

import java.util.List;

/**
 * 签到历史记录fragment适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-13 16:04:31
 */
public class SignRecordRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<UserSingedModel.SingedData.SingedModel> list;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public SignRecordRecyclerAdapter(Context context, List<UserSingedModel.SingedData.SingedModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_sign_record_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tvSignAddress.setText(list.get(position).getSignAddress());
        String temp = list.get(position).getSignDatetime();
        ((MyViewHolder) holder).tvSignTime.setText(temp.substring(temp.indexOf(" ") + 1));
        //设置Item事件监听
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
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSignTime;
        TextView tvSignAddress;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSignAddress = (TextView) itemView.findViewById(R.id.tv_sign_record_address);
            tvSignTime = (TextView) itemView.findViewById(R.id.tv_sign_record_time);
        }
    }
}

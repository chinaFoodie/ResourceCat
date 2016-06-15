package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.singed.TimeCycleModel;
import com.cn.clound.interfaces.OnItemClickLitener;

import java.util.List;

/**
 * 签到定位适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年5月10日 09:58:53
 */
public class SignTimeCycleAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<TimeCycleModel> listPoi;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public SignTimeCycleAdapter(Context context, List<TimeCycleModel> listPoi) {
        this.context = context;
        this.listPoi = listPoi;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_time_cycle_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).tvTimeName.setText(listPoi.get(position).getWeekName());
        if (listPoi.get(position).isChoosed()) {
            ((MyViewHolder) holder).isChoose.setChecked(true);
        } else {
            ((MyViewHolder) holder).isChoose.setChecked(false);
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
        return listPoi == null ? 0 : listPoi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTimeName;
        CheckBox isChoose;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTimeName = (TextView) itemView.findViewById(R.id.tv_time_cycle);
            isChoose = (CheckBox) itemView.findViewById(R.id.checkbox_chose_time_cycle);
        }
    }
}

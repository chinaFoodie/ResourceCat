package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.baidu.location.Poi;
import com.cn.clound.R;
import com.cn.clound.bean.singed.SingedPoiModel;

import java.util.List;

/**
 * 签到定位适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年5月10日 09:58:53
 */
public class SingedLocationRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<SingedPoiModel> listPoi;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public SingedLocationRecyclerAdapter(Context context, List<SingedPoiModel> listPoi) {
        this.context = context;
        this.listPoi = listPoi;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_singed_location_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((MyViewHolder) holder).tvPoiName.setText(listPoi.get(position).getPoi().getName());
        ((MyViewHolder) holder).tvPoiAddress.setText(listPoi.get(position).getLocation().getAddrStr());
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

        TextView tvPoiName;
        TextView tvPoiAddress;
        CheckBox isChoose;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPoiAddress = (TextView) itemView.findViewById(R.id.tv_location_poi_address);
            tvPoiName = (TextView) itemView.findViewById(R.id.tv_location_poi_name);
            isChoose = (CheckBox) itemView.findViewById(R.id.checkbox_chose_location);
        }
    }
}

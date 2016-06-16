package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.dept.UnCallocateMenberModel;
import com.cn.clound.interfaces.OnItemClickListener;
import com.hyphenate.easeui.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class UnAllocatedDeptRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> list;
    private OnItemClickListener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickListener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public UnAllocatedDeptRecyclerAdapter(Context context, List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_responsible_list_item_value, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tvResponName.setText(list.get(position).getPhone());
        ((MyViewHolder) holder).tvResponName.setText(list.get(position).getName());
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
        TextView tvResponName;
        TextView tvResponPhone;
        CircleImageView imgAvatar;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.responsible_avatar);
            tvResponPhone = (TextView) itemView.findViewById(R.id.tv_responsible_phone);
            tvResponName = (TextView) itemView.findViewById(R.id.tv_responsible_name);
        }
    }
}

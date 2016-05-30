package com.cn.clound.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.singed.GroupSingedModel;
import com.cn.clound.bean.singed.UserSingedModel;

import java.util.List;

/**
 * 团队签到历史记录fragment适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-13 16:04:31
 */
public class GroupRecordRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<GroupSingedModel.GroupData.GroupModel> list;
    private OnItemClickLitener onItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public GroupRecordRecyclerAdapter(Context context, List<GroupSingedModel.GroupData.GroupModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_recycler_group_record_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((MyViewHolder) holder).tvSignName.setText(list.get(position).getUserName());
        String tempStart = list.get(position).getFistSignInTime();
        ((MyViewHolder) holder).tvSignStart.setText(tempStart.substring(tempStart.indexOf(" ") + 1) + "\n" + list.get(position).getFirstSignAddress());
        String tempEnd = list.get(position).getFistSignInTime();
        ((MyViewHolder) holder).tvSignEnd.setText(tempEnd.substring(tempEnd.indexOf(" ") + 1) + "\n" + list.get(position).getLastSignAddress());
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
        TextView tvSignStart;
        TextView tvSignEnd;
        TextView tvSignName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvSignName = (TextView) itemView.findViewById(R.id.tv_group_record_name);
            tvSignStart = (TextView) itemView.findViewById(R.id.tv_group_record_start);
            tvSignEnd = (TextView) itemView.findViewById(R.id.tv_group_record_end);
        }
    }
}

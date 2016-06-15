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
import com.cn.clound.bean.dept.UnCallocateMenberModel;
import com.cn.clound.interfaces.OnItemClickLitener;
import com.hyphenate.easeui.widget.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class UnAllocatedDeptSwipeRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> list;
    private OnItemClickLitener onItemClickLitener;
    private Handler handler;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    public UnAllocatedDeptSwipeRecyclerAdapter(Context context, List<UnCallocateMenberModel.CallocateMenberModel.UnCallocateMenber> list, Handler handler) {
        this.context = context;
        this.list = list;
        this.handler = handler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_list_siwpe_recycler_un_allocated_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final SwipeMenuLayout itemView = (SwipeMenuLayout) holder.itemView;
        ((MyViewHolder) holder).tvResponPhone.setText(list.get(position).getPhone());
        ((MyViewHolder) holder).tvResponName.setText(list.get(position).getName());
        if (list.get(position).getIsSetting().equals("1")) {
            ((MyViewHolder) holder).imgIsSettings.setVisibility(View.GONE);
        } else {
            ((MyViewHolder) holder).imgIsSettings.setVisibility(View.VISIBLE);
        }
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
        ((MyViewHolder) holder).tvDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 100;
                msg.obj = position;
                handler.sendMessage(msg);
                itemView.smoothCloseMenu();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvResponName;
        TextView tvResponPhone;
        CircleImageView imgAvatar;
        TextView tvDel;
        ImageView imgIsSettings;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.responsible_avatar);
            tvResponPhone = (TextView) itemView.findViewById(R.id.tv_responsible_phone);
            tvResponName = (TextView) itemView.findViewById(R.id.tv_responsible_name);
            tvDel = (TextView) itemView.findViewById(R.id.btDelete);
            imgIsSettings = (ImageView) itemView.findViewById(R.id.img_is_settings);
        }
    }
}

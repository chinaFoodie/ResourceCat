package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.chat.ChatMenberModel;
import com.cn.clound.interfaces.OnItemClickListener;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * 部门人员列表适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月18日 17:48:36
 */
public class ChatMenberRecyclerAdapter extends RecyclerView.Adapter<ChatMenberRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<ChatMenberModel.ChatMenberData.ChatMenber> listUser;
    private OnItemClickListener mOnItemClickLitener;
    private boolean isCreate;
    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageForEmptyUri(R.mipmap.img_empty_defalt) // resource or drawable
            .showImageOnFail(R.mipmap.img_empty_defalt) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public ChatMenberRecyclerAdapter(Context context, List<ChatMenberModel.ChatMenberData.ChatMenber> listUser, boolean isCreate) {
        this.context = context;
        this.listUser = listUser;
        this.isCreate = isCreate;
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ChatMenberRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.chat_menber_grid_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ChatMenberRecyclerAdapter.MyViewHolder holder, final int position) {
        if (position < listUser.size()) {
            holder.tvDeptName.setText(listUser.get(position).getName());
            ImageLoader.getInstance().displayImage(listUser.get(position).getHead(), holder.imgAvatar, options);
        } else if (isCreate) {
            if (position == listUser.size()) {
                holder.imgAvatar.setImageResource(R.mipmap.img_add_chat_menber);
            } else {
                holder.imgAvatar.setImageResource(R.mipmap.img_del_chat_menber);
            }
        } else {
            if (position == listUser.size()) {
                holder.imgAvatar.setImageResource(R.mipmap.img_add_chat_menber);
            }
        }
        //设置Item事件监听
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (isCreate) {
            return listUser.size() + 2;
        } else {
            return listUser.size() + 1;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        CircleImageView imgAvatar;

        public MyViewHolder(View view) {
            super(view);
            tvDeptName = (TextView) view.findViewById(R.id.tv_chat_menber_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.img_chat_menber_head);
        }
    }
}

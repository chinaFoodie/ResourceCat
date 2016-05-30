package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.cn.clound.bean.dept.FindUnitMangerListModel;
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
public class ResponsibleAdapter extends RecyclerView.Adapter<ResponsibleAdapter.MyViewHolder> {
    private Context context;
    private List<FindUnitMangerListModel.UserListModel> listUser;
    private OnItemClickLitener mOnItemClickLitener;
    private Handler handler;
    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public ResponsibleAdapter(Context context, List<FindUnitMangerListModel.UserListModel> listUser, Handler handler) {
        this.context = context;
        this.listUser = listUser;
        this.handler = handler;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public ResponsibleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_responsible_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final ResponsibleAdapter.MyViewHolder holder, final int position) {
        final SwipeMenuLayout itemView = (SwipeMenuLayout) holder.itemView;
        holder.tvResponName.setText(listUser.get(position).getName() + " (" + listUser.get(position).getDutyName() + ")");
        holder.tvResponPhone.setText(listUser.get(position).getUserPhone());
        ImageLoader.getInstance().displayImage(listUser.get(position).getUserHead(), holder.imgAvatar, options);
        if (listUser.get(position).getIsSetting().equals("1")) {
            holder.isSettings.setVisibility(View.GONE);
        } else {
            holder.isSettings.setVisibility(View.VISIBLE);
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
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.smoothCloseMenu();
                Message msg = new Message();
                msg.what = 201;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvResponName;
        TextView tvResponPhone;
        CircleImageView imgAvatar;
        TextView tvOpen;
        TextView tvDelete;
        ImageView isSettings;

        public MyViewHolder(View view) {
            super(view);
            tvResponName = (TextView) view.findViewById(R.id.tv_responsible_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.responsible_avatar);
            tvResponPhone = (TextView) view.findViewById(R.id.tv_responsible_phone);
            tvOpen = (TextView) view.findViewById(R.id.btOpen);
            tvOpen.setVisibility(View.GONE);
            tvDelete = (TextView) view.findViewById(R.id.btDelete);
            isSettings = (ImageView) view.findViewById(R.id.img_is_settings);
        }
    }
}

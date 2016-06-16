package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.interfaces.OnItemClickListener;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * 会议详情参会人员适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月18日 17:48:36
 */
public class MeetingDetailsMenberRecyclerAdapter extends RecyclerView.Adapter<MeetingDetailsMenberRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<MeetingDetailsModel.MeetingDetails.DetailUser> listUser;
    private OnItemClickListener mOnItemClickLitener;
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

    public MeetingDetailsMenberRecyclerAdapter(Context context, List<MeetingDetailsModel.MeetingDetails.DetailUser> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public MeetingDetailsMenberRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.chat_menber_grid_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MeetingDetailsMenberRecyclerAdapter.MyViewHolder holder, final int position) {
        holder.tvDeptName.setText(listUser.get(position).getName());
        ImageLoader.getInstance().displayImage(listUser.get(position).getHeadImg(), holder.imgAvatar, options);
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
        return listUser != null ? listUser.size() : 0;
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

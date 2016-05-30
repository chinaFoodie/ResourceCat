package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.clound.R;
import com.cn.clound.bean.User.BottomUserModel;
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
public class AAABottomChoseChatMenberAdapter extends RecyclerView.Adapter<AAABottomChoseChatMenberAdapter.MyViewHolder> {
    private Context context;
    private List<BottomUserModel> listUser;
    private OnItemClickLitener mOnItemClickLitener;
    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.img_join_header_wechat) // resource or drawable
            .showImageForEmptyUri(R.mipmap.img_join_header_wechat) // resource or drawable
            .showImageOnFail(R.mipmap.img_join_header_wechat) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public AAABottomChoseChatMenberAdapter(Context context, List<BottomUserModel> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public AAABottomChoseChatMenberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_bottom_chose_menber_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final AAABottomChoseChatMenberAdapter.MyViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(listUser.get(position).getUserHead(), holder.imgAvatar, options);
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
        return listUser == null ? 0 : listUser.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;

        public MyViewHolder(View view) {
            super(view);
            imgAvatar = (CircleImageView) view.findViewById(R.id.bottom_contact_circleimage);
        }
    }
}

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
import com.cn.clound.bean.metting.MeetingPublishPersonModel;
import com.cn.clound.interfaces.OnItemClickLitener;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * 会议发布人适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-1 16:00:12
 */
public class PublishMeetingPersonRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<MeetingPublishPersonModel.MeetingPublishPerson> listPerson;
    private OnItemClickLitener onItemClickLitener;
    private Handler handler;

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

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

    public PublishMeetingPersonRecyclerAdapter(Context context, List<MeetingPublishPersonModel.MeetingPublishPerson> listPerson, Handler handler) {
        this.context = context;
        this.listPerson = listPerson;
        this.handler = handler;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_publish_manager_recycler_swipe_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final SwipeMenuLayout item = (SwipeMenuLayout) holder.itemView;
        ((MyViewHolder) holder).tvDeptName.setText(listPerson.get(position).getName());
        ((MyViewHolder) holder).tvDeptPhone.setText(listPerson.get(position).getDepName());
        ((MyViewHolder) holder).tvDeptLevle.setVisibility(View.GONE);
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", ((MyViewHolder) holder).imgAvatar, options);
        if (onItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickLitener.onItemClick(holder.itemView, position);
                }
            });
        }
        ((MyViewHolder) holder).tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.smoothCloseMenu();
                Message msg = new Message();
                msg.what = 1002;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPerson != null ? listPerson.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        TextView tvDeptPhone;
        ImageView tvDeptLevle;
        CircleImageView imgAvatar;
        TextView tvDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvDelete = (TextView) itemView.findViewById(R.id.btDelete);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.dept_avatar);
            tvDeptLevle = (ImageView) itemView.findViewById(R.id.is_joined);
            tvDeptName = (TextView) itemView.findViewById(R.id.tv_dept_name);
            tvDeptPhone = (TextView) itemView.findViewById(R.id.tv_dept_phone);
        }
    }
}

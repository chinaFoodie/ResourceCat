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
import com.cn.clound.bean.dept.FindDepUserListModel;
import com.cn.clound.interfaces.OnItemClickLitener;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-6 12:47:20
 */
public class DeptPepoleRecyclerAdapter extends RecyclerView.Adapter {
    private OnItemClickLitener onItemClickLitener;
    private Handler handler;

    public OnItemClickLitener getOnItemClickLitener() {
        return onItemClickLitener;
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

    private Context context;
    private List<FindDepUserListModel.DepUser> listUser;
    int[] imgResource = new int[]{R.mipmap.img_contact_header_join, R.mipmap.img_contact_header_up_down, R.mipmap.img_contact_header_org, R.mipmap.img_contact_header_group};
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

    public DeptPepoleRecyclerAdapter(Context context, List<FindDepUserListModel.DepUser> listUser, Handler handler) {
        this.context = context;
        this.listUser = listUser;
        this.handler = handler;
    }

    public void updateListView(List<FindDepUserListModel.DepUser> list) {
        this.listUser = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeadViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_list_dept_user_item, parent,
                    false));
        } else {
            return new ContentViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_dept_swipe_list_item, parent,
                    false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadViewHolder) {
            if (listUser.get(position).getIsSetting().equals("1")) {
                ((HeadViewHolder) holder).tvDeptLevle.setImageResource(R.mipmap.img_dept_details_charge);
                ((HeadViewHolder) holder).tvDeptLevle.setVisibility(View.VISIBLE);
            } else {
                ((HeadViewHolder) holder).tvDeptLevle.setImageResource(R.mipmap.img_menber_is_settings);
                ((HeadViewHolder) holder).tvDeptLevle.setVisibility(View.VISIBLE);
            }
            ((HeadViewHolder) holder).tvDeptName.setText(listUser.get(position).getName());
            ((HeadViewHolder) holder).tvDeptPhone.setText(listUser.get(position).getUserPhone());
            ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", ((HeadViewHolder) holder).imgAvatar, options);
        } else {
            final SwipeMenuLayout itemView = (SwipeMenuLayout) ((ContentViewHolder) holder).itemView;
            if (listUser.get(position).getIsSetting().equals("1")) {
                ((ContentViewHolder) holder).tvDeptLevle.setVisibility(View.GONE);
            } else {
                ((ContentViewHolder) holder).tvDeptLevle.setImageResource(R.mipmap.img_menber_is_settings);
                ((ContentViewHolder) holder).tvDeptLevle.setVisibility(View.VISIBLE);
            }
            ((ContentViewHolder) holder).tvDeptName.setText(listUser.get(position).getName());
            ((ContentViewHolder) holder).tvDeptPhone.setText(listUser.get(position).getUserPhone());
            ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", ((ContentViewHolder) holder).imgAvatar, options);
            ((ContentViewHolder) holder).btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SwipeMenuLayout) holder.itemView).smoothCloseMenu();
                    Message msg = new Message();
                    msg.what = 1000;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
            ((ContentViewHolder) holder).btTransfer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.smoothCloseMenu();
                    Message msg = new Message();
                    msg.what = 200;
                    msg.obj = position;
                    handler.sendMessage(msg);
                }
            });
            ((ContentViewHolder) holder).btDelete.setOnClickListener(new View.OnClickListener() {
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
        return listUser != null ? listUser.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if ("1".equals(listUser.get(position).getIsDepManger())) {
            return 0;
        } else {
            return 1;
        }
    }

    public static class HeadViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        TextView tvDeptPhone;
        ImageView tvDeptLevle;
        CircleImageView imgAvatar;

        public HeadViewHolder(View view) {
            super(view);
            tvDeptName = (TextView) view.findViewById(R.id.tv_dept_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.dept_avatar);
            tvDeptPhone = (TextView) view.findViewById(R.id.tv_dept_phone);
            tvDeptLevle = (ImageView) view.findViewById(R.id.is_joined);
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        TextView tvDeptPhone;
        ImageView tvDeptLevle;
        CircleImageView imgAvatar;
        View btTransfer;
        View btDelete;

        public ContentViewHolder(View view) {
            super(view);
            tvDeptName = (TextView) view.findViewById(R.id.tv_dept_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.dept_avatar);
            tvDeptPhone = (TextView) view.findViewById(R.id.tv_dept_phone);
            tvDeptLevle = (ImageView) view.findViewById(R.id.is_joined);
            btTransfer = view.findViewById(R.id.btOpen);
            btDelete = view.findViewById(R.id.btDelete);
        }
    }
}

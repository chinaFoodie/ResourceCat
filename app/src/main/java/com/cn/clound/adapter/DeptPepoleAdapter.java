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
 * 部门人员列表适配器
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月18日 17:48:36
 */
public class DeptPepoleAdapter extends RecyclerView.Adapter<DeptPepoleAdapter.MyViewHolder> {
    private Context context;
    private List<FindDepUserListModel.DepUser> listUser;
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

    public DeptPepoleAdapter(Context context, List<FindDepUserListModel.DepUser> listUser, Handler handler) {
        this.context = context;
        this.listUser = listUser;
        this.handler = handler;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public DeptPepoleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_dept_swipe_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final DeptPepoleAdapter.MyViewHolder holder, final int position) {
        final SwipeMenuLayout itemView = (SwipeMenuLayout) holder.itemView;
        holder.tvDeptName.setText(listUser.get(position).getName());
        if ("1".equals(listUser.get(position).getIsDepManger())) {
            holder.tvDeptLevle.setVisibility(View.VISIBLE);
            holder.btTransfer.setVisibility(View.GONE);
            holder.btDelete.setVisibility(View.GONE);
        } else {
            holder.tvDeptLevle.setVisibility(View.GONE);
            holder.btTransfer.setVisibility(View.VISIBLE);
            holder.btDelete.setVisibility(View.VISIBLE);
        }
        holder.tvDeptPhone.setText(listUser.get(position).getUserPhone());
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", holder.imgAvatar, options);
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
        holder.btTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemView.smoothCloseMenu();
                Message msg = new Message();
                msg.what = 200;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
        holder.btDelete.setOnClickListener(new View.OnClickListener() {
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
        TextView tvDeptName;
        TextView tvDeptPhone;
        ImageView tvDeptLevle;
        CircleImageView imgAvatar;
        View btTransfer;
        View btDelete;

        public MyViewHolder(View view) {
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

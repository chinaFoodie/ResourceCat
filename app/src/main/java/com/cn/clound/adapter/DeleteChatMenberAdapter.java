package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.activity.ChatGroupMenberActivity;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.view.SwipeMenuLayout;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.chat.ChatMenberModel;
import com.cn.clound.bean.dept.FindUnitMangerListModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * Class Fragment Message Adapter
 *
 * @author ChunFaLee(ly09219@gamil.com)
 * @date 2016年4月7日17:15:19
 */
public class DeleteChatMenberAdapter extends RecyclerView.Adapter {
    private Context context;
    private OnItemClickLitener mOnItemClickLitener;
    private List<ChatMenberModel.ChatMenberData.ChatMenber> listString;
    private String chooseModel;
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

    public DeleteChatMenberAdapter(Context context, List<ChatMenberModel.ChatMenberData.ChatMenber> listUser, String choosemodel, Handler handler) {
        this.context = context;
        this.listString = listUser;
        this.chooseModel = choosemodel;
        this.handler = handler;
    }

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ContentViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_list_delete_chat_menber, parent,
                    false));
        } else {
            return new MyViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_list_delete_chat_menber_item, parent,
                    false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {
            ((ContentViewHolder) holder).tvDeptName.setText(listString.get(position).getName());
            ImageLoader.getInstance().displayImage(listString.get(position).getHead(), ((ContentViewHolder) holder).imgAvatar, options);
            if (chooseModel.equals("1") && !listString.get(position).getIsCreate().equals("1")) {
                ((ContentViewHolder) holder).tvDeptLevle.setVisibility(View.VISIBLE);
            } else {
                ((ContentViewHolder) holder).tvDeptLevle.setVisibility(View.GONE);
            }
        } else {
            final SwipeMenuLayout itemView = (SwipeMenuLayout) holder.itemView;
            ((MyViewHolder) holder).tvDeptName.setText(listString.get(position).getName());
            ImageLoader.getInstance().displayImage(listString.get(position).getHead(), ((MyViewHolder) holder).imgAvatar, options);
            if (chooseModel.equals("1") && !listString.get(position).getIsCreate().equals("1")) {
                ((MyViewHolder) holder).tvDeptLevle.setVisibility(View.VISIBLE);
            } else {
                ((MyViewHolder) holder).tvDeptLevle.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((MyViewHolder) holder).tvDeptLevle.isChecked()) {
                        ((MyViewHolder) holder).tvDeptLevle.setChecked(false);
                        if (ChatGroupMenberActivity.listDel.contains(listString.get(position).getUserNo())) {
                            ChatGroupMenberActivity.listDel.remove(listString.get(position).getUserNo());
                        }
                    } else {
                        ((MyViewHolder) holder).tvDeptLevle.setChecked(true);
                        if (!ChatGroupMenberActivity.listDel.contains(listString.get(position).getUserNo()) && !listString.get(position).getIsCreate().equals("1")) {
                            ChatGroupMenberActivity.listDel.add(listString.get(position).getUserNo());
                        }
                    }
                }
            });
            ((MyViewHolder) holder).btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.smoothCloseMenu();
                    new AlertDialog(context).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Message msg = new Message();
                            msg.what = 1000;
                            msg.obj = listString.get(position).getUserNo();
                            handler.sendMessage(msg);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setMsg("是否确认删除此成员？").show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ("1".equals(listString.get(position).getIsCreate())) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        CheckBox tvDeptLevle;
        CircleImageView imgAvatar;
        View btDelete;

        public MyViewHolder(View view) {
            super(view);
            tvDeptName = (TextView) view.findViewById(R.id.del_menber_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.del_menber_avatar);
            tvDeptLevle = (CheckBox) view.findViewById(R.id.is_del);
            btDelete = view.findViewById(R.id.btDelete);
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView tvDeptName;
        CheckBox tvDeptLevle;
        CircleImageView imgAvatar;
        View btDelete;

        public ContentViewHolder(View view) {
            super(view);
            tvDeptName = (TextView) view.findViewById(R.id.del_menber_name);
            imgAvatar = (CircleImageView) view.findViewById(R.id.del_menber_avatar);
            tvDeptLevle = (CheckBox) view.findViewById(R.id.is_del);
            btDelete = view.findViewById(R.id.btDelete);
        }
    }
}

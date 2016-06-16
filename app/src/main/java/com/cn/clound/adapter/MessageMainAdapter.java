package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.common.utils.FuzzyDateTimeFormatterUtil;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.view.SwipeMenuLayout;
import com.cn.clound.interfaces.OnItemClickListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.ExtendedChatModel;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Class Fragment Message Adapter
 *
 * @author ChunFaLee(ly09219@gamil.com)
 * @date 2016年4月7日17:15:19
 */
public class MessageMainAdapter extends RecyclerView.Adapter<MessageMainAdapter.MyViewHolder> {
    private List<EMConversation> listString;
    private Context context;
    private OnItemClickListener mOnItemClickLitener;
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

    public MessageMainAdapter(Context context, List<EMConversation> listString, Handler handler) {
        this.context = context;
        this.listString = listString;
        this.handler = handler;
    }

    @Override
    public MessageMainAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.dt_main_message_swipe_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MessageMainAdapter.MyViewHolder holder, final int position) {
        final SwipeMenuLayout item = (SwipeMenuLayout) holder.itemView;
        EMConversation conversation = listString.get(position);
        holder.tvMsgTime.setText(long2Date(listString.get(position).getLastMessage().getMsgTime()));
        holder.tvMsgContent.setText(EaseSmileUtils.getSmiledText(context, EaseCommonUtils.getMessageDigest(conversation.getLastMessage(), context)),
                TextView.BufferType.SPANNABLE);
//        EaseUserUtils.setUserNick(conversation.getUserName(), holder.tvMsgName);
//        holder.tvMsgName.setText(listString.get(position).getUserName());
        EMMessage message = conversation.getLastMessage();
        String temp = message.getStringAttribute("extended_msg_json", "");
        if (conversation.getUnreadMsgCount() > 0) {
            holder.unReadCount.setVisibility(View.VISIBLE);
            holder.unReadCount.setText(conversation.getUnreadMsgCount() + "");
        } else {
            holder.unReadCount.setVisibility(View.GONE);
        }
        ExtendedChatModel extendedChatModel = GsonTools.getPerson(message.getStringAttribute("extended_msg_json", ""), ExtendedChatModel.class);
        if (extendedChatModel != null) {
            if (!message.getChatType().equals(EMMessage.ChatType.Chat)) {
                holder.tvMsgName.setText(extendedChatModel.getSessionName());
            } else {
                if (MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo().equals(extendedChatModel.getFromUserNo())) {
                    holder.tvMsgName.setText(extendedChatModel.getToUSerNick().equals("") ? conversation.getUserName() : extendedChatModel.getToUSerNick());
                } else {
                    holder.tvMsgName.setText(extendedChatModel.getFromUserNick().equals("") ? conversation.getUserName() : extendedChatModel.getFromUserNick());
                }
            }
        } else {
            if (!message.getChatType().equals(EMMessage.ChatType.Chat)) {
                EMGroup group = EMClient.getInstance().groupManager().getGroup(message.getUserName());
                if (group != null) {
                    holder.tvMsgName.setText(group.getGroupName());
                } else {
                    EaseUserUtils.setUserNick(conversation.getUserName(), holder.tvMsgName);
                }
            } else {
                EaseUserUtils.setUserNick(conversation.getUserName(), holder.tvMsgName);
            }
        }
        ImageLoader.getInstance().displayImage("http://img0.imgtn.bdimg.com/it/u=2647094456,2399988068&fm=21&gp=0.jpg", holder.imgAvatar, options);
        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.smoothCloseMenu();
                Message msg = new Message();
                msg.what = 1002;
                msg.obj = position;
                handler.sendMessage(msg);
            }
        });
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
        return listString.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsgName;
        TextView tvMsgContent;
        TextView tvMsgTime;
        CircleImageView imgAvatar;
        TextView unReadCount;
        TextView tvDelete;

        public MyViewHolder(View view) {
            super(view);
            tvMsgName = (TextView) view.findViewById(R.id.msg_name);
            tvMsgTime = (TextView) view.findViewById(R.id.time);
            tvMsgContent = (TextView) view.findViewById(R.id.message);
            imgAvatar = (CircleImageView) view.findViewById(R.id.avatar);
            unReadCount = (TextView) view.findViewById(R.id.unread_number);
            tvDelete = (TextView) view.findViewById(R.id.btDelete);
        }
    }

    public OnItemClickListener getmOnItemClickLitener() {
        return mOnItemClickLitener;
    }

    public void setmOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private String long2Date(long msgTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        String sd = FuzzyDateTimeFormatterUtil.getTimeAgo(context, new Date(msgTime));
//        String sd = sdf.format(new Date(msgTime));
        return sd;
    }
}

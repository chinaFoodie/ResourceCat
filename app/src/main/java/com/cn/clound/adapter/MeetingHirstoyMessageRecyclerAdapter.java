package com.cn.clound.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.application.MyApplication;
import com.cn.clound.bean.metting.HistoryGroupMessageModel;
import com.cn.clound.interfaces.OnItemVoiceClickListener;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * 会议聊天历史记录适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-2 15:40:06
 */
public class MeetingHirstoyMessageRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<HistoryGroupMessageModel.HistoryGroupMessage.GroupMessage> messages;
    private OnItemVoiceClickListener onItemClickLitener;

    public void setOnItemClickLitener(OnItemVoiceClickListener onItemClickLitener) {
        this.onItemClickLitener = onItemClickLitener;
    }

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

    public MeetingHirstoyMessageRecyclerAdapter(Context context, List<HistoryGroupMessageModel.HistoryGroupMessage.GroupMessage> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new FromViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_meeting_chat_message_from_item, parent,
                    false));
        } else {
            return new ComeViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.dt_meeting_chat_message_to_item, parent,
                    false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == 0) {
            if (messages.get(position).getPayload().getBodies().get(0).getType().equals("txt")) {
                //txt消息
                ((FromViewHolder) holder).rlContent.setVisibility(View.GONE);
                ((FromViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                ((FromViewHolder) holder).tvMsgState.setText(messages.get(position).getPayload().getBodies().get(0).getMsg());
            } else {
                //voice消息
            }
        } else {
            if (messages.get(position).getPayload().getBodies().get(0).getType().equals("txt")) {
                //txt消息
                ((ComeViewHolder) holder).rlContent.setVisibility(View.GONE);
                ((ComeViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                ((ComeViewHolder) holder).tvMsgState.setText(messages.get(position).getPayload().getBodies().get(0).getMsg());
            } else {
                //voice消息
                ((ComeViewHolder) holder).imgUnread.setVisibility(View.GONE);
            }
        }
        if (onItemClickLitener != null ) {//&& messages.get(position).getPayload().getBodies().get(0).getType().equals("txt")
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder instanceof FromViewHolder) {
                        onItemClickLitener.onItemClick(holder.itemView, ((FromViewHolder) holder).imgVoice, ((FromViewHolder) holder).readStutausView, position);
                    } else {
                        onItemClickLitener.onItemClick(holder.itemView, ((ComeViewHolder) holder).imgVoice, ((ComeViewHolder) holder).readStutausView, position);
                    }
                }
            });
        }
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFrom().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getImUser())) {
            return 0;
        } else {
            return 1;
        }
    }

    class FromViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userAvatarView;
        ImageView imgVoice;
        TextView voiceLengthView;
        ImageView readStutausView;
        TextView tvMsgState;
        RelativeLayout rlContent;

        public FromViewHolder(View itemView) {
            super(itemView);
            userAvatarView = (CircleImageView) itemView.findViewById(R.id.iv_userhead);
            imgVoice = (ImageView) itemView.findViewById(R.id.iv_voice);
            voiceLengthView = (TextView) itemView.findViewById(R.id.tv_length);
            readStutausView = (ImageView) itemView.findViewById(R.id.msg_status);
            tvMsgState = (TextView) itemView.findViewById(R.id.timestamp);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_msg_content);
        }
    }

    class ComeViewHolder extends RecyclerView.ViewHolder {
        TextView usernickView;
        CircleImageView userAvatarView;
        ImageView imgVoice;
        TextView voiceLengthView;
        ImageView readStutausView;
        TextView tvMsgState;
        RelativeLayout rlContent;
        ImageView imgUnread;

        public ComeViewHolder(View itemView) {
            super(itemView);
            usernickView = (TextView) itemView.findViewById(R.id.tv_userid);
            userAvatarView = (CircleImageView) itemView.findViewById(R.id.iv_userhead);
            imgVoice = (ImageView) itemView.findViewById(R.id.iv_voice);
            voiceLengthView = (TextView) itemView.findViewById(R.id.tv_length);
            readStutausView = (ImageView) itemView.findViewById(R.id.msg_status);
            tvMsgState = (TextView) itemView.findViewById(R.id.timestamp);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_msg_content);
            imgUnread = (ImageView) itemView.findViewById(R.id.iv_unread_voice);
        }
    }
}

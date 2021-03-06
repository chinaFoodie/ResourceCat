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
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.interfaces.OnItemVoiceClickListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.model.ExtendedChatModel;
import com.hyphenate.easeui.utils.GsonTools;
import com.hyphenate.easeui.widget.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

/**
 * 会议语音聊天适配器
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-2 15:40:06
 */
public class MeetingChatVoiceRecyclerAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<EMMessage> messages;
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

    public MeetingChatVoiceRecyclerAdapter(Context context, List<EMMessage> messages) {
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
        String temp = messages.get(position).getStringAttribute("extended_msg_json", "");
        ExtendedChatModel extendedChatModel = GsonTools.getPerson(temp, ExtendedChatModel.class);
        if (extendedChatModel != null && extendedChatModel.getMsgType().equals("MeetState")) {
            EMTextMessageBody voiceBody = (EMTextMessageBody) messages.get(position).getBody();
            if (holder instanceof FromViewHolder) {
                ((FromViewHolder) holder).rlContent.setVisibility(View.GONE);
                ((FromViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                ((FromViewHolder) holder).tvMsgState.setText(voiceBody.getMessage());
            } else {
                ((ComeViewHolder) holder).rlContent.setVisibility(View.GONE);
                ((ComeViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                ((ComeViewHolder) holder).tvMsgState.setText(voiceBody.getMessage());
            }
        } else {
            EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) messages.get(position).getBody();
            long sho = 1;
            if (position > 0) {
                sho = (messages.get(position - 1).getMsgTime() - messages.get(position).getMsgTime()) / 60000;
            }
            if (holder instanceof FromViewHolder) {
                ImageLoader.getInstance().displayImage("", ((FromViewHolder) holder).userAvatarView, options);
                ((FromViewHolder) holder).imgVoice.setImageResource(com.hyphenate.easeui.R.drawable.voice_to_icon);
                if (voiceBody.getLength() > 0) {
                    ((FromViewHolder) holder).voiceLengthView.setText(voiceBody.getLength() + "\"");
                    ((FromViewHolder) holder).voiceLengthView.setVisibility(View.VISIBLE);
                } else {
                    ((FromViewHolder) holder).voiceLengthView.setVisibility(View.GONE);
                }
                if (sho > 0) {
                    ((FromViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                    ((FromViewHolder) holder).tvMsgState.setText(DateUtil.transferLongToDate("MM-dd HH:mm:ss", messages.get(position).getMsgTime()));
                } else {
                    ((FromViewHolder) holder).tvMsgState.setVisibility(View.GONE);
                }
            } else {
                ImageLoader.getInstance().displayImage("", ((ComeViewHolder) holder).userAvatarView, options);
                ((ComeViewHolder) holder).imgVoice.setImageResource(com.hyphenate.easeui.R.drawable.voice_from_icon);
                if (voiceBody.getLength() > 0) {
                    ((ComeViewHolder) holder).voiceLengthView.setText(voiceBody.getLength() + "\"");
                    ((ComeViewHolder) holder).voiceLengthView.setVisibility(View.VISIBLE);
                } else {
                    ((ComeViewHolder) holder).voiceLengthView.setVisibility(View.GONE);
                }
                if (sho > 0) {
                    ((FromViewHolder) holder).tvMsgState.setVisibility(View.VISIBLE);
                    ((FromViewHolder) holder).tvMsgState.setText(DateUtil.transferLongToDate("MM-dd HH:mm:ss", messages.get(position).getMsgTime()));
                } else {
                    ((FromViewHolder) holder).tvMsgState.setVisibility(View.GONE);
                }
            }
        }
        holder.setIsRecyclable(false);
        if (onItemClickLitener != null) {
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
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).direct() == EMMessage.Direct.SEND ? 0 : 1;
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

        public ComeViewHolder(View itemView) {
            super(itemView);
            usernickView = (TextView) itemView.findViewById(R.id.tv_userid);
            userAvatarView = (CircleImageView) itemView.findViewById(R.id.iv_userhead);
            imgVoice = (ImageView) itemView.findViewById(R.id.iv_voice);
            voiceLengthView = (TextView) itemView.findViewById(R.id.tv_length);
            readStutausView = (ImageView) itemView.findViewById(R.id.msg_status);
            tvMsgState = (TextView) itemView.findViewById(R.id.timestamp);
            rlContent = (RelativeLayout) itemView.findViewById(R.id.rl_msg_content);
        }
    }
}

package com.hyphenate.easeui.widget.chatrow;

import java.util.Date;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.model.ExtendedChatModel;
import com.hyphenate.easeui.utils.EasePublicInfo;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.GsonTools;
import com.hyphenate.easeui.widget.CircleImageView;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.util.DateUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public abstract class EaseChatRow extends LinearLayout {
    protected static final String TAG = EaseChatRow.class.getSimpleName();

    /**
     * 状态模式初始化ImageLoader
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.ease_default_image) // resource or drawable
            .showImageForEmptyUri(R.drawable.ease_default_image) // resource or drawable
            .showImageOnFail(R.drawable.ease_default_image) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected CircleImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected EMCallBack messageSendCallback;
    protected EMCallBack messageReceiveCallback;

    protected MessageListItemClickListener itemClickListener;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);

        initView();
    }

    private void initView() {
        onInflatView();
        timeStampView = (TextView) findViewById(R.id.timestamp);
        userAvatarView = (CircleImageView) findViewById(R.id.iv_userhead);
        bubbleLayout = findViewById(R.id.bubble);
        usernickView = (TextView) findViewById(R.id.tv_userid);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        statusView = (ImageView) findViewById(R.id.msg_status);
        ackedView = (TextView) findViewById(R.id.tv_ack);
        deliveredView = (TextView) findViewById(R.id.tv_delivered);

        onFindViewById();
    }

    private void setShowOrHide(String showHide) {
        if (showHide.equals("changeGroupName")) {
            timeStampView.setVisibility(View.VISIBLE);
            userAvatarView.setVisibility(View.GONE);
            bubbleLayout.setVisibility(View.GONE);
        } else {
            timeStampView.setVisibility(View.GONE);
            userAvatarView.setVisibility(View.VISIBLE);
            bubbleLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据当前message和position设置控件属性等
     *
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
                          EaseChatMessageList.MessageListItemClickListener itemClickListener) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        setUpBaseView();
        onSetUpView();
        setClickListener();
    }

    private void setUpBaseView() {
        // 设置用户昵称头像，bubble背景等
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {
            if (position == 0) {
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
                // 两条消息时间离得如果稍长，显示时间
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        //设置头像和nick
        String temp = message.getStringAttribute("extended_msg_json", "");
        ExtendedChatModel extendedChatModel = GsonTools.getPerson(temp, ExtendedChatModel.class);
        if (extendedChatModel.getMsgType().equals("changeGroupName")) {
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            timestamp.setText(txtBody.getMessage());
            setShowOrHide("changeGroupName");
            timestamp.setVisibility(View.VISIBLE);
        } else {
            setShowOrHide("fff");
            if (message.direct() == Direct.SEND) {
                if (extendedChatModel != null) {
                    ImageLoader.getInstance().displayImage(extendedChatModel.getFromUserAvatar(), userAvatarView, options);
                } else {
                    ImageLoader.getInstance().displayImage(message.getStringAttribute("chatUserAvatar", ""), userAvatarView, options);
                }
            } else {
                if (extendedChatModel != null) {
                    ImageLoader.getInstance().displayImage(extendedChatModel.getToUserAvatar(), userAvatarView, options);
                    if (extendedChatModel.getFromUserNo().equals(EasePublicInfo.CURRENT_USER_ID)) {
                        usernickView.setText(extendedChatModel.getToUSerNick());
                        usernickView.setVisibility(View.VISIBLE);
                    } else {
                        usernickView.setText(extendedChatModel.getFromUserNick());
                        usernickView.setVisibility(View.VISIBLE);
                    }
                } else {
                    EaseUserUtils.setUserAvatar(context, message.getFrom(), userAvatarView);
                    EaseUserUtils.setUserNick(message.getFrom(), usernickView);
                }
            }
        }

        if (deliveredView != null) {
            if (message.isDelivered()) {
                deliveredView.setVisibility(View.VISIBLE);
            } else {
                deliveredView.setVisibility(View.INVISIBLE);
            }
        }

        if (ackedView != null) {
            if (message.isAcked()) {
                if (deliveredView != null) {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
                ackedView.setVisibility(View.VISIBLE);
            } else {
                ackedView.setVisibility(View.INVISIBLE);
            }
        }


        if (adapter instanceof EaseMessageAdapter) {
//            if (((EaseMessageAdapter) adapter).isShowAvatar())
//                userAvatarView.setVisibility(View.VISIBLE);
//            else
//                userAvatarView.setVisibility(View.GONE);
            if (usernickView != null) {
                if (((EaseMessageAdapter) adapter).isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.VISIBLE);
            }
            if (message.direct() == Direct.SEND) {
                if (((EaseMessageAdapter) adapter).getMyBubbleBg() != null)
                    bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getMyBubbleBg());
                // else
                // bubbleLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.chatto_bg));
            } else if (message.direct() == Direct.RECEIVE) {
                if (((EaseMessageAdapter) adapter).getOtherBuddleBg() != null)
                    bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getOtherBuddleBg());
//                else
//                    bubbleLayout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ease_chatfrom_bg));
            }
        }
    }

    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback() {
        if (messageSendCallback == null) {
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }

    /**
     * 设置消息接收callback
     */
    protected void setMessageReceiveCallback() {
        if (messageReceiveCallback == null) {
            messageReceiveCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            if (percentageView != null) {
                                percentageView.setText(progress + "%");
                            }
                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageReceiveCallback);
    }


    private void setClickListener() {
        if (bubbleLayout != null) {
            bubbleLayout.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (!itemClickListener.onBubbleClick(message)) {
                            //如果listener返回false不处理这个事件，执行lib默认的处理
                            onBubbleClick();
                        }
                    }
                }
            });

            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onResendClick(message);
                    }
                }
            });
        }

        userAvatarView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    if (message.direct() == Direct.SEND) {
                        itemClickListener.onUserAvatarClick(message);
                    } else {
                        itemClickListener.onUserAvatarClick(message);
                    }
                }
            }
        });
    }


    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (message.status() == EMMessage.Status.FAIL) {

                    if (message.getError() == EMError.MESSAGE_INCLUDE_ILLEGAL_CONTENT) {
                        Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_invalid_content), Toast.LENGTH_SHORT).show();
                    } else if (message.getError() == EMError.GROUP_NOT_JOINED) {
                        Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.error_send_not_in_the_group), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), Toast.LENGTH_SHORT).show();
                    }
                }

                onUpdateView();
            }
        });

    }

    /**
     * 填充layout
     */
    protected abstract void onInflatView();

    /**
     * 查找chatrow里的控件
     */
    protected abstract void onFindViewById();

    /**
     * 消息状态改变，刷新listview
     */
    protected abstract void onUpdateView();

    /**
     * 设置更新控件属性
     */
    protected abstract void onSetUpView();

    /**
     * 聊天气泡被点击事件
     */
    protected abstract void onBubbleClick();

}

package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.adapter.JoinMeetingRecyclerAdapter;
import com.cn.clound.adapter.MeetingChatVoiceRecyclerAdapter;
import com.cn.clound.adapter.OnItemVoiceClickListener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.metting.EnterStadiumModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.model.ExtendedChatModel;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 会议详情界面（相当于会议聊天等多个界面）
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月30日 15:58:51
 */
public class EnterMeetingActivity extends BaseActivity implements View.OnClickListener, OnItemVoiceClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.tv_click_watch_menber)
    TextView tvWatchMenber;
    @Bind(R.id.recycler_meeting_menber)
    RecyclerView recyclerview;
    @Bind(R.id.recycler_meeting_chat)
    RecyclerView chatRecycler;
    @Bind(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorderView;
    @Bind(R.id.rl_press_to_say)
    RelativeLayout rlPress2Say;
    @Bind(R.id.tv_enter_meeting_minute)
    TextView tvMeetMinute;
    @Bind(R.id.tv_enter_meeting_second)
    TextView tvMeetSecond;
    @Bind(R.id.tv_meeting_had_minute)
    TextView tvMeetHasMinite;
    @Bind(R.id.rl_bottom_click_sign)
    RelativeLayout rlMeetSign;
    @Bind(R.id.tv_no_start)
    TextView tvNoStart;
    @Bind(R.id.tv_meeting_sign)
    TextView tvSignOrStartMeet;
    @Bind(R.id.tv_meeting_sign_desc)
    TextView tvSignStartDesc;
    private int HTTP_START_MEETING = 155;
    private int HTTP_END_MEETING = 156;
    private int HTTP_ENTER_METTING = 157;
    private int HTTP_LEAVE_MEETING = 158;
    private MyHttpHelper httpHelper;
    private List<EnterStadiumModel.Stadium.MeetingUser> listMu = new ArrayList<>();
    private List<EMMessage> messages = new ArrayList<>();
    private JoinMeetingRecyclerAdapter adapter;
    private EnterStadiumModel esm;
    private MeetingChatVoiceRecyclerAdapter voiceAdapter;
    private CustomProgress progress;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_START_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    httpHelper.postStringBack(HTTP_ENTER_METTING, AppConfig.ENTER_MEETING, enterParmars(), handler, EnterStadiumModel.class);
                } else {
                    Toastor.showToast(EnterMeetingActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_END_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Toastor.showToast(EnterMeetingActivity.this, "会议已解散!");
                    EnterMeetingActivity.this.finish();
                } else {
                    Toastor.showToast(EnterMeetingActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_LEAVE_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    EnterMeetingActivity.this.finish();
                } else {
                    Toastor.showToast(EnterMeetingActivity.this, msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_ENTER_METTING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    esm = (EnterStadiumModel) msg.obj;
                    isSignedSeting();
                    Toastor.showToast(EnterMeetingActivity.this, "会议已经开始");
                } else {
                    Toastor.showToast(EnterMeetingActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 100) {
                voiceAdapter.notifyDataSetChanged();
                voiceAdapter.setOnItemClickLitener(EnterMeetingActivity.this);
                chatRecycler.scrollToPosition(voiceAdapter.getItemCount() - 1);
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    private HashMap<String, String> enterParmars() {
        HashMap<String, String> enter = new HashMap<String, String>();
        enter.put("token", TelephoneUtil.getIMEI(this));
        return enter;
    }

    private void isSignedSeting() {
        if (esm.getData().getIsSign().equals("0")) {
            rlMeetSign.setVisibility(View.VISIBLE);
            tvNoStart.setVisibility(View.VISIBLE);
            rlPress2Say.setVisibility(View.GONE);
            tvSignOrStartMeet.setText("签到");
            tvSignStartDesc.setText("请点击签到");
        } else if (esm.getData().getState().equals("0")) {
            if (esm.getData().getIsHost().equals("1")) {
                tvSignOrStartMeet.setText("开始");
                tvSignStartDesc.setText("请点击开始");
                tvBaseRight.setVisibility(View.VISIBLE);
                rlMeetSign.setVisibility(View.VISIBLE);
                tvNoStart.setVisibility(View.VISIBLE);
                rlPress2Say.setVisibility(View.GONE);
            } else {
                rlMeetSign.setVisibility(View.GONE);
                tvNoStart.setVisibility(View.VISIBLE);
                rlPress2Say.setVisibility(View.GONE);
            }
        } else {
            if (esm.getData().getIsHost().equals("1")) {
                rlMeetSign.setVisibility(View.GONE);
                tvNoStart.setVisibility(View.GONE);
                rlPress2Say.setVisibility(View.VISIBLE);
                tvBaseRight.setVisibility(View.VISIBLE);
            } else {
                tvBaseRight.setVisibility(View.GONE);
                rlMeetSign.setVisibility(View.GONE);
                tvNoStart.setVisibility(View.GONE);
                rlPress2Say.setVisibility(View.VISIBLE);
            }
        }
    }

    private void meetingCount() {
        Date temp = DateUtil.string2Date(esm.getData().getBeginAt() + ":00", "yyyy-MM-dd HH:mm:ss");
        final long[] time = {(temp.getTime() - new Date().getTime()) / 1000, (new Date().getTime() - temp.getTime()) / 1000};
        tvMeetMinute.post(new Runnable() {
            @Override
            public void run() {
                if (time[0] > 0) {
                    time[0]--;
                    long m = time[0] / 60;
                    long s = time[0] % 60;
                    StringBuffer minute = new StringBuffer();
                    StringBuffer second = new StringBuffer();
                    if (m < 10) {
                        minute.append(0);
                    }
                    if (s < 10) {
                        second.append(0);
                    }
                    minute.append(m);
                    second.append(s);
                    tvMeetMinute.setText(minute);
                    tvMeetSecond.setText(second);
                    tvMeetMinute.postDelayed(this, 1000);
                } else if (esm.getData().getState().equals("1")) {
                    time[1]++;
                    long m = time[1] / 60;
                    long s = time[1] % 60;
                    StringBuffer minute = new StringBuffer();
                    StringBuffer second = new StringBuffer();
                    if (m < 10) {
                        minute.append(0);
                    }
                    if (s < 10) {
                        second.append(0);
                    }
                    minute.append(m);
                    second.append(s);
                    tvMeetMinute.setText(minute);
                    tvMeetSecond.setText(second);
                    tvMeetMinute.postDelayed(this, 1000);
                    tvMeetHasMinite.setText("会议已进行");
                    tvMidTitle.setText("会议中");
                } else {
                    time[1]++;
                    long m = time[1] / 60;
                    long s = time[1] % 60;
                    StringBuffer minute = new StringBuffer();
                    StringBuffer second = new StringBuffer();
                    if (m < 10) {
                        minute.append(0);
                    }
                    if (s < 10) {
                        second.append(0);
                    }
                    minute.append(m);
                    second.append(s);
                    tvMeetMinute.setText(minute);
                    tvMeetSecond.setText(second);
                    tvMeetMinute.postDelayed(this, 1000);
                    tvMeetHasMinite.setText("会议已延时");
                    tvMidTitle.setText("会议推迟中");
                }
            }
        });
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_enter_meeting;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "请稍候...");
        progress.show();
        Intent intent = this.getIntent();
        esm = (EnterStadiumModel) intent.getSerializableExtra("enter_stadium_model");
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("会议倒计时");
        tvBaseRight.setText("散会");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvWatchMenber.setOnClickListener(this);
        rlMeetSign.setOnClickListener(this);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerview.setVisibility(View.GONE);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        voiceAdapter = new MeetingChatVoiceRecyclerAdapter(this, messages);
        chatRecycler.setAdapter(voiceAdapter);
        rlPress2Say.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        voiceRecorderView.startRecording();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getY() < 0) {
                            voiceRecorderView.showReleaseToCancelHint();
                        } else {
                            voiceRecorderView.showMoveUpToCancelHint();
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        int lenth = voiceRecorderView.stopRecoding();
                        if (lenth > 0) {
                            final EMMessage message = EMMessage.createVoiceSendMessage(voiceRecorderView.getVoiceFilePath(), lenth, esm.getData().getGroupId());
                            //自定义属性
                            ExtendedChatModel exm = new ExtendedChatModel();
                            exm.setSessionName(esm.getData().getGroupId());
                            exm.setMsgType("MeetMessage");
                            exm.setToUserNo(esm.getData().getGroupId());
                            exm.setToUserAvatar("");
                            exm.setToUSerNick(esm.getData().getGroupId());
                            exm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                            exm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                            exm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                            message.setAttribute("extended_msg_json", GsonTools.obj2json(exm));
                            EMClient.getInstance().chatManager().sendMessage(message);
                            message.setMessageStatusCallback(new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    messages.add(message);
                                    handler.sendEmptyMessage(100);
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(EnterMeetingActivity.this, s, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onProgress(int i, String s) {

                                }
                            });
                        } else if (voiceRecorderView.stopRecoding() == EMError.FILE_INVALID) {
                            Toast.makeText(EnterMeetingActivity.this, com.hyphenate.easeui.R.string.Recording_without_permission, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EnterMeetingActivity.this, com.hyphenate.easeui.R.string.The_recording_time_is_too_short, Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        voiceRecorderView.stopRecoding();
                        return false;
                }
            }
        });
        if (esm != null) {
            listMu = esm.getData().getUsers();
            adapter = new JoinMeetingRecyclerAdapter(EnterMeetingActivity.this, listMu);
            recyclerview.setAdapter(adapter);
            isSignedSeting();
            meetingCount();
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(esm.getData().getGroupId(), EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_CHATROOM), true);
            messages = conversation.getAllMessages();
            for (EMMessage emMessage : messages) {
                if (emMessage.getType() != EMMessage.Type.VOICE) {
                    messages.remove(emMessage);
                }
            }
            voiceAdapter = new MeetingChatVoiceRecyclerAdapter(EnterMeetingActivity.this, messages);
            chatRecycler.setAdapter(voiceAdapter);
            voiceAdapter.setOnItemClickLitener(EnterMeetingActivity.this);
            chatRecycler.scrollToPosition(voiceAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                if (esm.getData().getState().equals("1") && esm.getData().getIsSign().equals("1")) {
                    new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progress.show();
                            httpHelper.postStringBack(HTTP_LEAVE_MEETING, AppConfig.LEAVE_MEETING, startMeeting(), handler, BaseModel.class);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).setMsg("是否确认离开会场？").show();
                }
                break;
            case R.id.tv_base_right:
                new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progress.show();
                        httpHelper.postStringBack(HTTP_END_MEETING, AppConfig.END_MEETING, startMeeting(), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("是否确认解散会议？").show();
                break;
            case R.id.rl_bottom_click_sign:
                if (tvSignOrStartMeet.getText().toString().equals("签到")) {
                    startActivity(new Intent(this, SingedLocationActivity.class).putExtra("timeId", esm.getData().getId()));
                } else {
                    progress.show();
                    httpHelper.postStringBack(HTTP_START_MEETING, AppConfig.START_MEETING, startMeeting(), handler, BaseModel.class);
                }
                break;
            case R.id.tv_click_watch_menber:
                if (tvWatchMenber.getText().toString().equals("点击查看参会人员")) {
                    tvWatchMenber.setText("取消查看");
                    recyclerview.setVisibility(View.VISIBLE);
                } else {
                    tvWatchMenber.setText("点击查看参会人员");
                    recyclerview.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> startMeeting() {
        HashMap<String, String> start = new HashMap<String, String>();
        start.put("token", TelephoneUtil.getIMEI(this));
        start.put("id", esm.getData().getId());
        return start;
    }

    @Override
    public void onItemClick(View holderItem, ImageView v, ImageView iv_read_status, int position) {
        EMVoiceMessageBody body = (EMVoiceMessageBody) messages.get(position).getBody();
        if (!isPlaying) {
            preIndex = position;
            playVoice(messages.get(position), v, body.getLocalUrl(), iv_read_status);
        } else {
            stopPlayVoice(messages.get(preIndex), v);
            preIndex = position;
            playVoice(messages.get(position), v, body.getLocalUrl(), iv_read_status);
        }
    }

    MediaPlayer mediaPlayer = null;
    public String playMsgId;
    private AnimationDrawable voiceAnimation = null;
    public boolean isPlaying = false;
    private int preIndex;

    public void playVoice(final EMMessage message, final ImageView v, String filePath, ImageView iv_read_status) {
        if (!(new File(filePath).exists())) {
            return;
        }
        playMsgId = message.getMsgId();
        AudioManager audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        if (EaseUI.getInstance().getSettingsProvider().isSpeakerOpened()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            audioManager.setSpeakerphoneOn(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        } else {
            audioManager.setSpeakerphoneOn(false);// 关闭扬声器
            // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
            audioManager.setMode(AudioManager.MODE_IN_CALL);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
        }
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice(message, v); // stop animation
                }

            });
            mediaPlayer.start();
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                v.setImageResource(com.hyphenate.easeui.R.drawable.voice_from_icon);
            } else {
                v.setImageResource(com.hyphenate.easeui.R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) v.getDrawable();
            voiceAnimation.start();
            isPlaying = true;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
//                if (!message.isAcked() && chatType == EMMessage.ChatType.Chat) {
//                    // 告知对方已读这条消息
//                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
//                }
                if (!message.isListened() && iv_read_status != null && iv_read_status.getVisibility() == View.VISIBLE) {
                    // 隐藏自己未播放这条语音消息的标志
                    iv_read_status.setVisibility(View.INVISIBLE);
                    message.setListened(true);
                    EMClient.getInstance().chatManager().setMessageListened(message);
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void stopPlayVoice(EMMessage message, ImageView v) {
//        voiceAnimation.stop();
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            v.setImageResource(com.hyphenate.easeui.R.drawable.ease_chatfrom_voice_playing);
        } else {
            v.setImageResource(com.hyphenate.easeui.R.drawable.ease_chatto_voice_playing);
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        isPlaying = false;
        playMsgId = null;
        voiceAdapter.notifyDataSetChanged();
    }
}

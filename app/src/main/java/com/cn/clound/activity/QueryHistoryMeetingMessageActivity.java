package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.JoinMeetingRecyclerAdapter;
import com.cn.clound.adapter.MeetingHirstoyMessageRecyclerAdapter;
import com.cn.clound.interfaces.OnItemVoiceClickListener;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.bean.metting.EnterStadiumModel;
import com.cn.clound.bean.metting.HistoryGroupMessageModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.hyphenate.easeui.controller.EaseUI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 查询历史会议聊天记录
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月30日 15:58:51
 */
public class QueryHistoryMeetingMessageActivity extends BaseActivity implements View.OnClickListener, OnItemVoiceClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_click_watch_menber)
    TextView tvWatchMenber;
    @Bind(R.id.recycler_meeting_menber)
    RecyclerView recyclerview;
    @Bind(R.id.recycler_meeting_chat)
    RecyclerView chatRecycler;
    private List<EnterStadiumModel.Stadium.MeetingUser> listMu = new ArrayList<>();
    private List<HistoryGroupMessageModel.HistoryGroupMessage.GroupMessage> messages = new ArrayList<>();
    private JoinMeetingRecyclerAdapter adapter;
    private MeetingHirstoyMessageRecyclerAdapter voiceAdapter;
    private CustomProgress progress;
    private HistoryGroupMessageModel hgmm;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 100) {
                voiceAdapter = new MeetingHirstoyMessageRecyclerAdapter(QueryHistoryMeetingMessageActivity.this, messages);
                voiceAdapter.setOnItemClickLitener(QueryHistoryMeetingMessageActivity.this);
                chatRecycler.setAdapter(voiceAdapter);
                chatRecycler.scrollToPosition(voiceAdapter.getItemCount() - 1);
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_query_meeting_message;
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
        hgmm = (HistoryGroupMessageModel) intent.getSerializableExtra("history_meeting_message_model");
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("会议记录");
        llBack.setOnClickListener(this);
        tvWatchMenber.setOnClickListener(this);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerview.setVisibility(View.GONE);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (hgmm != null) {
            messages = hgmm.getData().getResult();
        }
        voiceAdapter = new MeetingHirstoyMessageRecyclerAdapter(this, messages);
        chatRecycler.setAdapter(voiceAdapter);
        voiceAdapter.setOnItemClickLitener(this);
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
                this.finish();
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

    @Override
    public void onItemClick(View holderItem, ImageView v, ImageView iv_read_status, int position) {
        if (!isPlaying) {
            preIndex = position;
            playVoice(messages.get(position), v, messages.get(position).getPayload().getBodies().get(0).getUrl(), iv_read_status);
        } else {
            stopPlayVoice(messages.get(preIndex), v);
            preIndex = position;
            playVoice(messages.get(position), v, messages.get(position).getPayload().getBodies().get(0).getUrl(), iv_read_status);
        }
    }

    MediaPlayer mediaPlayer = null;
    public String playMsgId;
    private AnimationDrawable voiceAnimation = null;
    public boolean isPlaying = false;
    private int preIndex;

    public void playVoice(final HistoryGroupMessageModel.HistoryGroupMessage.GroupMessage message, final ImageView v, String filePath, ImageView iv_read_status) {
        playMsgId = message.getMsg_id();
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
            Uri uri = Uri.parse(filePath);
            mediaPlayer.setDataSource(QueryHistoryMeetingMessageActivity.this, uri);
            mediaPlayer.prepare();
//            mediaPlayer.setVolume(0.4f, 0.4f);
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
            if (!message.getFrom().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getImUser())) {
                v.setImageResource(com.hyphenate.easeui.R.drawable.voice_from_icon);
            } else {
                v.setImageResource(com.hyphenate.easeui.R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) v.getDrawable();
            voiceAnimation.start();
            isPlaying = true;
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void stopPlayVoice(HistoryGroupMessageModel.HistoryGroupMessage.GroupMessage message, ImageView v) {
//        voiceAnimation.stop();
        if (!message.getFrom().equals(MyApplication.getInstance().getUm().getData().getUserInfo().getImUser())) {
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
//        handler.sendEmptyMessage(100);
    }
}

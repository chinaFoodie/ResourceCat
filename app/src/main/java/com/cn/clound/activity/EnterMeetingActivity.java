package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.JoinMeetingRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.metting.EnterStadiumModel;
import com.cn.clound.http.MyHttpHelper;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseVoiceRecorderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 会议详情界面（相当于会议聊天等多个界面）
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月30日 15:58:51
 */
public class EnterMeetingActivity extends BaseActivity implements View.OnClickListener {
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
    @Bind(R.id.input_menu)
    EaseChatInputMenu inputMenu;
    @Bind(R.id.voice_recorder)
    EaseVoiceRecorderView voiceRecorderView;

    private int HTTP_ENTER_METTING = 145;
    private MyHttpHelper httpHelper;
    private List<EnterStadiumModel.Stadium.MeetingUser> listMu = new ArrayList<>();
    private JoinMeetingRecyclerAdapter adapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_ENTER_METTING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    EnterStadiumModel esm = (EnterStadiumModel) msg.obj;
                    if (esm != null) {
                        adapter = new JoinMeetingRecyclerAdapter(EnterMeetingActivity.this, esm.getData().getUsers());
                        recyclerview.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(EnterMeetingActivity.this, msg.obj.toString());
                }
            }
        }
    };

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
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        tvMidTitle.setText("会议倒计时");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("散会");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvWatchMenber.setOnClickListener(this);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 5));
        recyclerview.setVisibility(View.GONE);
        httpHelper.postStringBack(HTTP_ENTER_METTING, AppConfig.ENTER_MEETING, enterParmars(), handler, EnterStadiumModel.class);
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                // 发送文本消息
//                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
//                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                //发送大表情(动态表情)
//                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });
    }

    private HashMap<String, String> enterParmars() {
        HashMap<String, String> enter = new HashMap<String, String>();
        enter.put("token", TelephoneUtil.getIMEI(this));
        return enter;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

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
                finish();
                break;
            case R.id.tv_base_right:
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
}

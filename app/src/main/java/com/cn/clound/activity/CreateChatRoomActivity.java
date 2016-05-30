package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.http.MyHttpHelper;

import butterknife.Bind;

/**
 * 创建群聊界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-28 10:43:35
 */
public class CreateChatRoomActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.et_add_chat_room_name)
    EditText etChatName;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_add_chat_room;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        tvBaseRight.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("创建群组");
        tvBaseRight.setOnClickListener(this);
        tvBaseRight.setText("创建");
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
                if (!"".equals(etChatName.getText().toString())) {
                    doCreateChatRoom(etChatName.getText().toString());
                } else {
                    Toastor.showToast(CreateChatRoomActivity.this, "存在必填项为空，请填写完成再试!");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 创建群组操作
     *
     * @param chatName
     */
    private void doCreateChatRoom(String chatName) {
        Intent intent = new Intent(this, ChoseChatRoomMenberActivity.class);
        intent.putExtra("chat_room_name", chatName);
        startActivity(intent);
    }
}

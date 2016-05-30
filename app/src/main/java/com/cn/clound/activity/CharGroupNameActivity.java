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
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 群名字修改界面
 *
 * @author ChunfaLee(ly09219@gmail.com).
 * @date 2016年5月5日 17:47:39
 */
public class CharGroupNameActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.et_achat_group_name)
    EditText etGroupName;
    private String groupNo = "";
    private String groupName = "";
    private String groupImNo = "";
    private int HTTP_UPDATE_CHAT_GROUP_NAME = 126;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_UPDATE_CHAT_GROUP_NAME) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("groupName", etGroupName.getText().toString());
                    EMMessage message = EMMessage.createTxtSendMessage(MyApplication.getInstance().getUm().getData().getUserInfo().getName() + "修改群名字为'" + etGroupName.getText().toString() + "'", groupImNo);
                    message.setChatType(EMMessage.ChatType.GroupChat);

                    //自定义属性
                    ExtendedChatModel exm = new ExtendedChatModel();
                    exm.setMsgType("changeGroupName");
                    exm.setSessionName(etGroupName.getText().toString());
                    exm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                    exm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                    exm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                    exm.setToUSerNick(etGroupName.getText().toString());
                    exm.setToUserNo(groupImNo);
                    exm.setToUserAvatar("");
                    message.setAttribute("extended_msg_json", GsonTools.obj2json(exm));
                    //发送消息
                    EMClient.getInstance().chatManager().sendMessage(message);
                    //设置返回数据
                    CharGroupNameActivity.this.setResult(1004, intent);
                    CharGroupNameActivity.this.finish();
                } else {
                    Toastor.showToast(CharGroupNameActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_chat_group_name;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        groupNo = this.getIntent().getStringExtra("groupNo");
        groupName = this.getIntent().getStringExtra("groupName");
        groupImNo = this.getIntent().getStringExtra("groupImNo");
        llBack.setVisibility(View.VISIBLE);
        tvBaseRight.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        etGroupName.setText(groupName);
        tvMidTitle.setText("群聊名称");
        tvBaseRight.setOnClickListener(this);
        tvBaseRight.setText("保存");
        httpHelper = MyHttpHelper.getInstance(this);
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
                if (!"".equals(etGroupName.getText().toString())) {
                    new AlertDialog(this).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            httpHelper.postStringBack(HTTP_UPDATE_CHAT_GROUP_NAME, AppConfig.UPDATE_CHAT_GROUP_NAME, updateParames(etGroupName.getText().toString()), handler, BaseModel.class);
                        }
                    }).setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setMsg("是否确认修改群名称？").show();
                } else {
                    Toastor.showToast(CharGroupNameActivity.this, "存在必填项为空，请填写完成再试!");
                }
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> updateParames(String s) {
        HashMap<String, String> update = new HashMap<String, String>();
        update.put("groupName", s);
        update.put("groupNo", groupNo);
        update.put("token", TelephoneUtil.getIMEI(this));
        return update;
    }
}

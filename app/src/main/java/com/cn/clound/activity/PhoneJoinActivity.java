package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.share.ShareModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.HashMap;

import butterknife.Bind;

/**
 * 手机号邀请
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-26 17:11:17
 */
public class PhoneJoinActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.et_join_name)
    EditText etJoinName;
    @Bind(R.id.et_join_phone)
    EditText etJoinPhone;
    @Bind(R.id.tv_send_message)
    TextView tvSendMsg;

    private int HTTP_GET_SHARE_MSG = 109;
    private MyHttpHelper httphelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_SHARE_MSG) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    ShareModel sm = (ShareModel) msg.obj;
                    if (null != sm.getData().getShare()) {
                        doSendSMSTo(etJoinPhone.getText().toString(), sm.getData().getShare().getAppName() + sm.getData().getShare().getContent() + sm.getData().getShare().getUrl());
                    } else {
                        Toastor.showToast(PhoneJoinActivity.this, msg.obj.toString());
                    }
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_phone_join;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httphelper = MyHttpHelper.getInstance(this);
        tvMidTitle.setText("手机号邀请");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvSendMsg.setOnClickListener(this);
    }

    /**
     * 获取分享数据
     */
    private void getShareMsg() {
        httphelper.postStringBack(HTTP_GET_SHARE_MSG, AppConfig.GET_SHARE_MSG, setParames(), handler, ShareModel.class);
    }

    /**
     * 获取分享数据参数配置
     *
     * @return
     */
    private HashMap<String, String> setParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("token", TelephoneUtil.getIMEI(this));
        return parames;
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
            case R.id.tv_send_message:
                getShareMsg();
                break;
            default:
                break;
        }
    }

    private void doSendSMSTo(String tel, String msg) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(tel)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + tel));
            intent.putExtra("sms_body", msg);
            startActivity(intent);
        } else {
            Toastor.showToast(this, "手机号码不合法,请重新输入!");
        }
    }
}

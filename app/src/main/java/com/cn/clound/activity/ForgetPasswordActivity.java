package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.common.utils.TimeCountUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 忘记密码界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-22 12:46:31
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_forget_strp1)
    LinearLayout llStep1;
    @Bind(R.id.ll_forget_strp2)
    LinearLayout llStep2;
    @Bind(R.id.ll_forget_strp3)
    LinearLayout llStep3;
    @Bind(R.id.tv_input_timer)
    TextView tvTimer;
    @Bind(R.id.et_captcha_phone)
    EditText etCaptcha;
    @Bind(R.id.et_captcha_value)
    EditText etCaptchaVlaue;

    private int backStep = 0;
    private int preStep;
    private TimeCountUtil timeCountUtil;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_forget_password;
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
        tvMidTitle.setText("找回密码");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("下一步");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvTimer.setOnClickListener(this);
        settingShow();
    }

    /**
     * 控制显示到第几步
     */
    private void settingShow() {
        switch (backStep) {
            case 0:
                llStep1.setVisibility(View.VISIBLE);
                llStep2.setVisibility(View.GONE);
                llStep3.setVisibility(View.GONE);
                tvBaseRight.setText("下一步");
                break;
            case 1:
                llStep1.setVisibility(View.GONE);
                llStep2.setVisibility(View.VISIBLE);
                llStep3.setVisibility(View.GONE);
                tvBaseRight.setText("下一步");
                if ("重发".equals(tvTimer.getText().toString()) && preStep < 1) {
                    timeCountUtil = new TimeCountUtil(this, 60000, 1000, tvTimer);
                    timeCountUtil.start();
                    getCaptcha();
                }
                break;
            case 2:
                llStep1.setVisibility(View.GONE);
                llStep2.setVisibility(View.GONE);
                llStep3.setVisibility(View.VISIBLE);
                tvBaseRight.setText("确认");
                break;
        }
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
        backStep = 0;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                if (backStep == 0) {
                    finish();
                } else {
                    preStep = backStep;
                    backStep--;
                    settingShow();
                }
                break;
            case R.id.tv_base_right:
                if (!"".equals(etCaptcha.getText().toString())) {
                    if (!"".equals(etCaptchaVlaue.getText().toString()) || backStep == 0) {
                        if (backStep == 2) {
                            finish();
                        } else {
                            backStep++;
                        }
                        settingShow();
                    } else {
                        Toastor.showToast(ForgetPasswordActivity.this, "验证码不能为空，请输入");
                    }
                } else {
                    Toastor.showToast(ForgetPasswordActivity.this, "手机号不能为空，请输入");
                }
                break;
            case R.id.tv_input_timer:
                if ("重发".equals(tvTimer.getText().toString())) {
                    timeCountUtil.start();
                    getCaptcha();
                }
                break;
            default:
                break;
        }
    }

    private void getCaptcha() {
        OkHttpUtils.post().url(AppConfig.GET_SMS_CAPTCHA).addParams("phone", etCaptcha.getText().toString()).addParams("token", TelephoneUtil.getIMEI(ForgetPasswordActivity.this)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    String status = json.getString("status");
                    String message = json.getString("message");
                    if (AppConfig.SUCCESS.equals(status)) {
                        String codeDate = json.getString("codeDate");
                        Toastor.showToast(ForgetPasswordActivity.this, "验证码为：" + codeDate);
                        etCaptchaVlaue.setText(codeDate);
                    } else {
                        Toastor.showToast(ForgetPasswordActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

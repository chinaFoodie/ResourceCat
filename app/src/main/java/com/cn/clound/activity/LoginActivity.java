package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.search.poi.PoiSearch;
import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.Log;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.data.PreferenceUtil;
import com.cn.clound.base.common.utils.AndroidUtil;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.User.UserModel;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EasePublicInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Class of user to do login
 *
 * @author ChunfaLee (ly09219@gmail.com)
 * @date 2016年4月11日14:14:08
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.et_login_username)
    EditText etUsername;
    @Bind(R.id.et_login_password)
    EditText etPassword;
    @Bind(R.id.tv_do_login)
    TextView tvLogin;
    @Bind(R.id.tv_login_forget_password)
    TextView tvForgetPsw;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_login;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化UI View
     */
    private void init() {
        tvLogin.setOnClickListener(this);
        tvForgetPsw.setOnClickListener(this);
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

    /**
     * 登录流程
     *
     * @param username 用户名
     * @param password 用户密码
     */
    private void dologin(String username, String password) {
        EMClient.getInstance().login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //进入主页面
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(int i, String s) {
                Log.e("------onError-----", s);
            }

            @Override
            public void onProgress(int i, String s) {
                Log.e("------onProgress-----", s);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_do_login:
                if (!"".equals(etUsername.getText().toString()) && !"".equals(etPassword.getText().toString())) {
                    localLogin(etUsername.getText().toString(), etPassword.getText().toString());
//                    dologin();
                } else {
                    Toastor.showToast(LoginActivity.this, "账号密码不能为空");
                }
                break;
            case R.id.tv_login_forget_password:
                //忘记密码跳转
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                break;
            default:
                break;
        }
    }

    private void localLogin(String username, String password) {
        OkHttpUtils.post().url(AppConfig.USER_LOGIN).headers(setHeaders()).params(setPargmes(username, password)).build().execute(new StringCallback() {
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
                        getUsreInfo();
                    } else {
                        Toastor.showToast(LoginActivity.this, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            private void getUsreInfo() {
                OkHttpUtils.post().url(AppConfig.GET_USER_INFO).addParams("token", TelephoneUtil.getIMEI(LoginActivity.this)).build().execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);
                            String status = json.getString("status");
                            if (AppConfig.SUCCESS.equals(status)) {
                                UserModel um = GsonTools.getPerson(response, UserModel.class);
                                MyApplication.getInstance().setUm(um);
                                EasePublicInfo.CURRENT_USER_ID = um.getData().getUserInfo().getUserNo();
                                dologin(um.getData().getUserInfo().getImUser(), um.getData().getUserInfo().getImPass());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    private Map<String, String> setPargmes(String username, String password) {
        HashMap<String, String> body = new HashMap<String, String>();
        body.put("token", TelephoneUtil.getIMEI(LoginActivity.this));
        body.put("type", "1");
        body.put("username", username);
        body.put("userpass", password);
        return body;
    }

    private Map<String, String> setHeaders() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Device_Id", AndroidUtil.getMacAddress(LoginActivity.this));
        headers.put("APP-Key", AppConfig.APP_KEY);
        return headers;
    }
}

package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;

import butterknife.Bind;

/**
 * 密码保护
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-20 15:56:21
 */
public class PasswordProtectionActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.rl_unlock_password)
    RelativeLayout rlUnlockPsw;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_password_protect;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    private void init() {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("密码保护");
        rlUnlockPsw.setOnClickListener(this);
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
                this.finish();
                break;
            case R.id.rl_unlock_password:
                startActivity(new Intent(this, GesturePasswordActivity.class));
                break;
            default:
                break;
        }
    }
}

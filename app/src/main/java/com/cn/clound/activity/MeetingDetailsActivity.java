package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;

import butterknife.Bind;

/**
 * 会议详情界面（相当于会议聊天等多个界面）
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月30日 15:58:51
 */
public class MeetingDetailsActivity extends BaseActivity implements View.OnClickListener {
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

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_meeting_details;
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
        tvMidTitle.setText("会议倒计时");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("散会");
        tvBaseRight.setOnClickListener(this);
        llBack.setOnClickListener(this);
        tvWatchMenber.setOnClickListener(this);
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
                    Toastor.showToast(this, "点击查看参会人员");
                } else {
                    tvWatchMenber.setText("点击查看参会人员");
                    Toastor.showToast(this, "取消查看");
                }
                break;
            default:
                break;
        }
    }
}

package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;

import butterknife.Bind;

/**
 * 报销审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-15 15:36:34
 */
public class WipedApprovalActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.dt_include_swiped_banner)
    View wipedBanner;
    @Bind(R.id.dt_include_swiped_edit)
    View wipedEdit;
    @Bind(R.id.rl_swiped_list_details)
    RelativeLayout rlWipedDetails;
    @Bind(R.id.recycler_wiped_approval_picture)
    RecyclerView recyclerViewWipedPic;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_wiped_approval;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llLeft.setVisibility(View.VISIBLE);
        llLeft.setOnClickListener(this);
        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText("取消");
        tvMidTitle.setText("报销审批");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("提交");
        tvBaseRight.setOnClickListener(this);

        rlWipedDetails.setOnClickListener(this);
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
            case R.id.rl_swiped_list_details:
                wipedEdit.setVisibility(View.GONE);
                wipedBanner.setVisibility(View.VISIBLE);
                tvBaseRight.setText("确定");
                tvMidTitle.setText("报销明细");
                break;
            case R.id.ll_base_left:
                if ("报销审批".equals(tvMidTitle.getText().toString())) {
                    this.finish();
                } else {
                    wipedBanner.setVisibility(View.GONE);
                    wipedEdit.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                    tvMidTitle.setText("报销审批");
                }
                break;
            case R.id.tv_base_right:
                if ("提交".equals(tvBaseRight.getText().toString())) {

                } else {
                    wipedBanner.setVisibility(View.GONE);
                    wipedEdit.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                    tvMidTitle.setText("报销审批");
                }
                break;
            default:
                break;
        }
    }
}

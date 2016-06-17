package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.fragment.AlreadyApproveFragment;
import com.cn.clound.fragment.AwaitApprovalFragment;
import com.cn.clound.http.MyHttpHelper;

import butterknife.Bind;

/**
 * 审批消息列表
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 17:45:30
 */
public class ApprovalMessageActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_mine_meeting)
    TextView tvAwaitApproval;
    @Bind(R.id.tv_history_metting)
    TextView tvHadApproval;
    @Bind(R.id.img_mine_meeting)
    ImageView imgMineBottom;
    @Bind(R.id.img_history_metting)
    ImageView imgHistoryBottom;
    private int index;
    private int currentTabIndex;
    private Fragment[] fragments;
    private TextView[] mainTvs;
    private ImageView[] mainImgs;
    private Fragment awaitFragment;
    private Fragment alreadyFragment;

    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_cloud_meeting;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("审批消息");
        mainTvs = new TextView[]{tvAwaitApproval, tvHadApproval};
        mainImgs = new ImageView[]{imgMineBottom, imgHistoryBottom};
        tvAwaitApproval.setText("待我审批");
        tvHadApproval.setText("我已审批");
        awaitFragment = new AwaitApprovalFragment();
        alreadyFragment = new AlreadyApproveFragment();
        fragments = new Fragment[]{awaitFragment, alreadyFragment};
        tvAwaitApproval.setOnClickListener(this);
        tvHadApproval.setOnClickListener(this);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.metting_fragment, alreadyFragment).hide(alreadyFragment).add(R.id.metting_fragment, awaitFragment).show(awaitFragment).commit();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mine_meeting:
                index = 0;
                onTabClicked();
                break;
            case R.id.tv_history_metting:
                index = 1;
                onTabClicked();
                break;
            case R.id.ll_base_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void onTabClicked() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.sign_fragment, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mainTvs[currentTabIndex].setTextColor(getResources().getColor(R.color.colorWhiteMainTab));
        mainTvs[index].setTextColor(getResources().getColor(R.color.colorBlueMainTab));
        mainImgs[currentTabIndex].setVisibility(View.GONE);
        mainImgs[index].setVisibility(View.VISIBLE);
        currentTabIndex = index;
    }
}

package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.fragment.HistoryMettingFragment;
import com.cn.clound.fragment.ManagerHistoryFragment;
import com.cn.clound.fragment.MineMettingFtagment;
import com.cn.clound.fragment.PublishingMeetingFragment;

import butterknife.Bind;

/**
 * 会议管理界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月31日 09:57:30
 */
public class MeetingManagerActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_mine_meeting)
    TextView tvMineMetting;
    @Bind(R.id.tv_history_metting)
    TextView tvHistoryMetting;
    @Bind(R.id.img_mine_meeting)
    ImageView imgMineBottom;
    @Bind(R.id.img_history_metting)
    ImageView imgHistoryBottom;

    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    //需要加载的fragment集合
    private Fragment[] fragments;
    private TextView[] mainTvs;
    private ImageView[] mainImgs;
    private Fragment publishingMeetingFragment;
    private Fragment managerHistoryFragment;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_meeting_manager;
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
        llBack.setOnClickListener(this);
        tvMidTitle.setText("会议管理");
        mainTvs = new TextView[]{tvMineMetting, tvHistoryMetting};
        mainImgs = new ImageView[]{imgMineBottom, imgHistoryBottom};
        publishingMeetingFragment = new PublishingMeetingFragment();
        managerHistoryFragment = new ManagerHistoryFragment();
        fragments = new Fragment[]{publishingMeetingFragment, managerHistoryFragment};
        tvMineMetting.setOnClickListener(this);
        tvHistoryMetting.setOnClickListener(this);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.metting_fragment, managerHistoryFragment).hide(managerHistoryFragment).add(R.id.metting_fragment, publishingMeetingFragment).show(publishingMeetingFragment).commit();
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
            case R.id.tv_mine_meeting:
                index = 0;
                onTabClicked();
                break;
            case R.id.tv_history_metting:
                index = 1;
                onTabClicked();
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

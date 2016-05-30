package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.fragment.HierarchyFragment;
import com.cn.clound.fragment.HistoryMettingFragment;
import com.cn.clound.fragment.MineMettingFtagment;
import com.cn.clound.utils.PopWindowUtil;
import com.cn.clound.view.PullRefreshLayout;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;

import java.util.Date;
import java.util.LinkedList;

import butterknife.Bind;

/**
 * 云会议界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 17:45:30
 */
public class CloudMeetingActivity extends BaseActivity implements View.OnClickListener {
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
    @Bind(R.id.img_base_right)
    ImageView imgBaseRight;
    @Bind(R.id.ll_base_right)
    LinearLayout llParent;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    //需要加载的fragment集合
    private Fragment[] fragments;
    private TextView[] mainTvs;
    private ImageView[] mainImgs;
    private Fragment mineMettingFragment;
    private Fragment historyMettingFragment;
    private PopWindowUtil popMenu;

    private LinkedList<String> data = new LinkedList<String>();

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
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.activity_cloud_meeting));
        imgBaseRight.setVisibility(View.VISIBLE);
        imgBaseRight.setImageResource(R.mipmap.dt_img_add);
        imgBaseRight.setOnClickListener(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dt_pop_metting_menu, null);
        popMenu = new PopWindowUtil(this, view);
        view.findViewById(R.id.tv_issued_metting).setOnClickListener(this);
        view.findViewById(R.id.tv_manager_metting).setOnClickListener(this);
        view.findViewById(R.id.tv_manager_issued_people).setOnClickListener(this);
        mainTvs = new TextView[]{tvMineMetting, tvHistoryMetting};
        mainImgs = new ImageView[]{imgMineBottom, imgHistoryBottom};
        mineMettingFragment = new MineMettingFtagment();
        historyMettingFragment = new HistoryMettingFragment();
        fragments = new Fragment[]{mineMettingFragment, historyMettingFragment};
        tvMineMetting.setOnClickListener(this);
        tvHistoryMetting.setOnClickListener(this);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.metting_fragment, historyMettingFragment).hide(historyMettingFragment).add(R.id.metting_fragment, mineMettingFragment).show(mineMettingFragment).commit();
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
            case R.id.tv_mine_meeting:
                index = 0;
                onTabClicked();
                break;
            case R.id.tv_history_metting:
                index = 1;
                onTabClicked();
                break;
            case R.id.img_base_right:
                popMenu.showPopupWindow(llParent);
                break;
            case R.id.tv_issued_metting:
                //Todo 发布会议
                startActivity(new Intent(this, IssuedMettingActivity.class));
                if (popMenu != null && popMenu.isShowing()) {
                    popMenu.dismiss();
                }
                break;
            case R.id.tv_manager_metting:
                // Todo 会议管理

                break;
            case R.id.tv_manager_issued_people:
                //Todo 发布人管理

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

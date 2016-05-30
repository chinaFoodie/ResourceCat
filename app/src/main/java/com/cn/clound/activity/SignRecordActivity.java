package com.cn.clound.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.fragment.GroupSignFragment;
import com.cn.clound.fragment.MineSignFragment;

import butterknife.Bind;

/**
 * 签到记录界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-13 10:32:31
 */
public class SignRecordActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_mine_sign)
    TextView tvMineSign;
    @Bind(R.id.tv_group_sign)
    TextView tvGroupSign;
    @Bind(R.id.rl_mine_sign)
    RelativeLayout rlMineSign;
    @Bind(R.id.rl_group_sign)
    RelativeLayout rlGroupSign;
    @Bind(R.id.img_mine_bottom)
    ImageView imgMineBottom;
    @Bind(R.id.img_group_bottom)
    ImageView imgGroupBottom;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    //需要加载的fragment集合
    private Fragment[] fragments;
    private MineSignFragment minSignFragment;
    private GroupSignFragment groupSignFragment;
    private TextView[] mainTvs;
    private ImageView[] mainImgs;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_sign_record;
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
        rlGroupSign.setOnClickListener(this);
        rlMineSign.setOnClickListener(this);
        tvMineSign.setOnClickListener(this);
        tvGroupSign.setOnClickListener(this);
        tvMidTitle.setText("签到记录");
        mainTvs = new TextView[]{tvMineSign, tvGroupSign};
        minSignFragment = new MineSignFragment();
        groupSignFragment = new GroupSignFragment();
        fragments = new Fragment[]{minSignFragment, groupSignFragment};
        mainImgs = new ImageView[]{imgMineBottom, imgGroupBottom};
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.sign_fragment, groupSignFragment).hide(groupSignFragment).add(R.id.sign_fragment, minSignFragment).show(minSignFragment).commit();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mine_sign:
                index = 0;
                onTabClicked();
                break;
            case R.id.tv_group_sign:
                index = 1;
                onTabClicked();
                break;
            case R.id.ll_base_back:
                finish();
                break;
        }
    }
}

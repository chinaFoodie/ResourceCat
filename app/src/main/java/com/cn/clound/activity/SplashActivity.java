package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.easemob.HuanXinHelper;
import com.hyphenate.chat.EMClient;

import butterknife.Bind;

/**
 * 开屏页
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年4月11日14:18:47
 */
public class SplashActivity extends BaseActivity {
    @Bind(R.id.splash_root)
    RelativeLayout rlSplash;
    private static final int sleepTime = 2000;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_splash;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化View
     */
    private void init() {
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1500);
        rlSplash.startAnimation(animation);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        new Thread(new Runnable() {
            public void run() {
                if (HuanXinHelper.getInstance().isLoggedIn()) {
                    // ** 免登陆情况 加载所有本地群和会话
                    //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                    //加上的话保证进了主页面会话和群组都已经load完毕
                    long start = System.currentTimeMillis();
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    long costTime = System.currentTimeMillis() - start;
                    //等待sleeptime时长
                    if (sleepTime - costTime > 0) {
                        try {
                            Thread.sleep(sleepTime - costTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //进入主页面
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                } else {
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }).start();
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
}

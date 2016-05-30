package com.cn.clound.base;

import android.app.Activity;
import android.app.Application;

import com.apkfuns.logutils.LogUtils;
import com.cn.clound.base.netstate.NetChangeObserver;
import com.cn.clound.base.netstate.NetWorkUtil;
import com.cn.clound.base.netstate.NetworkStateReceiver;
import com.cn.clound.utils.AppManager;

import org.litepal.LitePalApplication;

import java.io.File;

/**
 * MyApplication
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年3月29日11:27:31
 */
public class BaseApplication extends LitePalApplication {
    public Activity mCurrentActivity;
    private NetChangeObserver mNetChangeObserver;
    public File mNetCacheFile;

    public int mMaxCacheSize = 50 * 1024 * 1024;

    @Override
    public void onCreate() {
        super.onCreate();
        initLogUtils();
        registerNetWorkStateListener();// 注册网络状态监测器
    }

    private void initLogUtils() {
        // 配置日志是否输出(默认true)
        LogUtils.configAllowLog = true;
        // 配置日志前缀
        LogUtils.configTagPrefix = "ChunfaLee-";
    }

    private void registerNetWorkStateListener() {
        mNetChangeObserver = new NetChangeObserver() {
            @Override
            public void onConnect(NetWorkUtil.netType type) {
                super.onConnect(type);
                try {
                    BaseApplication.this.onConnect(type);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

            @Override
            public void onDisConnect() {
                super.onDisConnect();
                try {
                    BaseApplication.this.onDisConnect();
                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        };
        NetworkStateReceiver.registerObserver(mNetChangeObserver);
    }

    /**
     * 当前没有网络连接通知
     */
    public void onDisConnect() {
        mCurrentActivity = AppManager.getAppManager().currentActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof BaseActivity) {
                ((BaseActivity) mCurrentActivity).onDisConnect();
            }
        }
    }

    /**
     * 网络连接连接时通知
     */
    protected void onConnect(NetWorkUtil.netType type) {
        mCurrentActivity = AppManager.getAppManager().currentActivity();
        if (mCurrentActivity != null) {
            if (mCurrentActivity instanceof BaseActivity) {
                ((BaseActivity) mCurrentActivity).onConnect(type);
            }
        }
    }

    public File getNetCacheFile() {
        if (mNetCacheFile == null) {
            mNetCacheFile = new File(getCacheDir() + "/netcache");
        }
        if (!mNetCacheFile.exists()) {
            mNetCacheFile.mkdirs();
        }
        return mNetCacheFile;
    }
}

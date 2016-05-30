/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cn.clound.application;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.cn.clound.alarm.AlarmHelper;
import com.cn.clound.base.BaseApplication;
import com.cn.clound.bean.User.UserModel;
import com.cn.clound.broadcastreceiver.DataChangeReceiver;
import com.cn.clound.easemob.HuanXinHelper;
import com.cn.clound.utils.CrashHandler;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import org.litepal.LitePalApplication;

import java.io.File;

public class MyApplication extends BaseApplication {

    public static Context applicationContext;
    private static MyApplication instance;
    private DataChangeReceiver mTickReceiver;
    private IntentFilter mFilter;
    // login user name
    public final String PREF_USERNAME = "username";

    private UserModel um;
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    /**
     * 当前用户nickname,为了苹果推送不是userid而是昵称
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        applicationContext = this;
        instance = this;
        initImageLoader(applicationContext);
        //init demo helper
        HuanXinHelper.getInstance().init(applicationContext);
        SDKInitializer.initialize(getApplicationContext());
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
        LitePalApplication.initialize(this);
        //每次启动程序设置闹钟
        AlarmHelper.getInstance().setAlarmTime(applicationContext);
        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_TIME_TICK); //每分钟变化的action
        mFilter.addAction(Intent.ACTION_TIME_CHANGED); //设置了系统时间的action
        mTickReceiver = new DataChangeReceiver();
        registerReceiver(mTickReceiver, mFilter);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化imageloader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public WindowManager.LayoutParams getMywmParams() {
        return wmParams;
    }

    public UserModel getUm() {
        return um;
    }

    public void setUm(UserModel um) {
        this.um = um;
    }
}

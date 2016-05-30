package com.cn.clound.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cn.clound.broadcastreceiver.DataChangeReceiver;

/**
 * 闹钟监听服务
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月14日 17:40:06
 */
public class AlarmService extends Service {
    private DataChangeReceiver mTickReceiver;
    private IntentFilter mFilter;

    @Override
    public void onCreate() {
        super.onCreate();
        mFilter = new IntentFilter();
        mFilter.addAction(Intent.ACTION_TIME_TICK); //每分钟变化的action
        mFilter.addAction(Intent.ACTION_TIME_CHANGED); //设置了系统时间的action
        mTickReceiver = new DataChangeReceiver();
        registerReceiver(mTickReceiver, mFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

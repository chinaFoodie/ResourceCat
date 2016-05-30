package com.cn.clound.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.NotificationCompat;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;

import java.io.IOException;

/**
 * 闹钟提示Activity
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-18 16:48:05
 */
public class AlarmDialogActivity extends BaseActivity {
    private MediaPlayer m1 = null;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_alarm_dialog;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    private void init() {
        doSomething("云在教育", "我要去签到");
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

    private void doSomething(String title, String text) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlarmDialogActivity.this);
        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmDialogActivity.this, 0,
                new Intent(AlarmDialogActivity.this, MainActivity.class), 0);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(text)
                .setTicker("云在教育") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)
                .setOngoing(true)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                .setSmallIcon(R.drawable.ic_launcher).setContentIntent(pendingIntent);//设置通知小ICON
        mNotificationManager.notify(1000, mBuilder.build());
        AssetManager am = getAssets();
        try {
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audioManager.setSpeakerphoneOn(true);
            AssetFileDescriptor afd = am.openFd("sign.wav");
            m1 = new MediaPlayer();
            m1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            m1.prepare();
            m1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.cn.clound.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.cn.clound.R;
import com.cn.clound.activity.MainActivity;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.SharePreferceUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * 闹钟监听服务
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月14日 17:40:06
 */
public class AlarmServiceSound extends Service {
    private MediaPlayer m1 = null;
    private SharePreferceUtil share;

    @Override
    public void onCreate() {
        share = SharePreferceUtil.getInstance(this);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //接到闹钟之后做下一次闹钟set方法
        doSomething("签到", "我不是来签到的");
        return super.onStartCommand(intent, flags, startId);
    }

    private void doSomething(String title, String text) {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AlarmServiceSound.this);
        PendingIntent pendingIntent = PendingIntent.getActivity(AlarmServiceSound.this, 0,
                new Intent(AlarmServiceSound.this, MainActivity.class), 0);
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
            AssetFileDescriptor afd = am.openFd("sign.mp3");
            m1 = new MediaPlayer();
            m1.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            m1.prepare();
            m1.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private boolean compareDatewithWeek(String str) {
        boolean lcf = false;
        if (str.substring(str.length() - 1).equals(",")) {
            str = str.replace("1", "星期一").replace("2", "星期二").replace("3", "星期三")
                    .replace("4", "星期四").replace("5", "星期五").replace("6", "星期六").replace("7", "星期日");
            String[] temp = str.split(",");
            String currentWeek = DateUtil.getWeekOfDate(new Date());
            if (Arrays.asList(temp).contains(currentWeek)) {
                lcf = true;
            }
        }
        return lcf;
    }
}

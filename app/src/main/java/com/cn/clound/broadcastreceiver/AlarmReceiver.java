package com.cn.clound.broadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;

import com.cn.clound.R;
import com.cn.clound.activity.MainActivity;
import com.cn.clound.alarm.AlarmHelper;

import java.io.IOException;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-17 20:07:45
 */
public class AlarmReceiver extends BroadcastReceiver {
    private MediaPlayer m1 = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        doSomething(context, "签到", "我是接收的广播");
        AlarmHelper.getInstance().setAlarmTime(context);
    }

    private void doSomething(Context context, String title, String text) {
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);
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
        AssetManager am = context.getAssets();
        try {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
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
}
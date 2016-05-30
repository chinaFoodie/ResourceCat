package com.cn.clound.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;

import com.cn.clound.alarm.AlarmHelper;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.SharePreferceUtil;

/**
 * 时间变化广播
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-14 18:11:19
 */
public class DataChangeReceiver extends BroadcastReceiver {
    private Context context;
    private MediaPlayer m1 = null;
    private SharePreferceUtil share;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        share = SharePreferceUtil.getInstance(context);
        AlarmHelper.getInstance().setAlarmTime(context);
    }
}

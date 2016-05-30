package com.hyphenate.easeui.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/5/11.
 */
public class TimerCountUtil extends CountDownTimer {
    private Context mContext;

    public TimerCountUtil(Context mContext, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mContext = mContext;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if ((millisUntilFinished / 1000) < 5) {
            Toast.makeText(mContext, "你还可以讲话" + millisUntilFinished / 1000, Toast.LENGTH_SHORT).show();
        } else if (millisUntilFinished / 1000 == 0) {
        }
    }

    @Override
    public void onFinish() {
    }
}

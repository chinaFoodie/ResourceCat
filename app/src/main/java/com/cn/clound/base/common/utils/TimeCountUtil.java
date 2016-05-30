package com.cn.clound.base.common.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.cn.clound.R;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 */
public class TimeCountUtil extends CountDownTimer {

    private Activity mActivity;
    private TextView textView;

    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval, TextView textView) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
        this.textView = textView;
    }

    public TimeCountUtil(Activity mActivity, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mActivity = mActivity;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setClickable(false);//设置不能点击
        textView.setText(millisUntilFinished / 1000 + "秒");//设置倒计时时间
        Spannable span = new SpannableString(textView.getText().toString());//获取按钮的文字
        textView.setBackgroundColor(mActivity.getResources().getColor(R.color.colorWhiteMainTab));
        span.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//讲倒计时时间显示为红色
        textView.setText(span);
    }

    @Override
    public void onFinish() {
        textView.setText("重发");
        textView.setClickable(true);//重新获得点击
        textView.setBackgroundColor(mActivity.getResources().getColor(R.color.colorBlueMainTab));//
    }
}

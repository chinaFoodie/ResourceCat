package com.cn.clound.view.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-15 16:19:50
 */
public class CustomCalendar extends ViewGroup {
    public CustomCalendar(Context context) {
        super(context);
    }

    public CustomCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

package com.cn.clound.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import com.cn.clound.R;

/**
 * 下拉刷新给予framelayout
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-20 15:31:10
 */
public class PullRefreshLayout extends FrameLayout implements
        GestureDetector.OnGestureListener {
    public static final int STATE_CLOSE = 1;
    public static final int STATE_OPEN = 2;
    public static final int STATE_OPEN_MAX = 4;
    public static final int STATE_OPEN_MAX_RELEASE = 5;
    public static final int STATE_OPEN_RELEASE = 3;
    public static final int STATE_UPDATE = 6;
    public static final int STATE_UPDATE_SCROLL = 7;
    private static int MAXHEIGHT = 80;

    private ImageView mArrow;
    private String mDate;
    private GestureDetector mDetector;
    private Flinger mFlinger;
    private boolean mIsAutoScroller;
    private int mPading;
    private ProgressBar mProgressBar;
    private int mState;
    private TextView mTitle;
    private FrameLayout mUpdateContent;

    private UpdateHandle mUpdateHandle;

    private RotateAnimation mFlipAnimation;
    private RotateAnimation mReverseFlipAnimation;

    private String tag = "<font color=\"#666666\"><b><big>";
    private String end_tag = "</font></b></big><br/>";
    private String relese;
    private String pulldown;
    private String loadding;
    private String last_time;

    private boolean mIsOpen = true;

    public PullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        relese = genString("释放立即刷新");
        pulldown = genString("下拉刷新");
        loadding = genString("正在刷新,请稍后...");
        last_time = "上次刷新时间:";
        addUpdateBar();
        init();
    }

    private String genString(String text) {
        StringBuffer buffer = new StringBuffer(tag);
        buffer.append(text);
        buffer.append(end_tag);
        return buffer.toString();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean isAuto = mIsAutoScroller;
        mDetector.onTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int y = getChildAt(1).getTop();
                if (y != 0) {
                    updateView();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mState == STATE_OPEN) {
                    mState = STATE_OPEN_RELEASE;
                }
                if (mState == STATE_OPEN_MAX) {
                    mState = STATE_OPEN_MAX_RELEASE;
                }
                release();
                break;
        }
        if (mState != STATE_UPDATE) {
            isAuto = super.dispatchTouchEvent(ev);
        } else {
            return super.dispatchTouchEvent(ev);
        }
        int y = getChildAt(1).getTop();
        if (y != 0) {
            ev.setAction(MotionEvent.ACTION_CANCEL);
            super.dispatchTouchEvent(ev);
            updateView();
        }
        return isAuto;
    }

    private void init() {
        mDetector = new GestureDetector(this);
        mFlinger = new Flinger(getContext());
        mState = STATE_CLOSE;
        setDrawingCacheEnabled(true);
        setClipChildren(true);
        mDetector.setIsLongpressEnabled(false);
        MAXHEIGHT = (int) getContext().getResources().getDimension(
                R.dimen.size_updatebar_content_height);
    }

    private void updateView() {
        View view1 = getChildAt(0);
        View view2 = getChildAt(1);
        if (mDate == null)
            mDate = "";
        switch (mState) {
            case STATE_CLOSE:
                if (view1.getVisibility() != View.INVISIBLE)
                    view1.setVisibility(View.INVISIBLE);
            case STATE_OPEN:
            case STATE_OPEN_RELEASE: {
                // STATE_OPEN
                int offset = -mPading - view2.getTop();
                view2.offsetTopAndBottom(offset);
                if (view1.getVisibility() != View.VISIBLE)
                    view1.setVisibility(View.VISIBLE);
                int y1 = view1.getTop();// 相对于父窗口的顶部大小
                offset = -MAXHEIGHT - mPading - y1;
                view1.offsetTopAndBottom(offset);
                StringBuilder builder = new StringBuilder(pulldown);
                builder.append(last_time);
                builder.append(mDate);
                mTitle.setText(Html.fromHtml(builder.toString()));
                mProgressBar.setVisibility(View.INVISIBLE);
                mArrow.setVisibility(View.VISIBLE);
                if (!mIsOpen) {
                    mIsOpen = true;
                    mArrow.setAnimation(mReverseFlipAnimation);
                    mReverseFlipAnimation.start();
                }
                break;
            }
            case STATE_OPEN_MAX_RELEASE:
            case STATE_OPEN_MAX: {
                int offset = -mPading - view2.getTop();
                view2.offsetTopAndBottom(offset);
                if (view1.getVisibility() != View.VISIBLE)
                    view1.setVisibility(View.VISIBLE);
                offset = -MAXHEIGHT - mPading - view1.getTop();
                view1.offsetTopAndBottom(offset);
                StringBuilder builder = new StringBuilder(relese);
                builder.append(last_time);
                builder.append(mDate);
                mTitle.setText(Html.fromHtml(builder.toString()));
                mProgressBar.setVisibility(View.INVISIBLE);
                mArrow.setVisibility(View.VISIBLE);
                if (mIsOpen) {
                    mIsOpen = false;
                    mArrow.setAnimation(mFlipAnimation);
                    mFlipAnimation.start();
                }
                break;
            }
            case STATE_UPDATE:
                // STATE_UPDATE
            {
                int offset = -mPading - view2.getTop();
                view2.offsetTopAndBottom(offset);
                if (mProgressBar.getVisibility() != View.VISIBLE)
                    mProgressBar.setVisibility(View.VISIBLE);
                if (mArrow.getVisibility() != View.INVISIBLE)
                    mArrow.setVisibility(View.INVISIBLE);
                StringBuffer builder = new StringBuffer(loadding);
                builder.append(last_time);
                builder.append(mDate);
                mTitle.setText(Html.fromHtml(builder.toString()));
                offset = -MAXHEIGHT - mPading - view1.getTop();
                view1.offsetTopAndBottom(offset);
                if (view1.getVisibility() != View.VISIBLE)
                    view1.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mArrow.setVisibility(View.GONE);
                mArrow.clearAnimation();
                break;
            }
        }
        invalidate();
    }

    private void addUpdateBar() {
        Context context = getContext();
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(200);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(200);
        mReverseFlipAnimation.setFillAfter(true);
        View view = LayoutInflater.from(context).inflate(
                R.layout.vw_update_bar, null);
        view.setVisibility(View.INVISIBLE);
        addView(view);
        mArrow = new ImageView(context);
        FrameLayout.LayoutParams mArrowParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);
        ImageView.ScaleType localScaleType = ImageView.ScaleType.FIT_CENTER;
        mArrow.setScaleType(localScaleType);
        mArrow.setLayoutParams(mArrowParams);
        mArrow.setImageResource(R.mipmap.xlistview_arrow);
        mUpdateContent = (FrameLayout) getChildAt(0).findViewById(
                R.id.iv_content);
        mUpdateContent.addView(mArrow);
        FrameLayout.LayoutParams mProgressBarParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);
        mProgressBarParams.gravity = Gravity.CENTER_VERTICAL;
        mProgressBar = new ProgressBar(context);
        int padding = getResources().getDimensionPixelSize(
                R.dimen.size_updatebar_padding);
        mProgressBar.setPadding(padding, padding, padding, padding);
        mProgressBar.setLayoutParams(mProgressBarParams);
        mUpdateContent.addView(mProgressBar);
        mTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        // TODO Auto-generated method stub
        int mtop = -MAXHEIGHT - mPading;
        getChildAt(0).layout(0, mtop, getMeasuredWidth(), -mPading);
        int mbottom = getMeasuredHeight() - mPading;
        getChildAt(1).layout(0, -mPading, getMeasuredWidth(), mbottom);
    }

    public void endUpdate(String timeFomat) {
        mDate = timeFomat;
        if (mPading != 0) {
            mState = STATE_CLOSE;
            scrollToClose();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        AdapterView<?> adapterView = (AdapterView<?>) getChildAt(1);
        int k = adapterView.getCount();
        if (k == 0)
            return false;
        k = adapterView.getFirstVisiblePosition();// 获取第一个显示项目的position
        if (k == 0) {
            int t = adapterView.getChildAt(0).getTop();
            if (t != 0) {
                return false;
            } else {
                mPading = (int) (mPading + distanceY / 2);
                if (mPading > 0)
                    mPading = 0;
                if (Math.abs(mPading) <= MAXHEIGHT) {
                    mState = STATE_OPEN;
                } else {
                    mState = STATE_OPEN_MAX;
                }
                updateView();
            }
        }
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        return false;
    }

    private boolean release() {
        int tempStatus = STATE_OPEN_MAX_RELEASE;
        if (mPading >= 0) {
            return true;
        }
        switch (mState) {
            case STATE_OPEN_RELEASE:
                if (Math.abs(mPading) < MAXHEIGHT) {
                    tempStatus = STATE_OPEN_MAX_RELEASE;
                    mState = tempStatus;
                }
                scrollToClose();
                break;
            case STATE_OPEN_MAX_RELEASE:
                mState = tempStatus;
                scrollToUpdate();
                break;
        }
        return false;
    }

    private void scrollToClose() {
        mFlinger.startUsingDistance(-mPading, 2000);
    }

    private void scrollToUpdate() {
        mFlinger.startUsingDistance(-mPading - MAXHEIGHT, 1000);
    }

    class Flinger implements Runnable {
        private int mLastFlingX;
        private Scroller mScroller;

        public Flinger(Context context) {
            mScroller = new Scroller(context);
        }

        private void startCommon() {
            removeCallbacks(this);
        }

        public void run() {
            boolean auto = Math.abs(mPading) != MAXHEIGHT;
            boolean compute = mScroller.computeScrollOffset();
            move(mLastFlingX - mScroller.getCurrX(), auto);
            updateView();
            if (compute) {
                mLastFlingX = mScroller.getCurrX();
                post(this);
            } else {
                mIsAutoScroller = auto;
                removeCallbacks(this);
            }
        }

        public void startUsingDistance(int padding, int duration) {
            int i = 0;
            if (padding == 0)
                padding = -1;
            startCommon();
            mLastFlingX = i;
            mScroller.startScroll(i, 0, -padding, 0, duration);
            mIsAutoScroller = true;
            post(this);
        }
    }

    /**
     * 释放的时候使用
     */
    public void move(float f, boolean bool) {
        if (mState != STATE_CLOSE) {
            if (!bool) {
                // 刷新
                if (mState == STATE_OPEN_MAX_RELEASE) {
                    mState = STATE_UPDATE;
                    if (mUpdateHandle != null) {
                        mUpdateHandle.onUpdate();
                    }
                }
            }
            if (mState == STATE_OPEN_MAX_RELEASE
                    || mState == STATE_OPEN_RELEASE) {
                mPading += f;
            }
        } else {
            if (mIsAutoScroller) {
                mPading += f;
            }
        }
    }

    public abstract interface UpdateHandle {
        public abstract void onUpdate();
    }

    public void setUpdateDate(String timeFomat) {
        mDate = timeFomat;
    }

    public void setUpdateHandle(UpdateHandle paramUpdateHandle) {
        mUpdateHandle = paramUpdateHandle;
    }

    public void updateWithoutOffset() {
        mState = STATE_UPDATE_SCROLL;
        invalidate();
    }
}

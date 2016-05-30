package com.cn.clound.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cn.clound.R;

import java.util.ArrayList;


/**
 * @author Chunfa Lee
 * @version 1.00
 * @create 2016年3月30日11:15:14
 */
public class BaseViewPager extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 图片轮播视图
     */
    private ViewPager mAdvPager = null;

    /**
     * 滚动图片视图适配器
     */
    private ImageCycleAdapter mAdvAdapter;

    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mGroup;

    /**
     * 图片轮播指示器-个图
     */
    private ImageView mImageView = null;

    /**
     * 滚动图片指示器-视图列表
     */
    private ImageView[] mImageViews = null;
    int imageCount;
    /**
     * 图片滚动当前图片下标
     */

    /**
     * 手机密度
     */
    private float mScale;

    /**
     * @param context
     */
    public BaseViewPager(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public BaseViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.dt_cycle_view, this);
        mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
        mAdvPager.setOnPageChangeListener(new GuidePageChangeListener());
        mAdvPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视图
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);
    }

    /**
     * 装填图片数据
     *
     * @param imageUrlList
     * @param PagerBannersListener
     */
    public void setImageResources(ArrayList<String> imageUrlList,
                                  PagerBannersListener PagerBannersListener) {
        // 清除所有子视图
        mGroup.removeAllViews();
        // 图片广告数量
        imageCount = imageUrlList.size();
        mImageViews = new ImageView[imageCount];
        for (int i = 0; i < imageCount; i++) {
            mImageView = new ImageView(mContext);
            int imagePadding = (int) (mScale * 3 + 0.5f);
            // 设置圆点间距
            LayoutParams layoutParams = new LayoutParams(6, 6);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            mImageView.setPadding(imagePadding, imagePadding, imagePadding,
                    imagePadding);
            mImageView.setLayoutParams(layoutParams);

            mImageViews[i] = mImageView;
            if (i == 0) {
                mImageViews[i].setBackgroundResource(R.mipmap.dot_selected);
            } else {
                mImageViews[i].setBackgroundResource(R.mipmap.dot_unselected);
            }
            mGroup.addView(mImageViews[i]);
        }
        mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList,
                PagerBannersListener);
        mAdvPager.setAdapter(mAdvAdapter);
        // startImageTimerTask();
        if (imageCount != 1) {
            mAdvPager.setCurrentItem(100);
        }
    }

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片每3秒滚动一次
        // mHandler.postDelayed(mImageTimerTask, 3000);
        mHandler.sendEmptyMessageDelayed(1, 3000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        // mHandler.removeCallbacks(mImageTimerTask);
        mHandler.removeMessages(1);
    }

    /**
     * 图片自动轮播Task
     */

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    int totalCount = imageCount;
                    int currentItem = mAdvPager.getCurrentItem();
                    int toItem = currentItem + 1 == totalCount ? 0
                            : currentItem + 1;
                    mAdvPager.setCurrentItem(toItem, true);
                    // 每3秒钟发送一个message，用于切换viewPager中的图片
                    this.sendEmptyMessageDelayed(1, 3000);
                    break;
            }
        }
    };

    /**
     * 轮播图片状态监听器
     *
     * @author luomin
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
                // startImageTimerTask(); // 开始下次计时
                if (arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (mAdvPager.getCurrentItem() == imageCount - 1) {
                        mAdvPager.setCurrentItem(0);
                    } else if (mAdvPager.getCurrentItem() == 0) {
                        mAdvPager.setCurrentItem(imageCount);
                    }
                }
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            if (index > 2) {
                index = index % imageCount;
            }
            // 设置图片滚动指示器背景
            mImageViews[index].setBackgroundResource(R.mipmap.dot_selected);
            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i]
                            .setBackgroundResource(R.mipmap.dot_unselected);
                }
            }
        }
    }

    private class ImageCycleAdapter extends PagerAdapter {

        /**
         * 图片视图缓存列表
         */
        private ArrayList<ImageView> mImageViewCacheList;

        /**
         * 图片资源列表
         */
        private ArrayList<String> mAdList = new ArrayList<String>();

        /**
         * 广告图片点击监听器
         */
        private PagerBannersListener mPagerBannersListener;

        private Context mContext;

        public ImageCycleAdapter(Context context, ArrayList<String> adList,
                                 PagerBannersListener PagerBannersListener) {
            mContext = context;
            mAdList = adList;
            mPagerBannersListener = PagerBannersListener;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            // return mAdList.size();
            if (mAdList.size() == 1) {
                return mAdList.size();
            } else {
                return Integer.MAX_VALUE;
            }
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            String imageUrl = mAdList.get(position % mAdList.size());
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            // 设置图片点击监听
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    mPagerBannersListener.onImageClick(position, v);
                    // T.showShort(mContext,position+"");
                }
            });
            imageView.setTag(imageUrl);
            container.addView(imageView);
            mPagerBannersListener.ShowImage(imageUrl, imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

    }

    /**
     * 播放监听事件
     */
    public static interface PagerBannersListener {

        /**
         * 加载图片资源
         *
         * @param imageURL
         * @param imageView
         */
        public void ShowImage(String imageURL, ImageView imageView);

        /**
         * 单击图片事件
         *
         * @param position
         * @param imageView
         */
        public void onImageClick(int position, View imageView);
    }

}

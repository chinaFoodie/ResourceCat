package com.cn.clound.base.common.gallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 显示相册图片
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-16 11:41:11
 */
public class ShowImageActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.child_grid)
    GridView mGridView;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    private List<String> list;
    private ChildAdapter adapter;
    private List<String> listWiped = new ArrayList<>();

    @Override
    protected int getMainContentViewId() {
        return R.layout.base_show_image_activity;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("确定");
        tvBaseRight.setOnClickListener(this);
        tvMidTitle.setText(getIntent().getStringExtra("folder_name"));
        list = getIntent().getStringArrayListExtra("data");
        adapter = new ChildAdapter(this, list, mGridView);
        mGridView.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.tv_base_right:
                Intent picIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("wiped_pic_list", (Serializable) listWiped);
                picIntent.putExtras(bundle);
                this.setResult(1005, picIntent);
                this.finish();
                break;
            default:
                break;
        }
    }

    public class ChildAdapter extends BaseAdapter {

        private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
        /**
         * 用来存储图片的选中情况
         */
        private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
        private GridView mGridView;
        private List<String> list;
        protected LayoutInflater mInflater;
        private Context context;

        public ChildAdapter(Context context, List<String> list, GridView mGridView) {
            this.list = list;
            this.mGridView = mGridView;
            mInflater = LayoutInflater.from(context);
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            String path = list.get(position);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.base_grid_child_item, null);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
                viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
                //用来监听ImageView的宽和高
                viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

                    @Override
                    public void onMeasureSize(int width, int height) {
                        mPoint.set(width, height);
                    }
                });

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
            }
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);
            viewHolder.mImageView.setTag(path);
            viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //如果是未选中的CheckBox,则添加动画
                    if (!mSelectMap.containsKey(position) || !mSelectMap.get(position)) {
                        if (listWiped.contains(list.get(position))) {
                            listWiped.remove(list.get(position));
                        } else {
                            listWiped.add(list.get(position));
                        }
                        addAnimation(viewHolder.mCheckBox);
                    }
                    mSelectMap.put(position, isChecked);
                }
            });
            viewHolder.mCheckBox.setChecked(mSelectMap.containsKey(position) ? mSelectMap.get(position) : false);
            //利用NativeImageLoader类加载本地图片
            Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {

                @Override
                public void onImageLoader(Bitmap bitmap, String path) {
                    ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
                    if (bitmap != null && mImageView != null) {
                        mImageView.setImageBitmap(bitmap);
                    }
                }
            });

            if (bitmap != null) {
                viewHolder.mImageView.setImageBitmap(bitmap);
            } else {
                viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
            }
            return convertView;
        }

        /**
         * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
         *
         * @param view
         */
        private void addAnimation(View view) {
            float[] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
            AnimatorSet set = new AnimatorSet();
            set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
                    ObjectAnimator.ofFloat(view, "scaleY", vaules));
            set.setDuration(150);
            set.start();
        }


        /**
         * 获取选中的Item的position
         *
         * @return
         */
        public List<Integer> getSelectItems() {
            List<Integer> list = new ArrayList<Integer>();
            for (Iterator<Map.Entry<Integer, Boolean>> it = mSelectMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Boolean> entry = it.next();
                if (entry.getValue()) {
                    list.add(entry.getKey());
                }
            }

            return list;
        }


        public class ViewHolder {
            public MyImageView mImageView;
            public CheckBox mCheckBox;
        }
    }
}

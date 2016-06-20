package com.cn.clound.base.common.gallery;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.clound.R;
import com.cn.clound.activity.WipedApprovalActivity;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.view.CustomProgress;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年6月16日 09:44:59
 */
public class GalleryActivity extends BaseActivity implements View.OnClickListener {
    private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
    private List<ImageBean> list = new ArrayList<ImageBean>();
    private final static int SCAN_OK = 1;
    private GroupAdapter adapter;
    private CustomProgress progress;
    @Bind(R.id.gv_gallery_grid)
    GridView mGroupGridView;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    // 关闭进度条
                    progress.dismiss();
                    adapter = new GroupAdapter(GalleryActivity.this, list = subGroupOfImage(mGruopMap), mGroupGridView);
                    mGroupGridView.setAdapter(adapter);
                    break;
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.base_activity_gallery;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        progress = new CustomProgress(this, "加载中...");
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText("相册");
        getImages();
        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> childList = mGruopMap.get(list.get(position).getFolderName());
                Intent mIntent = new Intent(GalleryActivity.this, ShowImageActivity.class);
                mIntent.putStringArrayListExtra("data", (ArrayList<String>) childList);
                mIntent.putExtra("folder_name", list.get(position).getFolderName());
                startActivityForResult(mIntent, 6711);
            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == 6711) {
            Intent picIntent = new Intent();
            Bundle bundle = data.getExtras();
            picIntent.putExtras(bundle);
            this.setResult(1005, picIntent);
            this.finish();
        }
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        progress.show();
        new Thread(new Runnable() {

            @Override
            public void run() {
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = GalleryActivity.this.getContentResolver();
                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    // 获取该图片的父路径名
                    String parentName = new File(path).getParentFile().getName();
                    // 根据父路径名将图片放入到mGroupMap中
                    if (!mGruopMap.containsKey(parentName)) {
                        List<String> chileList = new ArrayList<String>();
                        chileList.add(path);
                        mGruopMap.put(parentName, chileList);
                    } else {
                        mGruopMap.get(parentName).add(path);
                    }
                }
                mCursor.close();
                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);

            }
        }).start();
    }

    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中 所以需要遍历HashMap将数据组装成List
     *
     * @param mGroupMap
     * @return
     */
    private List<ImageBean> subGroupOfImage(HashMap<String, List<String>> mGroupMap) {
        if (mGroupMap.size() == 0) {
            return null;
        }
        List<ImageBean> list = new ArrayList<>();
        Iterator<Map.Entry<String, List<String>>> it = mGroupMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ImageBean mImageBean = new ImageBean();
            String key = entry.getKey();

            List<String> value = entry.getValue();
            mImageBean.setFolderName(key);
            mImageBean.setImageCounts(value.size());
            mImageBean.setTopImagePath(value.get(0));// 获取该组的第一张图片

            list.add(mImageBean);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            default:
                break;
        }
    }

    public class GroupAdapter extends BaseAdapter {

        private List<ImageBean> list;
        private Point mPoint = new Point(0, 0);// 用来封装ImageView的宽和高的对象
        private GridView mGridView;
        protected LayoutInflater mInflater;

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

        public GroupAdapter(Context context, List<ImageBean> list, GridView mGridView) {
            this.list = list;
            this.mGridView = mGridView;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            ImageBean mImageBean = list.get(position);
            String path = mImageBean.getTopImagePath();
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.base_grid_group_item, null);
                viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.group_image);
                viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.group_title);
                viewHolder.mTextViewCounts = (TextView) convertView.findViewById(R.id.group_count);

                // 用来监听ImageView的宽和高
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

            viewHolder.mTextViewTitle.setText(mImageBean.getFolderName());
            viewHolder.mTextViewCounts.setText(Integer.toString(mImageBean.getImageCounts()));
            // 给ImageView设置路径Tag,这是异步加载图片的小技巧
            viewHolder.mImageView.setTag(path);
            //
            ImageLoader.getInstance().displayImage("file://" + path, viewHolder.mImageView);
            // 利用NativeImageLoader类加载本地图片
//            Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {
//
//                @Override
//                public void onImageLoader(Bitmap bitmap, String path) {
//                    ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
//                    if (bitmap != null && mImageView != null) {
//                        mImageView.setImageBitmap(bitmap);
//                    }
//                }
//            });

//            if (bitmap != null) {
//                viewHolder.mImageView.setImageBitmap(bitmap);
//            } else {
//                viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
//            }
            return convertView;
        }

        public class ViewHolder {
            public MyImageView mImageView;
            public TextView mTextViewTitle;
            public TextView mTextViewCounts;
        }
    }
}

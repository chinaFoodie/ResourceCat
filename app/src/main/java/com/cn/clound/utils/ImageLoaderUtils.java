package com.cn.clound.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.cn.clound.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Class Of ImageLoader Utils
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年4月12日10:50:27
 */
public class ImageLoaderUtils {
    /**
     * 状态模式初始化ImageLoader
     */
    static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_launcher) // resource or drawable
            .showImageForEmptyUri(R.mipmap.ic_launcher) // resource or drawable
            .showImageOnFail(R.mipmap.ic_launcher) // resource or drawable
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    /**
     * 显示圆角图片裁剪
     */
    public static void showRoundImage(String imageURL, final ImageView imageView) {
        String a = ImageLoader.getInstance().getDiscCache().get(imageURL).getPath();
//        try {
//            imageView.setImageBitmap(BitmapUtil.toRoundCorner(BitmapFactory.decodeFile(a), 1000));
//        } catch (Exception e) {
        ImageLoader.getInstance().loadImage(imageURL, options, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(BitmapUtil.toRoundCorner(loadedImage, 1000));
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
//    }
}

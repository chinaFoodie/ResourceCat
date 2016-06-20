package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.Log;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.gallery.GalleryActivity;
import com.cn.clound.view.ActionSheetDialog;
import com.hyphenate.easeui.widget.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;

/**
 * 个人信息
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-20 11:56:10
 */
public class PersonalInfoActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.rl_personal_info_avatar)
    RelativeLayout rlPersonalAvatar;
    @Bind(R.id.civ_personal_avatar)
    CircleImageView civAvatar;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_personal_info;
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
        tvMidTitle.setText("个人信息");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("编辑");
        tvBaseRight.setOnClickListener(this);

        rlPersonalAvatar.setOnClickListener(this);
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
        if (requestCode == 6717) {
            //相机返回回调
            if (data == null) {
                return;
            }
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMddHHmmss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = new File("/sdcard/DCIM/image/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/DCIM/image/" + name;
            try {
                b = new FileOutputStream(fileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toastor.showToast(this, fileName);
            try {
                civAvatar.setImageBitmap(bitmap);// 将图片显示在ImageView里
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == 6718) {
            //相册返回回调
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.tv_base_right:
                if (tvBaseRight.getText().toString().equals("编辑")) {
                    tvBaseRight.setText("保存");
                } else {
                    tvBaseRight.setText("编辑");
                }
                break;
            case R.id.rl_personal_info_avatar:
                new ActionSheetDialog(PersonalInfoActivity.this).builder()
                        .setTitle("选择照片").setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, 6717);
                                    }
                                })
                        .addSheetItem("相册选取", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent galleyIntent = new Intent(PersonalInfoActivity.this, GalleryActivity.class);
                                        startActivityForResult(galleyIntent, 6718);
                                    }
                                })
                        .show();
                break;
            default:
                break;
        }
    }
}

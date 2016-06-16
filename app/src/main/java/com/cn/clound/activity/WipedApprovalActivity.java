package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;

/**
 * 报销审批界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-6-15 15:36:34
 */
public class WipedApprovalActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llLeft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.dt_include_swiped_banner)
    View wipedBanner;
    @Bind(R.id.dt_include_swiped_edit)
    View wipedEdit;
    @Bind(R.id.rl_swiped_list_details)
    RelativeLayout rlWipedDetails;
    @Bind(R.id.recycler_wiped_approval_picture)
    RecyclerView recyclerViewWipedPic;
    @Bind(R.id.ll_get_wiped_pic)
    LinearLayout llGetWiped;

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_wiped_approval;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llLeft.setVisibility(View.VISIBLE);
        llLeft.setOnClickListener(this);
        tvBaseLeft.setVisibility(View.VISIBLE);
        tvBaseLeft.setText("取消");
        tvMidTitle.setText("报销审批");

        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("提交");
        tvBaseRight.setOnClickListener(this);

        rlWipedDetails.setOnClickListener(this);
        llGetWiped.setOnClickListener(this);
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
        if (resultCode == Activity.RESULT_OK) {
            String sdStatus = Environment.getExternalStorageState();
            if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                return;
            }
            new DateFormat();
            String name = DateFormat.format("yyyyMMddhhMMss", Calendar.getInstance(Locale.CHINA)) + ".jpeg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File file = new File("/sdcard/image/");
            file.mkdirs();// 创建文件夹
            String fileName = "/sdcard/image/" + name;
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
//                view.setImageBitmap(bitmap);// 将图片显示在ImageView里
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_swiped_list_details:
                wipedEdit.setVisibility(View.GONE);
                wipedBanner.setVisibility(View.VISIBLE);
                tvBaseRight.setText("确定");
                tvMidTitle.setText("报销明细");
                break;
            case R.id.ll_base_left:
                if ("报销审批".equals(tvMidTitle.getText().toString())) {
                    this.finish();
                } else {
                    wipedBanner.setVisibility(View.GONE);
                    wipedEdit.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                    tvMidTitle.setText("报销审批");
                }
                break;
            case R.id.tv_base_right:
                if ("提交".equals(tvBaseRight.getText().toString())) {

                } else {
                    wipedBanner.setVisibility(View.GONE);
                    wipedEdit.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("提交");
                    tvMidTitle.setText("报销审批");
                }
                break;
            case R.id.ll_get_wiped_pic:
                new ActionSheetDialog(WipedApprovalActivity.this).builder()
                        .setTitle("添加图片")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, 6710);
                                    }
                                })
                        .addSheetItem("相册选取", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent galleyIntent = new Intent(WipedApprovalActivity.this, GalleryActivity.class);
                                        startActivity(galleyIntent);
                                    }
                                })
                        .show();
                break;
            default:
                break;
        }
    }
}

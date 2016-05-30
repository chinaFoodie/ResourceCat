package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.cn.clound.R;
import com.cn.clound.adapter.ResponsibleAdapter;
import com.cn.clound.adapter.SingedRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.data.DataKeeper;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.singed.UserSingedModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;

/**
 * 签到界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年4月15日 17:57:31
 */
public class SignedActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.recycler_singed_list)
    RecyclerView recyclerView;
    @Bind(R.id.img_go_to_singed_location)
    ImageView imgGetLocation;
    @Bind(R.id.img_sign_in_list)
    ImageView imgSignList;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.tv_sign_dept_name)
    TextView tvDeptName;
    @Bind(R.id.tv_sign_curent_date)
    TextView tvCurrentDate;
    @Bind(R.id.tv_sign_current_week)
    TextView tvCurrentWeek;
    private CustomProgress progress;
    private int HTTP_QUERY_USER_SIGNIN_LIST = 133;
    private SingedRecyclerAdapter adapter;
    private MyHttpHelper httpHelper;
    private boolean isNeedRefresh = false;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_USER_SIGNIN_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    UserSingedModel usm = (UserSingedModel) msg.obj;
                    if (usm != null && usm.getData().getResult().size() > 0) {
                        adapter = new SingedRecyclerAdapter(SignedActivity.this, usm.getData().getResult());
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter = new SingedRecyclerAdapter(SignedActivity.this, null);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(SignedActivity.this, msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_sign;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        imgGetLocation.setOnClickListener(this);
        imgSignList.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.activity_sign));
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("设置");
        tvBaseRight.setOnClickListener(this);
        progress = new CustomProgress(this, "");
        progress.show();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvDeptName.setText(MyApplication.getInstance().getUm().getData().getUserInfo().getUnitName());
        tvCurrentDate.setText(DateUtil.getCurDateStr("yyyy-MM-dd"));
        tvCurrentWeek.setText(DateUtil.getWeekOfDate(new Date()));
        httpHelper.postStringBack(HTTP_QUERY_USER_SIGNIN_LIST, AppConfig.QUERY_USER_SIGNIN_LIST, signin(), handler, UserSingedModel.class);
    }

    private HashMap<String, String> signin() {
        HashMap<String, String> sigin = new HashMap<String, String>();
        sigin.put("pageNo", "1");
        sigin.put("pageSize", "10000");
        sigin.put("queryDate", DateUtil.getCurDateStr("yyyy-MM-dd"));
        sigin.put("token", TelephoneUtil.getIMEI(this));
        return sigin;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isNeedRefresh) {
            progress.show();
            httpHelper.postStringBack(HTTP_QUERY_USER_SIGNIN_LIST, AppConfig.QUERY_USER_SIGNIN_LIST, signin(), handler, UserSingedModel.class);
            isNeedRefresh = false;
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        isNeedRefresh = true;
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
                finish();
                break;
            case R.id.img_go_to_singed_location:
                startActivity(new Intent(this, SingedLocationActivity.class));
                break;
            case R.id.img_sign_in_list:
                startActivity(new Intent(this, SignRecordActivity.class));
                break;
            case R.id.tv_base_right:
                startActivity(new Intent(this, SignReminderActivity.class));
                break;
            default:
                break;
        }
    }
}

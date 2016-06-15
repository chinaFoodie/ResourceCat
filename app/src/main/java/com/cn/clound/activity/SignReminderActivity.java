package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.interfaces.OnItemClickLitener;
import com.cn.clound.adapter.SignTipRecyclerAdapter;
import com.cn.clound.alarm.AlarmHelper;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.SwipeMenuRecyclerView;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.singed.QuerySignModel;
import com.cn.clound.bean.singed.SetSignModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 签到提醒界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月14日 10:41:00
 */
public class SignReminderActivity extends BaseActivity implements View.OnClickListener, OnItemClickLitener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.img_base_right)
    ImageView imgBaseRight;
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.rl_to_add_sign_reminder)
    RelativeLayout llSettingSignReminder;
    @Bind(R.id.sign_swipe_menu_recyclerview)
    SwipeMenuRecyclerView swipeMenuRecyclerView;
    @Bind(R.id.ll_no_sign_reminder)
    LinearLayout llNoSignReminder;
    private SignTipRecyclerAdapter adapter;
    private CustomProgress progress;
    private MyHttpHelper httpHelper;
    private int HTTP_QUERY_MY_SIGN_TIP = 137;
    private int HTTP_DEL_MY_SIGN_TIP = 138;
    private int HTTP_SET_MY_SIGN_TIP = 139;
    private boolean NEED_REFURSE = false;
    private SharePreferceUtil share;
    private List<QuerySignModel.QuerySign> list = new ArrayList<QuerySignModel.QuerySign>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_MY_SIGN_TIP) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    QuerySignModel qsm = (QuerySignModel) msg.obj;
                    if (qsm != null && qsm.getData().size() > 0) {
                        llNoSignReminder.setVisibility(View.GONE);
                        swipeMenuRecyclerView.setVisibility(View.VISIBLE);
                        AlarmHelper.getInstance().setAlarmTime(SignReminderActivity.this);
                        list = qsm.getData();
                        adapter = new SignTipRecyclerAdapter(SignReminderActivity.this, list, handler);
                        swipeMenuRecyclerView.setAdapter(adapter);
                        adapter.setOnItemClickLitener(SignReminderActivity.this);
                    } else {
                        llNoSignReminder.setVisibility(View.VISIBLE);
                        swipeMenuRecyclerView.setVisibility(View.GONE);
                        Toastor.showToast(SignReminderActivity.this, "查询数据为空");
                    }
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                } else {
                    llNoSignReminder.setVisibility(View.VISIBLE);
                    swipeMenuRecyclerView.setVisibility(View.GONE);
                    Toastor.showToast(SignReminderActivity.this, msg.obj.toString());
                    if (progress != null && progress.isShowing()) {
                        progress.dismiss();
                    }
                }
            } else if (msg.arg1 == HTTP_DEL_MY_SIGN_TIP) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    httpHelper.postStringBack(HTTP_QUERY_MY_SIGN_TIP, AppConfig.QUERY_MY_SIGN_TIP, queryParmars(), handler, QuerySignModel.class);
                } else {
                    Toastor.showToast(SignReminderActivity.this, msg.obj.toString());
                }
            } else if (msg.what == 101) {
                progress.show();
                QuerySignModel.QuerySign qs = (QuerySignModel.QuerySign) msg.obj;
                httpHelper.postStringBack(HTTP_DEL_MY_SIGN_TIP, AppConfig.DEL_MY_SIGN_TIP, delSign(qs), handler, BaseModel.class);
            } else if (HTTP_SET_MY_SIGN_TIP == msg.arg1) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    httpHelper.postStringBack(HTTP_QUERY_MY_SIGN_TIP, AppConfig.QUERY_MY_SIGN_TIP, queryParmars(), handler, QuerySignModel.class);
                }
            } else if (msg.what == 100) {
                progress.show();
                int index = (int) msg.obj;
                int state = msg.arg2;
                httpHelper.postStringBack(HTTP_SET_MY_SIGN_TIP, AppConfig.SET_MY_SIGN_TIP, setSign(index, state), handler, SetSignModel.class);
            }
        }
    };

    private HashMap<String, String> setSign(int index, int state) {
        HashMap<String, String> parmears = new HashMap<String, String>();
        parmears.put("id", list.get(index).getId());
        parmears.put("remindTime", list.get(index).getRemindTime());
        parmears.put("repeatStr", list.get(index).getRepeatStr());
        parmears.put("state", state + "");
        parmears.put("token", TelephoneUtil.getIMEI(this));
        parmears.put("type", list.get(index).getType());
        return parmears;
    }

    private HashMap<String, String> delSign(QuerySignModel.QuerySign qs) {
        HashMap<String, String> delParmars = new HashMap<String, String>();
        delParmars.put("id", qs.getId());
        delParmars.put("token", TelephoneUtil.getIMEI(this));
        return delParmars;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_sign_reminder;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        share = SharePreferceUtil.getInstance(this);
        httpHelper = MyHttpHelper.getInstance(this);
        progress = new CustomProgress(this, "请稍候...");
        progress.show();
        llBack.setVisibility(View.VISIBLE);
        imgBaseRight.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        imgBaseRight.setOnClickListener(this);
        llSettingSignReminder.setOnClickListener(this);
        tvMidTitle.setText("签到提醒");
        imgBaseRight.setImageResource(R.mipmap.dt_img_add);
        httpHelper.postStringBack(HTTP_QUERY_MY_SIGN_TIP, AppConfig.QUERY_MY_SIGN_TIP, queryParmars(), handler, QuerySignModel.class);
        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private HashMap<String, String> queryParmars() {
        HashMap<String, String> query = new HashMap<String, String>();
        query.put("token", TelephoneUtil.getIMEI(this));
        return query;
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (NEED_REFURSE) {
            httpHelper.postStringBack(HTTP_QUERY_MY_SIGN_TIP, AppConfig.QUERY_MY_SIGN_TIP, queryParmars(), handler, QuerySignModel.class);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        NEED_REFURSE = true;
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
        NEED_REFURSE = false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_back:
                this.finish();
                break;
            case R.id.img_base_right:
                startActivity(new Intent(this, SignTimePeriodActivity.class));
                break;
            case R.id.rl_to_add_sign_reminder:
                startActivity(new Intent(this, SignTimePeriodActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent();
        intent.setClass(this, SignTimePeriodActivity.class);
        share.setCache("set_sign_tip_cycle", list.get(position).getRepeatStr());
        Bundle bundle = new Bundle();
        bundle.putSerializable("sign_tip_model", list.get(position));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}

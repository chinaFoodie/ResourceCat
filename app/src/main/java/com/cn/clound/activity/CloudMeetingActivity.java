package com.cn.clound.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MeetingPublishPersonModel;
import com.cn.clound.fragment.HistoryMettingFragment;
import com.cn.clound.fragment.MineMettingFtagment;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.utils.PopWindowUtil;
import com.cn.clound.view.dialog.TipAddMeetingIssuedDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;

/**
 * 云会议界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-4-15 17:45:30
 */
public class CloudMeetingActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.tv_mine_meeting)
    TextView tvMineMetting;
    @Bind(R.id.tv_history_metting)
    TextView tvHistoryMetting;
    @Bind(R.id.img_mine_meeting)
    ImageView imgMineBottom;
    @Bind(R.id.img_history_metting)
    ImageView imgHistoryBottom;
    @Bind(R.id.img_base_right)
    ImageView imgBaseRight;
    @Bind(R.id.ll_base_right)
    LinearLayout llParent;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    //需要加载的fragment集合
    private Fragment[] fragments;
    private TextView[] mainTvs;
    private ImageView[] mainImgs;
    private Fragment mineMettingFragment;
    private Fragment historyMettingFragment;
    private PopWindowUtil popMenu;
    private List<String> listRole = new ArrayList<>();
    private LinkedList<String> data = new LinkedList<String>();

    private MyHttpHelper httpHelper;
    private int HTTP_GET_MEETING_ISSUED = 163;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_MEETING_ISSUED) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingPublishPersonModel mppm = (MeetingPublishPersonModel) msg.obj;
                    if (mppm.getData().size() > 0) {
                        startActivity(new Intent(CloudMeetingActivity.this, IssuedMettingActivity.class));
                    } else {
                        toastDialog();
                    }
                } else {
                    Toastor.showToast(CloudMeetingActivity.this, msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_cloud_meeting;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        listRole = MyApplication.getInstance().getRole().getData().getResult();
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(this);
        llBack.setVisibility(View.VISIBLE);
        llBack.setOnClickListener(this);
        tvMidTitle.setText(getResources().getString(R.string.activity_cloud_meeting));
        if ((MyApplication.getInstance().getRole() != null && MyApplication.getInstance().getRole().getData().getResult().contains("1"))
                || MyApplication.getInstance().getRole().getData().getResult().contains("4")) {
            imgBaseRight.setVisibility(View.VISIBLE);
        } else {
            imgBaseRight.setVisibility(View.GONE);
        }
        imgBaseRight.setImageResource(R.mipmap.dt_img_add);
        imgBaseRight.setOnClickListener(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dt_pop_metting_menu, null);
        popMenu = new PopWindowUtil(this, view);
        view.findViewById(R.id.tv_issued_metting).setOnClickListener(this);
        view.findViewById(R.id.tv_manager_metting).setOnClickListener(this);
        view.findViewById(R.id.tv_manager_issued_people).setOnClickListener(this);
//        LinearLayout llMeetingIssued = (LinearLayout) view.findViewById(R.id.ll_issued_meeting);
//        LinearLayout llMeetingManager = (LinearLayout) view.findViewById(R.id.ll_manager_meeting);
        LinearLayout llIssuedPeople = (LinearLayout) view.findViewById(R.id.ll_manager_issued_people);
        if (listRole.contains("1")) {
            llIssuedPeople.setVisibility(View.VISIBLE);
        } else {
            llIssuedPeople.setVisibility(View.GONE);
        }
        mainTvs = new TextView[]{tvMineMetting, tvHistoryMetting};
        mainImgs = new ImageView[]{imgMineBottom, imgHistoryBottom};
        mineMettingFragment = new MineMettingFtagment();
        historyMettingFragment = new HistoryMettingFragment();
        fragments = new Fragment[]{mineMettingFragment, historyMettingFragment};
        tvMineMetting.setOnClickListener(this);
        tvHistoryMetting.setOnClickListener(this);
        FragmentTransaction transaction;
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.metting_fragment, historyMettingFragment).hide(historyMettingFragment).add(R.id.metting_fragment, mineMettingFragment).show(mineMettingFragment).commit();
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
            case R.id.tv_mine_meeting:
                index = 0;
                onTabClicked();
                break;
            case R.id.tv_history_metting:
                index = 1;
                onTabClicked();
                break;
            case R.id.img_base_right:
                popMenu.showPopupWindow(llParent);
                break;
            case R.id.tv_issued_metting:
                //Todo 发布会议
                httpHelper.postStringBack(HTTP_GET_MEETING_ISSUED, AppConfig.GET_PUBLISH_MEETING_PERSON, getParams(), handler, MeetingPublishPersonModel.class);
                if (popMenu != null && popMenu.isShowing()) {
                    popMenu.dismiss();
                }
                break;
            case R.id.tv_manager_metting:
                // Todo 会议管理
                startActivity(new Intent(this, MeetingManagerActivity.class));
                if (popMenu != null && popMenu.isShowing()) {
                    popMenu.dismiss();
                }
                break;
            case R.id.tv_manager_issued_people:
                //Todo 发布人管理
                startActivity(new Intent(this, PublishPersonManagerActivity.class));
                if (popMenu != null && popMenu.isShowing()) {
                    popMenu.dismiss();
                }
                break;
            case R.id.ll_base_back:
                finish();
                break;
            default:
                break;
        }
    }

    private HashMap<String, String> getParams() {
        HashMap<String, String> getPerson = new HashMap<String, String>();
        getPerson.put("token", TelephoneUtil.getIMEI(this));
        return getPerson;
    }

    public void onTabClicked() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.sign_fragment, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mainTvs[currentTabIndex].setTextColor(getResources().getColor(R.color.colorWhiteMainTab));
        mainTvs[index].setTextColor(getResources().getColor(R.color.colorBlueMainTab));
        mainImgs[currentTabIndex].setVisibility(View.GONE);
        mainImgs[index].setVisibility(View.VISIBLE);
        currentTabIndex = index;
    }

    private void toastDialog() {
        new TipAddMeetingIssuedDialog(this).builder().setCancelable(false).
                setTitle("对不起！").setNegativeButton(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toastor.showToast(CloudMeetingActivity.this, "取消");
            }
        }).setPositiveButton(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toastor.showToast(CloudMeetingActivity.this, "确定");
            }
        }).show();
    }
}

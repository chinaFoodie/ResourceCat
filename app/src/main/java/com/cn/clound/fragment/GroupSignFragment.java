package com.cn.clound.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.clound.R;
import com.cn.clound.adapter.GroupRecordRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.singed.GroupSingedModel;
import com.cn.clound.http.MyHttpHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;

/**
 * 团队签到碎片
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016-5-13 17:00:48
 */
public class GroupSignFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.tv_current_date)
    TextView tvCurrentDate;
    @Bind(R.id.tv_current_week)
    TextView tvCurrentWeek;
    @Bind(R.id.rl_prev_day)
    RelativeLayout rlPreDay;
    @Bind(R.id.rl_next_day)
    RelativeLayout rlNextDay;
    @Bind(R.id.recycler_group_record)
    RecyclerView recycler;

    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private int HTTP_GET_GROUP_SIGN_LIST = 135;
    private GroupRecordRecyclerAdapter adapter;
    private MyHttpHelper httpHelper;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_GROUP_SIGN_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    GroupSingedModel gsm = (GroupSingedModel) msg.obj;
                    if (gsm != null && gsm.getData().getResult().size() > 0) {
                        adapter = new GroupRecordRecyclerAdapter(getActivity(), gsm.getData().getResult());
                        recycler.setAdapter(adapter);
                    } else {
                        Toastor.showToast(getActivity(), "未查询到记录");
                        adapter = new GroupRecordRecyclerAdapter(getActivity(), null);
                        recycler.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
        }
    };

    public GroupSignFragment() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_group_sign;
    }

    @Override
    public void onFragmentAttach(Fragment fragment, Activity activity) {

    }

    @Override
    public void onFragmentCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }

    @Override
    public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(getActivity());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(date); // 当期日期
        initDate(currentDate);
        rlNextDay.setOnClickListener(this);
        rlPreDay.setOnClickListener(this);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_GET_GROUP_SIGN_LIST, AppConfig.QUERY_GROUP_SIGNIN_LIST, groupPareams(currentDate), handler, GroupSingedModel.class);
    }

    private HashMap<String, String> groupPareams(String date) {
        HashMap<String, String> parmeas = new HashMap<String, String>();
        parmeas.put("pageNo", "1");
        parmeas.put("pageSize", "100000");
        parmeas.put("queryDate", date);
        parmeas.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parmeas;
    }


    private void initDate(String date) {
        try {
            Date temp = DateUtil.simpleDateFormat(date);
            String week = DateUtil.getWeekOfDate(temp);
            tvCurrentWeek.setText(week);
            tvCurrentDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {

    }

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {

    }

    @Override
    public void onFragmentPaused(Fragment fragment) {

    }

    @Override
    public void onFragmentStopped(Fragment fragment) {

    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {

    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_prev_day:
                currentDate = DateUtil.topreday(currentDate);
                initDate(currentDate);
                httpHelper.postStringBack(HTTP_GET_GROUP_SIGN_LIST, AppConfig.QUERY_GROUP_SIGNIN_LIST, groupPareams(currentDate), handler, GroupSingedModel.class);
                break;
            case R.id.rl_next_day:
                currentDate = DateUtil.tonextday(currentDate);
                initDate(currentDate);
                httpHelper.postStringBack(HTTP_GET_GROUP_SIGN_LIST, AppConfig.QUERY_GROUP_SIGNIN_LIST, groupPareams(currentDate), handler, GroupSingedModel.class);
                break;
        }
    }
}

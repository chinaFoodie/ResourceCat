package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cn.clound.R;
import com.cn.clound.activity.MeetingDetailsActivity;
import com.cn.clound.activity.MeetingManagerDetailsActivity;
import com.cn.clound.adapter.HistoryManagerRecyclerAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MyMettingModel;
import com.cn.clound.http.MyHttpHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 管理历史会议界面fragment
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-31 10:31:47
 */
public class ManagerHistoryFragment extends BaseFragment implements OnItemClickLitener {
    @Bind(R.id.recycler_manager_history)
    RecyclerView recyclerView;

    private int HTTP_GET_MANAGER_HISTORY = 147;
    private MyHttpHelper httpHelper;
    private HistoryManagerRecyclerAdapter adapter;
    private List<MyMettingModel.MeetingData.MineMetting> listMeeting = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_MANAGER_HISTORY) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MyMettingModel mmm = (MyMettingModel) msg.obj;
                    if (mmm != null) {
                        listMeeting = mmm.getDate().getResult();
                        adapter = new HistoryManagerRecyclerAdapter(getActivity(), listMeeting);
                        recyclerView.setAdapter(adapter);
                        adapter.setOnItemClickLitener(ManagerHistoryFragment.this);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_manager_history;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_GET_MANAGER_HISTORY, AppConfig.GET_MANAGER_HISTORY_MEETING, getParmas(), handler, MyMettingModel.class);
    }

    private HashMap<String, String> getParmas() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("pageNo", "1");
        parames.put("pageSize", "10000");
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parames;
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
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getActivity(), MeetingManagerDetailsActivity.class).putExtra("meeting_id", listMeeting.get(position).getMeetingId()));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }
}

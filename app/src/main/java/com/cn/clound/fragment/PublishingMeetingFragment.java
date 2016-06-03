package com.cn.clound.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.cn.clound.R;
import com.cn.clound.activity.IssuedMettingActivity;
import com.cn.clound.activity.MeetingDetailsActivity;
import com.cn.clound.adapter.MineMettingRecyclerAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.MeetingDetailsModel;
import com.cn.clound.bean.metting.MyMettingModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 发布中会议fragment
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-31 10:28:21
 */
public class PublishingMeetingFragment extends BaseFragment implements OnItemClickLitener {
    @Bind(R.id.ptrScrollView_home)
    PullToRefreshScrollView mPtrScrollView;
    @Bind(R.id.recycler_mine_metting)
    RecyclerView recyclerview;

    private MineMettingRecyclerAdapter adapter;
    private int HTTP_GET_PUBLISH_MEETING = 146;
    private int HTTP_QUERY_MEETING_DETAILS = 159;
    private int index;
    private MyHttpHelper httpHelper;
    private List<MyMettingModel.MeetingData.MineMetting> lsitMeeting = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_PUBLISH_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MyMettingModel mmm = (MyMettingModel) msg.obj;
                    if (mmm != null) {
                        if (mmm.getDate().getResult() != null && mmm.getDate().getResult().size() > 0) {
                            lsitMeeting = mmm.getDate().getResult();
                            adapter = new MineMettingRecyclerAdapter(getActivity(), lsitMeeting);
                            recyclerview.setAdapter(adapter);
                            adapter.setOnItemClickLitener(PublishingMeetingFragment.this);
                        } else {
                            Toastor.showToast(getActivity(), "暂无数据");
                        }
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_QUERY_MEETING_DETAILS) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MeetingDetailsModel mdm = (MeetingDetailsModel) msg.obj;
                    Intent update = new Intent(getActivity(), IssuedMettingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("update_meeting_info", mdm);
                    update.putExtra("meeting_id", lsitMeeting.get(index).getMeetingId());
                    update.putExtras(bundle);
                    startActivity(update);
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_publishing_meeting;
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
        mPtrScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });
        httpHelper = MyHttpHelper.getInstance(getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_GET_PUBLISH_MEETING, AppConfig.GET_PUBLISH_MEETING, getParames(), handler, MyMettingModel.class);
    }

    private HashMap<String, String> getParames() {
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
        index = position;
        httpHelper.postStringBack(HTTP_QUERY_MEETING_DETAILS, AppConfig.QUERY_MEETING_DETAILS, detailsParmeas(lsitMeeting.get(position).getMeetingId()), handler, MeetingDetailsModel.class);
    }

    private HashMap<String, String> detailsParmeas(String meetingId) {
        HashMap<String, String> details = new HashMap<String, String>();
        details.put("id", meetingId);
        details.put("token", TelephoneUtil.getIMEI(getActivity()));
        return details;
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String[] result) {
            mPtrScrollView.onRefreshComplete();
        }
    }
}

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
import com.cn.clound.activity.MeetingDetailsActivity;
import com.cn.clound.adapter.HistoryMettingRecyclerAdapter;
import com.cn.clound.adapter.MineMettingRecyclerAdapter;
import com.cn.clound.adapter.OnItemClickLitener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.BaseModel;
import com.cn.clound.bean.metting.HistoryMeetingModel;
import com.cn.clound.easemob.db.InviteMessgeDao;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.AlertDialog;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;
import com.hyphenate.chat.EMClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 历史会议界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016年5月23日 16:15:28
 */
public class HistoryMettingFragment extends BaseFragment implements OnItemClickLitener {
    @Bind(R.id.ptrScrollView_history)
    PullToRefreshScrollView pullToRefreshScrollView;
    @Bind(R.id.recycler_history_metting)
    RecyclerView recyclerview;

    private HistoryMettingRecyclerAdapter adapter;
    private int HTTP_QUERY_HISTORY_MEETING = 143;
    private int HTTP_DELETE_HISTORY_MEETING = 144;
    private MyHttpHelper httpHelper;
    private List<HistoryMeetingModel.HistoryMeeting.MeetingModel> listMeeting = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_HISTORY_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    HistoryMeetingModel hmm = (HistoryMeetingModel) msg.obj;
                    if (hmm != null && hmm.getData().getResult().size() > 0) {
                        listMeeting = hmm.getData().getResult();
                        adapter = new HistoryMettingRecyclerAdapter(getActivity(), listMeeting, handler);
                        recyclerview.setAdapter(adapter);
                        adapter.setOnItemClickLitener(HistoryMettingFragment.this);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_DELETE_HISTORY_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    httpHelper.postStringBack(HTTP_QUERY_HISTORY_MEETING, AppConfig.QUERY_MINE_HISTORY_MEETING, history(), handler, HistoryMeetingModel.class);
                    Toastor.showToast(getActivity(), msg.obj.toString());
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.what == 1002) {
                final int index = (int) msg.obj;
                new AlertDialog(getActivity()).builder().setCancelable(false).setTitle("温馨提示").setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpHelper.postStringBack(HTTP_DELETE_HISTORY_MEETING, AppConfig.DELETE_MINE_HISTORY_MEETING, delParames(listMeeting.get(index).getMeetingId()), handler, BaseModel.class);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setMsg("是否确认删除此会议？").show();
            }
        }
    };

    private HashMap<String, String> delParames(String meetingId) {
        HashMap<String, String> del = new HashMap<String, String>();
        del.put("token", TelephoneUtil.getIMEI(getActivity()));
        del.put("meetingId", meetingId);
        return del;
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_history_metting;
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

    }

    @Override
    public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        httpHelper = MyHttpHelper.getInstance(getActivity());
        pullToRefreshScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_QUERY_HISTORY_MEETING, AppConfig.QUERY_MINE_HISTORY_MEETING, history(), handler, HistoryMeetingModel.class);
    }

    private HashMap<String, String> history() {
        HashMap<String, String> history = new HashMap<String, String>();
        history.put("token", TelephoneUtil.getIMEI(getActivity()));
        history.put("pageNo", "1");
        history.put("pageSize", "10000");
        return history;
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
        startActivity(new Intent(getActivity(), MeetingDetailsActivity.class).putExtra("is_show_bottom", "show").putExtra("meeting_id", listMeeting.get(position).getMeetingId()));
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
            pullToRefreshScrollView.onRefreshComplete();
        }
    }
}

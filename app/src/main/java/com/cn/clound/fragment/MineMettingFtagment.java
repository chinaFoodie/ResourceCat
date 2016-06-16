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
import android.widget.ImageView;
import android.widget.ScrollView;

import com.cn.clound.R;
import com.cn.clound.activity.EnterMeetingActivity;
import com.cn.clound.activity.MeetingDetailsActivity;
import com.cn.clound.adapter.MineMettingRecyclerAdapter;
import com.cn.clound.interfaces.OnItemClickListener;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.application.MyApplication;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.bean.metting.EnterStadiumModel;
import com.cn.clound.bean.metting.MyMettingModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshBase;
import com.cn.clound.view.refreshlinearlayout.PullToRefreshScrollView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.ExtendedChatModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 我的会议界面
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-23 16:14:22
 */
public class MineMettingFtagment extends BaseFragment implements View.OnClickListener, OnItemClickListener {
    @Bind(R.id.ptrScrollView_home)
    PullToRefreshScrollView mPtrScrollView;
    @Bind(R.id.recycler_mine_metting)
    RecyclerView recyclerview;
    @Bind(R.id.img_enter_meeting)
    ImageView imgEnterMeeting;

    private MineMettingRecyclerAdapter adapter;
    private int HTTP_GET_MINE_MEETING = 142;
    private int HTTP_ENTER_METTING = 145;
    private MyHttpHelper httpHelper;
    private CustomProgress progress;
    private boolean IS_NEED_REFRESH = false;
    private List<MyMettingModel.MeetingData.MineMetting> lsitMeeting = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_GET_MINE_MEETING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    MyMettingModel mmm = (MyMettingModel) msg.obj;
                    if (mmm != null) {
                        if (mmm.getDate().getResult() != null && mmm.getDate().getResult().size() > 0) {
                            lsitMeeting = mmm.getDate().getResult();
                            adapter = new MineMettingRecyclerAdapter(getActivity(), lsitMeeting);
                            recyclerview.setAdapter(adapter);
                            adapter.setOnItemClickLitener(MineMettingFtagment.this);
                        } else {
                            Toastor.showToast(getActivity(), "暂无数据");
                        }
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            } else if (msg.arg1 == HTTP_ENTER_METTING) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    EnterStadiumModel esm = (EnterStadiumModel) msg.obj;
                    Intent enter = new Intent(getActivity(), EnterMeetingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("enter_stadium_model", esm);
                    enter.putExtras(bundle);
                    if (esm.getData().getState().equals("1")) {
                        EMMessage message = EMMessage.createTxtSendMessage(MyApplication.getInstance().getUm().getData().getUserInfo().getName() + "加入了会议", esm.getData().getGroupId());
                        message.setChatType(EMMessage.ChatType.GroupChat);
                        //自定义属性
                        ExtendedChatModel exm = new ExtendedChatModel();
                        exm.setSessionName(esm.getData().getGroupId());
                        exm.setMsgType("MeetState");
                        exm.setToUserNo(esm.getData().getGroupId());
                        exm.setToUserAvatar("");
                        exm.setToUSerNick(esm.getData().getGroupId());
                        exm.setFromUserAvatar(MyApplication.getInstance().getUm().getData().getUserInfo().getHead());
                        exm.setFromUserNick(MyApplication.getInstance().getUm().getData().getUserInfo().getName());
                        exm.setFromUserNo(MyApplication.getInstance().getUm().getData().getUserInfo().getUserNo());
                        message.setAttribute("extended_msg_json", GsonTools.obj2json(exm));
                        EMClient.getInstance().chatManager().sendMessage(message);
                    }
                    startActivity(enter);
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_mine_metting;
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
        progress = new CustomProgress(getActivity(), "请稍候...");
        progress.show();
        mPtrScrollView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
                    public void onRefresh(
                            PullToRefreshBase<ScrollView> refreshView) {
                        new GetDataTask().execute();
                    }
                });
        httpHelper = MyHttpHelper.getInstance(getActivity());
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        imgEnterMeeting.setOnClickListener(this);
        httpHelper.postStringBack(HTTP_GET_MINE_MEETING, AppConfig.GET_MINE_MEETING, getParames(), handler, MyMettingModel.class);
    }

    private HashMap<String, String> getParames() {
        HashMap<String, String> parames = new HashMap<String, String>();
        parames.put("pageNo", "1");
        parames.put("pageSize", "10000");
        parames.put("token", TelephoneUtil.getIMEI(getActivity()));
        return parames;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_enter_meeting:
                progress.show();
                httpHelper.postStringBack(HTTP_ENTER_METTING, AppConfig.ENTER_MEETING, enterParmars(), handler, EnterStadiumModel.class);
                break;
        }
    }

    private HashMap<String, String> enterParmars() {
        HashMap<String, String> enter = new HashMap<String, String>();
        enter.put("token", TelephoneUtil.getIMEI(getActivity()));
        return enter;
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getActivity(), MeetingDetailsActivity.class).putExtra("is_show_bottom", "dontshow").putExtra("meeting_id", lsitMeeting.get(position).getMeetingId()));
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

    @Override
    public void onFragmentStarted(Fragment fragment) {

    }

    @Override
    public void onFragmentResumed(Fragment fragment) {
        if (IS_NEED_REFRESH) {
            httpHelper.postStringBack(HTTP_GET_MINE_MEETING, AppConfig.GET_MINE_MEETING, getParames(), handler, MyMettingModel.class);
        }
    }

    @Override
    public void onFragmentPaused(Fragment fragment) {

    }

    @Override
    public void onFragmentStopped(Fragment fragment) {
        IS_NEED_REFRESH = true;
    }

    @Override
    public void onFragmentDestroyed(Fragment fragment) {
        IS_NEED_REFRESH = false;
    }

    @Override
    public void onFragmentDetach(Fragment fragment) {

    }

    @Override
    public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {

    }
}

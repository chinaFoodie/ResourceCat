package com.cn.clound.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cn.clound.R;
import com.cn.clound.adapter.CalendarAdapter;
import com.cn.clound.adapter.SignRecordRecyclerAdapter;
import com.cn.clound.appconfig.AppConfig;
import com.cn.clound.base.BaseFragment;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.TelephoneUtil;
import com.cn.clound.base.view.StickyLayout;
import com.cn.clound.bean.singed.UserSingedModel;
import com.cn.clound.http.MyHttpHelper;
import com.cn.clound.view.CustomProgress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

/**
 * 我的签到界面
 *
 * @author ChunfaLee(ly09219@gamil.com)
 * @date 2016年5月13日 14:37:57
 */
public class MineSignFragment extends BaseFragment implements View.OnClickListener, StickyLayout.OnGiveUpTouchEventListener {
    @Bind(R.id.calendar_flipper)
    ViewFlipper flipper;
    @Bind(R.id.tv_mine_sign_month)
    TextView currentMonth;
    @Bind(R.id.rl_prev_month)
    RelativeLayout prevMonth;
    @Bind(R.id.rl_next_month)
    RelativeLayout nextMonth;
    @Bind(R.id.recycler_sign_record)
    RecyclerView recyclerView;
    @Bind(R.id.sticky_layout)
    StickyLayout stickyLayout;
    @Bind(R.id.tv_current_date)
    TextView tvCurrentDate;
    @Bind(R.id.tv_current_week)
    TextView tvCurrentWeek;
    private GridView gridView = null;
    private int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private int jumpYear = 0;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private String currentWeek = "";
    private CalendarAdapter calV = null;
    private GestureDetector gestureDetector = null;
    private CustomProgress progress;
    private MyHttpHelper httpHelper;
    private int HTTP_QUERY_USER_SIGNIN_LIST = 134;
    private SignRecordRecyclerAdapter adapter;
    private List<String> listClick = new ArrayList<String>();
    private String chooseDate = "";
    /**
     * 每次添加gridview到viewflipper中时给的标记
     */
    private int gvFlag = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == HTTP_QUERY_USER_SIGNIN_LIST) {
                if (msg.what == Integer.parseInt(AppConfig.SUCCESS)) {
                    UserSingedModel usm = (UserSingedModel) msg.obj;
                    if (usm != null && usm.getData().getResult().size() > 0) {
                        adapter = new SignRecordRecyclerAdapter(getActivity(), usm.getData().getResult());
                        recyclerView.setAdapter(adapter);
                    } else {
                        Toastor.showToast(getActivity(), "未查询到记录");
                        adapter = new SignRecordRecyclerAdapter(getActivity(), null);
                        recyclerView.setAdapter(adapter);
                    }
                } else {
                    Toastor.showToast(getActivity(), msg.obj.toString());
                }
            }
            if (progress != null && progress.isShowing()) {
                progress.dismiss();
            }
        }
    };

    public MineSignFragment() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
    }

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_fragment_mine_sign;
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
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);
        gestureDetector = new GestureDetector(getContext(), new MyGestureListener());
        flipper.removeAllViews();
        calV = new CalendarAdapter(getActivity(), getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        addGridView();
        gridView.setAdapter(calV);
        flipper.addView(gridView, 0);
        addTextToTopTextView(currentMonth);
        progress = new CustomProgress(getActivity(), "");
        progress.show();
        initDate(currentDate);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        httpHelper.postStringBack(HTTP_QUERY_USER_SIGNIN_LIST, AppConfig.QUERY_USER_SIGNIN_LIST, signin(DateUtil.getCurDateStr("yyyy-MM-dd")), handler, UserSingedModel.class);
        stickyLayout.setOnGiveUpTouchEventListener(this);
    }

    private HashMap<String, String> signin(String date) {
        HashMap<String, String> sigin = new HashMap<String, String>();
        sigin.put("pageNo", "1");
        sigin.put("pageSize", "10000");
        sigin.put("queryDate", date);
        sigin.put("token", TelephoneUtil.getIMEI(getActivity()));
        return sigin;
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
            case R.id.rl_prev_month:
                enterPrevMonth(gvFlag);
                break;
            case R.id.rl_next_month:
                enterNextMonth(gvFlag);
                break;
        }
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop() == 0 && lm.findFirstVisibleItemPosition() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
            if (e1.getX() - e2.getX() > 120) {
                // 像左滑动
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                // 向右滑动
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月

        calV = new CalendarAdapter(getActivity(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        gridView.setAdapter(calV);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_slide_right_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_slide_left_exit));
        flipper.showNext();
        flipper.removeViewAt(0);
    }

    /**
     * 移动到上一个月
     *
     * @param gvFlag
     */
    private void enterPrevMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth--; // 上一个月

        calV = new CalendarAdapter(getActivity(), this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        gridView.setAdapter(calV);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_slide_left_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fragment_slide_right_exit));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    /**
     * 添加头部的年份 闰哪月等信息
     *
     * @param view
     */
    public void addTextToTopTextView(TextView view) {
        StringBuffer textDate = new StringBuffer();
        // draw = getResources().getDrawable(R.drawable.top_day);
        // view.setBackgroundDrawable(draw);
        textDate.append(calV.getShowYear()).append("年").append(calV.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = getActivity().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(getActivity());
        gridView.setNumColumns(7);
        gridView.setColumnWidth(40);
        // gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        if (Width == 720 && Height == 1280) {
            gridView.setColumnWidth(40);
        }
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        // 去除gridView边框
        gridView.setVerticalSpacing(0);
        gridView.setHorizontalSpacing(0);
        gridView.setOnTouchListener(new View.OnTouchListener() {
            // 将gridview中的触摸事件回传给gestureDetector

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return MineSignFragment.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = calV.getStartPositon();
                int endPosition = calV.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = calV.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = calV.getShowYear();
                    String scheduleMonth = calV.getShowMonth();
                    if (listClick != null && listClick.size() > 0) {
                        listClick.removeAll(listClick);
                    }
                    if (scheduleMonth.length() == 1) {
                        scheduleMonth = "0" + scheduleMonth;
                    }
                    if (scheduleDay.length() == 1) {
                        scheduleDay = "0" + scheduleDay;
                    }
                    chooseDate = scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;
                    if (chooseDate.compareTo(currentDate) <= 0) {
                        listClick.add(chooseDate);
                        initDate(chooseDate);
                        httpHelper.postStringBack(HTTP_QUERY_USER_SIGNIN_LIST, AppConfig.QUERY_USER_SIGNIN_LIST, signin(chooseDate), handler, UserSingedModel.class);
                        calV.refreshAdapter(listClick);
                    } else {
                        Toastor.showLongToast(getActivity(), "选择时间超前，不能查看");
                    }
                }
            }
        });
        gridView.setLayoutParams(params);
    }
}

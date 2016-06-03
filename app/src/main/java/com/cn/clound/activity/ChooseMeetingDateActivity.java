package com.cn.clound.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cn.clound.R;
import com.cn.clound.adapter.CalendarMeetingAdapter;
import com.cn.clound.adapter.MeetingTimeExpandAdapter;
import com.cn.clound.base.BaseActivity;
import com.cn.clound.base.common.assist.Toastor;
import com.cn.clound.base.view.StickyLayout;
import com.cn.clound.bean.metting.MeetingTimeExpandModel;
import com.cn.clound.bean.metting.MeetingTimeModel;
import com.cn.clound.view.dialog.TimePickerDialog;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * 选择会议日期
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-24 18:48:47
 */
public class ChooseMeetingDateActivity extends BaseActivity implements View.OnClickListener, StickyLayout.OnGiveUpTouchEventListener {
    @Bind(R.id.tv_base_title)
    TextView tvMidTitle;
    @Bind(R.id.ll_base_left)
    LinearLayout llleft;
    @Bind(R.id.tv_base_left)
    TextView tvBaseLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.tv_meetings_month)
    TextView currentMonth;
    @Bind(R.id.calendar_flipper)
    ViewFlipper flipper;
    @Bind(R.id.rl_prev_month)
    RelativeLayout prevMonth;
    @Bind(R.id.rl_next_month)
    RelativeLayout nextMonth;
    @Bind(R.id.sticky_meeting_time_layout)
    StickyLayout stickyLayout;
    @Bind(R.id.expand_listview)
    ExpandableListView expandableListView;
    private CalendarMeetingAdapter adapter = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private String currentDate = "";
    private GridView gridView = null;
    private int jumpMonth = 0;
    private int jumpYear = 0;
    private List<String> listClick = new ArrayList<String>();
    private int gvFlag = 0;
    private String chooseDate = "";
    private MeetingTimeExpandAdapter expandAdapter;
    private List<MeetingTimeExpandModel> listExpand = new ArrayList<MeetingTimeExpandModel>();
    private List<MeetingTimeModel> listPreData = new ArrayList<MeetingTimeModel>();
    private TimePickerDialog ep;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<MeetingTimeExpandModel.MeetingTime> temp = new ArrayList<>();
            if (msg.what == 101) {
                temp.removeAll(temp);
                int groupPosition = (int) msg.obj;
                MeetingTimeExpandModel.MeetingTime met = new MeetingTimeExpandModel().new MeetingTime();
                met.setDate(listExpand.get(groupPosition).getMeetingDate());
                met.setTime("00:00-00:00");
                met.setUpdate("删除");
                temp.add(met);
                if (listExpand.get(groupPosition).getTimeList() != null) {
                    listExpand.get(groupPosition).getTimeList().add(met);
                } else {
                    listExpand.get(groupPosition).setTimeList(temp);
                }
                expandAdapter.notifyDataSetChanged();
            } else if (msg.what == 102) {
                int groupsition = Integer.parseInt(msg.obj.toString()) / 1000;
                int childposition = Integer.parseInt(msg.obj.toString()) % 1000;
                listExpand.get(groupsition).getTimeList().remove(childposition);
                expandAdapter.notifyDataSetChanged();
            } else if (msg.what == 103) {
                expandAdapter.notifyDataSetChanged();
            } else if (msg.what == 104) {
                adapter.refreshAdapter(listClick);
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.dt_activity_choose_meeting_date;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        listPreData = (List<MeetingTimeModel>) getIntent().getSerializableExtra("update_meeting_list");
        tvMidTitle.setText("设置会议时间");
        llleft.setVisibility(View.VISIBLE);
        llleft.setOnClickListener(this);
        tvBaseLeft.setText("取消");
        tvBaseRight.setText("确定");
        tvBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setOnClickListener(this);
        prevMonth.setOnClickListener(this);
        nextMonth.setOnClickListener(this);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentDate = sdf.format(date); // 当期日期
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        gestureDetector = new GestureDetector(this, new MyGestureListener());
        flipper.removeAllViews();
        adapter = new CalendarMeetingAdapter(this, getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        addGridView();
        addTextToTopTextView(currentMonth);
        gridView.setAdapter(adapter);
        flipper.addView(gridView, 0);
        stickyLayout.setOnGiveUpTouchEventListener(this);
        expandAdapter = new MeetingTimeExpandAdapter(this, listExpand, handler, expandableListView);
        expandableListView.setAdapter(expandAdapter);
        expandableListView.setGroupIndicator(null);
        ep = new TimePickerDialog(ChooseMeetingDateActivity.this, R.style.ActionSheetDialogStyle);
        if (listPreData != null) {
            String predate = "";
            MeetingTimeExpandModel mtem = null;
            List<MeetingTimeExpandModel.MeetingTime> listmt = null;
            for (MeetingTimeModel mtm : listPreData) {
                if (!predate.equals(mtm.getMeetingDate())) {
                    mtem = new MeetingTimeExpandModel();
                    listmt = new ArrayList<>();//
                    listClick.add(mtm.getMeetingDate());
                }
                if (mtm.getMeetingUpdate().equals("添加")) {
                    mtem.setMeetingUpdate(mtm.getMeetingUpdate());
                    mtem.setMeetingTime(mtm.getMeetingTime());
                    mtem.setMeetingDate(mtm.getMeetingDate());
                } else {
                    MeetingTimeExpandModel.MeetingTime mt = new MeetingTimeExpandModel().new MeetingTime();
                    mt.setUpdate(mtm.getMeetingUpdate());
                    mt.setTime(mtm.getMeetingTime());
                    mt.setDate(mtm.getMeetingDate());
                    listmt.add(mt);
                    mtem.setTimeList(listmt);
                }
                if (!predate.equals(mtm.getMeetingDate())) {
                    listExpand.add(mtem);
                }
                predate = mtm.getMeetingDate();
                handler.sendEmptyMessage(104);
            }
        }
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 取得屏幕的宽度和高度
        WindowManager windowManager = this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int Width = display.getWidth();
        int Height = display.getHeight();

        gridView = new GridView(this);
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
                return ChooseMeetingDateActivity.this.gestureDetector.onTouchEvent(event);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // TODO Auto-generated method stub
                // 点击任何一个item，得到这个item的日期(排除点击的是周日到周六(点击不响应))
                int startPosition = adapter.getStartPositon();
                int endPosition = adapter.getEndPosition();
                if (startPosition <= position + 7 && position <= endPosition - 7) {
                    String scheduleDay = adapter.getDateByClickItem(position).split("\\.")[0]; // 这一天的阳历
                    // String scheduleLunarDay =
                    // calV.getDateByClickItem(position).split("\\.")[1];
                    // //这一天的阴历
                    String scheduleYear = adapter.getShowYear();
                    String scheduleMonth = adapter.getShowMonth();
                    if (scheduleMonth.length() == 1) {
                        scheduleMonth = "0" + scheduleMonth;
                    }
                    if (scheduleDay.length() == 1) {
                        scheduleDay = "0" + scheduleDay;
                    }
                    chooseDate = scheduleYear + "-" + scheduleMonth + "-" + scheduleDay;
                    if (chooseDate.compareTo(currentDate) >= 0) {
                        if (!listClick.contains(chooseDate)) {
                            listClick.add(chooseDate);
                            MeetingTimeExpandModel mtem = new MeetingTimeExpandModel();
                            mtem.setMeetingUpdate("添加");
                            mtem.setMeetingTime("00:00-00:00");
                            mtem.setMeetingDate(chooseDate);//.substring(chooseDate.indexOf("-") + 1)
                            listExpand.add(mtem);
                        } else {
                            MeetingTimeExpandModel del = null;
                            for (MeetingTimeExpandModel mte : listExpand) {
                                if (mte.getMeetingDate().equals(chooseDate)) {//.substring(chooseDate.indexOf("-") + 1)
                                    del = mte;
                                }
                            }
                            listExpand.remove(del);
                            listClick.remove(chooseDate);
                        }
                        expandAdapter.notifyDataSetChanged();
                        adapter.refreshAdapter(listClick);
                    } else {
                        Toastor.showLongToast(ChooseMeetingDateActivity.this, "选择时间已过，请重新选择！");
                    }
                }
            }
        });
        gridView.setLayoutParams(params);
        /**点击二级列表groupItem**/
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, final int groupPosition, long id) {
                ep.show();
                ep.setOnDateChangerListener(new TimePickerDialog.DateChangerListener() {
                    @Override
                    public void currentDate(String startTime, String endTime) {
                        ep.dismiss();
                        listExpand.get(groupPosition).setMeetingTime(startTime + "-" + endTime);
                        handler.sendEmptyMessage(103);
                    }
                });
                return true;
            }
        });
        /**点击二级列表childItem**/
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, final int groupPosition, final int childPosition, long id) {
                ep.show();
                ep.setOnDateChangerListener(new TimePickerDialog.DateChangerListener() {
                    @Override
                    public void currentDate(String startTime, String endTime) {
                        ep.dismiss();
                        listExpand.get(groupPosition).getTimeList().get(childPosition).setTime(startTime + "-" + endTime);
                        handler.sendEmptyMessage(103);
                    }
                });
                return true;
            }
        });
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
        textDate.append(adapter.getShowYear()).append("年").append(adapter.getShowMonth()).append("月").append("\t");
        view.setText(textDate);
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

    /**
     * 移动到下一个月
     *
     * @param gvFlag
     */
    private void enterNextMonth(int gvFlag) {
        addGridView(); // 添加一个gridView
        jumpMonth++; // 下一个月
        adapter = new CalendarMeetingAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        gridView.setAdapter(adapter);
        adapter.refreshAdapter(listClick);
        addTextToTopTextView(currentMonth); // 移动到下一月后，将当月显示在头标题中
        gvFlag++;
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fragment_slide_right_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fragment_slide_left_exit));
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
        adapter = new CalendarMeetingAdapter(this, this.getResources(), jumpMonth, jumpYear, year_c, month_c, day_c, listClick);
        gridView.setAdapter(adapter);
        adapter.refreshAdapter(listClick);
        gvFlag++;
        addTextToTopTextView(currentMonth); // 移动到上一月后，将当月显示在头标题中
        flipper.addView(gridView, gvFlag);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.fragment_slide_left_enter));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.fragment_slide_right_exit));
        flipper.showPrevious();
        flipper.removeViewAt(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_base_left:
                this.finish();
                break;
            case R.id.rl_prev_month:
                enterPrevMonth(gvFlag);
                break;
            case R.id.rl_next_month:
                enterNextMonth(gvFlag);
                break;
            case R.id.tv_base_right:
                Intent back = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("meeting_time_list", (Serializable) listExpand);
                back.putExtras(bundle);
                this.setResult(1005, back);
                this.finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {
        if (expandableListView.getFirstVisiblePosition() == (expandableListView.getCount() - 1)) {
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
                enterNextMonth(gvFlag);
                return true;
            } else if (e1.getX() - e2.getX() < -120) {
                enterPrevMonth(gvFlag);
                return true;
            }
            return false;
        }
    }
}

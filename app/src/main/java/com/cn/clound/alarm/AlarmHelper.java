package com.cn.clound.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.cn.clound.base.common.time.DateUtil;
import com.cn.clound.base.common.utils.GsonTools;
import com.cn.clound.base.common.utils.SharePreferceUtil;
import com.cn.clound.bean.singed.QuerySignModel;
import com.cn.clound.broadcastreceiver.AlarmReceiver;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 闹钟管理工具
 *
 * @author ChunfaLee(ly09219@gmail.com)
 * @date 2016-5-18 09:48:17
 */
public class AlarmHelper {
    public static AlarmHelper instance;
    public static SharePreferceUtil share;

    public static AlarmHelper getInstance() {
        if (instance == null) {
            instance = new AlarmHelper();
        }
        return instance;
    }

    public static void setAlarmTime(Context context) {
        boolean needCreate = true;
        share = SharePreferceUtil.getInstance(context);
        String currentTime = DateUtil.getCurDateStr("HH:mm");
        String temp = share.getString("my_sign_tip_list");
        QuerySignModel qsm = GsonTools.getPerson(temp, QuerySignModel.class);
        Comparator comp = new ComparatorImpl();
        if (qsm != null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (qsm.getData().size() > 0) {
                Collections.sort(qsm.getData(), comp);
                for (QuerySignModel.QuerySign qs : qsm.getData()) {
                    if (needCreate && qs.getState().equals("1") && compareDatewithWeek(qs.getRepeatStr()) && currentTime.compareTo(qs.getRemindTime()) < 0) {
                        needCreate = false;
                        String tempTime = qs.getRemindTime();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tempTime.substring(0, tempTime.indexOf(":"))));
                        calendar.set(Calendar.MINUTE, Integer.parseInt(tempTime.substring(tempTime.indexOf(":") + 1)));
                        calendar.set(Calendar.SECOND, 0);
                        calendar.set(Calendar.MILLISECOND, 0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
                    }
                    if (needCreate) {
                        am.cancel(sender);
                    }
                }
            }
            needCreate = true;
        }
    }

    public static void cancelAlarm() {
    }

    public static boolean compareDatewithWeek(String str) {
        boolean lcf = false;
        if (str.substring(str.length() - 1).equals(",")) {
            str = str.substring(str.length() - 1);
        } else {
            str = str.replace("1", "星期一").replace("2", "星期二").replace("3", "星期三")
                    .replace("4", "星期四").replace("5", "星期五").replace("6", "星期六").replace("7", "星期日");
        }
        String[] temp = str.split(",");
        String currentWeek = DateUtil.getWeekOfDate(new Date());
        if (Arrays.asList(temp).contains(currentWeek)) {
            lcf = true;
        }
        return lcf;
    }

    static class ComparatorImpl implements Comparator<QuerySignModel.QuerySign> {
        @Override
        public int compare(QuerySignModel.QuerySign lhs, QuerySignModel.QuerySign rhs) {
            String time1 = lhs.getRemindTime();
            String time2 = rhs.getRemindTime();
            if (time1.compareTo(time2) > 0) {
                return 1;
            } else if (time1.compareTo(time2) < 0) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public static String getNumber(String re) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(re);
        String aaa = m.replaceAll("").trim();
        return aaa.substring(aaa.length() - 5);
    }
}

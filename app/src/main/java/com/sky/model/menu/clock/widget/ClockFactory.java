package com.sky.model.menu.clock.widget;

import android.util.Log;

import java.util.Calendar;

/**
 * Created by Administrator on 17-8-7.
 */
public class ClockFactory {

    public static String decodeDay(int daynum) {
        if (daynum == 0xfe) {
            return "每天";
        }
        Log.i("tag", "daynum:" + daynum);
        String[] dayAry = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日", "响铃一次"};
        String days = "";
        int temp = dayAry.length - 1;
        for (int i = 0; i < dayAry.length; i++) {
            if (((daynum >> temp) & 1) == 1) {
                days += dayAry[i] + " ";
            }
            temp--;
        }
        return days;
    }

    public long getTriggerTime(Calendar triggerTime, int daynum) {
        Calendar calendar = (Calendar) triggerTime.clone();
        //判断是否为单次
        if (daynum == 0x01) {
            return -1;
        }
        Calendar now = Calendar.getInstance();
        if (calendar.before(now)) {
            calendar.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        }
        if (calendar.before(now)) {
            int n = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (n == 0) {
                n = 7;
            }

            int i = 0;
            while (true) {
                i++;
                if (((daynum >> (8 - n + i)) & 1) == 1) {
                    break;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, i);
        }
        return calendar.getTimeInMillis();
    }
}

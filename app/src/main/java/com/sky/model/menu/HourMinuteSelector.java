package com.sky.model.menu;

import android.app.Activity;

import com.sky.constantcalendar.R;
import com.sky.plug.wheel.adapters.WheelTextAdapter;
import com.sky.plug.wheel.views.WheelView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-8-3.
 */
public class HourMinuteSelector {

    private Activity activity;

    DecimalFormat df = new DecimalFormat("00");
    List<String> hours = new ArrayList<>();
    List<String> minutes = new ArrayList<>();

    private WheelView wvHour;
    private WheelView wvMinute;

    private WheelTextAdapter mHourAdapter;
    private WheelTextAdapter mMinuteAdapter;

    //选中的年月日下表
    private int selectYearIndex, selectMonthIndex, selectDayIndex;

    //最大显示文字
    private static final int MAXTEXTSIZE = 40;
    //最小显示文字
    private static final int MINTEXTSIZE = 20;
    private static final int VISIBLEITEMS = 3;

    public HourMinuteSelector(Activity activity){
        this.activity = activity;
    }

    public void init() {

        //初始化年、月、日三个选择器
        wvHour = (WheelView) activity.findViewById(R.id.hourView);
        setHour();

        wvMinute = (WheelView) activity.findViewById(R.id.minuteView);
        setMinute();
    }
    public void setHour() {
        initHours();
        mHourAdapter = new WheelTextAdapter(activity, hours);
        mHourAdapter.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        mHourAdapter.setVisibleItems(VISIBLEITEMS);
        mHourAdapter.setCurrentIndex(0);

        wvHour.setVisibleItems(VISIBLEITEMS);
        wvHour.setCyclic(true);
        wvHour.setStyle(MAXTEXTSIZE, MINTEXTSIZE);
        wvHour.setViewAdapter(mHourAdapter);
        wvHour.setCurrentItem(selectYearIndex);
    }

    private void setMinute() {
        initMinutes();
        mMinuteAdapter = new WheelTextAdapter(activity, minutes);
        mMinuteAdapter.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        mMinuteAdapter.setVisibleItems(VISIBLEITEMS);
        mMinuteAdapter.setCurrentIndex(0);

        wvMinute.setVisibleItems(VISIBLEITEMS);
        wvMinute.setCyclic(true);
        wvMinute.setStyle(MAXTEXTSIZE, MINTEXTSIZE);
        wvMinute.setViewAdapter(mMinuteAdapter);
        wvMinute.setCurrentItem(0);
    }

    //初始化小时
    private void initHours() {
        for (int h = 0; h <= 23; h++) {
            hours.add(df.format(h));
        }
    }

    //初始化分钟
    private void initMinutes() {
        for (int m = 0; m <= 59; m++) {
            minutes.add(df.format(m));
        }
    }

}

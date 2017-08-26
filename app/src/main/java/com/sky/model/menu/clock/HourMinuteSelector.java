package com.sky.model.menu.clock;

import android.app.Activity;

import com.sky.constantcalendar.R;
import com.sky.plug.wheel.views.OnWheelChangedListener;
import com.sky.plug.wheel.views.WheelView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-8-3.
 */
public class HourMinuteSelector {

    private static final int ITEM_HEIGHT = 150;
    private Activity activity;

    DecimalFormat df = new DecimalFormat("00");
    List<String> hours = new ArrayList<>();
    List<String> minutes = new ArrayList<>();

    private WheelView wvHour;
    private WheelView wvMinute;

    //选中的时分
    private int selectHourIndex, selectMinuteIndex;

    //最大显示文字
    private static final int MAXTEXTSIZE = 40;
    //最小显示文字
    private static final int MINTEXTSIZE = 20;
    private static final int VISIBLEITEMS = 3;

    public HourMinuteSelector(Activity activity) {
        this.activity = activity;
    }

    public void init(int h, int m) {
        selectHourIndex = h;
        selectMinuteIndex = m;

        //初始化年、月、日三个选择器
        wvHour = (WheelView) activity.findViewById(R.id.hourView);
        setHour();
        wvHour.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectHourIndex = newValue;
            }
        });

        wvMinute = (WheelView) activity.findViewById(R.id.minuteView);
        setMinute();
        wvMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectMinuteIndex = newValue;
            }
        });
    }

    public void setHour() {
        initHours();
        wvHour.setVisibleItems(VISIBLEITEMS);
        wvHour.setCyclic(true);
        wvHour.setItemHeight(ITEM_HEIGHT);
        wvHour.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        wvHour.loadData(activity, hours, selectHourIndex);
    }

    private void setMinute() {
        initMinutes();
        wvMinute.setVisibleItems(VISIBLEITEMS);
        wvMinute.setCyclic(true);
        wvMinute.setItemHeight(ITEM_HEIGHT);
        wvMinute.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        wvMinute.loadData(activity, minutes, selectMinuteIndex);
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

    public String getTime() {
        return hours.get(selectHourIndex) + ":" + minutes.get(selectMinuteIndex);
    }
}

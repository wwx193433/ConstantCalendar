package com.sky.model.calendar;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.calendar.widget.DayInfo;
import com.sky.model.calendar.widget.LunarCalendar;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;
import com.sky.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 17-7-18.
 * Calendar切换Fragment
 */
public class CalendarFragment extends Fragment {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public static final String ARG_PAGE = "pagenum";
    private LunarCalendar lunarCalendar = new LunarCalendar();
    private int pageNum;
    private List<DayInfo> dates;
    private @ColorInt
    int solarColor, lunarColor,blackColor,specialColor,whiteColor;
    private Resources resources;
    private CalendarUtil calendarUtil;
    Calendar calendar = Calendar.getInstance();
    private Calendar currentCalendar;

    private Activity activity;


    public CalendarFragment() {
        calendarUtil = new CalendarUtil();
    }
    public static Fragment create(int pageNumber) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNum = getArguments().getInt(ARG_PAGE);
        calendar.add(Calendar.MONTH, pageNum - Constant.pagecount / 2);
        activity =getActivity();

        this.dates = lunarCalendar.getDaysOfThisMonth(calendar.getTime());
        resources = activity.getResources();
        blackColor = resources.getColor(R.color.black);
        specialColor = resources.getColor(R.color.special);
        whiteColor = resources.getColor(R.color.white);
        this.currentCalendar = (Calendar) calendar.clone();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        LinearLayout calendarPage = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.calendar_page,parent,false);
        calendarPage.setTag("page" + pageNum);

        for(int i = 0;i<dates.size()/7;i++){
            LinearLayout row = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.calendar_item, parent, false);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.FILL_PARENT, 1.0f);
            row.setLayoutParams(rowParams);
            for(int k = 0;k<7;k++){
                solarColor = blackColor;
                lunarColor = Utility.setAlpha(blackColor, 80);
                final DayInfo dayInfo = dates.get(i * 7 + k);

                //获取阳历日期
                Calendar cal = Calendar.getInstance();
                cal.setTime(dayInfo.getSolarDate());

                //设置阳历天数
                String solarDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                LinearLayout day = (LinearLayout)((LinearLayout) row.getChildAt(k)).getChildAt(0);
                ViewGroup.LayoutParams dayParams = day.getLayoutParams();
                dayParams.width = dayParams.height;
                day.setLayoutParams(dayParams);
                ((TextView)day.getChildAt(0)).setText(solarDay);

                //设置阳历日期
                ((TextView)day.getChildAt(1)).setText(sdf.format(dayInfo.getSolarDate()));

                //设置农历日期
                String lunarText = dayInfo.getSpecailDay()==null?dayInfo.getLunarChinaDay():dayInfo.getSpecailDay();
                ((TextView)day.getChildAt(2)).setText(lunarText);

                //设置周末颜色
                if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
                    solarColor = specialColor;
                }

                //设置特殊节日的颜色
                if(dayInfo.getSpecailDay()!=null){
                    lunarColor = specialColor;
                }

                //设置非当月文字透明度
                if(cal.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)){
                    solarColor = Utility.setAlpha(solarColor, 40);
                    lunarColor = Utility.setAlpha(lunarColor, 40);
                }

                //设置当天的背景色
                if(sdf.format(cal.getTime()).equals(sdf.format(new java.util.Date()))){
                    day.setBackgroundResource(R.drawable.day_selected_special);
                    lunarColor = solarColor = whiteColor;
                }

                ((TextView)day.getChildAt(0)).setTextColor(solarColor);
                ((TextView)day.getChildAt(2)).setTextColor(lunarColor);
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setCalendar(dayInfo.getSolarDate());
                    }
                });

            }
            calendarPage.addView(row);
        }
        return calendarPage;
    }

    public void setCalendar(java.util.Date date){
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewPager);
        int position = calendarUtil.getMonthSpace(date)+Constant.pagecount/2;
        viewPager.setCurrentItem(position, true);

        //设置阳历日期
        TextView solar_text = (TextView) activity.findViewById(R.id.solar_text);
        solar_text.setText(sdf.format(date));

        //设置阴历日期
        TextView lunar_text = (TextView) activity.findViewById(R.id.lunar_text);
        String lunarDate = calendarUtil.getLunarDate(date);
        String week = calendarUtil.getWeekDay(date);
        lunar_text.setText(lunarDate+" "+week);


        //设置天干地支
        TextView dv_solar_date = (TextView) activity.findViewById(R.id.dv_solar_date);
        TextView dv_week = (TextView) activity.findViewById(R.id.dv_week);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String chineseYear = calendarUtil.getChineseYear(calendar.get(Calendar.YEAR));
        dv_solar_date.setText(chineseYear+lunarDate);
        dv_week.setText(week);

        LinearLayout container = (LinearLayout) viewPager.findViewWithTag("page"+position);

        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) container.getChildAt(i);
            for(int j = 0;j<row.getChildCount();j++){
                LinearLayout day = (LinearLayout)((LinearLayout) row.getChildAt(j)).getChildAt(0);
                String solarDate = ((TextView)day.getChildAt(1)).getText().toString();
                if(solarDate.equals(sdf.format(new java.util.Date()))){
                    continue;
                }
                if (solarDate.equals(sdf.format(date))) {
                    day.setBackgroundResource(R.drawable.day_selected_gray);
                } else {
                    day.setBackgroundResource(0);
                }
            }
        }
    }
}

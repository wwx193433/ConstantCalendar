package com.sky.constantcalendar;

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

import com.sky.calendar.DayInfo;
import com.sky.calendar.LunarCalendar;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;
import com.sky.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17-7-18.
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
        LinearLayout calendarPage = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.calendar_page, parent, false);
        calendarPage.setTag("page" + pageNum);

        for(int i = 0;i<dates.size()/7;i++){
            LinearLayout row = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.calendar_item, parent, false);
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
                    solarColor = Utility.setAlpha(solarColor, 60);
                    lunarColor = Utility.setAlpha(lunarColor, 60);
                }

                //设置当天的背景色
                if(sdf.format(cal.getTime()).equals(sdf.format(new Date()))){
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

    public void setCalendar(Date date){
        ViewPager viewPager = (ViewPager) activity.findViewById(R.id.viewPager);
        int position = calendarUtil.getMonthSpace(date)+Constant.pagecount/2;
        viewPager.setCurrentItem(position, true);

        //设置阳历日期
        TextView solar_text = (TextView) activity.findViewById(R.id.solar_text);
        solar_text.setText(sdf.format(date));

        //设置阴历日期
        TextView lunar_text = (TextView) activity.findViewById(R.id.lunar_text);
        String lunarAndWeek = calendarUtil.getLunarAndWeek(date);
        lunar_text.setText(lunarAndWeek);

        LinearLayout container = (LinearLayout) viewPager.findViewWithTag("page"+position);
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) container.getChildAt(i);
            for(int j = 0;j<row.getChildCount();j++){
                LinearLayout day = (LinearLayout)((LinearLayout) row.getChildAt(j)).getChildAt(0);
                String solarDate = ((TextView)day.getChildAt(1)).getText().toString();
                if(solarDate.equals(sdf.format(new Date()))){
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

package com.sky.model.calendar;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sky.constantcalendar.R;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 17-8-2.
 * Calendar 滑动viewpager
 */
public class CalendarViewPager {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy.MM.dd");
    private CalendarUtil calendarUtil;
    private TextView solar_text, lunar_text, dv_solar_date, dv_week;
    private PagerAdapter pagerAdapter;

    private View view;
    private ViewPager viewPager;

    public void setPagerAdapter(PagerAdapter pagerAdapter) {
        this.pagerAdapter = pagerAdapter;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void init(){
        initView();

        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(Constant.pagecount / 2);
        setPageListener(viewPager);
    }

    private void initView() {
        //日历滑动页
        calendarUtil = new CalendarUtil();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        solar_text = (TextView) view.findViewById(R.id.solar_text);
        lunar_text = (TextView) view.findViewById(R.id.lunar_text);
        dv_solar_date = (TextView) view.findViewById(R.id.dv_solar_date);
        dv_week = (TextView) view.findViewById(R.id.dv_week);
    }

    //日历滑动监听
    public void setPageListener(final ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                SimpleDateFormat ymformat = new SimpleDateFormat("yyyy-MM");
                Calendar today = Calendar.getInstance();
                Calendar current = (Calendar) today.clone();
                current.add(Calendar.MONTH, position - Constant.pagecount / 2);
                if (!ymformat.format(today.getTime()).equals(ymformat.format(current.getTime()))) {
                    current.set(Calendar.DAY_OF_MONTH, 1);
                }

                LinearLayout container = (LinearLayout) viewPager.findViewWithTag("page" + position);
                for (int i = 0; i < container.getChildCount(); i++) {
                    LinearLayout row = (LinearLayout) container.getChildAt(i);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        LinearLayout day = (LinearLayout) ((LinearLayout) row.getChildAt(j)).getChildAt(0);
                        String solarDate = ((TextView) day.getChildAt(1)).getText().toString();
                        if (solarDate.equals(sdf.format(new Date()))) {
                            continue;
                        }
                        if (solarDate.equals(sdf.format(current.getTime()))) {
                            day.setBackgroundResource(R.drawable.day_selected_gray);
                        } else {
                            day.setBackgroundResource(0);
                        }
                    }
                }

                solar_text.setText(sdf.format(current.getTime()));
                String lunarDate = calendarUtil.getLunarDate(current.getTime());
                String week = calendarUtil.getWeekDay(current.getTime());
                String chineseYear = calendarUtil.getChineseYear(current.get(Calendar.YEAR));
                lunar_text.setText(lunarDate + " " + week);
                dv_solar_date.setText(chineseYear + lunarDate);
                dv_week.setText(week);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

}

package com.sky.model.calendar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.menu.almanac.Almanac;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.APIUtil;
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
    private IconFontTextview should, avoid;
    private PagerAdapter pagerAdapter;

    private View view;
    private ViewPager viewPager;

    private AlmanacHandler almanacHandler;

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

    class AlmanacHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String yi = bundle.getString("yi");
            String ji = bundle.getString("ji");
            should.setIconText(yi);
            avoid.setIconText(ji);
        }
    }

    private void initView() {
        //日历滑动页
        calendarUtil = new CalendarUtil();
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        solar_text = (TextView) view.findViewById(R.id.solar_text);
        lunar_text = (TextView) view.findViewById(R.id.lunar_text);
        dv_solar_date = (TextView) view.findViewById(R.id.dv_solar_date);
        dv_week = (TextView) view.findViewById(R.id.dv_week);
        should = (IconFontTextview) view.findViewById(R.id.should);
        avoid = (IconFontTextview) view.findViewById(R.id.avoid);
        almanacHandler = new AlmanacHandler();
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

                getAlamanacData(current.getTime());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 获取黄历数据
     * @param date
     */
    private void getAlamanacData(final Date date) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Almanac am = APIUtil.getAlmanac(date);
                if(am==null){
                    return;
                }
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("yi", am.getYi());
                bundle.putString("ji", am.getJi());
                msg.setData(bundle);
                almanacHandler.sendMessage(msg);
            }
        }).start();
    }

}

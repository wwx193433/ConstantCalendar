package com.sky.model.calendar;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.calendar.widget.DatePopupWindow;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 17-8-2.
 * 头部日期选择器
 */
public class CalendarHeadSelector {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Activity activity;
    private LinearLayout headDate;
    private CalendarUtil calendarUtil;
    private TextView solar_text, lunar_text, dv_solar_date, dv_week;
    private View view;

    public void setView(View view) {
        this.view = view;
    }

    public CalendarHeadSelector(Activity activity){
        this.activity = activity;
    }

    public void init(){
        //初始化组件
        initView();

        //设置默认为今天
        setDefaultDate();

        //头部日期选择监听
        headDate = (LinearLayout) view.findViewById(R.id.header_date);
        headDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String solarText = solar_text.getText().toString();
                Date solarDate = new Date();
                try {
                    solarDate = sdf.parse(solarText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePopupWindow pw = new DatePopupWindow(activity, solarDate);

                //出现在布局底端
                pw.showAsDropDown(view);
                backgroundAlpha(0.4f);
                pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });

                pw.onSelectedDate(new DatePopupWindow.DateSelectInterface() {
                    @Override
                    public void onDateSelectedCallBack(Date date) {
                        setCalendar(date);
                    }
                });
            }
        });
    }

    //设置默认日期
    private void setDefaultDate() {
        //设置阳历日期
        solar_text.setText(sdf.format(new Date()));

        //设置阴历日期
        String lunarDate = calendarUtil.getLunarDate(new Date());
        String week = calendarUtil.getWeekDay(new Date());
        lunar_text.setText(lunarDate+" "+week);

        //设置天干地支
        Calendar calendar = Calendar.getInstance();
        String chineseYear = calendarUtil.getChineseYear(calendar.get(Calendar.YEAR));
        dv_solar_date.setText(chineseYear+lunarDate);
        dv_week.setText(week);
    }

    //设置页面不透明度
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = this.activity.getWindow().getAttributes();
        lp.alpha = f;
        this.activity.getWindow().setAttributes(lp);
    }

    //选择后设置日期
    public void setCalendar(Date date) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        int position = calendarUtil.getMonthSpace(date) + Constant.pagecount / 2;
        viewPager.setCurrentItem(position, true);

        //设置阳历日期
        solar_text.setText(sdf.format(date));

        //设置阴历日期
        String lunarDate = calendarUtil.getLunarDate(date);
        String week = calendarUtil.getWeekDay(date);
        lunar_text.setText(lunarDate+" "+week);

        //设置天干地支
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String chineseYear = calendarUtil.getChineseYear(calendar.get(Calendar.YEAR));
        dv_solar_date.setText(chineseYear+lunarDate);
        dv_week.setText(week);


        LinearLayout container = (LinearLayout) viewPager.findViewWithTag("page" + position);
        for (int i = 0; i < container.getChildCount(); i++) {
            LinearLayout row = (LinearLayout) container.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                LinearLayout day = (LinearLayout) ((LinearLayout) row.getChildAt(j)).getChildAt(0);
                String solarDate = ((TextView) day.getChildAt(1)).getText().toString();
                if (solarDate.equals(sdf.format(new Date()))) {
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

    //初始化组件
    private void initView() {
        //日历滑动页
        calendarUtil = new CalendarUtil();
        solar_text = (TextView) view.findViewById(R.id.solar_text);
        lunar_text = (TextView) view.findViewById(R.id.lunar_text);
        dv_solar_date = (TextView) view.findViewById(R.id.dv_solar_date);
        dv_week = (TextView) view.findViewById(R.id.dv_week);
    }
}

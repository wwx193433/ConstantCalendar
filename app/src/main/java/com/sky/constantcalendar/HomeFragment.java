package com.sky.constantcalendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.adapter.CalendarPagerAdapter;
import com.sky.calendar.DatePopupWindow;
import com.sky.calendar.LunarCalendar;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy.MM.dd");
    private ViewPager viewPager;
    private CalendarPagerAdapter calendarPagerAdapter;
    private LunarCalendar lunarCalendar;
    private CalendarUtil calendarUtil;
    private TextView solar_text, lunar_text, dv_solar_date, dv_week;
    private LinearLayout layout_date;
    private View homeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        homeView = view;

        //初始化组件
        init(view);

        //创建日期表格
        calendarPagerAdapter = new CalendarPagerAdapter(getFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);
        viewPager.setCurrentItem(Constant.pagecount / 2);
        setPageListener(viewPager);
        return view;
    }

    /**
     * 初始化组件
     */
    private void init(View v) {
        lunarCalendar = new LunarCalendar();
        calendarUtil = new CalendarUtil();

        viewPager = (ViewPager) v.findViewById(R.id.viewPager);

        solar_text = (TextView) v.findViewById(R.id.solar_text);
        lunar_text = (TextView) v.findViewById(R.id.lunar_text);
        dv_solar_date = (TextView) v.findViewById(R.id.dv_solar_date);
        dv_week = (TextView) v.findViewById(R.id.dv_week);

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

        layout_date = (LinearLayout) v.findViewById(R.id.layout_date);
        layout_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String solarText = solar_text.getText().toString();
                Date solarDate = new Date();
                try {
                    solarDate = ymdformat.parse(solarText);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePopupWindow pw = new DatePopupWindow(getActivity(), solarDate);

                //出现在布局底端
                pw.showAsDropDown(homeView);
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

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = f;
        getActivity().getWindow().setAttributes(lp);
    }

    public void setCalendar(Date date) {
        ViewPager viewPager = (ViewPager) homeView.findViewById(R.id.viewPager);
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

    /**
     * 菜单定义
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.note:
                return true;
            default:
                return false;
        }
    }
}



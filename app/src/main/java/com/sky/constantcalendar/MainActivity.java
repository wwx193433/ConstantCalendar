package com.sky.constantcalendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.adapter.CalendarPagerAdapter;
import com.sky.calendar.DatePopupWindow;
import com.sky.calendar.LunarCalendar;
import com.sky.util.CalendarUtil;
import com.sky.util.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat ymdformat = new SimpleDateFormat("yyyy.MM.dd");
    private ViewPager viewPager;
    private CalendarPagerAdapter calendarPagerAdapter;
    private LunarCalendar lunarCalendar;
    private CalendarUtil calendarUtil;
    private TextView solar_text, lunar_text, dv_solar_date, dv_week;
    private LinearLayout layout_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //取消状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.main);

        //初始化组件
        init();

        //创建日期表格
        calendarPagerAdapter = new CalendarPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);
        viewPager.setCurrentItem(Constant.pagecount / 2);
        setPageListener(viewPager);
    }

    /**
     * 初始化组件
     */
    private void init() {
        lunarCalendar = new LunarCalendar();
        calendarUtil = new CalendarUtil();

        viewPager = (ViewPager) this.findViewById(R.id.viewPager);

        solar_text = (TextView) this.findViewById(R.id.solar_text);
        lunar_text = (TextView) this.findViewById(R.id.lunar_text);
        dv_solar_date = (TextView) this.findViewById(R.id.dv_solar_date);
        dv_week = (TextView) this.findViewById(R.id.dv_week);

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

        layout_date = (LinearLayout) this.findViewById(R.id.layout_date);
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
                DatePopupWindow pw = new DatePopupWindow(MainActivity.this, solarDate);

                //出现在布局底端
                LinearLayout mainView = (LinearLayout) findViewById(R.id.main);
                pw.showAsDropDown(mainView);
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
                        Toast.makeText(MainActivity.this, "date" + sdf.format(date), Toast.LENGTH_SHORT).show();
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
                lunar_text.setText(lunarDate+" "+week);
                dv_solar_date.setText(chineseYear+lunarDate);
                dv_week.setText(week);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }

    public void setCalendar(Date date) {
        ViewPager viewPager = (ViewPager) this.findViewById(R.id.viewPager);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 菜单定义
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.note:
                Toast.makeText(MainActivity.this, "测试", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}



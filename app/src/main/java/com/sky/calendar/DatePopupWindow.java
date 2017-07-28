package com.sky.calendar;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.wheel.adapters.AbstractWheelTextAdapter;
import com.sky.plug.wheel.views.OnWheelChangedListener;
import com.sky.plug.wheel.views.OnWheelScrollListener;
import com.sky.plug.wheel.views.WheelView;
import com.sky.util.CalendarUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 17-7-21.
 */
public class DatePopupWindow extends PopupWindow implements android.view.View.OnClickListener {

    DecimalFormat df = new DecimalFormat("00");

    Calendar cal = Calendar.getInstance();
    private CalendarUtil calendarUtil = new CalendarUtil();
    private Context context;
    private WheelView wvYear;
    private WheelView wvMonth;
    private WheelView wvDay;

    private TextView confirmBtn, cancelBtn;

    private Date defaultDate, selectedDate;

    private DateSelectInterface dateSelectInterface;



    //年月日数据集合
    private ArrayList<String> yearList = new ArrayList<String>();
    private ArrayList<String> monthList = new ArrayList<String>();
    private ArrayList<String> dayList = new ArrayList<String>();

    //年月日适配器
    private CalendarTextAdapter mYearAdapter;
    private CalendarTextAdapter mMonthAdapter;
    private CalendarTextAdapter mDayAdapter;

    //选中的年月日下表
    private int selectYearIndex, selectMonthIndex, selectDayIndex;

    //最大显示文字
    private static final int MAXTEXTSIZE = 16;
    //最小显示文字
    private static final int MINTEXTSIZE = 13;
    private static final int VISIBLEITEMS = 5;

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;

        cal.setTime(defaultDate);
        selectYearIndex = cal.get(Calendar.YEAR) - 1900;
        selectMonthIndex = cal.get(Calendar.MONTH);
        selectDayIndex = cal.get(Calendar.DAY_OF_MONTH)-1;
    }

    public DatePopupWindow(Context context, Date defaultDate) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.datepicker, null);
        setContentView(view);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        setDefaultDate(defaultDate);
        init(view);
    }

    private void init(View view) {

        //初始化年、月、日三个选择器
        wvYear = (WheelView) view.findViewById(R.id.yearView);
        wvMonth = (WheelView) view.findViewById(R.id.monthView);
        wvDay = (WheelView) view.findViewById(R.id.dayView);

        //确认、取消按钮
        confirmBtn = (TextView) view.findViewById(R.id.date_select_confirm);
        cancelBtn = (TextView) view.findViewById(R.id.date_select_cancel);

        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        /*************************************************** 处理年份 *************************************************/
        setYear();
        wvYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectYearIndex = newValue;
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextStyle(currentText, mYearAdapter);
                setDay();
            }
        });
        wvYear.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextStyle(currentText, mYearAdapter);
            }
        });

        /*************************************************** 处理月份 *************************************************/
        setMonth();
        wvMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectMonthIndex = newValue;
                String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextStyle(currentText, mMonthAdapter);
                setDay();
            }
        });
        wvMonth.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
            }
        });

        /*************************************************** 处理日期 *************************************************/
        setDay();
        wvDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectDayIndex = newValue;
                String currentText = (String) mDayAdapter.getItemText(wheel.getCurrentItem());
                setTextStyle(currentText, mDayAdapter);
            }
        });

        wvDay.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void setYear() {
        initYears();
        mYearAdapter = new CalendarTextAdapter(context, yearList, selectYearIndex);
        wvYear.setVisibleItems(VISIBLEITEMS);
        wvYear.setCyclic(true);
        wvYear.setViewAdapter(mYearAdapter);
        wvYear.setCurrentItem(selectYearIndex);
    }

    public void setMonth() {
        initMonths();
        mMonthAdapter = new CalendarTextAdapter(context, monthList, selectMonthIndex);
        wvMonth.setVisibleItems(VISIBLEITEMS);
        wvMonth.setCyclic(true);
        wvMonth.setViewAdapter(mMonthAdapter);
        wvMonth.setCurrentItem(selectMonthIndex);
    }

    public void setDay() {
        initDays();
        mDayAdapter = new CalendarTextAdapter(context, dayList, selectDayIndex);
        wvDay.setVisibleItems(VISIBLEITEMS);
        wvDay.setCyclic(true);
        wvDay.setViewAdapter(mDayAdapter);
        wvDay.setCurrentItem(selectDayIndex);
    }

    public void initYears() {
        for (int y = 1900; y < 2100; y++) {
            yearList.add(y + "年");
        }
    }

    public void initMonths() {
        for (int m = 1; m <= 12; m++) {
            monthList.add(df.format(m) + "月");
        }
    }

    public void initDays() {
        String yearString = yearList.get(selectYearIndex);
        String monthString = monthList.get(selectMonthIndex);
        int year = Integer.parseInt(yearString.substring(0, yearString.length() - 1));
        int month = Integer.parseInt(monthString.substring(0, monthString.length() - 1));
        int day;
        boolean leap;
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            leap = true;
        } else {
            leap = false;
        }
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                day = 30;
                break;
            case 2:
                day = leap ? 29 : 28;
                break;
            default:
                day = 31;
                break;
        }
        dayList.clear();

        for (int d = 1; d <= day; d++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month-1);
            cal.set(Calendar.DAY_OF_MONTH, d);
            dayList.add(df.format(d) + "日 "+calendarUtil.getWeekString(cal));
        }
    }

    private class CalendarTextAdapter extends AbstractWheelTextAdapter {
        ArrayList<String> list;

        protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem) {
            super(context, R.layout.wheel_date_box, NO_RESOURCE, currentItem, MAXTEXTSIZE, MINTEXTSIZE);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.date_select_confirm:

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                String yearString = yearList.get(selectYearIndex);
                String monthString = monthList.get(selectMonthIndex);
                String dayString = dayList.get(selectDayIndex);
                int year = Integer.parseInt(yearString.substring(0, yearString.length() - 1));
                int month = Integer.parseInt(monthString.substring(0, monthString.length() - 1));
                int day = Integer.parseInt(dayString.substring(0, 2));

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                selectedDate = calendar.getTime();
                dateSelectInterface.onDateSelectedCallBack(selectedDate);
                dismiss();
                break;
            case R.id.date_select_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 设置字体大小
     *
     * @param currentItemText
     * @param adapter
     */
    public void setTextStyle(String currentItemText, CalendarTextAdapter adapter) {
        ArrayList<View> list = adapter.getTestViews();
        String currentText;
        for (int i = 0; i < list.size(); i++) {
            TextView tv = (TextView) list.get(i);
            currentText = tv.getText().toString();
            if (currentItemText.equals(currentText)) {
                tv.setTextSize(MAXTEXTSIZE);
            } else {
                tv.setTextSize(MINTEXTSIZE);
            }
        }
    }

    public void onSelectedDate(DateSelectInterface dateSelectInterface){
            this.dateSelectInterface = dateSelectInterface;
    }

    /**
     * 定义回调接口
     */
    public interface DateSelectInterface{
        public void onDateSelectedCallBack(Date date);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT < 24){
            super.showAtLocation(anchor, 0, 0, Gravity.END);
        }else{
            super.setFocusable(true);
            //    在某个控件下方弹出
            super.showAsDropDown(anchor);
        }
    }

}

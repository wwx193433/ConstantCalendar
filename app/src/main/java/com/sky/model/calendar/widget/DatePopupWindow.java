package com.sky.model.calendar.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sky.constantcalendar.R;
import com.sky.plug.wheel.views.OnWheelChangedListener;
import com.sky.plug.wheel.views.WheelView;
import com.sky.util.CalendarUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17-7-21.
 * 头部日期弹出框
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DatePopupWindow extends PopupWindow implements android.view.View.OnClickListener {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    DecimalFormat df = new DecimalFormat("00");
    private LunarCalendar lunarCalendar = new LunarCalendar();
    Calendar cal = Calendar.getInstance();
    private CalendarUtil calendarUtil = new CalendarUtil();
    private Context context;
    private WheelView wvSolarYear, wvSolarMonth, wvSolarDay;
    private WheelView wvLunarYear, wvLunarMonth, wvLunarDay;
    private LinearLayout wv_solar, wv_lunar;
    private Button solarbtn, lunarbtn;
    private Button confirmBtn, todayBtn;
    private Date selectedDate;
    private DateSelectInterface dateSelectInterface;
    private static final int ITEM_HEIGHT = 120;

    //年月日数据集合
    private List<String> solarYearList = new ArrayList<>();
    private List<String> solarMonthList = new ArrayList<>();
    private List<String> solarDayList = new ArrayList<>();

    private List<String> lunarYearList = new ArrayList<>();
    private List<String> lunarMonthList = new ArrayList<>();
    private List<String> lunarDayList = new ArrayList<>();

    //选中的年月日下表
    private int solarYearIndex, solarMonthIndex, solarDayIndex;
    private int lunarYearIndex, lunarMonthIndex, lunarDayIndex;

    //最大显示文字
    private static final int MAXTEXTSIZE = 17;
    //最小显示文字
    private static final int MINTEXTSIZE = 14;
    private static final int VISIBLEITEMS = 3;

    public void setDate(Date date) {

        cal.setTime(date);
        solarYearIndex = cal.get(Calendar.YEAR) - 1900;
        solarMonthIndex = cal.get(Calendar.MONTH);
        solarDayIndex = cal.get(Calendar.DAY_OF_MONTH) - 1;
    }

    public DatePopupWindow(Context context, Date defaultDate) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.datepicker, null);
        setContentView(view);

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);

        setDate(defaultDate);

        //确认、回到今天按钮
        confirmBtn = (Button) view.findViewById(R.id.date_select_confirm);
        todayBtn = (Button) view.findViewById(R.id.date_select_today);
        confirmBtn.setOnClickListener(this);
        todayBtn.setOnClickListener(this);

        initSolarWheelView(view);
        initLunarWheelView(view);
    }

    private void initSolarWheelView(View view) {
        //初始化年、月、日三个选择器
        wvSolarYear = (WheelView) view.findViewById(R.id.solarYearView);
        wvSolarMonth = (WheelView) view.findViewById(R.id.solarMonthView);
        wvSolarDay = (WheelView) view.findViewById(R.id.solarDayView);
        wv_solar = (LinearLayout) view.findViewById(R.id.wv_solar);

        solarbtn = (Button) view.findViewById(R.id.solarbtn);
        solarbtn.setOnClickListener(this);

        /*************************************************** 处理年份 *************************************************/
        setSolarYear();
        wvSolarYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                solarYearIndex = newValue;
                setSolarDay();
            }
        });

        /*************************************************** 处理月份 *************************************************/
        setSolarMonth();
        wvSolarMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                solarMonthIndex = newValue;
                setSolarDay();
            }
        });

        /*************************************************** 处理日期 *************************************************/
        setSolarDay();
        wvSolarDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                solarDayIndex = newValue;
            }
        });
    }

    /**
     * 农历日期设置
     *
     * @param view
     */
    private void initLunarWheelView(View view) {

        //初始化年、月、日三个选择器
        wvLunarYear = (WheelView) view.findViewById(R.id.lunarYearView);
        wvLunarMonth = (WheelView) view.findViewById(R.id.lunarMonthView);
        wvLunarDay = (WheelView) view.findViewById(R.id.lunarDayView);
        wv_lunar = (LinearLayout) view.findViewById(R.id.wv_lunar);

        lunarbtn = (Button) view.findViewById(R.id.lunarbtn);
        lunarbtn.setOnClickListener(this);

        /*************************************************** 处理年份 *************************************************/
        setLunarYear();
        wvLunarYear.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                lunarYearIndex = newValue;
                setLunarMonth();
                setLunarDay();
            }
        });

        /*************************************************** 处理月份 *************************************************/
        setLunarMonth();
        wvLunarMonth.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                lunarMonthIndex = newValue;
                setLunarDay();
            }
        });

        /*************************************************** 处理日期 *************************************************/
        setLunarDay();
        wvLunarDay.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                lunarDayIndex = newValue;
            }
        });
    }

    public void setLunarYear() {
        initLunarYears();

        wvLunarYear.setVisibleItems(VISIBLEITEMS);
        wvLunarYear.setItemHeight(ITEM_HEIGHT);
        wvLunarYear.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvLunarYear.loadData(context, lunarYearList, lunarYearIndex);
        wvLunarYear.setCyclic(true);
    }

    public void setSolarYear() {
        initSolarYears();

        wvSolarYear.setVisibleItems(VISIBLEITEMS);
        wvSolarYear.setItemHeight(ITEM_HEIGHT);
        wvSolarYear.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvSolarYear.loadData(context, solarYearList, solarYearIndex);
        wvSolarYear.setCyclic(true);
    }

    public void setLunarMonth() {
        initLunarMonths();

        wvLunarMonth.setVisibleItems(VISIBLEITEMS);
        wvLunarMonth.setItemHeight(ITEM_HEIGHT);
        wvLunarMonth.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvLunarMonth.loadData(context, lunarMonthList, lunarMonthIndex);
        wvLunarMonth.setCyclic(true);
    }

    public void setSolarMonth() {
        initSolarMonths();

        wvSolarMonth.setVisibleItems(VISIBLEITEMS);
        wvSolarMonth.setItemHeight(ITEM_HEIGHT);
        wvSolarMonth.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvSolarMonth.loadData(context, solarMonthList, solarMonthIndex);
        wvSolarMonth.setCyclic(true);
    }

    public void setLunarDay() {
        initLunarDays();

        wvLunarDay.setVisibleItems(VISIBLEITEMS);
        wvLunarDay.setItemHeight(ITEM_HEIGHT);
        wvLunarDay.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvLunarDay.loadData(context, lunarDayList, lunarDayIndex);
        wvLunarDay.setCyclic(true);
    }

    public void setSolarDay() {
        initSolarDays();

        wvSolarDay.setVisibleItems(VISIBLEITEMS);
        wvSolarDay.setItemHeight(ITEM_HEIGHT);
        wvSolarDay.setStyle("", MAXTEXTSIZE, MINTEXTSIZE);
        wvSolarDay.loadData(context, solarDayList, solarDayIndex);
        wvSolarDay.setCyclic(true);
    }

    public void initSolarYears() {
        solarYearList.clear();
        for (int y = 1900; y < 2100; y++) {
            solarYearList.add(y + "年");
        }
    }

    public void initLunarYears() {
        lunarYearList.clear();
        for (int y = 1900; y < 2100; y++) {
            lunarYearList.add(y + "年");
        }
    }

    public void initSolarMonths() {
        solarMonthList.clear();
        for (int m = 1; m <= 12; m++) {
            solarMonthList.add(df.format(m) + "月");
        }
    }

    public void initLunarMonths() {
        lunarMonthList.clear();
        lunarMonthList = lunarCalendar.getLunarMonths(lunarYearIndex + 1900);
    }

    public void initLunarDays() {
        lunarDayList.clear();
        lunarDayList = lunarCalendar.getLunarMonthDays(lunarYearIndex + 1900, lunarMonthIndex);
    }

    public void initSolarDays() {
        int year = solarYearIndex + 1900;
        int month = solarMonthIndex + 1;
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
        solarDayList.clear();

        for (int d = 1; d <= day; d++) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, d);
            solarDayList.add(df.format(d) + "日 " + calendarUtil.getWeekString(cal));
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.date_select_confirm:
                if(wv_solar.getVisibility() == View.GONE){
                    setSolarDate();
                }
                selectedDate = getSelectSolarDate();
                dateSelectInterface.onDateSelectedCallBack(selectedDate);
                dismiss();
                break;
            case R.id.date_select_today:
                selectedDate = new Date();
                dateSelectInterface.onDateSelectedCallBack(selectedDate);
                dismiss();
                break;
            case R.id.solarbtn:
                wv_solar.setVisibility(View.VISIBLE);
                wv_lunar.setVisibility(View.GONE);
                setSolarDate();
                break;
            case R.id.lunarbtn:
                wv_lunar.setVisibility(View.VISIBLE);
                wv_solar.setVisibility(View.GONE);
                setLunarDate();
                break;
            default:
                break;
        }
    }

    public void onSelectedDate(DateSelectInterface dateSelectInterface) {
        this.dateSelectInterface = dateSelectInterface;
    }

    /**
     * 获取选中的阳历日期
     *
     * @return
     */
    public void setLunarDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 1900 + solarYearIndex);
        calendar.set(Calendar.MONTH, solarMonthIndex);
        calendar.set(Calendar.DAY_OF_MONTH, solarDayIndex + 1);
        //获取到阳历日期
        DayInfo dayInfo = lunarCalendar.solarToLunar(calendar.getTime());

        lunarYearIndex = dayInfo.getLunarYear() - 1900;
        int leapMonth = lunarCalendar.getLeapMonth(dayInfo.getLunarYear());
        lunarMonthIndex = dayInfo.getLunarMonth() >= leapMonth ? dayInfo.getLunarMonth() : (dayInfo.getLunarMonth() - 1);
        lunarDayIndex = dayInfo.getLunarDay() - 1;
        wvLunarYear.setCurrentItem(lunarYearIndex);
        wvLunarMonth.setCurrentItem(lunarMonthIndex);
        wvLunarDay.setCurrentItem(lunarDayIndex);
    }

    /**
     * 获取阳历日期index
     *
     * @return
     */
    public void setSolarDate() {
        int year = 1900 + lunarYearIndex;
        int leapMonth = LunarCalendar.getLeapMonth(year);
        int month;
        if (leapMonth == 0) {
            month = lunarMonthIndex + 1;
        } else {
            if (lunarMonthIndex >= leapMonth) {
                month = lunarMonthIndex;
            } else {
                month = lunarMonthIndex + 1;
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, lunarDayIndex + 1);
        String lunarDate = sdf.format(calendar.getTime());
        boolean isLeap = leapMonth > 0 && leapMonth == month;
        Date solarDate = lunarCalendar.lunarToSolar(lunarDate, isLeap);
        Calendar sc = Calendar.getInstance();
        sc.setTime(solarDate);
        solarYearIndex = sc.get(Calendar.YEAR) - 1900;
        solarMonthIndex = sc.get(Calendar.MONTH);
        solarDayIndex = sc.get(Calendar.DAY_OF_MONTH) - 1;
        wvSolarYear.setCurrentItem(solarYearIndex);
        wvSolarMonth.setCurrentItem(solarMonthIndex);
        wvSolarDay.setCurrentItem(solarDayIndex);
    }


    /**
     * 获取选中的阳历日期
     * @return
     */
    public Date getSelectSolarDate() {
        Calendar calendar = Calendar.getInstance();
        String yearString = solarYearList.get(solarYearIndex);
        String monthString = solarMonthList.get(solarMonthIndex);
        String dayString = solarDayList.get(solarDayIndex);
        int year = Integer.parseInt(yearString.substring(0, yearString.length() - 1));
        int month = Integer.parseInt(monthString.substring(0, monthString.length() - 1));
        int day = Integer.parseInt(dayString.substring(0, 2));

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime();
    }

    /**
     * 定义回调接口
     */
    public interface DateSelectInterface {
        void onDateSelectedCallBack(Date date);
    }

    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT < 24) {
            super.showAtLocation(anchor, 0, 0, Gravity.END);
        } else {
            super.setFocusable(true);
            //    在某个控件下方弹出
            super.showAsDropDown(anchor);
        }
    }

}

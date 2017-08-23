package com.sky.util;

import com.sky.model.calendar.widget.DayInfo;
import com.sky.model.calendar.widget.LunarCalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17-7-12.
 */
public class CalendarUtil {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private LunarCalendar lunarCalendar = new LunarCalendar();

    /**
     * 获取指定日期所在的月数据
     * @param date
     * @return
     */
    public List<Date> getDaysOfThisMonth(Date date) {
        List<Date> dates = new ArrayList<>();

        Calendar startCalendar = Calendar.getInstance();
        Date startDay = getMonthStart(date);
        startCalendar.setTime(startDay);
        Calendar endCalendar = Calendar.getInstance();
        Date endDay = getMonthEnd(date);
        endCalendar.setTime(endDay);

        while(startCalendar.getTime().before(endCalendar.getTime())){
            dates.add(startDay);
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return dates;
    }

    public Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        int index = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DAY_OF_MONTH, -index);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return cal.getTime();
    }

    public Date getMonthStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int index = cal.get(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DAY_OF_MONTH, 1-index);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return cal.getTime();
    }

    public String getWeekDay(Date date){
        String weekAry[]={"日","一","二","三","四","五","六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int index = cal.get(Calendar.DAY_OF_WEEK)-1;
        String week = "星期"+weekAry[index];
        return week;
    }
    public String getChineseYear(int year){
        return lunarCalendar.getChineseEra(year)+lunarCalendar.getZodiac(year)+"年";
    }

    public String getLunarAndWeek(Date date) {
        DayInfo dayInfo = lunarCalendar.solarToLunar(date);
        return dayInfo.getLunarChinaMonth() + dayInfo.getLunarChinaDay() + " " + getWeekDay(date) ;
    }

    /**
     * 获取农历日期
     * @param date
     * @return
     */
    public String getLunarDate(Date date){
        DayInfo dayInfo = lunarCalendar.solarToLunar(date);
        return dayInfo.getLunarChinaMonth() + dayInfo.getLunarChinaDay();
    }

    /**
     * @param date <Date>
     * @return int
     */
    public static int getMonthSpace(Date date){
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        Calendar today = Calendar.getInstance();
        int month = (day.get(Calendar.YEAR) - today.get(Calendar.YEAR)) * 12 + day.get(Calendar.MONTH) - today.get(Calendar.MONTH);
        return month;
    }

    public String getWeekString(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        if(sdf.format(cal.getTime()).equals(sdf.format(new Date()))){
            return "今天";
        }

        int dayIndex = cal.get(Calendar.DAY_OF_WEEK);
        switch(dayIndex){
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
        }
        return "";
    }

    public String getCurrentDayAndWeek(){
        Calendar calendar = Calendar.getInstance();
        return sdf.format(calendar.getTime())+" "+ getWeekString(calendar);
    }
}

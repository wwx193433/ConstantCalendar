package com.sky.model.calendar.widget;

import java.util.Date;

/**
 * 每日封装
 */
public class DayInfo {

    // 阳历日期
    private Date solarDate;

    // 阴历年份
    private int lunarYear;
    // 阴历月份
    private int lunarMonth;
    // 阴历日期
    private int lunarDay;
    // 特殊节假日
    private String specailDay;
    //中文名称
    private String lunarChinaDay;
    private String lunarChinaMonth;

    //是否润年
    private boolean isLeapYear;
    //是否润月
    private boolean isLeapMonth;

    public Date getSolarDate() {
        return solarDate;
    }

    public void setSolarDate(Date solarDate) {
        this.solarDate = solarDate;
    }

    public int getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(int lunarYear) {
        this.lunarYear = lunarYear;
    }

    public int getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(int lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public int getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(int lunarDay) {
        this.lunarDay = lunarDay;
    }

    public String getSpecailDay() {
        return specailDay;
    }

    public void setSpecailDay(String specailDay) {
        this.specailDay = specailDay;
    }

    public boolean isLeapYear() {
        return isLeapYear;
    }

    public void setLeapYear(boolean isLeapYear) {
        this.isLeapYear = isLeapYear;
    }

    public boolean isLeapMonth() {
        return isLeapMonth;
    }

    public void setLeapMonth(boolean isLeapMonth) {
        this.isLeapMonth = isLeapMonth;
    }

    public String getLunarChinaDay() {
        return lunarChinaDay;
    }

    public void setLunarChinaDay(String lunarChinaDay) {
        this.lunarChinaDay = lunarChinaDay;
    }

    public String getLunarChinaMonth() {
        return lunarChinaMonth;
    }

    public void setLunarChinaMonth(String lunarChinaMonth) {
        this.lunarChinaMonth = lunarChinaMonth;
    }

    @Override
    public String toString() {
        return "DayInfo{" +
                "solarDate=" + solarDate +
                ", lunarYear=" + lunarYear +
                ", lunarMonth=" + lunarMonth +
                ", lunarDay=" + lunarDay +
                ", specailDay='" + specailDay + '\'' +
                ", lunarChinaDay='" + lunarChinaDay + '\'' +
                ", lunarChinaMonth='" + lunarChinaMonth + '\'' +
                ", isLeapYear=" + isLeapYear +
                ", isLeapMonth=" + isLeapMonth +
                '}';
    }
}

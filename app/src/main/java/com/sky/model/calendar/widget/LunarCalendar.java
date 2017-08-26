package com.sky.model.calendar.widget;

import com.sky.util.CalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>描述</b>: 日历转换工具类：阴历和阳历日期互换(阴历日期范围19000101~20491229)<br>
 *
 * @author wwx193433 2015-1-5
 */
public class LunarCalendar {

    private SolarTerms solarTerms = new SolarTerms();

    final static String chineseNumber[] = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二"};
    final static String chineseTen[] = {"初", "十", "廿", "三"};

    // 计算阴历日期参照1900年到2049年
    private final static int[] LUNAR_INFO = {0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0,
            0x09ad0, 0x055d2, // 1900-1909
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977, // 1910-1919
            0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, // 1920-1929
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950, // 1930-1939
            0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, // 1940-1949
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0, // 1950-1959
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0, // 1960-1969
            0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6, // 1970-1979
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570, // 1980-1989
            0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, // 1990-1999
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5, // 2000-2009
            0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, // 2010-2019
            0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530, // 2020-2029
            0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, // 2030-2039
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0, // 2040-2049
            /** Add By JJonline@JJonline.Cn **/
            0x14b63, 0x09370, 0x049f8, 0x04970, 0x064b0, 0x168a6, 0x0ea50, 0x06b20, 0x1a6c4, 0x0aae0, // 2050-2059
            0x0a2e0, 0x0d2e3, 0x0c960, 0x0d557, 0x0d4a0, 0x0da50, 0x05d55, 0x056a0, 0x0a6d0, 0x055d4, // 2060-2069
            0x052d0, 0x0a9b8, 0x0a950, 0x0b4a0, 0x0b6a6, 0x0ad50, 0x055a0, 0x0aba4, 0x0a5b0, 0x052b0, // 2070-2079
            0x0b273, 0x06930, 0x07337, 0x06aa0, 0x0ad50, 0x14b55, 0x04b60, 0x0a570, 0x054e4, 0x0d160, // 2080-2089
            0x0e968, 0x0d520, 0x0daa0, 0x16aa6, 0x056d0, 0x04ae0, 0x0a9d4, 0x0a2d0, 0x0d150, 0x0f252, // 2090-2099
            0x0d520};// 2100;

    // 允许输入的最小年份
    private final static int MIN_YEAR = 1900;
    // 允许输入的最大年份
    private final static int MAX_YEAR = 2099;
    // 当年是否有闰月
    private static boolean isLeapYear;
    // 阳历日期计算起点
    private final static String START_DATE = "19000130";

    // 星座名称
    private static String[] constellationName = {"白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座",
            "水瓶座", "双鱼座"};

    // 二十八星宿
    private static String[] chineseConstellationName = {
            // 四 五 六 日 一 二 三
            "角木蛟", "亢金龙", "女土蝠", "房日兔", "心月狐", "尾火虎", "箕水豹", "斗木獬", "牛金牛", "氐土貉", "虚日鼠", "危月燕", "室火猪", "壁水獝", "奎木狼",
            "娄金狗", "胃土彘", "昴日鸡", "毕月乌", "觜火猴", "参水猿", "井木犴", "鬼金羊", "柳土獐", "星日马", "张月鹿", "翼火蛇", "轸水蚓"};

    // 农历相关数据
    private static String tiangan[] = {"癸", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬"};
    private static String dizhi[] = {"亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌"};
    private static String shengxiao[] = {"猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗"};
    private static String[] monthString = {"出错", "正月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "冬月",
            "腊月"};

    /**
     * 计算天干地支
     */
    public String getChineseEra(int year) {
        int i = (year - 3) % 10;
        int j = (year - 3) % 12;
        return tiangan[i] + dizhi[j];
    }

    /**
     * 计算生肖
     */
    public String getZodiac(int year) {
        int n = (year - 3) % 12;
        return shengxiao[n];
    }

    /**
     * 计算阴历 {@code year}年闰哪个月 1-12 , 没闰传回 0
     *
     * @param year 阴历年
     * @return (int)月份
     * @author liu 2015-1-5
     */
    public static int getLeapMonth(int year) {
        return (int) (LUNAR_INFO[year - 1900] & 0xf);
    }

    /**
     * 计算阴历{@code year}年闰月多少天
     *
     * @param year 阴历年
     * @return (int)天数
     * @author liu 2015-1-5
     */
    private static int getLeapMonthDays(int year) {
        if (getLeapMonth(year) != 0) {
            if ((LUNAR_INFO[year - 1900] & 0xf0000) == 0) {
                return 29;
            } else {
                return 30;
            }
        } else {
            return 0;
        }
    }

    /**
     * 计算阴历{@code lunarYeay}年{@code month}月的天数
     *
     * @param lunarYeay 阴历年
     * @param month     阴历月
     * @return (int)该月天数
     * @throws Exception
     * @author liu 2015-1-5
     */
    private static int getMonthDays(int lunarYeay, int month) {
        // 0X0FFFF[0000 {1111 1111 1111} 1111]中间12位代表12个月，1为大月，0为小月
        int bit = 1 << (16 - month);
        if (((LUNAR_INFO[lunarYeay - 1900] & 0x0FFFF) & bit) == 0) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 计算阴历{@code year}年的总天数
     *
     * @param year 阴历年
     * @return (int)总天数
     * @author liu 2015-1-5
     */
    private static int getYearDays(int year) {
        int sum = 29 * 12;
        for (int i = 0x8000; i >= 0x8; i >>= 1) {
            if ((LUNAR_INFO[year - 1900] & 0xfff0 & i) != 0) {
                sum++;
            }
        }
        return sum + getLeapMonthDays(year);
    }

    /**
     * 计算两个阳历日期相差的天数。
     *
     * @param startDate 开始时间
     * @param endDate   截至时间
     * @return (int)天数
     * @author liu 2017-3-2
     */
    private static int daysBetween(Date startDate, Date endDate) {
        int days = 0;
        // 将转换的两个时间对象转换成Calendar对象
        Calendar can1 = Calendar.getInstance();
        can1.setTime(startDate);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(endDate);
        // 拿出两个年份
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);
        // 天数

        Calendar can = null;
        // 如果can1 < can2
        // 减去小的时间在这一年已经过了的天数
        // 加上大的时间已过的天数
        if (can1.before(can2)) {
            days -= can1.get(Calendar.DAY_OF_YEAR);
            days += can2.get(Calendar.DAY_OF_YEAR);
            can = can1;
        } else {
            days -= can2.get(Calendar.DAY_OF_YEAR);
            days += can1.get(Calendar.DAY_OF_YEAR);
            can = can2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {
            // 获取小的时间当前年的总天数
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            // 再计算下一年。
            can.add(Calendar.YEAR, 1);
        }
        return days;
    }

    /**
     * 检查阴历日期是否合法
     *
     * @param lunarYear     阴历年
     * @param lunarMonth    阴历月
     * @param lunarDay      阴历日
     * @param leapMonthFlag 闰月标志
     * @throws Exception
     */
    private static void checkLunarDate(int lunarYear, int lunarMonth, int lunarDay, boolean leapMonthFlag)
            throws Exception {
        if ((lunarYear < MIN_YEAR) || (lunarYear > MAX_YEAR)) {
            throw (new Exception("非法农历年份！"));
        }
        if ((lunarMonth < 1) || (lunarMonth > 12)) {
            throw (new Exception("非法农历月份！"));
        }
        if ((lunarDay < 1) || (lunarDay > 30)) { // 中国的月最多30天
            throw (new Exception("非法农历天数！"));
        }

        int leap = getLeapMonth(lunarYear);// 计算该年应该闰哪个月
        if ((leapMonthFlag == true) && (lunarMonth != leap)) {
            throw (new Exception("非法闰月！"));
        }
    }

    /**
     * 阴历转换为阳历
     *
     * @param lunarDate     阴历日期,格式YYYYMMDD
     * @param leapMonthFlag 是否为闰月
     * @return 阳历日期, 格式：YYYYMMDD
     * @throws Exception
     * @author liu 2015-1-5
     */
    public Date lunarToSolar(String lunarDate, boolean leapMonthFlag){
        int lunarYear = Integer.parseInt(lunarDate.substring(0, 4));
        int lunarMonth = Integer.parseInt(lunarDate.substring(4, 6));
        int lunarDay = Integer.parseInt(lunarDate.substring(6, 8));

        try {
            checkLunarDate(lunarYear, lunarMonth, lunarDay, leapMonthFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int offset = 0;

        for (int i = MIN_YEAR; i < lunarYear; i++) {
            int yearDaysCount = getYearDays(i); // 求阴历某年天数
            offset += yearDaysCount;
        }
        // 计算该年闰几月
        int leapMonth = getLeapMonth(lunarYear);

        // 当年没有闰月或月份早于闰月或和闰月同名的月份
        if (leapMonth == 0 || (lunarMonth < leapMonth) || (lunarMonth == leapMonth && !leapMonthFlag)) {
            for (int i = 1; i < lunarMonth; i++) {
                int tempMonthDaysCount = getMonthDays(lunarYear, i);
                offset += tempMonthDaysCount;
            }
            offset += lunarDay; // 加上当月的天数
        } else {// 当年有闰月，且月份晚于或等于闰月
            for (int i = 1; i < lunarMonth; i++) {
                int tempMonthDaysCount = getMonthDays(lunarYear, i);
                offset += tempMonthDaysCount;
            }
            if (lunarMonth > leapMonth) {
                int temp = getLeapMonthDays(lunarYear); // 计算闰月天数
                offset += temp; // 加上闰月天数
                offset += lunarDay;
            } else { // 如果需要计算的是闰月，则应首先加上与闰月对应的普通月的天数
                // 计算月为闰月
                int temp = getMonthDays(lunarYear, lunarMonth); // 计算非闰月天数
                offset += temp;
                offset += lunarDay;
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date myDate = null;
        try {
            myDate = formatter.parse(START_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(myDate);
        c.add(Calendar.DATE, offset);
        myDate = c.getTime();

        return myDate;
    }

    /**
     * 阳历日期转换为阴历日期
     *
     * @param solarDate 阳历日期,格式YYYYMMDD
     * @return 阴历日期
     * @throws Exception
     * @author liu 2015-1-5
     */
    public DayInfo solarToLunar(Date solarDate) {
        int i;
        int temp = 0;
        int lunarYear;
        int lunarMonth; // 农历月份
        int lunarDay; // 农历当月第几天
        boolean leapMonthFlag = false;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date startDate = null;

        try {
            startDate = formatter.parse(START_DATE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int offset = daysBetween(startDate, solarDate);

        for (i = MIN_YEAR; i <= MAX_YEAR; i++) {
            temp = getYearDays(i); // 求当年农历年天数
            if (offset - temp < 1) {
                break;
            } else {
                offset -= temp;
            }
        }
        lunarYear = i;

        int leapMonth = getLeapMonth(lunarYear);// 计算该年闰哪个月
        // 设定当年是否有闰月
        if (leapMonth > 0) {
            isLeapYear = true;
        } else {
            isLeapYear = false;
        }

        for (i = 1; i <= 12; i++) {
            if (i == leapMonth + 1 && isLeapYear) {
                temp = getLeapMonthDays(lunarYear);
                isLeapYear = false;
                leapMonthFlag = true;
                i--;
            } else {
                try {
                    temp = getMonthDays(lunarYear, i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            offset -= temp;
            if (offset <= 0) {
                break;
            }
        }

        offset += temp;
        lunarMonth = i;
        lunarDay = offset;
        DayInfo day = new DayInfo();
        day.setLunarYear(lunarYear);
        day.setLunarMonth(lunarMonth);
        day.setLunarDay(lunarDay);
        day.setLeapYear(leapMonthFlag);
        day.setLunarChinaMonth((leapMonthFlag & (lunarMonth == leapMonth)) ? "闰" + monthString[lunarMonth] : monthString[lunarMonth]);
        day.setLunarChinaDay(getChinaDayString(lunarDay));
        day.setLeapMonth(leapMonthFlag & (lunarMonth == leapMonth));
        return day;
    }

    /**
     * 阳历日期转换为阴历日期
     *
     * @param startDate
     * @param endDate   阳历日期,格式YYYYMMDD
     * @return 阴历日期
     * @throws Exception
     * @author liu 2015-1-5
     */
    public List<DayInfo> solarToLunar(Date startDate, Date endDate) {
        DayInfo dayInfo = solarToLunar(startDate);

        List<DayInfo> lunarDays = new ArrayList<DayInfo>();

        //农历假日
        Map<String, String> lunarHolidayMap = getLunarHoliday();
        //阳历假日
        Map<String, String> solarHolidayMap = getSolarHoliday();

        int offset = daysBetween(startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int year = dayInfo.getLunarYear();
        int month = dayInfo.getLunarMonth();
        int day = dayInfo.getLunarDay();
        // 计算该年闰哪个月
        int leapMonth = getLeapMonth(year);
        // 设定当年是否有闰月
        isLeapYear = leapMonth > 0 ? true : false;
        boolean isLeapMonth = dayInfo.isLeapMonth();
        boolean isLeapFlag = !isLeapMonth;

        //24节气
        Map<String, String> solarTermMap = solarTerms.getTerms(year);
        int monthDays = 0;
        if (isLeapMonth) {
            monthDays = getLeapMonthDays(year);
        } else {
            try {
                monthDays = getMonthDays(year, month);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i <= offset + 1; i++) {
            DayInfo lunarDay = new DayInfo();
            if (day > monthDays) {
                day = 1;
                month++;
                if (month == leapMonth + 1 && isLeapFlag) {
                    monthDays = getLeapMonthDays(year);
                    isLeapMonth = true;
                    isLeapFlag = false;
                    month--;
                } else {
                    if (month > 12) {
                        month = 1;
                        year++;
                        // 计算该年闰哪个月
                        leapMonth = getLeapMonth(year);
                        solarTermMap = solarTerms.getTerms(year);
                        // 设定当年是否有闰月
                        isLeapFlag = isLeapYear = leapMonth > 0 ? true : false;
                    }
                    try {
                        monthDays = getMonthDays(year, month);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    isLeapMonth = false;
                }
            }
            lunarDay.setLunarYear(year);
            lunarDay.setLunarMonth(month);
            lunarDay.setLunarDay(day);

            lunarDay.setLeapYear(isLeapYear);
            lunarDay.setLeapMonth(isLeapMonth);
            lunarDay.setSolarDate(calendar.getTime());

            //阳历假日
            SimpleDateFormat mdFormat = new SimpleDateFormat("MMdd");
            String solarTimeKey = mdFormat.format(lunarDay.getSolarDate());
            //农历假日
            String lunarTimeSey = contactMonthAndDay(month, day);

            if (solarHolidayMap.containsKey(solarTimeKey)) {
                lunarDay.setSpecailDay(solarHolidayMap.get(solarTimeKey));
            } else if (lunarHolidayMap.containsKey(lunarTimeSey)) {
                lunarDay.setSpecailDay(lunarHolidayMap.get(lunarTimeSey));
            } else if (solarTermMap.containsKey(solarTimeKey)) {
                lunarDay.setSpecailDay(solarTermMap.get(solarTimeKey));
            }
            lunarDay.setLunarChinaMonth(isLeapMonth ? "闰" + monthString[month] : monthString[month]);
            lunarDay.setLunarChinaDay(getChinaDayString(day));
            lunarDays.add(lunarDay);
            day++;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return lunarDays;
    }

    private String contactMonthAndDay(int month, int day) {
        return (month < 10 ? "0" + month : "" + month) + (day < 10 ? "0" + day : "" + day);
    }

    /**
     * 获取农历部分节假日
     *
     * @return
     */
    public Map<String, String> getLunarHoliday() {
        Map<String, String> lunarHolidayMap = new HashMap<String, String>();
        lunarHolidayMap.put("0101", "春节");
        lunarHolidayMap.put("0115", "元宵");
        lunarHolidayMap.put("0505", "端午");
        lunarHolidayMap.put("0707", "七夕情人");
        lunarHolidayMap.put("0715", "中元");
        lunarHolidayMap.put("0815", "中秋");
        lunarHolidayMap.put("0909", "重阳");
        lunarHolidayMap.put("1208", "腊八");
        lunarHolidayMap.put("1224", "小年");
        lunarHolidayMap.put("0100", "除夕");
        return lunarHolidayMap;
    }

    /**
     * 获取农历部分节假日
     *
     * @return
     */
    public Map<String, String> getSolarHoliday() {
        Map<String, String> solarHolidayMap = new HashMap<String, String>();
        solarHolidayMap.put("0101", "元旦");
        solarHolidayMap.put("0214", "情人");
        solarHolidayMap.put("0308", "妇女");
        solarHolidayMap.put("0312", "植树");
        solarHolidayMap.put("0315", "消费者权益日");
        solarHolidayMap.put("0401", "愚人");
        solarHolidayMap.put("0501", "劳动");
        solarHolidayMap.put("0504", "青年");
        solarHolidayMap.put("0512", "护士");
        solarHolidayMap.put("0601", "儿童");
        solarHolidayMap.put("0701", "建党");
        solarHolidayMap.put("0801", "建军");
        solarHolidayMap.put("0808", "父亲");
        solarHolidayMap.put("0909", "毛泽东逝世纪念");
        solarHolidayMap.put("0910", "教师");
        solarHolidayMap.put("0928", "孔子诞辰");
        solarHolidayMap.put("1001", "国庆");
        solarHolidayMap.put("1006", "老人");
        solarHolidayMap.put("1024", "联合国日");
        solarHolidayMap.put("1112", "孙中山诞辰纪念");
        solarHolidayMap.put("1220", "澳门回归纪念");
        solarHolidayMap.put("1225", "圣诞");
        solarHolidayMap.put("1226", "毛泽东诞辰纪念");
        return solarHolidayMap;
    }

    public String getChinaDayString(int day) {
        int n = day % 10 == 0 ? 9 : day % 10 - 1;
        if (day > 30)
            return "";
        if (day == 10)
            return "初十";
        else
            return chineseTen[day / 10] + chineseNumber[n];
    }

    public List<DayInfo> getDaysOfThisMonth(Date date) {
        CalendarUtil calendarUtil = new CalendarUtil();
        Date startDate = calendarUtil.getMonthStart(date);
        Date endDate = calendarUtil.getMonthEnd(date);
        List<DayInfo> dates = solarToLunar(startDate, endDate);
        return dates;
    }

    public List<String> getLunarMonths(int lunarYear) {
        int count = 12;
        if(lunarYear == 2099){
            count = 11;
        }
        int leapMonth = getLeapMonth(lunarYear);
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            months.add(monthString[i]);
            if (i == leapMonth) {
                months.add("闰" + monthString[i]);
            }
        }
        return months;
    }

    /**
     * 获取阴历每月天数
     * @param year
     * @param monthIndex
     * @return
     */
    public List<String> getLunarMonthDays(int year, int monthIndex) {
        CalendarUtil calendarUtil = new CalendarUtil();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<String> days = new ArrayList<>();
        int count = 0;
        int month = 0;
        int leapMonth = getLeapMonth(year);
        if(leapMonth>0){
            if(monthIndex>=leapMonth){
                month = monthIndex;
                if(monthIndex == leapMonth){
                    count = getLeapMonthDays(year);
                }else{
                    count = getMonthDays(year, month);
                }
            }else{
                month = monthIndex+1;
                count = getLeapMonthDays(year);
            }
        }else{
            month = monthIndex + 1;
            count = getMonthDays(year, month);
        }
        //临界值
        if(year == 2099 && month == 10){
            count = 20;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String lunarDate = sdf.format(calendar.getTime());
        Date solarDate = lunarToSolar(lunarDate, (leapMonth > 0) && leapMonth == month);
        Calendar solarCalendar = Calendar.getInstance();
        solarCalendar.setTime(solarDate);
        for(int i= 1;i<=count;i++){
            String day = getChinaDayString(i);
            days.add(day+ " " +calendarUtil.getWeekString(solarCalendar));
            solarCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        return days;
    }

    /**
     * 获取农历日期下标
     * @param solarDate
     * @return
     */
    public int[] getLunarIndex(Date solarDate) {
        int ary[] = new int[3];
        DayInfo dayInfo = solarToLunar(solarDate);
        ary[0] = dayInfo.getLunarYear() - 1900;
        int leapMonth = getLeapMonth(dayInfo.getLunarYear());
        if(dayInfo.isLeapMonth() || dayInfo.getLunarMonth()>leapMonth){
            ary[1] = dayInfo.getLunarMonth();
        }else{
            ary[1] = dayInfo.getLunarMonth()-1;
        }
        ary[2] = dayInfo.getLunarDay() -1;
        return ary;
    }
}
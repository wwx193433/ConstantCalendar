package com.sky.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 17-7-14.
 */
public class Utility {

    /**
     * 透明度	对应十六进制
     100%	ff
     90%	e6
     85%	d9
     80%	cc
     70%	b3
     60%	99
     50%	80
     40%	66
     30%	4d
     20%	33
     15%	26
     10%	1a
     5%	0d
     0%	00
     * @param color
     * @return
     */
    public static @ColorInt int setAlpha(@ColorInt int color, int percent){
        String hexColor = Integer.toHexString(color).substring(2);
        hexColor = "#"+Integer.toHexString((int)(percent*2.55))+hexColor;
        return Color.parseColor(hexColor);
    }

    /**
     * 获取屏幕的宽度px
     */
    public static int getScreenWidth(Context ct) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager) ct.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕的高度px
     */
    public static int getScreenHeight(Context ct) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager manager = (WindowManager) ct.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int getPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int getDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //使用正则表达式判断电话号码
    public static boolean checkMobileNumber(String tel) {
        Pattern p = Pattern.compile("^(13[0-9]|15([0-3]|[5-9])|14[5,7,9]|17[1,3,5,6,7,8]|18[0-9])\\d{8}$");
        Matcher m = p.matcher(tel);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 数字过滤
     * @param str
     * @return
     */
    public static String filterNumber(String str){
        if(null==str || str.equals("")){
            return "";
        }
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher m = pattern.matcher(str);
        return m.replaceAll("").trim();
    }


    public static String getWeekDay(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar current = Calendar.getInstance();
        String weekday = "";
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(sdf.format(current.getTime()).equals(sdf.format(date))){
            return "今天";
        }
        current.add(Calendar.DAY_OF_MONTH, -1);
        if(sdf.format(current.getTime()).equals(sdf.format(date))){
            return "昨天";
        }

        switch(day){
            case 1:
                weekday = "周日";
                break;
            case 2:
                weekday = "周一";
                break;
            case 3:
                weekday = "周二";
                break;
            case 4:
                weekday = "周三";
                break;
            case 5:
                weekday = "周四";
                break;
            case 6:
                weekday = "周五";
                break;
            case 7:
                weekday = "周六";
                break;
            default:
                break;
        }
        return weekday;
    }
}

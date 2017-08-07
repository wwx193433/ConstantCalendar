package com.sky.model.menu.widget;

import android.util.Log;

/**
 * Created by Administrator on 17-8-7.
 */
public class ClockFactory {

    public static String decodeDay(int daynum){
        if(daynum == 0xfe){
            return "每天";
        }
        Log.i("tag", "daynum:"+daynum);
        String[] dayAry = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日", "响铃一次"};
        String days = "";
        int temp =dayAry.length-1;
        for(int i = 0;i<dayAry.length;i++){
            if(((daynum>>temp) & 1) == 1){
                days+=dayAry[i]+" ";
            }
            temp --;
        }
        return days;
    }
}

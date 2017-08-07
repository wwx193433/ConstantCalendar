package com.sky.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.WindowManager;

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

}

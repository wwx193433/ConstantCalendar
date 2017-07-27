package com.sky.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.calendar.DayInfo;
import com.sky.calendar.LunarCalendar;
import com.sky.constantcalendar.R;
import com.sky.util.CalendarUtil;
import com.sky.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 17-7-18.
 */
public class CalendarGridViewAdapter extends BaseAdapter {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private CalendarUtil calendarUtil = new CalendarUtil();
    private LunarCalendar lunarCalendar = new LunarCalendar();
    private @ColorInt int solarColor, lunarColor,blackColor,specialColor,whiteColor;
    private Resources resources;
    private List<DayInfo> dates;
    private Activity activity;
    private Calendar currentCalendar;

    public CalendarGridViewAdapter(Activity activity, Calendar calendar) {
        this.activity = activity;
        this.dates = lunarCalendar.getDaysOfThisMonth(calendar.getTime());
        resources = activity.getResources();
        blackColor = resources.getColor(R.color.black);
        specialColor = resources.getColor(R.color.special);
        whiteColor = resources.getColor(R.color.white);
        this.currentCalendar = (Calendar) calendar.clone();
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public Object getItem(int position) {
        return dates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(activity).inflate(R.layout.day, parent, false);
        }
        solarColor = blackColor;
        lunarColor = Utility.setAlpha(blackColor, 80);

        DayInfo dayInfo = dates.get(position);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dayInfo.getSolarDate());

        TextView dateView = (TextView) convertView.findViewById(R.id.id_tv_date);
        dateView.setText(sdf.format(dayInfo.getSolarDate()));

        TextView solarView = (TextView) convertView.findViewById(R.id.id_tv_solar);
        String solarDay = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        solarView.setText(solarDay);

        TextView lunarView = (TextView) convertView.findViewById(R.id.id_tv_lunar);
        String lunarText = dayInfo.getSpecailDay()==null?dayInfo.getLunarChinaDay():dayInfo.getSpecailDay();
        lunarView.setText(lunarText);

        //设置周末颜色
        if (cal.get(Calendar.DAY_OF_WEEK) == 1 || cal.get(Calendar.DAY_OF_WEEK) == 7) {
            solarColor = specialColor;
        }

        //设置特殊节日的颜色
        if(dayInfo.getSpecailDay()!=null){
            lunarColor = specialColor;
        }

        //设置非当月文字透明度
        if(cal.get(Calendar.MONTH) != currentCalendar.get(Calendar.MONTH)){
            solarColor = Utility.setAlpha(solarColor, 60);
            lunarColor = Utility.setAlpha(lunarColor, 60);
        }

        //设置当天的背景色
         if(sdf.format(cal.getTime()).equals(sdf.format(new Date()))){
             convertView.setBackgroundResource(R.drawable.day_selected_special);
             lunarColor = solarColor = whiteColor;
         }

        solarView.setTextColor(solarColor);
        lunarView.setTextColor(lunarColor);

        return convertView;
    }
}

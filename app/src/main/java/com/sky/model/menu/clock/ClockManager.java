package com.sky.model.menu.clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.SimpleAdapter;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.sky.constantcalendar.R;
import com.sky.plug.widget.WrapContentListView;
import com.sky.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-12.
 */
public class ClockManager {

    private ClockService clockService;
    private Context context;
    private SimpleAdapter adapter;
    private List<Map<String, String>> datas;

    private WrapContentListView listView;
    private View view;

    public ClockManager(Context context) {
        clockService = new ClockDao(context);
        datas = new ArrayList<>();
        this.context = context;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void showGetupData() {

        listView = (WrapContentListView) view.findViewById(R.id.clockList);
        bindActionForListivew();
        queryClockDatas();
        adapter = new SimpleAdapter(context, datas, R.layout.clock_item, new String[]{"clock_id", "clock_time", "clock_name", "clock_date"},
                new int[]{R.id.clock_id, R.id.clock_time, R.id.clock_name, R.id.clock_date});
        listView.setAdapter(adapter);

    }

    private void queryClockDatas() {
        datas.clear();
        List<Clock> list = clockService.queryGetupData();
        CalendarUtil calendarUtil = new CalendarUtil();
        String dateAndWeek = calendarUtil.getCurrentDayAndWeek();
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> map = new HashMap<>();
            map.put("clock_id", list.get(i).getId() + "");
            map.put("clock_time", list.get(i).getTime());
            map.put("clock_name", list.get(i).getTitle());
            map.put("clock_date", dateAndWeek);
            datas.add(map);
        }
    }

    private void bindActionForListivew() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem delMenuItem = new SwipeMenuItem(
                        context.getApplicationContext());
                delMenuItem.setBackground(new ColorDrawable(0XFFE8483F));
                // set item width
                delMenuItem.setWidth(dp2px(60));
                delMenuItem.setTitle("删除");
                delMenuItem.setTitleSize(18);
                delMenuItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(delMenuItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new WrapContentListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        int id = Integer.parseInt(datas.get(position).get("clock_id"));
                        boolean flag = clockService.deleteGetupById(id);
                        if (flag) {
                            //清除条目
                            datas.remove(position);
                            adapter.notifyDataSetChanged();
                            //关闭对应的闹钟
                            closeClock(id);
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void update() {
        queryClockDatas();
        adapter.notifyDataSetChanged();
    }

    //获取屏幕的大小

    private int dp2px(int dp)

    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * 打开起床闹钟
     *
     * @param id
     */
    public void openClock(int id, long millisTimes) {
        Intent intent = new Intent("com.sky.model.clock.action");
        intent.putExtra("id", id);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, millisTimes, pi);//设置闹钟
        } else {
            am.set(AlarmManager.RTC_WAKEUP, millisTimes, pi);//设置闹钟
        }
    }

    /**
     * 关闭起床闹钟
     *
     * @param id
     */
    public void closeClock(int id) {
        Intent intent = new Intent("com.sky.model.clock.action");
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(Activity.ALARM_SERVICE);
        am.cancel(pi);
    }

    public void startAllClock() {
        List<Clock> clockList = clockService.queryGetupData();
        for(Clock clock:clockList){
            //打开起床闹钟
            Calendar triggerTime = Calendar.getInstance();
            triggerTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock.getTime().split(":")[0]));
            triggerTime.set(Calendar.MINUTE, Integer.parseInt(clock.getTime().split(":")[1]));
            triggerTime.set(Calendar.SECOND, 0);
            openClock(clock.getId(), clock.getTriggerTime());
        }
    }
}

package com.sky.model.menu.clock.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sky.model.menu.clock.ClockManager;

/**
 * Created by Administrator on 17-8-22.
 */
public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            //重新计算闹铃时间，并调第一步的方法设置闹铃时间及闹铃间隔时间
            ClockManager clockManager = new ClockManager(context);
            clockManager.startAllClock();
        }
    }
}

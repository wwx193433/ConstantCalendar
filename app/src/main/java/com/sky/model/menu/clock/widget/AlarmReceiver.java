package com.sky.model.menu.clock.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sky.model.menu.clock.RingActivity;

/**
 * Created by Administrator on 17-8-18.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals("com.sky.model.clock.action")){
            int id = intent.getIntExtra("id", -1);
            Intent i = new Intent();
            i.setAction("com.sky.model.clock.action");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setClass(context, RingActivity.class);
            i.putExtra("id", id);
            context.startActivity(i);
        }
    }
}

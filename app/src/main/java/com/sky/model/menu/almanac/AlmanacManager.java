package com.sky.model.menu.almanac;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;

/**
 * Created by Administrator on 17-8-27.
 */
public class AlmanacManager implements View.OnClickListener {

    private Activity activity;
    private View view;
    private LinearLayout almanac_layout;
    private TextView solar_text;

    public AlmanacManager(Activity activity) {
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void init() {
        almanac_layout = (LinearLayout) view.findViewById(R.id.almanac_layout);
        almanac_layout.setOnClickListener(this);
        solar_text = (TextView) view.findViewById(R.id.solar_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.almanac_layout:
                Intent intent = new Intent(activity, AlmanacActivity.class);
                intent.putExtra("solarDateString", solar_text.getText().toString());
                activity.startActivity(intent);
                break;
            default:
                break;
        }

    }
}

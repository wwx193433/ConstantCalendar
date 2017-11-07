package com.sky.model.menu.almanac;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.APIUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 17-8-27.
 */
public class AlmanacManager implements View.OnClickListener {

    private Activity activity;
    private View view;
    private IconFontTextview should, avoid;
    private LinearLayout almanac_layout;
    private TextView solar_text;
    private Almanac am;
    private Date solarDate = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AlmanacManager(Activity activity) {
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void init(){
        almanac_layout = (LinearLayout) view.findViewById(R.id.almanac_layout);
        almanac_layout.setOnClickListener(this);
        solar_text = (TextView) view.findViewById(R.id.solar_text);

        should = (IconFontTextview) view.findViewById(R.id.should);
        avoid = (IconFontTextview) view.findViewById(R.id.avoid);

        String solarDateString = solar_text.getText().toString();
        if(!solarDateString.equals("")){
            try {
                solarDate = sdf.parse(solarDateString);
                getYJ();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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

    public void getYJ() {
        final Handler myHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                am = APIUtil.getAlmanac(solarDate);
                myHandler.post(new Thread() {
                    public void run() {
                        if (am!=null) {
                            //设置宜忌
                            should.setIconText(am.getYi());
                            avoid.setIconText(am.getJi());
                        }else{
                            should.setIconText("无");
                            avoid.setIconText("无");
                        }

                    }
                });
            }
        }).start();
    }
}

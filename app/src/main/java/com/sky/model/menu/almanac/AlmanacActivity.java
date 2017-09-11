package com.sky.model.menu.almanac;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.calendar.widget.DayInfo;
import com.sky.model.calendar.widget.LunarCalendar;
import com.sky.plug.widget.FangzhengFontTextView;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.APIUtil;
import com.sky.util.CalendarUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 17-8-26.
 */
public class AlmanacActivity extends Activity implements SensorEventListener, View.OnClickListener {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private LunarCalendar lunarCalendar;
    private CalendarUtil calendarUtil;

    private Calendar currentDate;

    private int defaultColor, specialColor;

    private FrameLayout round_frame;

    //罗盘
    private ImageView roundImage;
    //传感器管理器
    private SensorManager manager;
    //罗盘转动度数
    float predegree = 0;

    //阳历日期
    private IconFontTextview am_solarDate, am_lunar_yestoday, am_lunar_tomorrow;
    //农历日期
    private FangzhengFontTextView am_lunarDate;
    //干支
    private TextView am_ganzhi;
    //宜、忌
    private IconFontTextview icon_back, am_yi, am_ji;
    //五行、十二神、吉神宜趋、值神、冲煞、胎神吉方、彭祖百忌
    private TextView am_wuxing, am_sheng12, am_jsyq, am_zhiri, am_chongsha, am_tszf, am_jrhh, am_pzbj, am_cai, am_xi;
    //时辰宜忌
    private TextView am_zishi, am_choushi, am_yinshi, am_maoshi, am_chenshi, am_sishi, am_wushi, am_weishi,am_shenshi, am_youshi, am_xushi, am_haishi;
    private LinearLayout scLayout;

    private LinearLayout ll_yj, ll_wx, ll_jc, ll_js, ll_lp, ll_zs, ll_cs, ll_ts, ll_jrhh, ll_pzbj, ll_scyj;

    private Almanac am;

    @Override
    protected void onResume() {
        /**
         * 获取方向传感器
         * 通过SensorManager对象获取相应的Sensor类型的对象
         */
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        //应用在前台时候注册监听器
        manager.registerListener(this, sensor,
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.almanac);
        //初始化组件
        initViews();

        //设置点击事件
        setClickEvent();

        //设置页面数据
        setPageData();

    }

    private void setClickEvent() {

        icon_back.setOnClickListener(this);
        am_lunar_yestoday.setOnClickListener(this);
        am_lunar_tomorrow.setOnClickListener(this);
        ll_yj.setOnClickListener(this);
        ll_wx.setOnClickListener(this);
        ll_jc.setOnClickListener(this);
        ll_js.setOnClickListener(this);
        ll_lp.setOnClickListener(this);
        ll_zs.setOnClickListener(this);
        ll_cs.setOnClickListener(this);
        ll_ts.setOnClickListener(this);
        ll_jrhh.setOnClickListener(this);
        ll_pzbj.setOnClickListener(this);
        ll_scyj.setOnClickListener(this);
    }

    private void setPageData() {
        currentDate = Calendar.getInstance();

        //获取阳历日期
        String solarDateString = getIntent().getStringExtra("solarDateString");
        try {
            Date date = sdf.parse(solarDateString);
            currentDate.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //调用API黄历数据
        dispatchAndSetAlmanacData();
    }

    /**
     * 初始化组件
     */
    public void initViews() {
        lunarCalendar = new LunarCalendar();
        calendarUtil = new CalendarUtil();
        round_frame = (FrameLayout) this.findViewById(R.id.round_frame);
        roundImage = (ImageView) this.findViewById(R.id.roundImage);
        round_frame.setKeepScreenOn(true);//屏幕高亮
        //获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        //设置颜色
        defaultColor = getResources().getColor(R.color.defaultColor);
        specialColor = getResources().getColor(R.color.soil);

        am_solarDate = (IconFontTextview) this.findViewById(R.id.am_solarDate);
        am_lunar_yestoday = (IconFontTextview) this.findViewById(R.id.am_lunar_yestoday);
        am_lunar_tomorrow = (IconFontTextview) this.findViewById(R.id.am_lunar_tomorrow);
        am_lunarDate = (FangzhengFontTextView) this.findViewById(R.id.am_lunarDate);
        am_ganzhi = (TextView) this.findViewById(R.id.am_ganzhi);
        icon_back = (IconFontTextview) this.findViewById(R.id.icon_back);
        am_yi = (IconFontTextview) this.findViewById(R.id.am_yi);
        am_ji = (IconFontTextview) this.findViewById(R.id.am_ji);
        am_wuxing = (TextView) this.findViewById(R.id.am_wuxing);
        am_sheng12 = (TextView) this.findViewById(R.id.am_sheng12);
        am_jsyq = (TextView) this.findViewById(R.id.am_jsyq);
        am_zhiri = (TextView) this.findViewById(R.id.am_zhiri);
        am_chongsha = (TextView) this.findViewById(R.id.am_chongsha);
        am_tszf = (TextView) this.findViewById(R.id.am_tszf);
        am_jrhh = (TextView) this.findViewById(R.id.am_jrhh);
        am_pzbj = (TextView) this.findViewById(R.id.am_pzbj);
        am_cai = (TextView) this.findViewById(R.id.am_cai);
        am_xi = (TextView) this.findViewById(R.id.am_xi);

        scLayout = (LinearLayout) this.findViewById(R.id.scLayout);
        am_zishi = (TextView) this.findViewById(R.id.am_zishi);
        am_choushi = (TextView) this.findViewById(R.id.am_choushi);
        am_yinshi = (TextView) this.findViewById(R.id.am_yinshi);
        am_maoshi = (TextView) this.findViewById(R.id.am_maoshi);
        am_chenshi = (TextView) this.findViewById(R.id.am_chenshi);
        am_sishi = (TextView) this.findViewById(R.id.am_sishi);
        am_wushi = (TextView) this.findViewById(R.id.am_wushi);
        am_weishi = (TextView) this.findViewById(R.id.am_weishi);
        am_shenshi = (TextView) this.findViewById(R.id.am_shenshi);
        am_youshi = (TextView) this.findViewById(R.id.am_youshi);
        am_xushi = (TextView) this.findViewById(R.id.am_xushi);
        am_haishi = (TextView) this.findViewById(R.id.am_haishi);

        ll_yj = (LinearLayout) this.findViewById(R.id.ll_yiji);
        ll_wx = (LinearLayout) this.findViewById(R.id.ll_wuxing);
        ll_jc = (LinearLayout) this.findViewById(R.id.ll_jc);
        ll_js = (LinearLayout) this.findViewById(R.id.ll_js);
        ll_lp = (LinearLayout) this.findViewById(R.id.ll_lp);
        ll_zs = (LinearLayout) this.findViewById(R.id.ll_zs);
        ll_cs = (LinearLayout) this.findViewById(R.id.ll_cs);
        ll_ts = (LinearLayout) this.findViewById(R.id.ll_ts);
        ll_jrhh = (LinearLayout) this.findViewById(R.id.ll_jrhh);
        ll_pzbj = (LinearLayout) this.findViewById(R.id.ll_pzbj);
        ll_scyj = (LinearLayout) this.findViewById(R.id.ll_scyj);
    }

    /**
     * 调用API黄历数据
     */
    private void dispatchAndSetAlmanacData() {
        final Handler myHandler = new Handler();
        final Date date = currentDate.getTime();

        //获取农历和星期
        DayInfo dayInfo = lunarCalendar.solarToLunar(date);

        final String week = calendarUtil.getWeekDay(dayInfo.getSolarDate());

        //设置阳历日期
        am_solarDate.setIconText(sdf.format(date));

        //设置农历日期
        String lunarDateString = calendarUtil.getLunarDate(dayInfo.getSolarDate());
        am_lunarDate.setText(lunarDateString);

        new Thread() {
            public void run() {
                am = APIUtil.getAlmanac(date);
                myHandler.post(new Thread() {
                    public void run() {
                        //设置干支
                        am_ganzhi.setText(am.getGanzhi() + " " + week);

                        //设置宜忌
                        am_yi.setIconText(am.getYi());
                        am_ji.setIconText(am.getJi());

                        //设置五行
                        am_wuxing.setText(am.getWuxing());
                        //设置十二神
                        am_sheng12.setText(am.getSheng12());
                        //设置吉神宜趋
                        am_jsyq.setText(am.getJsyq());
                        //设置值神
                        am_zhiri.setText(am.getZhiri());
                        //设置冲煞
                        am_chongsha.setText(am.getChongsha());
                        //设置胎神吉方
                        am_tszf.setText(am.getTszf());
                        //设置胎神吉方
                        am_jrhh.setText(am.getJrhh());
                        //设置彭祖百忌
                        am_pzbj.setText(am.getPzbj());

                        //设置财喜
                        am_cai.setText(am.getCai());
                        am_xi.setText(am.getXi());

                        //设置时辰宜忌
                        if(null == am.getShichenMap()){
                            return;
                        }
                        am_zishi.setText(am.getShichenMap().get("zishi").get("jixiong"));
                        am_choushi.setText(am.getShichenMap().get("choushi").get("jixiong"));
                        am_yinshi.setText(am.getShichenMap().get("yinshi").get("jixiong"));
                        am_maoshi.setText(am.getShichenMap().get("maoshi").get("jixiong"));
                        am_chenshi.setText(am.getShichenMap().get("chenshi").get("jixiong"));
                        am_sishi.setText(am.getShichenMap().get("sishi").get("jixiong"));
                        am_wushi.setText(am.getShichenMap().get("wushi").get("jixiong"));
                        am_weishi.setText(am.getShichenMap().get("weishi").get("jixiong"));
                        am_shenshi.setText(am.getShichenMap().get("shenshi").get("jixiong"));
                        am_youshi.setText(am.getShichenMap().get("youshi").get("jixiong"));
                        am_xushi.setText(am.getShichenMap().get("xushi").get("jixiong"));
                        am_haishi.setText(am.getShichenMap().get("haishi").get("jixiong"));

                        //标注当前时辰
                        setCurrentShichen();
                    }
                });
            }
        }.start();
    }

    /**
     * 标注当前时辰
     */
    private void setCurrentShichen() {
        int index = getCurrentShichenIndex();
        for(int i = 0;i<scLayout.getChildCount();i++){
            LinearLayout layout = (LinearLayout)scLayout.getChildAt(i);
            ((TextView)layout.getChildAt(0)).setTextColor(defaultColor);
            ((TextView)layout.getChildAt(1)).setTextColor(defaultColor);
            ((TextView)layout.getChildAt(2)).setTextColor(defaultColor);
        }
        if(index>-1){
            ((TextView)((LinearLayout)scLayout.getChildAt(index)).getChildAt(0)).setTextColor(specialColor);
            ((TextView)((LinearLayout)scLayout.getChildAt(index)).getChildAt(1)).setTextColor(specialColor);
            ((TextView)((LinearLayout)scLayout.getChildAt(index)).getChildAt(2)).setTextColor(specialColor);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.icon_back:
                finish();
                break;
            case R.id.am_lunar_yestoday:
                currentDate.add(Calendar.DAY_OF_MONTH, -1);
                dispatchAndSetAlmanacData();
                break;
            case R.id.am_lunar_tomorrow:
                currentDate.add(Calendar.DAY_OF_MONTH, 1);
                dispatchAndSetAlmanacData();
                break;
//            case R.id.ll_yiji:
//                Intent intent = new Intent(this, AlmanacInfoActivity.class);
//                startActivity(intent);
//                break;
            case R.id.ll_scyj:
                Intent scIntent = new Intent(this, ShichenActivity.class);
                //意图放置bundle变量
                scIntent.putExtra("almanac", am);
                startActivity(scIntent);
                break;
            case R.id.ll_lp:
                Intent lpIntent = new Intent(this, RoundActivity.class);
                Log.i("tags", am.toString());
                String cai = am.getCai();
                String xi = am.getXi();
                lpIntent.putExtra("cai", cai);
                lpIntent.putExtra("xi", xi);
                startActivity(lpIntent);
                break;
            default:
                break;
        }

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float degree = event.values[0];// 存放了方向值
        /**动画效果*/
        RotateAnimation animation = new RotateAnimation(predegree, -degree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(50);
        animation.setFillAfter(true);
        round_frame.startAnimation(animation);
        predegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public int getCurrentShichenIndex(){
        int index = -1;
        SimpleDateFormat hhFormat = new SimpleDateFormat("HH");
        String hh = hhFormat.format(new Date());
        if(!sdf.format(currentDate.getTime()).equals(sdf.format(new Date()))){
            return index;
        }
        switch(hh){
            case "00": case "23":
                index = 0;//子时、壬子时
                break;
            case "01": case "02":
                index = 1;//丑时、癸丑时
                break;
            case "03": case "04":
                index = 2;//寅时、甲寅时
                break;
            case "05": case "06":
                index = 3;//卯时、乙卯时
                break;
            case "07": case "08":
                index = 4;//辰时、丙辰时
                break;
            case "09": case "10":
                index = 5;//巳时、丁巳时
                break;
            case "11": case "12":
                index = 6;//午时、戊午时
                break;
            case "13": case "14":
                index = 7;//未时、己未时
                break;
            case "15": case "16":
                index = 8;//申时、庚申时
                break;
            case "17": case "18":
                index = 9;//酉时、辛酉时
                break;
            case "19": case "20":
                index = 10;//戌时、壬戌时
                break;
            case "21": case "22":
                index = 11;//亥时、癸亥时
                break;
            default:
                break;
        }
        return index;
    }
}

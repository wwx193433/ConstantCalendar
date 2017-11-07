package com.sky.model.weather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.util.BaiduMap;
import com.sky.util.Utility;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-9.
 */
public class WeatherManager {

    public static final String TAG = "WEATHER_REPORT";
    private static final int GET_WEATHER_REPORT_SUCCESS = 1;
    private Handler wHandler;
    private ImageView weather_type_image;
    private View view;
    private Activity activity;
    private LinearLayout weather_report;
    private BaiduMap baiduMap;
    private WeatherService weatherService;
    private BroadcastReceiver broadcastReceiver;
    private String weatherId;

    public static int INIT_WEATHER_REQUESTCODE = 0;

    private int green, red, orange,maroon,yellow, purple;

    public static String LOCATION_BCR = "location_bcr";

    public WeatherManager(Activity activity) {
        this.activity = activity;
        green = ContextCompat.getColor(activity, R.color.pm_green);
        yellow = ContextCompat.getColor(activity, R.color.pm_yellow);
        orange = ContextCompat.getColor(activity, R.color.pm_orange);
        red = ContextCompat.getColor(activity, R.color.pm_red);
        purple = ContextCompat.getColor(activity, R.color.pm_purple);
        maroon = ContextCompat.getColor(activity, R.color.pm_maroon);
    }

    private TextView report_wendu, report_type, report_high_low, report_pm, report_cityname, report_shidu, report_fengxiang;

    public void setView(View view) {
        this.view = view;
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case GET_WEATHER_REPORT_SUCCESS:
                    updateWeatherReportForm((Weather) msg.obj);
                    break;
                default:
                    break;
            }

        }
    };

    private void updateWeatherReportForm(Weather weather) {
        report_wendu.setText(weather.getWendu() + "°");
        String type = weather.getType();
        if(null!=type && !type.equals("")){
            report_type.setText(type);
            int dayTypeImage = getDayTypeImage(weather.getType());
            weather_type_image.setImageResource(dayTypeImage);
        }
        if(null!=weather.getPm25()){
            int pm25 = Integer.parseInt(weather.getPm25());
            int color = getPmColor(pm25);
            report_pm.setBackgroundColor(color);
            report_pm.setText(weather.getPm25() + " " + weather.getQuality());
        }
        String wendu_range= Utility.filterNumber(weather.getHigh())+"°/"+ Utility.filterNumber(weather.getLow())+"°";
        report_high_low.setText(wendu_range);
        report_cityname.setText(weather.getCity());
        report_shidu.setText(weather.getShidu());
        report_fengxiang.setText(weather.getFengxiang() + weather.getFengli());

    }

    public void init() {
        weatherService = new WeatherDao(activity);

        weather_report = (LinearLayout) view.findViewById(R.id.weather_report);
        weather_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weatherId == null || weatherId.equals("")){
                    return;
                }
                Intent intent = new Intent(activity, WeatherActivity.class);
                intent.putExtra("weatherId", weatherId);
                activity.startActivityForResult(intent, INIT_WEATHER_REQUESTCODE);
            }
        });

        weather_type_image = (ImageView) view.findViewById(R.id.weather_type_image);
        report_wendu = (TextView) view.findViewById(R.id.report_wendu);
        report_type = (TextView) view.findViewById(R.id.report_type);
        report_high_low = (TextView) view.findViewById(R.id.report_high_low);
        report_pm = (TextView) view.findViewById(R.id.report_pm);
        report_cityname = (TextView) view.findViewById(R.id.report_cityname);
        report_shidu = (TextView) view.findViewById(R.id.report_shidu);
        report_fengxiang = (TextView) view.findViewById(R.id.report_fengxiang);

        //注册广播
        registerBroadCastReceiver();

        //百度定位
        baiduMap = new BaiduMap(activity);
        baiduMap.locateStart();
    }

    public void unregisterBroadCastReceiver(){
        activity.unregisterReceiver(broadcastReceiver);
    }

    /**
     * 注册广播监听位置
     */
    private void registerBroadCastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String areaName = intent.getStringExtra("areaName");
                if(areaName!=null && !areaName.equals("")){
                    City city = weatherService.queryCityByName(areaName);
                    getDayWeatherReportData(city.getWeatherId(), handler);
                    weatherId =city.getWeatherId();
                    weatherService.saveMySelectedCity(city.getAreaId());
                }
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(LOCATION_BCR);
        activity.registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }
    

    /**
     * 获取天气数据
     *
     * @param weatherId
     */
    public void getDayWeatherReportData(final String weatherId, final Handler handler) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + weatherId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    String response = sb.toString();
                    if (null != response && !response.equals("")) {
                        Message message = new Message();
                        Weather weather = parseOneDayData(response);
                        if (weather != null) {
                            weather.setWeatherId(weatherId);
                            message.what = GET_WEATHER_REPORT_SUCCESS;
                            message.obj = weather;
                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getWeekWeatherReportData(final String weatherId, Handler handler) {
        wHandler = handler;
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + weatherId;
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    String response = sb.toString();
                    if (null != response && !response.equals("")) {
                        Weather weather = parseOneDayData(response);
                        if (weather != null) {
                            Message dayMessage = new Message();
                            weather.setWeatherId(weatherId);
                            dayMessage.what = 1;
                            dayMessage.obj = weather;
                            wHandler.sendMessage(dayMessage);
                        }

                        //获取一周数据
                        List<Map<String, Object>> weekWeatherData = parseOneWeekData(response);
                        if (weekWeatherData.size() > 0) {
                            Message weekMessage = new Message();
                            weekMessage.what = 2;
                            weekMessage.obj = weekWeatherData;
                            wHandler.sendMessage(weekMessage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 获取天气类型对应的图标
     *
     * @param type
     * @return
     */
    public int getTypeIcon(String type) {
        int icon = 0;
        switch (type) {
            case "晴":
                icon = R.string.icon_qing;
                break;
            case "阴":
                icon = R.string.icon_yin;
                break;
            case "雾":
                icon = R.string.icon_wu;
                break;
            case "多云":
                icon = R.string.icon_duoyun;
                break;
            case "小雨":
                icon = R.string.icon_xiaoyu;
                break;
            case "中雨":
                icon = R.string.icon_zhongyu;
                break;
            case "大雨":
                icon = R.string.icon_dayu;
                break;
            case "阵雨":
                icon = R.string.icon_zhenyu;
                break;
            case "雷阵雨":
                icon = R.string.icon_leizhenyu;
                break;
            case "暴雨":
                icon = R.string.icon_baoyu;
                break;
            case "大暴雨":
                icon = R.string.icon_dabaoyu;
                break;
            case "特大暴雨":
                icon = R.string.icon_tedabaoxue;
                break;
            case "阵雪":
                icon = R.string.icon_zhenxue;
                break;
            case "暴雪":
                icon = R.string.icon_baoxue;
                break;
            case "大雪":
                icon = R.string.icon_daxue;
                break;
            case "小雪":
                icon = R.string.icon_xiaoxue;
                break;
            case "雨夹雪":
                icon = R.string.icon_yujiaxue;
                break;
            case "中雪":
                icon = R.string.icon_zhongxue;
                break;
            case "沙尘暴":
                icon = R.string.icon_shachenbao;
                break;
            default:
                break;
        }
        return icon;
    }

    public int getNightTypeImage(String type){
        int dayIcon = 0;
        switch (type) {
            case "晴":
                dayIcon = R.drawable.w_qing_night;
                break;
            case "阴":
                dayIcon = R.drawable.w_yin_night;
                break;
            case "雾":
                dayIcon = R.drawable.w_wu_night;
                break;
            case "霾":
                dayIcon = R.drawable.w_wumai_night;
                break;
            case "多云":
                dayIcon = R.drawable.w_duoyun_night;
                break;
            case "小雨":
                dayIcon = R.drawable.w_xiaoyu_night;
                break;
            case "中雨":
                dayIcon = R.drawable.w_zhongyu_night;
                break;
            case "大雨":
                dayIcon = R.drawable.w_dayu_night;
                break;
            case "阵雨":
                dayIcon = R.drawable.w_zhneyu_night;
                break;
            case "雷阵雨":
                dayIcon = R.drawable.w_leizhenyu_night;
                break;
            case "暴雨":
                dayIcon = R.drawable.w_baoyu_night;
                break;
            case "大暴雨":
                dayIcon = R.drawable.w_baoyu_night;
                break;
            case "特大暴雨":
                dayIcon = R.drawable.w_baoyu_night;
                break;
            case "阵雪":
                dayIcon = R.drawable.w_xiaoxue_night;
                break;
            case "暴雪":
                dayIcon = R.drawable.w_baoxue_night;
                break;
            case "大雪":
                dayIcon = R.drawable.w_daxue_night;
                break;
            case "小雪":
                dayIcon = R.drawable.w_xiaoxue_night;
                break;
            case "雨夹雪":
                dayIcon = R.drawable.w_xiaoxue_night;
                break;
            case "中雪":
                dayIcon = R.drawable.w_zhongxue_night;
                break;
            case "沙尘暴":
                dayIcon = R.drawable.w_wumai_night;
                break;
            default:
                break;
        }
        return dayIcon;
    }

    public int getDayTypeImage(String type){
        int dayIcon = 0;
        switch (type) {
            case "晴":
                dayIcon = R.drawable.w_qing_day;
                break;
            case "阴":
                dayIcon = R.drawable.w_yin_day;
                break;
            case "雾":
                dayIcon = R.drawable.w_wu_day;
                break;
            case "雾霾":
                dayIcon = R.drawable.w_wumai_day;
                break;
            case "多云":
                dayIcon = R.drawable.w_duoyun_day;
                break;
            case "小雨":
                dayIcon = R.drawable.w_xiaoyu_day;
                break;
            case "中雨":
                dayIcon = R.drawable.w_zhongyu_day;
                break;
            case "大雨":
                dayIcon = R.drawable.w_dayu_day;
                break;
            case "阵雨":
                dayIcon = R.drawable.w_zhneyu_night;
                break;
            case "雷阵雨":
                dayIcon = R.drawable.w_leizhenyu_day;
                break;
            case "暴雨":
                dayIcon = R.drawable.w_baoyu_day;
                break;
            case "大暴雨":
                dayIcon = R.drawable.w_baoyu_day;
                break;
            case "特大暴雨":
                dayIcon = R.drawable.w_baoyu_day;
                break;
            case "阵雪":
                dayIcon = R.drawable.w_xiaoxue_day;
                break;
            case "暴雪":
                dayIcon = R.drawable.w_baoxue_day;
                break;
            case "大雪":
                dayIcon = R.drawable.w_daxue_day;
                break;
            case "小雪":
                dayIcon = R.drawable.w_xiaoxue_day;
                break;
            case "雨夹雪":
                dayIcon = R.drawable.w_xiaoxue_day;
                break;
            case "中雪":
                dayIcon = R.drawable.w_zhongxue_day;
                break;
            case "沙尘暴":
                dayIcon = R.drawable.w_wumai_day;
                break;
            default:
                break;
        }
        return dayIcon;
    }

    public int getBackground(String type) {
        int backImage = 0;
        switch (type) {
            case "晴":
                backImage =R.drawable.w_pic_qing_day;
                break;
            case "阴":
                backImage =R.drawable.w_pic_yin_day;
                break;
            case "雾":
                backImage =R.drawable.w_pic_wu_day;
                break;
            case "霾":
                backImage =R.drawable.w_pic_mai;
                break;
            case "多云":
                backImage =R.drawable.w_pic_duoyun_day;
                break;
            case "小雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "中雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "大雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "阵雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "雷阵雨":
                backImage =R.drawable.w_pic_leiyu;
                break;
            case "暴雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "大暴雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "特大暴雨":
                backImage =R.drawable.w_pic_yu;
                break;
            case "阵雪":
                backImage =R.drawable.w_pic_xue;
                break;
            case "暴雪":
                backImage =R.drawable.w_pic_xue;
                break;
            case "大雪":
                backImage =R.drawable.w_pic_xue;
                break;
            case "小雪":
                backImage =R.drawable.w_pic_xue;
                break;
            case "雨夹雪":
                backImage =R.drawable.w_pic_yujiaxue;
                break;
            case "中雪":
                backImage =R.drawable.w_pic_xue;
                break;
            case "沙尘暴":
                backImage =R.drawable.w_pic_sha;
                break;
            default:
                break;
        }
        return backImage;
    }

    /**
     * 解析天气数据xml
     */
    public Weather parseOneDayData(String xmlData) {
        int fengliCount = 0;
        int fengxiangCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        Weather weather = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Map<String, String> daymap = new HashMap<>();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "start parse weather report xml!!");
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            weather = new Weather();
                        }
                        if (null != weather) {

                            if (xmlPullParser.getName().equals("city")) {//城市
                                eventType = xmlPullParser.next();
                                weather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {//更新时间
                                eventType = xmlPullParser.next();
                                weather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {//温度
                                eventType = xmlPullParser.next();
                                weather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {//风力
                                eventType = xmlPullParser.next();
                                weather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("shidu")) {//湿度
                                eventType = xmlPullParser.next();
                                weather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {//风向
                                eventType = xmlPullParser.next();
                                weather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("pm25")) {//PM
                                eventType = xmlPullParser.next();
                                weather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {//环境空气质量
                                eventType = xmlPullParser.next();
                                weather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {//日期
                                eventType = xmlPullParser.next();
                                weather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {//最高温度
                                eventType = xmlPullParser.next();
                                weather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {//最低温度
                                eventType = xmlPullParser.next();
                                weather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {//天气类型
                                eventType = xmlPullParser.next();
                                weather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return weather;
    }


    public List<Map<String, Object>> parseOneWeekData(String xmlData) {
        int count = -1;
        List<Map<String, Object>> mapList = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            Map<String, Object> daymap = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.i(TAG, "start parse weather report xml!!");
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("yesterday")) {
                            mapList = new ArrayList<>();
                        }
                        if (null != mapList) {
                            String nodeName = xmlPullParser.getName();
                            if (nodeName.equals("date_1") || nodeName.equals("date")) {//日期
                                daymap = new HashMap<>();
                                eventType = xmlPullParser.next();
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(Calendar.DAY_OF_MONTH, count);
                                daymap.put("date", calendar.getTime());
                                count++;
                            } else if (nodeName.equals("high_1") || nodeName.equals("high")) {//高温
                                eventType = xmlPullParser.next();
                                daymap.put("high", Utility.filterNumber(xmlPullParser.getText()));
                            } else if (nodeName.equals("low_1") || nodeName.equals("low")) {//低温
                                eventType = xmlPullParser.next();
                                daymap.put("low", Utility.filterNumber(xmlPullParser.getText()));
                            } else if (nodeName.equals("type_1") || nodeName.equals("type")) {//天气类型
                                eventType = xmlPullParser.next();
                                if (!daymap.containsKey("type_day")) {
                                    daymap.put("type_day", xmlPullParser.getText());
                                } else {
                                    daymap.put("type_night", xmlPullParser.getText());
                                }
                            } else if (xmlPullParser.getName().equals("fx_1") || xmlPullParser.getName().equals("fengxiang")) {//温度
                                eventType = xmlPullParser.next();
                                if (!daymap.containsKey("fengxiang_day")) {
                                    daymap.put("fengxiang_day", xmlPullParser.getText());
                                } else {
                                    daymap.put("fengxiang_night", xmlPullParser.getText());
                                }
                            } else if (xmlPullParser.getName().equals("fengli") || xmlPullParser.getName().equals("fl_1")) {//风力
                                eventType = xmlPullParser.next();
                                if (!daymap.containsKey("fengli_day")) {
                                    daymap.put("fengli_day", xmlPullParser.getText());
                                } else {
                                    daymap.put("fengli_night", xmlPullParser.getText());
                                    mapList.add(daymap);
                                }
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return mapList;
    }

    /**
     * 获取PM值对应的颜色
     * @param pm25
     * @return
     */
    public int getPmColor(int pm25) {
        int color = 0;
        if (pm25 <= 50) {
            color = green;
        } else if (pm25 >= 51 && pm25 <= 100) {
            color = yellow;
        } else if (pm25 >= 101 && pm25 <= 150) {
            color = orange;
        } else if (pm25 >= 151 && pm25 <= 200) {
            color = red;
        } else if (pm25 >= 201 && pm25 <= 300) {
            color = purple;
        }else{
            color = maroon;
        }
        return color;
    }
}

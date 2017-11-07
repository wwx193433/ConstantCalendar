package com.sky.model.weather;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.CustomProgressDialog;
import com.sky.plug.widget.IconFontButton;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.Utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-5.
 */
public class WeatherActivity extends Activity implements View.OnClickListener{
    private TextView wt_type, wt_pm, wt_wendu;
    private IconFontTextview wt_wendu_range, wt_shidu, wt_fengli, wt_pm_icon;
    private ImageView wt_type_icon;
    private IconFontButton city_setting_btn, backBtn;
    private WeatherChartView weatherChart;
    private ScrollView weather_view;

    private WeatherService weatherService;
    private WeatherManager weatherManager;
    private Dialog progressDialog;
    private Spinner citySpinner;
    private List<Map<String, String>> citys;
    private GridView dayWeatherGrid, nightWeatherGrid;
    private SimpleAdapter spinnerAdapter;
    private List<Map<String, String>> weekChartData;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progressDialog.hide();
            switch(msg.what){
                case 1:
                    updateTodayWeatherForm((Weather) msg.obj);
                    break;
                case 2:
                    updateWeekWeatherForm((List<Map<String, Object>>) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 更新一周天气表
     * @param weekWeatherData
     */
    private void updateWeekWeatherForm(List<Map<String, Object>> weekWeatherData) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");

        List<Map<String, Object>> weatherChartList = new ArrayList<>();
        int[] highData = new int[weekWeatherData.size()];
        int[] lowData = new int[weekWeatherData.size()];

        for(int i =0;i<weekWeatherData.size();i++){
            Map<String, Object> map = weekWeatherData.get(i);
            Map<String, Object> datamap = new HashMap<>();
            Date date = (Date) map.get("date");
            String type_day = (String) map.get("type_day");
            String type_night = (String) map.get("type_night");
            datamap.put("wt_chart_weekday", Utility.getWeekDay(date));
            datamap.put("wt_chart_date", sdf.format(date));
            datamap.put("wt_chart_typeday_icon", weatherManager.getDayTypeImage(type_day));
            datamap.put("wt_chart_typenight_icon", weatherManager.getNightTypeImage(type_night));
            datamap.put("wt_chart_typeday", type_day);
            datamap.put("wt_chart_typenight", type_night);
            weatherChartList.add(datamap);
            highData[i] = Integer.parseInt((String)map.get("high"));
            lowData[i] = Integer.parseInt((String)map.get("low"));
        }
        weatherChart.setHighPoints(highData);
        weatherChart.setLowPoints(lowData);
        weatherChart.invalidate();

        WeekChartAdapter dayWeatherAdapter = new WeekChartAdapter(this, weatherChartList, 0);
        dayWeatherGrid.setAdapter(dayWeatherAdapter);
        WeekChartAdapter nightWeatherAdapter = new WeekChartAdapter(this, weatherChartList, 1);
        nightWeatherGrid.setAdapter(nightWeatherAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_frame);

        initViews();

        //根据城市代码获取天气数据
        String weatherId = getIntent().getStringExtra("weatherId");
        setSpinnerItemSelected(weatherId);

    }

    private void initViews() {
        progressDialog = CustomProgressDialog.createLoadingDialog(this, "正在加载中...");
        progressDialog.show();

        weatherService = new WeatherDao(this);

        weather_view = (ScrollView) this.findViewById(R.id.weather_view);

        citySpinner = (Spinner) this.findViewById(R.id.citySpinner);
        citys = new ArrayList<>();
        weekChartData = new ArrayList<>();

        weatherChart = (WeatherChartView) this.findViewById(R.id.weather_chart);

        city_setting_btn = (IconFontButton) this.findViewById(R.id.city_setting_btn);
        backBtn = (IconFontButton) this.findViewById(R.id.backBtn);

        dayWeatherGrid = (GridView) this.findViewById(R.id.day_weather_grid);
        nightWeatherGrid = (GridView) this.findViewById(R.id.night_weather_grid);

        wt_wendu = (TextView) this.findViewById(R.id.wt_wendu);
        wt_type = (TextView) this.findViewById(R.id.wt_type);
        wt_pm = (TextView) this.findViewById(R.id.wt_pm);

        wt_pm_icon = (IconFontTextview) this.findViewById(R.id.wt_pm_icon);
        wt_wendu_range = (IconFontTextview) this.findViewById(R.id.wt_wendu_range);
        wt_shidu = (IconFontTextview) this.findViewById(R.id.wt_shidu);
        wt_fengli = (IconFontTextview) this.findViewById(R.id.wt_fengli);
        wt_type_icon = (ImageView) this.findViewById(R.id.wt_type_icon);

        weatherManager = new WeatherManager(this);

        getCitys();
        //适配器
        spinnerAdapter = new SimpleAdapter(this, citys, R.layout.weather_city_spinner_item,
                new String[]{"title_city_name"}, new int[]{R.id.title_city_name});

        //加载适配器
        citySpinner.setAdapter(spinnerAdapter);
        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progressDialog.show();
                weatherManager.getWeekWeatherReportData(citys.get(position).get("weatherId"), handler);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        city_setting_btn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.city_setting_btn:
                Intent intent = new Intent(this, CityActivity.class);
                this.startActivityForResult(intent, 1);
                break;
            case R.id.backBtn:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==4){
            if(data != null) {
                String weatherId = data.getStringExtra("weatherId");
                getCitys();
                spinnerAdapter.notifyDataSetChanged();
                setSpinnerItemSelected(weatherId);
            }
        }
        if(requestCode==1 && resultCode==5){
            if(data != null) {
                String weatherId = data.getStringExtra("weatherId");
                setSpinnerItemSelected(weatherId);
            }
        }
        if(requestCode==1 && resultCode==6){
            getCitys();
            spinnerAdapter.notifyDataSetChanged();
        }
    }

    public void setSpinnerItemSelected(String value){
        for(int i=0;i<citys.size();i++){
            if(value.equals(citys.get(i).get("weatherId"))){
                citySpinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }

    public void updateTodayWeatherForm(Weather weather){
        wt_wendu.setText(weather.getWendu());
        wt_type.setText(weather.getType());
        int backImageResId = 0;
        if(null!=weather.getType()){
            int imgId = weatherManager.getDayTypeImage(weather.getType());
            wt_type_icon.setImageResource(imgId);
            //动态背景
            backImageResId = weatherManager.getBackground(weather.getType());
        }
        if(backImageResId!=0){
            weather_view.setBackgroundResource(backImageResId);
        }
        String wendu_range= Utility.filterNumber(weather.getHigh())+"/"+ Utility.filterNumber(weather.getLow())+"℃";
        wt_wendu_range.setIconText(wendu_range);
        wt_shidu.setIconText(weather.getShidu());
        wt_fengli.setIconText(weather.getFengxiang() + weather.getFengli());

        if(weather.getPm25()!=null) {
            int pm25 = Integer.parseInt(weather.getPm25());
            int color = weatherManager.getPmColor(pm25);
            wt_pm_icon.setColor(color);
            wt_pm.setText(weather.getPm25() + " " + weather.getQuality());
            wt_pm.setVisibility(View.VISIBLE);
            wt_pm_icon.setVisibility(View.VISIBLE);
        }else{
            wt_pm.setVisibility(View.GONE);
            wt_pm_icon.setVisibility(View.GONE);
        }

    }

    public List<Map<String,String>> getCitys() {
        citys.clear();
        List<City> cityList = weatherService.getMySelectedCitys();
        for(City city:cityList){
            Map<String, String> cityMap = new HashMap<>();
            cityMap.put("title_city_name", city.getAreaName());
            cityMap.put("weatherId", city.getWeatherId());
            cityMap.put("areaId", city.getWeatherId());
            citys.add(cityMap);
        }
        return citys;
    }
}

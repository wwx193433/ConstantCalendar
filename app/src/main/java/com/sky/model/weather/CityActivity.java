package com.sky.model.weather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.CustomProgressDialog;
import com.sky.plug.widget.IconFontButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-10.
 */
public class CityActivity extends Activity implements View.OnClickListener{

    private ListView cityListView;
    private Dialog dialogProgress;
    private int count = 0;
    private WeatherService weatherService;
    private WeatherManager weatherManager;
    private CityAdapter cityAdapter;
    private List<Map<String, Object>> weatherData;
    private IconFontButton backBtn,icon_add, icon_edit, icon_confirm;


    private Handler handler = new Handler() {
        List<Weather> weathers = new ArrayList<>();
        @Override
        public void handleMessage(Message msg) {
            weatherData.clear();
            dialogProgress.hide();
            switch(msg.what){
                case 1:
                    weathers.add((Weather) msg.obj);
                    if(weathers.size()==count){
                        for(Weather weather:weathers){
                            Map<String, Object> weatherMap = new HashMap<>();
                            if(weather.getType()!=null) {
                                int icon = weatherManager.getTypeIcon(weather.getType());
                                if(icon!=0){
                                    weatherMap.put("type_icon", icon);
                                }
                                weatherMap.put("city_type", weather.getType());
                            }
                            weatherMap.put("city_name", weather.getCity());
                            weatherMap.put("city_wendu", weather.getLow()+" "+weather.getHigh());
                            weatherMap.put("weather_id", weather.getWeatherId());
                            weatherData.add(weatherMap);
                        }
                        cityAdapter.notifyDataSetChanged();
                        dialogProgress.hide();
                    }
                    break;
                case 2:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citys);
        initViews();
    }

    private void initViews() {
        dialogProgress = CustomProgressDialog.createLoadingDialog(this, "正在加载中...");
        dialogProgress.show();
        cityListView = (ListView) this.findViewById(R.id.cityListView);
        backBtn = (IconFontButton)this.findViewById(R.id.backBtn);
        icon_add = (IconFontButton)this.findViewById(R.id.icon_add);
        icon_edit = (IconFontButton)this.findViewById(R.id.icon_edit);
        icon_confirm = (IconFontButton)this.findViewById(R.id.icon_confirm);

        backBtn.setOnClickListener(this);
        icon_add.setOnClickListener(this);
        icon_edit.setOnClickListener(this);
        icon_confirm.setOnClickListener(this);

        weatherService = new WeatherDao(this);

        weatherManager = new WeatherManager(this);
        weatherData = new ArrayList<>();

        //获取自选城市
        List<City> myCityList = weatherService.getMySelectedCitys();
        count = myCityList.size();
        for(City city:myCityList){
            weatherManager.getDayWeatherReportData(city.getWeatherId(), handler);
        }
        cityAdapter = new CityAdapter(this, weatherData);
        cityListView.setAdapter(cityAdapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String weatherId = weatherData.get(position).get("weather_id").toString();
                intent.putExtra("weatherId", weatherId);
                setResult(5, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.icon_add:
                if(weatherData.size()>=9){
                    new  AlertDialog.Builder(this).setTitle("手机抱怨了").setMessage("9个城市还不够用？").setPositiveButton("确定",null).show();
                    return;
                }
                Intent intent = new Intent(this, CitySelectActivity.class);
                startActivityForResult(intent, 2);
                break;
            case R.id.icon_edit:
                icon_confirm.setVisibility(View.VISIBLE);
                icon_edit.setVisibility(View.GONE);
                cityAdapter.setEnableDeleteButton(true);
                cityAdapter.notifyDataSetChanged();
                break;
            case R.id.icon_confirm:
                icon_confirm.setVisibility(View.GONE);
                icon_edit.setVisibility(View.VISIBLE);
                cityAdapter.setEnableDeleteButton(false);
                cityAdapter.notifyDataSetChanged();
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
        if(requestCode==2 && resultCode == 3 && data != null){
            setResult(4, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(6);
        super.onBackPressed();
    }
}

package com.sky.model.weather;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.CustomProgressDialog;
import com.sky.plug.widget.IconFontButton;
import com.sky.util.BaiduMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-10.
 */
public class CitySelectActivity extends Activity {

    private Dialog dialogProgress;
    private BaiduMap baiduMap;

    private WeatherService weatherService;
    private GridView hotCityGridView;
    private ListView searchCitysView;
    private EditText searchEditText;
    private SimpleAdapter hotCityAdapter, searchCityAdapter;
    private List<Map<String, String>> hotCitys;
    private List<Map<String, String>> searchCitys;
    private BroadcastReceiver broadcastReceiver;
    private TextView address;

    private LinearLayout home_city_tab, search_city_tab;
    private IconFontButton backBtn;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            dialogProgress.hide();
            switch (msg.what) {
                case 1://更新热门城市
                    hotCityAdapter.notifyDataSetChanged();
                    break;
                case 2://加载查询城市
                    searchCityAdapter.notifyDataSetChanged();
                    break;
                case 3://返回用户选中的城市weatherId
                    String weatherId = (String) msg.obj;
                    Intent intent = new Intent();
                    intent.putExtra("weatherId", weatherId);
                    setResult(3, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_select);
        initViews();

        hotCityAdapter = new SimpleAdapter(this, hotCitys, R.layout.hot_city_item,
                new String[]{"hotAreaId", "hotAreaName", "weatherId"}, new int[]{R.id.hotAreaId, R.id.hotAreaName, R.id.weatherId});
        hotCityGridView.setAdapter(hotCityAdapter);
        hotCityGridView.setSelector(new ColorDrawable(Color.TRANSPARENT)); // 去除点击时的背景色
        loadHotCitys();
        hotCityGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String areaId = hotCitys.get(position).get("hotAreaId");
                String weatherId = hotCitys.get(position).get("weatherId");

//                saveSelectedCity(areaId, weatherId);
                boolean flag = weatherService.saveMySelectedCity(areaId);
                if(flag){
                    Intent intent = new Intent();
                    intent.putExtra("weatherId", weatherId);
                    setResult(3, intent);
                    finish();
                }
            }
        });


        searchCityAdapter = new SimpleAdapter(this, searchCitys, R.layout.search_city_item,
                new String[]{"searchAreaId", "searchAreaName", "searchWeatherId"}, new int[]{R.id.searchAreaId, R.id.searchAreaName, R.id.searchWeatherId});
        searchCitysView.setAdapter(searchCityAdapter);

        //注册广播
        registerBroadCastReceiver();

        baiduMap = new BaiduMap(this);
        baiduMap.locateStart();
    }


    public void unregisterBroadCastReceiver(){
        this.unregisterReceiver(broadcastReceiver);
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
                    address.setText(city.getLocationAddr());
                }else{
                    address.setText("定位失败");
                }
            }
        };
        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(WeatherManager.LOCATION_BCR);
        this.registerReceiver(broadcastReceiver, intentToReceiveFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != baiduMap){
            baiduMap.locateStop();
        }
        unregisterBroadCastReceiver();
    }

    private void initViews() {
        dialogProgress = CustomProgressDialog.createLoadingDialog(this, "正在加载中...");
        dialogProgress.show();

        weatherService = new WeatherDao(this);
        hotCityGridView = (GridView) this.findViewById(R.id.hotCityGridView);
        hotCitys = new ArrayList<>();
        searchCitys = new ArrayList<>();
        address = (TextView) this.findViewById(R.id.locate_address);

        backBtn = (IconFontButton) this.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchCitysView = (ListView) this.findViewById(R.id.searchCitysView);

        searchEditText = (EditText) this.findViewById(R.id.searchEditText);
        search_city_tab = (LinearLayout) this.findViewById(R.id.search_city_tab);
        home_city_tab = (LinearLayout) this.findViewById(R.id.home_city_tab);
        home_city_tab.setVisibility(View.VISIBLE);
        search_city_tab.setVisibility(View.GONE);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    home_city_tab.setVisibility(View.VISIBLE);
                    search_city_tab.setVisibility(View.GONE);
                    searchCitys.clear();
                    searchCityAdapter.notifyDataSetChanged();
                } else {
                    home_city_tab.setVisibility(View.GONE);
                    search_city_tab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hideKeyboard();
                    String keyword = searchEditText.getText().toString();
                    if (null != keyword && !keyword.equals("")) {
                        dialogProgress.show();
                        loadQueryCitys(keyword);
                    }
                }
                return false;
            }
        });

        searchCitysView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String areaId = searchCitys.get(position).get("searchAreaId");
                String weatherId = searchCitys.get(position).get("searchWeatherId");
                saveSelectedCity(areaId, weatherId);
            }
        });
    }

    /**
     * 保存自选城市到数据库
     * @param areaId
     * @param weatherId
     */
    private void saveSelectedCity(final String areaId, final String weatherId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = weatherService.saveMySelectedCity(areaId);
                if(flag){
                    Message message = Message.obtain();
                    message.what = 3;
                    message.obj = weatherId;
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    private void hideKeyboard() {
        // 先隐藏键盘
        ((InputMethodManager) searchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(this
                                .getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 加载热门城市
     */

    private void loadHotCitys() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<City> cityList = weatherService.queryHotCitys();
                hotCitys.clear();
                for (City city : cityList) {
                    Map<String, String> cityMap = new HashMap<>();
                    cityMap.put("hotAreaId", city.getAreaId());
                    cityMap.put("hotAreaName", city.getAreaName());
                    cityMap.put("weatherId", city.getWeatherId());
                    hotCitys.add(cityMap);
                }
                Message message = Message.obtain();
                message.what = 1;
                message.obj = hotCitys;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 加载查询后的结果
     */
    private void loadQueryCitys(final String keyword){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<City> cityList = weatherService.searchCitysByKeyword(keyword);
                searchCitys.clear();
                for (City city : cityList) {
                    Map<String, String> cityMap = new HashMap<>();
                    cityMap.put("searchAreaId", city.getAreaId());
                    cityMap.put("searchAreaName", city.getDetailAreaName());
                    cityMap.put("searchWeatherId", city.getWeatherId());
                    searchCitys.add(cityMap);
                }
                Message message = Message.obtain();
                message.what = 2;
                message.obj = searchCitys;
                handler.sendMessage(message);
            }
        }).start();
    }
}

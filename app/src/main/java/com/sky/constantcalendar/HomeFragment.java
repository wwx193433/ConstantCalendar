package com.sky.constantcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.sky.model.ad.NativeAdManager;
import com.sky.model.calendar.CalendarDrawerMenu;
import com.sky.model.calendar.CalendarHeadSelector;
import com.sky.model.calendar.CalendarPagerAdapter;
import com.sky.model.calendar.CalendarViewPager;
import com.sky.model.login.LoginManager;
import com.sky.model.menu.almanac.AlmanacManager;
import com.sky.model.menu.clock.ClockManager;
import com.sky.model.weather.WeatherManager;

public class HomeFragment extends Fragment {

    private ScrollView scrollView;
    private ClockManager clockManager;
    private WeatherManager weatherManager;

    private ViewGroup native_ad_container;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        Activity activity = getActivity();

        initViews(view);

        //登录
        LoginManager loginManager = new LoginManager(activity);
        loginManager.setView(view);
        loginManager.init();

        //头部日期选择框
        CalendarHeadSelector calendarSelector = new CalendarHeadSelector(activity);
        calendarSelector.setView(view);
        calendarSelector.init();

        //中间日期滑动框
        CalendarPagerAdapter calendarPagerAdapter = new CalendarPagerAdapter(getFragmentManager());
        CalendarViewPager calendarPager = new CalendarViewPager();
        calendarPager.setView(view);
        calendarPager.setPagerAdapter(calendarPagerAdapter);
        calendarPager.init();

        //黄历数据
        AlmanacManager almanacManager = new AlmanacManager(activity);
        almanacManager.setView(view);
        almanacManager.init();

        //天气
        weatherManager = new WeatherManager(activity);
        weatherManager.setView(view);
        weatherManager.init();

        //右部滑出菜单控制
        CalendarDrawerMenu calendarDrawerMenu = new CalendarDrawerMenu(this, activity);
        calendarDrawerMenu.setView(view);
        calendarDrawerMenu.init();

        //闹钟实例
        clockManager = new ClockManager(getActivity());
        clockManager.setView(view);
        clockManager.showGetupData();

        NativeAdManager nativeAdManager = new NativeAdManager(getContext(), native_ad_container);
        nativeAdManager.loadNativeAD();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null!=weatherManager){
            weatherManager.unregisterBroadCastReceiver();
        }
    }

    private void initViews(View view) {
        scrollView = (ScrollView) view.findViewById(R.id.homeScroller);
        scrollView.smoothScrollTo(0, 0);

        native_ad_container = (ViewGroup) view.findViewById(R.id.native_ad_container);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //闹钟
        if(requestCode == CalendarDrawerMenu.CLOCKREQUESTCODE){
            if(resultCode == CalendarDrawerMenu.CLOCKRESULTCODE){
                clockManager.update();
            }
        }
    }
}



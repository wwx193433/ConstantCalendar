package com.sky.constantcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.model.calendar.CalendarDrawerMenu;
import com.sky.model.calendar.CalendarHeadSelector;
import com.sky.model.calendar.CalendarPagerAdapter;
import com.sky.model.calendar.CalendarViewPager;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        Activity activity = getActivity();

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

        //右部滑出菜单控制
        CalendarDrawerMenu calendarDrawerMenu = new CalendarDrawerMenu(activity);
        calendarDrawerMenu.setView(view);
        calendarDrawerMenu.init();

        return view;
    }

}



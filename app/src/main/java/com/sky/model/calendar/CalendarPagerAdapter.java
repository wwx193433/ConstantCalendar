package com.sky.model.calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.sky.util.Constant;

/**
 * Calendar PagerAdapter
 */
public class CalendarPagerAdapter extends FragmentPagerAdapter {

    public CalendarPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return CalendarFragment.create(position);
    }

    @Override
    public int getCount() {
        return Constant.pagecount;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }
}

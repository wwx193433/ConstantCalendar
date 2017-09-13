package com.sky.model.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 17-9-12.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fm = null;
        switch(position){
            case 0:
                fm = new UserLoginFragment();
                break;
            default:
                fm = new MobileLoginFragment();
                break;
        }

        return fm;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

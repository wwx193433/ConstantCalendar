package com.sky.model.calendar;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.menu.ClockActivity;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.Utility;

/**
 * Created by Administrator on 17-8-2.
 * 抽屉导航菜单
 */
public class CalendarDrawerMenu implements View.OnClickListener{

    //父View
    private View view;
    //主Activity
    private Activity activity;
    //菜单背景颜色
    private int bgColor;
    private int specialColor;
    private int blackColor;
    //控制菜单的按钮
    private LinearLayout menuBtn;
    //菜单按钮文字
    private IconFontTextview iconview;
    //抽屉容器
    private DrawerLayout drawerCalendarMenu;
    //右部菜单
    private LinearLayout rightMenu;

    public CalendarDrawerMenu(Activity activity){
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void init(){
        initView();
        setDrawerCalendarMenuListener();
        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawerLayout();
            }
        });
    }

    //初始化组件
    private void initView() {
        //设置背景颜色
        specialColor = activity.getResources().getColor(R.color.special);
        blackColor = activity.getResources().getColor(R.color.black);
        bgColor = activity.getResources().getColor(R.color.white);
        bgColor = Utility.setAlpha(bgColor, 80);
        rightMenu = (LinearLayout) view.findViewById(R.id.rightMenu);
        rightMenu.setBackgroundColor(bgColor);

        //初始化菜单
        initMenuBar();

        //抽屉初始化，菜单控制按钮初始化
        drawerCalendarMenu=(DrawerLayout) view.findViewById(R.id.drawerCalendar);
        menuBtn = (LinearLayout) view.findViewById(R.id.menuBtn);
        iconview = (IconFontTextview)menuBtn.getChildAt(0);
    }

    //初始化菜单
    private void initMenuBar() {
        for(int i = 0;i<rightMenu.getChildCount();i++){
            TextView menu = (TextView) rightMenu.getChildAt(i);
            menu.setOnClickListener(this);
        }
    }

    /**
     * 抽屉监听
     */
    private void setDrawerCalendarMenuListener() {
        drawerCalendarMenu.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
                iconview.setText(activity.getResources().getString(R.string.icon_back));
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                iconview.setText(activity.getResources().getString(R.string.icon_menu));
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    /**
     * 切换抽屉
     */
    private void toggleDrawerLayout() {
        if (!drawerCalendarMenu.isDrawerOpen(Gravity.RIGHT)) {
            drawerCalendarMenu.openDrawer(Gravity.RIGHT);
        } else {
            drawerCalendarMenu.closeDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public void onClick(View v) {
        //颜色切换
        focusMenu(v.getId());
        switch(v.getId()){
            case R.id.clm_holiday://节假日

                break;
            case R.id.clm_note://记事

                break;
            case R.id.clm_clock://闹钟
                Intent intent = new Intent(activity, ClockActivity.class);
                activity.startActivity(intent);
                toggleDrawerLayout();
                focusMenu(0);
                break;
            case R.id.clm_comment://意见

                break;
            case R.id.clm_setting://设置

                break;
            default:
                break;
        }
    }

    /**
     * 菜单颜色切换
     * @param id
     */
    public void focusMenu(int id){
        for(int i = 0;i<rightMenu.getChildCount();i++){
            TextView menu = (TextView) rightMenu.getChildAt(i);
            if(menu.getId() == id){
                menu.setTextColor(specialColor);
            }else{
                menu.setTextColor(blackColor);
            }
        }
    }
}

package com.sky.model.menu.clock.widget;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.wheel.adapters.WheelTextAdapter;
import com.sky.plug.wheel.views.OnWheelChangedListener;
import com.sky.plug.wheel.views.WheelView;
import com.sky.plug.widget.ToggleIconFontTextview;

import java.util.ArrayList;

/**
 * Created by Administrator on 17-8-3.
 */
public class ClockSleepWindow extends PopupWindow implements android.view.View.OnClickListener {

    private static final int ITEM_HEIGHT = 150;
    private Context context;
    private WheelView wvMinute;
    private WheelView wvTimes;
    private boolean sleepOpen = true;

    private ToggleIconFontTextview closeSleepBtn;
    private TextView confirmBtn, cancelBtn;

    private LinearLayout sleepTimeBox;

    //年月日数据集合
    private ArrayList<String> minuteList = new ArrayList<>();
    private ArrayList<String> timesList = new ArrayList<>();

    //年月日适配器
    private WheelTextAdapter mMinuteAdapter;
    private WheelTextAdapter mTimesAdapter;
    private int specialColor, blackColor;

    //选中的年月日下标
    private int selectMinuteIndex = 4, selectTimesIndex = 1;//默认每5分钟，连续2次

    //数据回调接口
    private DataInterface dataInterface;

    //最大显示文字
    private static final int MAXTEXTSIZE = 35;
    //最小显示文字
    private static final int MINTEXTSIZE = 25;
    private static final int VISIBLEITEMS = 3;


    public ClockSleepWindow(Context context, String initValue) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.sleep_time, null);
        setContentView(view);

        if(initValue.equals("-1")){//关闭小睡
            sleepOpen = false;
        }else if(null!=initValue && !initValue.equals("")){
            selectMinuteIndex = Integer.parseInt(initValue.split(",")[0])-1;
            selectTimesIndex = Integer.parseInt(initValue.split(",")[1])-1;
        }

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        init(view);
    }

    private void init(View view) {

        closeSleepBtn = (ToggleIconFontTextview) view.findViewById(R.id.closeSleepBtn);
        //设置背景颜色
        specialColor = context.getResources().getColor(R.color.special);
        blackColor = context.getResources().getColor(R.color.black);
        //初始化年、月、日三个选择器
        wvMinute = (WheelView) view.findViewById(R.id.minuteView);
        wvTimes = (WheelView) view.findViewById(R.id.timesView);

        //小睡时间控制面板
        sleepTimeBox = (LinearLayout) view.findViewById(R.id.sleepTimeBox);

        //确认、取消按钮
        closeSleepBtn = (ToggleIconFontTextview) view.findViewById(R.id.closeSleepBtn);
        confirmBtn = (TextView) view.findViewById(R.id.slp_confirm_btn);
        cancelBtn = (TextView) view.findViewById(R.id.slp_cancel_btn);

        closeSleepBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

        /*************************************************** 处理年份 *************************************************/
        setMinute();
        wvMinute.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectMinuteIndex = newValue;
            }
        });

        /*************************************************** 处理月份 *************************************************/
        setTimes();
        wvTimes.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                selectTimesIndex = newValue;
            }
        });
        toggleLittleSleep(sleepOpen);
    }

    //开关控制小睡功能
    private void toggleLittleSleep(boolean sleepOpen) {
        wvMinute.setEnabled(sleepOpen);
        wvTimes.setEnabled(sleepOpen);
        closeSleepBtn.setOpen(sleepOpen);
    }


    public void setMinute() {
        initMinutes();
        wvMinute.setVisibleItems(VISIBLEITEMS);
        wvMinute.setCyclic(true);
        wvMinute.setItemHeight(ITEM_HEIGHT);
        wvMinute.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        wvMinute.loadData(context, minuteList, selectMinuteIndex);
    }

    public void setTimes() {
        initTimes();
        wvTimes.setVisibleItems(VISIBLEITEMS);
        wvTimes.setCyclic(true);
        wvTimes.setItemHeight(ITEM_HEIGHT);
        wvTimes.setStyle("sans-serif-thin", MAXTEXTSIZE, MINTEXTSIZE);
        wvTimes.loadData(context, timesList, selectTimesIndex);
    }

    public void initMinutes() {
        for (int y = 1; y < 61; y++) {
            minuteList.add(y+"");
        }
    }

    public void initTimes() {
        for (int m = 1; m < 16; m++) {
            timesList.add(m + "");
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.closeSleepBtn:
                if(closeSleepBtn.isOpen()){//关闭
                    sleepOpen = false;
                }else{//打开
                    sleepOpen = true;
                }
                toggleLittleSleep(sleepOpen);
                break;
            case R.id.slp_cancel_btn:
                dismiss();
                break;
            case R.id.slp_confirm_btn:
                int minuteValue = Integer.parseInt(minuteList.get(selectMinuteIndex));
                int timesValue = Integer.parseInt(timesList.get(selectTimesIndex));
                dataInterface.onDataCallBack(sleepOpen, minuteValue, timesValue);
                dismiss();
                break;
            default:
                break;
        }

    }

    @Override
    public void showAsDropDown(View anchor) {
        if(Build.VERSION.SDK_INT < 24){
            super.showAtLocation(anchor, 0, 0, Gravity.END);
        }else{
            super.setFocusable(true);
            //    在某个控件下方弹出
            super.showAsDropDown(anchor);
        }
    }
    /**
     * 定义回调接口
     */
    public interface DataInterface{
        void onDataCallBack(boolean sleepOpen, int minute, int times);
    }
    public void onSelectData(DataInterface dataInterface){
        this.dataInterface = dataInterface;
    }
}

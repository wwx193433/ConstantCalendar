package com.sky.model.menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.menu.widget.CheckDayWindow;
import com.sky.model.menu.widget.ClockFactory;
import com.sky.model.menu.widget.ClockSleepWindow;

/**
 * Created by Administrator on 17-8-2.
 */
public class ClockActivity extends Activity implements View.OnClickListener{

    //返回上一层、保存数据、小睡设置、响铃周期按钮
    private LinearLayout backBtn, saveBtn, sleepBtn,bellCycleBtn;

    //闹钟标题
    private EditText bellTitle;
    //小睡设置
    private TextView sleepText, sleepValue;
    //响铃周期
    private TextView bellCycleText, bellCycleValue;
    //铃声设置
    private TextView bellMusicText, bellMusicValue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);

        initView();

        //时分选择器
        HourMinuteSelector hourMinuteSelector = new HourMinuteSelector(this);
        hourMinuteSelector.init();

        //设置小睡初始值(关闭小睡)
        setLittleSleep(false, 0, 0);
    }

    private void initView() {
        backBtn = (LinearLayout) this.findViewById(R.id.clk_back);
        saveBtn = (LinearLayout) this.findViewById(R.id.clk_save);
        sleepBtn = (LinearLayout) this.findViewById(R.id.sleepBtn);
        bellCycleBtn = (LinearLayout) this.findViewById(R.id.bellCycleBtn);

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        sleepBtn.setOnClickListener(this);
        bellCycleBtn.setOnClickListener(this);

        bellTitle = (EditText) this.findViewById(R.id.bell_title);
        sleepText = (TextView) this.findViewById(R.id.sleepText);
        sleepValue = (TextView) this.findViewById(R.id.sleepValue);
        bellCycleText = (TextView) this.findViewById(R.id.bellCycleText);
        bellCycleValue = (TextView) this.findViewById(R.id.bellCycleValue);
        bellMusicText = (TextView) this.findViewById(R.id.bellMusicText);
        bellMusicValue = (TextView) this.findViewById(R.id.bellMusicValue);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clk_back:
                finish();
                break;
            case R.id.clk_save:

                break;
            case R.id.sleepBtn:// 小睡
                String initValue = sleepValue.getText().toString();
                ClockSleepWindow clockSleepWindow = new ClockSleepWindow(this, initValue);

                //出现在布局底端
                clockSleepWindow.showAsDropDown(getContentView());
                backgroundAlpha(0.4f);
                clockSleepWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                clockSleepWindow.onSelectData(new ClockSleepWindow.DataInterface() {
                    @Override
                    public void onDataCallBack(boolean sleepFlag, int minute, int times) {
                        setLittleSleep(sleepFlag, minute, times);
                    }
                });
                break;
            case R.id.bellCycleBtn://响铃周期
                String bcValue = bellCycleValue.getText().toString();
                int daynum = 0xf8;
                if(!bcValue.equals("") && !bcValue.equals(null)){
                    daynum = Integer.parseInt(bcValue);
                }
                CheckDayWindow checkDayWindow = new CheckDayWindow(this, daynum);
                //出现在布局底端
                checkDayWindow.showAsDropDown(getContentView());
                backgroundAlpha(0.4f);
                checkDayWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                checkDayWindow.onSelectData(new CheckDayWindow.DataInterface() {
                    @Override
                    public void onDataCallBack(int daynum) {
                        bellCycleValue.setText(String.valueOf(daynum));
                        bellCycleText.setText(ClockFactory.decodeDay(daynum));
                    }
                });
                break;
            default:
                break;
        }
    }

    //设置小睡时间
    private void setLittleSleep(boolean sleepFlag, int minute, int times) {
        if(sleepFlag){
            sleepText.setText("每隔" + minute + "分钟，共" + times + "次");
            sleepValue.setText(minute + "," + times);
        }else{
            sleepText.setText("已关闭");
            sleepValue.setText("-1");
        }
    }

    //设置页面不透明度
    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }
    private View getContentView(){
        return this.findViewById(android.R.id.content);
    }
}

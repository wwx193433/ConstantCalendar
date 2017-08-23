package com.sky.model.menu.clock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.calendar.CalendarDrawerMenu;
import com.sky.model.menu.clock.bell.BellActivity;
import com.sky.model.menu.clock.widget.CheckDayWindow;
import com.sky.model.menu.clock.widget.ClockFactory;
import com.sky.model.menu.clock.widget.ClockSleepWindow;
import com.sky.util.Constant;

import java.util.Calendar;

/**
 * Created by Administrator on 17-8-2.
 */
public class ClockActivity extends Activity implements View.OnClickListener{

    private static final int REQUESTCODE = 0x00;
    private static final int RESPONSECODE = 0xff;

    private ClockService clockService;
    private ClockManager clockManager;
    private ClockFactory clockFactory;

    //返回上一层、保存数据、小睡设置、响铃周期, 铃声设置按钮
    private LinearLayout backBtn, saveBtn, titleBtn,sleepBtn,bellCycleBtn,bellSongBtn;

    //时分选择器
    private HourMinuteSelector hourMinuteSelector;
    //小睡
    private ClockSleepWindow clockSleepWindow;
    //周期
    private CheckDayWindow checkDayWindow;

    //闹钟标题
    private TextView bellTitle;
    //小睡设置
    private TextView sleepText, sleepValue;
    //响铃周期
    private TextView bellCycleText, bellCycleValue;
    //铃声设置
    private TextView bellSongText, bellSongValue, bellType;

    //设置初始化响铃周期
    private int defaultDayNum = 0xf8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);

        initView();

        //时分选择器
        hourMinuteSelector = new HourMinuteSelector(this);
        hourMinuteSelector.init(7, 30);

        //设置闹钟标题
        setClockTitle();

        //设置小睡初始值(关闭小睡)
        setLittleSleep(false, 0, 0);

        //初始化响铃周期
        this.bellCycleValue.setText(String.valueOf(this.defaultDayNum));
        this.bellCycleText.setText(ClockFactory.decodeDay(this.defaultDayNum));

        //初始化铃声
        this.bellSongText.setText(Constant.DEFAULT_RING_NAME);
        this.bellSongValue.setText(Constant.DEFAULT_RING_URL);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        this.clockService = new ClockDao(this);
        clockManager = new ClockManager(this);
        clockFactory = new ClockFactory();
        backBtn = (LinearLayout) this.findViewById(R.id.clk_back);
        saveBtn = (LinearLayout) this.findViewById(R.id.clk_save);
        titleBtn = (LinearLayout) this.findViewById(R.id.titleBtn);
        sleepBtn = (LinearLayout) this.findViewById(R.id.sleepBtn);
        bellCycleBtn = (LinearLayout) this.findViewById(R.id.bellCycleBtn);
        bellSongBtn = ((LinearLayout)findViewById(R.id.bellSongBtn));

        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        sleepBtn.setOnClickListener(this);
        bellCycleBtn.setOnClickListener(this);
        bellSongBtn.setOnClickListener(this);

        bellTitle = (TextView) this.findViewById(R.id.bell_title);
        sleepText = (TextView) this.findViewById(R.id.sleepText);
        sleepValue = (TextView) this.findViewById(R.id.sleepValue);
        bellCycleText = (TextView) this.findViewById(R.id.bellCycleText);
        bellCycleValue = (TextView) this.findViewById(R.id.bellCycleValue);
        bellSongText = ((TextView)findViewById(R.id.bellSongText));
        bellSongValue = ((TextView)findViewById(R.id.bellSongValue));
        bellType = ((TextView)findViewById(R.id.bellType));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.clk_back:
                finish();
                break;
            case R.id.clk_save:
                //保存起床闹钟数据
                Clock clock = saveData();
                clockManager.openClock(clock.getId(), clock.getTriggerTime());
                //设置返回值
                setResult(CalendarDrawerMenu.CLOCKRESULTCODE, getIntent());
                finish();
                break;

            case R.id.sleepBtn:// 小睡
                String initValue = sleepValue.getText().toString();
                clockSleepWindow = new ClockSleepWindow(this, initValue);

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
                checkDayWindow = new CheckDayWindow(this, daynum);
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
            case R.id.bellSongBtn:
                Intent intent = new Intent(this, BellActivity.class);
                String url = this.bellSongValue.getText().toString();
                String type = this.bellType.getText().toString();
                intent.putExtra("url", url);
                intent.putExtra("type", type);
                startActivityForResult(intent, REQUESTCODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTCODE && resultCode == RESPONSECODE){
            this.bellSongText.setText(data.getStringExtra("name"));
            this.bellSongValue.setText(data.getStringExtra("url"));
            this.bellType.setText(data.getStringExtra("type"));
        }
    }

    private void setClockTitle() {
        bellTitle.setText(Constant.GETUPCLOCKSTRING);
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


    private Clock saveData()
    {
        String time = this.hourMinuteSelector.getTime();
        String title = this.bellTitle.getText().toString();
        String sleep = this.sleepValue.getText().toString();
        String cycle = this.bellCycleValue.getText().toString();
        String bell = this.bellSongValue.getText().toString();
        String type = this.bellType.getText().toString();
        Clock clock = new Clock();
        clock.setTitle(title);
        clock.setTime(time);
        clock.setSleep(sleep);
        clock.setCycle(cycle);
        clock.setBell(bell);
        clock.setBellType(type);

        //打开起床闹钟
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock.getTime().split(":")[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(clock.getTime().split(":")[1]));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long triggerTime = clockFactory.getTriggerTime(calendar, Integer.parseInt(cycle));
        Log.i("tags", "triggerTime:" + triggerTime);
        clock.setTriggerTime(triggerTime);
        this.clockService.saveGetupData(clock);
        return clock;
    }
}
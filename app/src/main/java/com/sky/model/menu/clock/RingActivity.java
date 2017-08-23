package com.sky.model.menu.clock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.model.menu.clock.widget.ClockFactory;
import com.skyfishjy.library.RippleBackground;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Administrator on 17-8-18.
 */
public class RingActivity extends Activity implements View.OnTouchListener {

    private SimpleDateFormat hm = new SimpleDateFormat("HH:mm");
    private ClockService clockService;
    private ClockFactory clockFactory;
    private MediaPlayer player;
    private PowerManager.WakeLock mWakelock;
    private Runnable runnable;
    private FrameLayout ringLayout;
    private LinearLayout sleepLayout, nextRingLayout, circleLayout, currentLayout;
    private TextView currentTime, nextTime, clock_title;
    private RippleBackground rippleBackground;
    private ImageView clockImage;
    private Animation animation;
    private Handler handler;
    private Clock clock;
    private Calendar clockTime;
    boolean isAnimated = true;

    private int screenWidth, screenHeight;
    private int clockWidth, clockHeight;
    boolean outside = false;
    boolean sleepFlag = false;

    @Override
    protected void onResume() {
        super.onResume();

        //获取闹钟数据
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        clock = clockService.queryClockById(id);
        Log.i("tags", "querydata:" + clock.toString());

        //获取闹钟原始时间
        clockTime = Calendar.getInstance();
        clockTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(clock.getTime().split(":")[0]));
        clockTime.set(Calendar.MINUTE, Integer.parseInt(clock.getTime().split(":")[1]));
        clockTime.set(Calendar.SECOND, 0);
        clockTime.set(Calendar.MILLISECOND, 0);

        //判断是否还有小睡
        sleepFlag = hasSleep(clock);
        if(sleepFlag){//获取下一次触发时间
            sleepLayout.setVisibility(View.VISIBLE);
            Calendar triggerCalendar = Calendar.getInstance();
            triggerCalendar.setTimeInMillis(clock.getTriggerTime());
            triggerCalendar.add(Calendar.MINUTE, Integer.parseInt(clock.getSleep().split(",")[0]));
            clock.setTriggerTime(triggerCalendar.getTimeInMillis());
            Log.i("tags", "还有小睡ssssss");
        }else{//获取下一次触发时间
            long triggerTime = clockFactory.getTriggerTime(clockTime, Integer.parseInt(clock.getCycle()));
            clock.setTriggerTime(triggerTime);
            Log.i("tags", "没有小睡ssssssssssss");
        }

        currentTime.setText(hm.format(new Date()));
        clock_title.setText(clock.getTitle());
        nextRingLayout.setVisibility(View.GONE);

        //唤醒闹钟
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
        mWakelock.acquire();

        //开始动画
        startAnimation();
        isAnimated = true;

        //播放音乐
        playMusic(clock.getBell());

        //启动定时器
        scheduleTask();

        //设定触摸延迟
        touchDelayClock();
    }

    /**
     * 判断是否还有小睡
     * @param clock
     * @return
     */
    private boolean hasSleep(Clock clock) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sleep = clock.getSleep();
        if(!sleep.contains(",")){
            return false;
        }
        Calendar calendar = (Calendar) clockTime.clone();
        int dm = Integer.parseInt(sleep.split(",")[0]) * Integer.parseInt(sleep.split(",")[1]);
        calendar.add(Calendar.MINUTE, dm);
        Calendar triggerCalendar = Calendar.getInstance();
        triggerCalendar.setTimeInMillis(clock.getTriggerTime());
        Log.i("tags","triggerCalendar:"+sdf.format(triggerCalendar.getTime())+"   ;calendar:"+sdf.format(calendar.getTime()));
        Log.i("tags","触发时间："+triggerCalendar.getTimeInMillis()+"    所有小睡结束时间："+calendar.getTimeInMillis());
        if(triggerCalendar.before(calendar)){
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放锁
        mWakelock.release();
        handler.removeCallbacks(runnable);
        stopMusic();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED//屏幕锁定显示
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD//系统会自动解锁屏幕
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON//当window被显示的时候，系统把FLAG_TURN_SCREEN_ON当做一个用户活动事件，用以点亮屏幕
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//当window对用户可见的时候，系统让屏幕处于高亮状态

        setContentView(R.layout.ring);

        //初始化组件
        initViews();

        //图片拖动效果
        clockImage.setOnTouchListener(this);

        ringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sleepFlag) {
                    sleepLayout.setVisibility(View.GONE);
                    nextRingLayout.setVisibility(View.VISIBLE);
                    stopAnimation();
                    isAnimated = false;
                    setSleepClock();
                    //停止20秒响铃控制
                    handler.removeCallbacks(runnable);
                    //关闭音乐
                    stopMusic();
                }
            }
        });
    }

    /**
     * 设定触摸屏幕延迟
     */
    private void touchDelayClock() {
        if (!sleepFlag) {
            sleepLayout.setVisibility(View.GONE);
        } else {
            sleepLayout.setVisibility(View.VISIBLE);
        }
    }

    int lastX = 0, lastY = 0;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        clockWidth = clockImage.getWidth();
        clockHeight = clockImage.getHeight();
        int circleCenterX = circleLayout.getLeft() + circleLayout.getWidth() / 2;
        int circleCenterY = circleLayout.getTop() + circleLayout.getHeight() / 2;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN://按下
                //去除扩散和晃动效果
                stopAnimation();
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                circleLayout.setVisibility(View.VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE://移动
                int dx = (int) (event.getRawX() - lastX);
                int dy = (int) (event.getRawY() - lastY);

                int left = v.getLeft() + dx;
                int right = v.getRight() + dx;
                int top = v.getTop() + dy;
                int bottom = v.getBottom() + dy;
                if (left < 0) {
                    left = 0;
                    right = left + v.getWidth();
                }
                if (right > screenWidth) {
                    right = screenWidth;
                    left = right - v.getWidth();
                }
                if (top < 0) {
                    top = 0;
                    bottom = top + v.getHeight();
                }
                if (bottom > screenHeight) {
                    bottom = screenHeight;
                    top = bottom - v.getHeight();
                }

                v.layout(left, top, right, bottom);
                lastX = (int) event.getRawX();
                lastY = (int) event.getRawY();
                if (Math.abs(left + clockWidth / 2 - circleCenterX) > circleLayout.getWidth() / 2 ||
                        Math.abs(top + clockHeight / 2 - circleCenterY) > circleLayout.getHeight() / 2) {
                    outside = true;
                    circleLayout.getBackground().setAlpha(255);
                } else {
                    outside = false;
                    circleLayout.getBackground().setAlpha(200);
                }

                break;
            case MotionEvent.ACTION_UP://抬起
                if (outside) { //拖动到圈外
                    setCycleClock();
                    //关闭闹钟
                    finish();
                } else {//没有拖动到圈外
                    v.layout(circleCenterX - clockWidth / 2, circleCenterY - clockHeight / 2, circleCenterX + clockWidth / 2, circleCenterY + clockHeight / 2);
                    if(isAnimated){
                        startAnimation();
                    }
                    circleLayout.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        return true;
    }

    //定时停止闹铃
    private void scheduleTask() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (sleepFlag) {
                    sleepLayout.setVisibility(View.GONE);
                    nextRingLayout.setVisibility(View.VISIBLE);
                    stopAnimation();
                    isAnimated = false;
                    setSleepClock();
                }else{
                    setCycleClock();
                    finish();
                }
                stopMusic();
            }
        };
        handler.postDelayed(runnable, 20000);
    }

    private void initViews() {
        clockService = new ClockDao(this);
        clockFactory = new ClockFactory();

        ringLayout = (FrameLayout) this.findViewById(R.id.ringLayout);
        sleepLayout = (LinearLayout) findViewById(R.id.sleepLayout);
        nextRingLayout = (LinearLayout) findViewById(R.id.nextRingLayout);
        circleLayout = (LinearLayout) findViewById(R.id.circleLayout);
        currentLayout = (LinearLayout) findViewById(R.id.currentLayout);

        rippleBackground = (RippleBackground) findViewById(R.id.content);
        clockImage = (ImageView) findViewById(R.id.centerImage);
        animation = AnimationUtils.loadAnimation(RingActivity.this, R.anim.anim_clock);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        handler = new Handler();
        currentTime = (TextView) this.findViewById(R.id.currentTime);
        clock_title = (TextView) this.findViewById(R.id.clock_title);
        nextTime = (TextView) this.findViewById(R.id.nextTime);
    }

    /**
     * 开始动画
     */
    private void startAnimation() {
        //水波纹扩散效果
        rippleBackground.startRippleAnimation();
        //闹钟图片晃动下效果
        clockImage.startAnimation(animation);
    }

    /**
     * 停止动画
     */
    private void stopAnimation() {
        //水波纹扩散效果
        rippleBackground.stopRippleAnimation();
        //闹钟图片晃动下效果
        clockImage.clearAnimation();
    }

    /**
     * 设定下一次睡眠闹钟
     */
    public void setSleepClock() {
        ClockManager clockManager = new ClockManager(this);
        clockManager.closeClock(clock.getId());

        clockManager.openClock(clock.getId(), clock.getTriggerTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(clock.getTriggerTime());
        nextTime.setText(hm.format(calendar.getTime()));
        clockService.updateClock(clock);
    }

    /**
     * 设定下一次周期闹钟
     */
    public void setCycleClock() {
        ClockManager clockManager = new ClockManager(this);
        clockManager.closeClock(clock.getId());
        clockManager.openClock(clock.getId(), clock.getTriggerTime());
        clockService.updateClock(clock);
    }

    /**
     *  铃声播放
     * @param url
     */
    public void playMusic(String url) {
        player = MediaPlayer.create(this, Uri.parse(url));
        player.setLooping(false);
        player.start();//开始播放
    }

    public void stopMusic(){
        if(null!=player){
            player.stop();
        }
    }

}

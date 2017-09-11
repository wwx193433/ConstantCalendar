package com.sky.model.menu.almanac;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontTextview;
import com.sky.util.Utility;

/**
 * Created by Administrator on 17-8-26.
 */
public class RoundActivity extends Activity implements SensorEventListener, View.OnClickListener {

    private TextView lp_direction;
    private ImageView roundImage;
    private TextView tv_cai, tv_xi;
    private IconFontTextview icon_back, icon_cai, icon_xi;
    private ImageView gravity_ball;
    private String cai, xi;

    private int specialColor, defaultColor;

    private float move_param;

    private FrameLayout fl_lp;

    //传感器管理器
    private SensorManager manager;
    //罗盘转动度数
    float predegree = 0;

    @Override
    protected void onResume() {
        /**
         * 获取方向传感器
         * 通过SensorManager对象获取相应的Sensor类型的对象
         */
        Sensor ori_sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Sensor acc_sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //应用在前台时候注册监听器
        manager.registerListener(this, ori_sensor,
                SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, acc_sensor,
                SensorManager.SENSOR_DELAY_GAME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round);

        initViews();

        icon_back.setOnClickListener(this);
        icon_cai.setOnClickListener(this);
        icon_xi.setOnClickListener(this);
    }

    public void initViews() {
        //获取系统服务（SENSOR_SERVICE)返回一个SensorManager 对象
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lp_direction = (TextView) this.findViewById(R.id.lp_direction);
        fl_lp = (FrameLayout) this.findViewById(R.id.fl_lp);
        roundImage = (ImageView) this.findViewById(R.id.roundImage);
        tv_cai = (TextView) this.findViewById(R.id.tv_cai);
        tv_xi = (TextView) this.findViewById(R.id.tv_xi);
        gravity_ball = (ImageView) this.findViewById(R.id.gravity_ball);
        icon_back = (IconFontTextview) this.findViewById(R.id.icon_back);
        icon_cai = (IconFontTextview) this.findViewById(R.id.icon_cai);
        icon_xi = (IconFontTextview) this.findViewById(R.id.icon_xi);

        defaultColor = getResources().getColor(R.color.defaultColor);
        specialColor = getResources().getColor(R.color.lightRed);

        //获取财、喜所在方向度数
        cai = getIntent().getStringExtra("cai");
        xi = getIntent().getStringExtra("xi");
        lp_direction.setText("财神：" + cai);

        //设置罗盘数据
        setRoundView();

        //设置财、喜状态
        tv_cai.setBackgroundResource(R.drawable.caixi_circle_back_activity);
        tv_cai.setTextColor(Color.WHITE);

    }

    private void setRoundView() {
        ViewGroup.MarginLayoutParams cp = (ViewGroup.MarginLayoutParams) tv_cai.getLayoutParams();
        ViewGroup.MarginLayoutParams xp = (ViewGroup.MarginLayoutParams) tv_xi.getLayoutParams();

        int caiDegree = getDegree(cai);
        int xiDegree = getDegree(xi);

        //获取财、喜的位置偏移量
        int[] p_cai = getPosition(caiDegree);
        int[] p_xi = getPosition(xiDegree);
        cp.leftMargin = p_cai[0];
        cp.topMargin = p_cai[1];
        tv_cai.setLayoutParams(cp);

        xp.leftMargin = p_xi[0];
        xp.topMargin = p_xi[1];
        tv_xi.setLayoutParams(xp);
    }

    private int getDegree(String cai) {
        int degree = 0;
        switch (cai) {
            case "正东":
                degree = 0;
                break;
            case "正南":
                degree = 270;
                break;
            case "正西":
                degree = 180;
                break;
            case "正北":
                degree = 90;
                break;
            case "东北":
                degree = 45;
                break;
            case "西北":
                degree = 135;
                break;
            case "西南":
                degree = 225;
                break;
            case "东南":
                degree = 315;
                break;
            default:
                break;
        }
        return degree;
    }

    public int getWidth() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;     // 屏幕宽度（像素）
        return width;
    }

    /**
     * 获取财喜的位置
     *
     * @return
     */
    public int[] getPosition(double degree) {
        int[] position = new int[2];
        int r = (getWidth() - Utility.getPx(this, 32)) / 2;
        move_param = (float)0.266 * r / 10;
        int d = Utility.getPx(this, 12);
        int top = getWidth() / 2 - (int) (Math.sin((degree / 180) * Math.PI) * r) - d;
        int left = getWidth() / 2 + (int) (Math.cos((degree / 180) * Math.PI) * r) - d;
        position[0] = left;
        position[1] = top;
        return position;
    }

    float mLastX, mLastY, mLastZ;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];// 存放了方向值
            /**罗盘转动效果*/
            RotateAnimation round_animation = new RotateAnimation(predegree, -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            round_animation.setDuration(50);
            round_animation.setFillAfter(true);
            fl_lp.startAnimation(round_animation);
            RotateAnimation icon_animation = new RotateAnimation(degree, -predegree,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            icon_animation.setDuration(50);
            icon_animation.setFillAfter(true);
            tv_cai.startAnimation(icon_animation);
            tv_xi.startAnimation(icon_animation);
            predegree = -degree;
        }


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            moveAction(mLastX,event.values[SensorManager.DATA_X] * move_param, mLastY, -event.values[SensorManager.DATA_Y] * move_param);
            mLastX = event.values[SensorManager.DATA_X] * move_param;
            mLastY = -event.values[SensorManager.DATA_Y] * move_param;
            mLastZ = event.values[SensorManager.DATA_Z] * move_param;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.icon_back:
                finish();
                break;
            case R.id.icon_cai:
                Log.i("tags", "cai");
                //设置财、喜状态
                tv_cai.setBackgroundResource(R.drawable.caixi_circle_back_activity);
                tv_cai.setTextColor(Color.WHITE);
                //设置财、喜状态
                tv_xi.setBackgroundResource(R.drawable.caixi_circle_back);
                tv_xi.setTextColor(specialColor);
                icon_cai.setIconTextColor(specialColor);
                icon_xi.setIconTextColor(defaultColor);
                lp_direction.setText("财神：" + cai);
                break;
            case R.id.icon_xi:

                Log.i("tags", "xi");
                //设置财、喜状态
                tv_xi.setBackgroundResource(R.drawable.caixi_circle_back_activity);
                tv_xi.setTextColor(Color.WHITE);
                //设置财、喜状态
                tv_cai.setBackgroundResource(R.drawable.caixi_circle_back);
                tv_cai.setTextColor(specialColor);

                icon_xi.setIconTextColor(specialColor);
                icon_cai.setIconTextColor(defaultColor);
                lp_direction.setText("喜神：" + xi);
                break;
            default:
                break;
        }

    }

    private void moveAction(float fromX,float toX,float fromY,float toY){
        AnimationSet animationSet = new AnimationSet(true);
        final TranslateAnimation animation = new TranslateAnimation(fromX, toX,fromY, toY);
        animation.setDuration(300);//设置动画持续时间
//        animation.setFillAfter(true);//动画结束后是否回到原位
        animationSet.addAnimation(animation);
        gravity_ball.startAnimation(animation);
    }
}

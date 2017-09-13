package com.sky.model.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontTextview;

/**
 * Created by Administrator on 17-9-12.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener{

    private IconFontTextview icon_back;
    private ViewPager loginPager;
    private int defaultColor, soilColor;

    private RadioGroup loginRadio;
    private RadioButton userLoginBtn, mobileLoginBtn;
    private View slide_line;

    private int screenW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initViews();
        setListener();
    }

    /**
     * 设置点击事件
     */
    private void setListener() {
        icon_back.setOnClickListener(this);
        loginRadio.setOnCheckedChangeListener(this);
        loginPager.addOnPageChangeListener(this );
    }

    /**
     * 初始化组件
     */
    private void initViews() {

        defaultColor = getResources().getColor(R.color.defaultColor);
        soilColor = getResources().getColor(R.color.soil);
        slide_line = this.findViewById(R.id.slide_line);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenW = dm.widthPixels;

        userLoginBtn = (RadioButton) this.findViewById(R.id.userLoginBtn);
        mobileLoginBtn = (RadioButton) this.findViewById(R.id.mobileLoginBtn);

        // 设置下划线宽度
        slide_line.setLayoutParams(new LinearLayout.LayoutParams(screenW / 2,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        loginRadio = (RadioGroup) this.findViewById(R.id.loginRadio);
        icon_back = (IconFontTextview) this.findViewById(R.id.login_icon_back);
        loginPager = (ViewPager) this.findViewById(R.id.loginPager);
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        loginPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.icon_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.userLoginBtn:
                loginPager.setCurrentItem(0);
                move(screenW / 2, 0);
                userLoginBtn.setTextColor(soilColor);
                mobileLoginBtn.setTextColor(defaultColor);
                break;
            case R.id.mobileLoginBtn:
                loginPager.setCurrentItem(1);
                move(0, screenW / 2);
                userLoginBtn.setTextColor(defaultColor);
                mobileLoginBtn.setTextColor(soilColor);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if(position == 0){
            userLoginBtn.setChecked(true);
        }else{
            mobileLoginBtn.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private void move(float fromX,float toX){
        AnimationSet animationSet = new AnimationSet(true);
        final TranslateAnimation animation = new TranslateAnimation(fromX, toX,0, 0);
        animation.setDuration(300);//设置动画持续时间
        animationSet.addAnimation(animation);
        animation.setFillAfter(true);//动画结束后是否回到原位
        slide_line.startAnimation(animation);
    }
}

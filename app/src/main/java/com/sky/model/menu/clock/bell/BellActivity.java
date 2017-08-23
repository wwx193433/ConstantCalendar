package com.sky.model.menu.clock.bell;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.util.Constant;

/**
 * Created by Administrator on 17-8-7.
 */

public class BellActivity extends FragmentActivity implements View.OnClickListener{

    private static final int RESPONSECODE = 0xFF;
    private LinearLayout bellMenuBar;
    private AlarmFragment alarmFragment;
    private MusicFragment musicFragment;
    private TextView backBtn, confirmBtn;
    private TextView musicBtn, alarmBtn;
    private int index = 0;
    private String alarmName, alarmUrl, musicName, musicUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bell);

        initViews();

        // 初始化并设置当前Fragment
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if(type.equals(Constant.RING)){
            index = 1;
        }
        initFragment(index);
    }

    private void initViews() {
        bellMenuBar = (LinearLayout) this.findViewById(R.id.bellMenuBar);
        backBtn = (TextView) this.findViewById(R.id.bc_back);
        confirmBtn = (TextView) this.findViewById(R.id.bc_confirm);

        musicBtn = (TextView) this.findViewById(R.id.musicBtn);
        alarmBtn = (TextView) this.findViewById(R.id.alarmBtn);

        backBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);

        musicBtn.setOnClickListener(this);
        alarmBtn.setOnClickListener(this);
    }


    private void initFragment(int index) {

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        //菜单颜色设置
        for(int i = 0;i<bellMenuBar.getChildCount();i++){
            TextView tv = (TextView)bellMenuBar.getChildAt(i);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundColor(getResources().getColor(R.color.lightGray));
        }
        TextView tv = (TextView)bellMenuBar.getChildAt(index);
        tv.setTextColor(getResources().getColor(R.color.special));
        tv.setBackgroundColor(getResources().getColor(R.color.white));

        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);

        switch (index) {
            case 0:
                if (musicFragment == null) {
                    musicFragment = new MusicFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    musicFragment.setArguments(bundle);
                    transaction.add(R.id.bell_frame, musicFragment);
                } else {
                    transaction.show(musicFragment);
                }
                musicFragment.onCheckData(new MusicFragment.DataInterface() {
                    @Override
                    public void onDataCheckCallBack(String name, String url) {
                        musicName = name;
                        musicUrl = url;
                    }

                });
                break;
            case 1:
                if (alarmFragment == null) {
                    alarmFragment = new AlarmFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("url", url);
                    alarmFragment.setArguments(bundle);
                    transaction.add(R.id.bell_frame, alarmFragment);
                } else {
                    transaction.show(alarmFragment);
                }
                alarmFragment.onCheckData(new AlarmFragment.DataInterface() {
                    @Override
                    public void onDataCheckCallBack(String name, String url) {
                        alarmName = name;
                        alarmUrl = url;
                    }
                });
                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bc_back:
                finish();
                break;
            case R.id.bc_confirm:
                Intent intent=getIntent();
                if(index == 0){
                    musicFragment.sendData();
                    intent.putExtra("name", musicName);
                    intent.putExtra("url", musicUrl);
                    intent.putExtra("type", Constant.MUSIC);
                }else{
                    alarmFragment.sendData();
                    intent.putExtra("name", alarmName);
                    intent.putExtra("url", alarmUrl);
                    intent.putExtra("type", Constant.RING);
                }
                setResult(RESPONSECODE, intent);
                finish();
                break;
            case R.id.musicBtn:
                index = 0;
                initFragment(index);
                break;
            case R.id.alarmBtn:
                index = 1;
                initFragment(index);
                break;
            default:
                break;
        }
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if(alarmFragment != null){
            transaction.hide(alarmFragment);
        }
        if(musicFragment != null){
            transaction.hide(musicFragment);
        }
    }
}

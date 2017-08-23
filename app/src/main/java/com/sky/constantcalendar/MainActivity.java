package com.sky.constantcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.system.PermissionTool;

/**
 * Created by Administrator on 17-7-31.
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener{

    private HomeFragment homeFragment;
    private AttentionFragment attentionFragment;
    private FindFragment findFragment;
    private SelfFragment selfFragment;
    private PermissionTool permissionTool;

    private LinearLayout menubar;

    //底部菜单页签
    private LinearLayout calendar_btn,attention_btn,find_btn,self_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();

        // 初始化并设置当前Fragment
        initFragment(0);

        //外部存储卡读写权限申请
        permissionTool.applyStoragePermission();

    }

    private void initView() {
        permissionTool = new PermissionTool(this);
        menubar = (LinearLayout) this.findViewById(R.id.menubar);
        calendar_btn = (LinearLayout) this.findViewById(R.id.calendar_btn);
        attention_btn = (LinearLayout) this.findViewById(R.id.attention_btn);
        find_btn = (LinearLayout) this.findViewById(R.id.find_btn);
        self_btn = (LinearLayout) this.findViewById(R.id.self_btn);

        calendar_btn.setOnClickListener(this);
        attention_btn.setOnClickListener(this);
        find_btn.setOnClickListener(this);
        self_btn.setOnClickListener(this);
    }

    private void initFragment(int index) {

        //菜单颜色设置
        for(int i = 0;i<menubar.getChildCount();i++){
            ((TextView)((LinearLayout)menubar.getChildAt(i)).getChildAt(0)).setTextColor(getResources().getColor(R.color.gray));
        }
        ((TextView)((LinearLayout)menubar.getChildAt(index)).getChildAt(0)).setTextColor(getResources().getColor(R.color.special));

        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.fl_content, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                if (attentionFragment == null) {
                    attentionFragment = new AttentionFragment();
                    transaction.add(R.id.fl_content, attentionFragment);
                } else {
                    transaction.show(attentionFragment);
                }
                break;
            case 2:
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.fl_content, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 3:
                if (selfFragment == null) {
                    selfFragment = new SelfFragment();
                    transaction.add(R.id.fl_content, selfFragment);
                } else {
                    transaction.show(selfFragment);
                }
                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();
    }

    //隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(attentionFragment != null){
            transaction.hide(attentionFragment);
        }
        if(findFragment != null){
            transaction.hide(findFragment);
        }
        if(selfFragment != null){
            transaction.hide(selfFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.calendar_btn:
                initFragment(0);
                break;
            case R.id.attention_btn:
                initFragment(1);
                break;
            case R.id.find_btn:
                initFragment(2);
                break;
            case R.id.self_btn:
                initFragment(3);
                break;
            default:
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

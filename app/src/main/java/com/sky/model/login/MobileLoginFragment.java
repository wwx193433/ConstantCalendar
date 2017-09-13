package com.sky.model.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sky.constantcalendar.R;

/**
 * Created by Administrator on 17-9-12.
 */
public class MobileLoginFragment extends Fragment implements View.OnClickListener{

    //验证码
    private Button verifyCodeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mobile_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        verifyCodeBtn = (Button) view.findViewById(R.id.verifyCodeBtn);
        verifyCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.verifyCodeBtn:
                
                break;
            default:
                break;
        }

    }
}

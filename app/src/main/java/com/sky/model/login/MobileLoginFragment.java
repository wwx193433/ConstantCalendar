package com.sky.model.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.constantcalendar.R;
import com.sky.util.Utility;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by Administrator on 17-9-12.
 */
public class MobileLoginFragment extends Fragment implements View.OnClickListener {

    private EventHandler eventHandler;
    private Handler handler;

    //获取验证码
    private Button loginBtn, clearBtn, verifyCodeBtn;
    //验证码
    private EditText phone, verifyCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mobile_login, container, false);
        initViews(view);

        //注册短信验证SDK
        registEventHandler();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.i("tags", "event:"+event+"  result:"+result+"   data:"+data);

                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(getActivity(), "验证成功",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            Toast.makeText(getActivity(), "验证码已发送，请注意查收",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "获取验证码失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //倒计时一分钟
                        startTimer();
                    }

                if (result == SMSSDK.RESULT_ERROR) {
                    Throwable throwable = (Throwable) data;
                    Log.i("tags", throwable.getMessage());
                }
            }
        };

        return view;
    }

    private void initViews(View view) {
        phone = (EditText) view.findViewById(R.id.phone);
        verifyCode = (EditText) view.findViewById(R.id.verifyCode);

        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        clearBtn = (Button) view.findViewById(R.id.clearBtn);
        verifyCodeBtn = (Button) view.findViewById(R.id.verifyCodeBtn);

        loginBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        verifyCodeBtn.setOnClickListener(this);


        /**
         * 默认登录按钮不可用
         */
        loginBtn.getBackground().mutate().setAlpha(153);
        loginBtn.setEnabled(false);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                activeLoginButton();
            }
        });

        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                activeLoginButton();
            }
        });
    }

    /**
     * 登录按钮激活
     */
    private void activeLoginButton() {
        if (phone.getText().toString().trim().equals("")
                || verifyCode.getText().toString().trim().equals("")) {
            loginBtn.getBackground().mutate().setAlpha(153);
            loginBtn.setEnabled(false);
        } else {
            loginBtn.getBackground().mutate().setAlpha(255);
            loginBtn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verifyCodeBtn://获取验证码
                //验证手机号码
                if (checkPhoneNumber()) {
                    //获取验证码
                    SMSSDK.getVerificationCode("+86", phone.getText().toString().trim());
                    verifyCode.setFocusable(true);
                    verifyCode.setFocusableInTouchMode(true);
                    verifyCode.requestFocus();
                }
                break;
            case R.id.clearBtn:
                phone.setText("");
                break;
            case R.id.loginBtn:
                SMSSDK.submitVerificationCode("+86", phone.getText().toString().trim(), verifyCode.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    /**
     * 校验电话号码
     *
     * @return
     */
    public boolean checkPhoneNumber() {
        String phoneNumber = phone.getText().toString().trim();
        if (phoneNumber.equals("")) {
            phone.setError("手机号码不能为空");
            return false;
        }
        boolean pass = Utility.checkMobileNumber(phone.getText().toString().trim());
        if (!pass) {
            phone.setError("手机号码格式错误");
            return false;
        }
        return true;
    }


    public void registEventHandler() {
        // 创建EventHandler对象
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Log.i("tags", "222222222222222222222222222222");
                Message message = Message.obtain();
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                handler.sendMessage(message);
            }
        };
        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    /**
     * 开启定时器
     */
    public void startTimer() {
        verifyCodeBtn.setEnabled(false);
        MyCountTimer timer = new MyCountTimer(60000, 1000);
        timer.start();
    }

    class MyCountTimer extends CountDownTimer {
        public MyCountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            verifyCodeBtn.setText((millisUntilFinished / 1000) + "秒");
        }

        @Override
        public void onFinish() {
            verifyCodeBtn.setEnabled(true);
            verifyCodeBtn.setText("重新获取");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
}



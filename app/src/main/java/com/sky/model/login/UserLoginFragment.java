package com.sky.model.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sky.constantcalendar.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 17-9-12.
 */
public class UserLoginFragment extends Fragment implements View.OnClickListener{
    private Button loginBtn, forgetPwdBtn;
    private EditText username, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_login, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        loginBtn = (Button) view.findViewById(R.id.loginBtn);
        forgetPwdBtn = (Button) view.findViewById(R.id.forgetPwdBtn);

        loginBtn.setOnClickListener(this);
        forgetPwdBtn.setOnClickListener(this);

        /**
         * 默认登录按钮不可用
         */
        loginBtn.getBackground().mutate().setAlpha(153);
        loginBtn.setEnabled(false);

        username.addTextChangedListener(new TextWatcher() {
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

        password.addTextChangedListener(new TextWatcher() {
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
        if (username.getText().toString().trim().equals("")
                || password.getText().toString().trim().equals("")) {
            loginBtn.getBackground().mutate().setAlpha(153);
            loginBtn.setEnabled(false);
        } else {
            loginBtn.getBackground().mutate().setAlpha(255);
            loginBtn.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.loginBtn:
                new Thread(checkLoginTask).start();
                break;
            case R.id.forgetPwdBtn:

                break;
            default:
                break;
        }
    }

    Runnable checkLoginTask = new Runnable() {
        @Override
        public void run() {
            checkLoginInfo("whoadmins", "!QAZ010Aa393");
        }
    };

    /**
     * 请求服务器
     */
    public void checkLoginInfo(String username, String password){
        HttpURLConnection conn = null;

        //发送GET请求
        try {
            URL url = new URL("");
            conn = (HttpURLConnection) url.openConnection();
            //HttpURLConnection默认就是用GET发送请求，所以下面的setRequestMethod可以省略
            conn.setRequestMethod("GET");
            //HttpURLConnection默认也支持从服务端读取结果流，所以下面的setDoInput也可以省略
            conn.setDoInput(true);
            //禁用网络缓存
            conn.setUseCaches(false);
            //调用getInputStream方法后，服务端才会收到请求，并阻塞式地接收服务端返回的数据
            InputStream is = conn.getInputStream();
            byte[] data = readStream(is);   // 把输入流转换成字符数组
            String str = new String(data);
            switch(str){
                case "success":
                    Log.i("tag", "登录成功，开始跳转.....");
                    break;
                case "empty":
                    Log.i("tag", "用户名不存在");
                    break;
                case "fail":
                    Log.i("tag", "用户名或密码不正确");
                    break;
                default:
                    break;
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 把输入流转换成字符数组
     * @param inputStream   输入流
     * @return  字符数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream){
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                bout.write(buffer, 0, len);
            }
            bout.close();
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return bout.toByteArray();
    }
}
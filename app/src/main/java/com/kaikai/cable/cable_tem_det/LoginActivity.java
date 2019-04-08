package com.kaikai.cable.cable_tem_det;

import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.*;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import bsEnum.urlEnum;
import common.Util;


public class LoginActivity extends ActionBarActivity
{
    private EditText textUserName = null;
    private EditText textPassword = null;
    private Button butRegist = null;
    private Button butLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textUserName = (EditText)findViewById(R.id.et_account);
        textPassword = (EditText)findViewById(R.id.et_password);
        butRegist = (Button)findViewById(R.id.btn_regist);
        butLogin = (Button)findViewById(R.id.btn_login);

        //登录按钮事件
        butLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View var1) {
                final String userName = textUserName.getText().toString();
                final String password = textPassword.getText().toString();
                System.err.println(userName+password);
                if (userName.equals("") || password.equals("")) {
                //if (false) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> requestMap = new HashMap<String, String>();
                            requestMap.put("userName", userName);
                            requestMap.put("password", password);
                            String requestData = Util.json_encode(requestMap);
                            String response = Util.sendJsonPost(requestData, urlEnum.LOGIN_URL);
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            responseMap = Util.json_decode(data, response);
                            String code = responseMap.get("code");
                            System.err.println("code:" + code);
                            if (code != null) {
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
                            } else
                            {

                            }
                        }
                    }).start();
                }
            }
        });
    }
}

package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.*;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import bsEnum.urlEnum;
import common.Util;


public class RegistActivity extends ActionBarActivity
{
    private EditText textUserName = null;
    private EditText textPassword = null;
    private EditText textUseremail = null;
    private EditText textUserphone = null;
    private Button btnRegist = null;

    //监听手机的返回键,注册界面按下返回键返回至登录界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至登录界面
            startActivity(new Intent(RegistActivity.this, LoginActivity.class));
            RegistActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        textUserName = (EditText)findViewById(R.id.re_account);
        textPassword = (EditText)findViewById(R.id.re_password);
        textUseremail = (EditText)findViewById(R.id.re_email);
        textUserphone = (EditText)findViewById(R.id.re_phone);
        btnRegist = (Button)findViewById(R.id.rebtn_regist);

        //确认注册按钮事件
        btnRegist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View var1) {
                final String userName = textUserName.getText().toString();
                final String password = textPassword.getText().toString();
                final String userEmail = textUseremail.getText().toString();
                final String userPhone = textUserphone.getText().toString();
                System.err.println(userName + password + userEmail + userPhone);
                //非空判断,若为空则无法注册,给出提示信息
                if (userName.equals("") ) {
                    //if (false) {
                    Toast.makeText(RegistActivity.this, "请输入用户名!", Toast.LENGTH_SHORT).show();
                } else if(password.equals("")){
                    Toast.makeText(RegistActivity.this, "请输入密码!", Toast.LENGTH_SHORT).show();
                } else if(userEmail.equals("")){
                    Toast.makeText(RegistActivity.this, "请输入邮箱!", Toast.LENGTH_SHORT).show();
                }else if(userPhone.equals("")){
                    Toast.makeText(RegistActivity.this, "请输入手机号!", Toast.LENGTH_SHORT).show();
                } else {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> requestMap = new HashMap<String, String>();
                            requestMap.put("userName", userName);
                            requestMap.put("password", password);
                            requestMap.put("userEmail", userEmail);
                            requestMap.put("userPhone", userPhone);
                            String requestData = Util.json_encode(requestMap);
                            String response = Util.sendJsonPost(requestData, urlEnum.REGIST_URL);
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            responseMap = Util.json_decode(data, response);
                            String code = responseMap.get("code");
                            System.err.println("code:" + code);
                            if (code != null) {
                                Looper.prepare();
                                Toast.makeText(RegistActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
                            } else {
                                //注册成功,给出提示信息,跳转至登录界面
                                Looper.prepare();
                                Toast.makeText(RegistActivity.this, "注册成功,快去登录吧!", Toast.LENGTH_SHORT).show();
                                //Looper.loop();
                                startActivity(new Intent(RegistActivity.this, LoginActivity.class));
                                RegistActivity.this.finish();
                                Looper.loop();
                            }
                        }
                    }).start();
                }
            }
        });

    }
}

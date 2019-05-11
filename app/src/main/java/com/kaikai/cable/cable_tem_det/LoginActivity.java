package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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


public class LoginActivity extends ActionBarActivity
{
    private EditText textUserName = null;
    private EditText textPassword = null;
    private Button butRegist = null;
    private Button butLogin = null;
    private long exitTime = 0;

    @Override
    //监听手机的返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                //登录界面按下返回键,给出提示信息
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        textUserName = (EditText)findViewById(R.id.et_account);
        textPassword = (EditText)findViewById(R.id.et_password);
        butRegist = (Button)findViewById(R.id.btn_regist);
        butLogin = (Button)findViewById(R.id.btn_login);
        //注册按钮事件
        butRegist.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
                LoginActivity.this.finish();
            }
        });
        //登录按钮事件
        butLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View var1) {
                final String account = textUserName.getText().toString();
                final String password = textPassword.getText().toString();
                System.err.println(account+password);
                if (account.equals("") || password.equals("")) {
                //if (false) {
                    Toast.makeText(LoginActivity.this, "用户名和密码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> requestMap = new HashMap<String, String>();
                            requestMap.put("account", account);
                            requestMap.put("password", password);
                            String requestData = Util.json_encode(requestMap);
                            String response = Util.sendJsonPost(requestData, urlEnum.LOGIN_URL);
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            responseMap = Util.json_decode(data, response);
                            String code = responseMap.get("code");
                            System.err.println("code:" + code);
                            System.err.println("UuID:" + Util.uid);
                            if (code != null) {
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
                            } else//登录成功,跳转至功能选择界面
                            {
                                /*Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                Bundle bundle_path = new Bundle();
                                bundle_path.putSerializable("UID", Util.uid);
                                intent.putExtras(bundle_path);
                                startActivity(intent);*/
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                LoginActivity.this.finish();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}

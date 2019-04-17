package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import bsEnum.urlEnum;
import common.Util;

/**
 * Created by cxx on 2019/4/14.
 */
public class UpdatePWActivity extends ActionBarActivity {

    private EditText textPw1 = null;
    private EditText textPw2 = null;
    private Button btnupPW = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至个人中心
            startActivity(new Intent(UpdatePWActivity.this, SafeActivity.class));
            UpdatePWActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatepw);
        ActionBar actionBar=getSupportActionBar();
        textPw1 = (EditText) findViewById(R.id.new_pw);
        textPw2 = (EditText) findViewById(R.id.renew_pw);
        btnupPW = (Button) findViewById(R.id.btn_upPW);
        //登录按钮事件
        btnupPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                final String pw1 = textPw1.getText().toString();
                final String pw2 = textPw2.getText().toString();
                System.err.println(pw1 + pw2);
                if (pw1.equals(pw2)) {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> requestMap = new HashMap<String, String>();
                            requestMap.put("password", pw1);
                            requestMap.put("uid", Util.uid);
                            String requestData = Util.json_encode(requestMap);
                            String response = Util.sendJsonPost(requestData, urlEnum.ChangePassWd_URL);
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            responseMap = Util.json_decode(data, response);
                            String code = responseMap.get("code");
                            System.err.println("code:" + code);
                            if (code != null) {
                                Looper.prepare();
                                Toast.makeText(UpdatePWActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
                            } else
                            {
                                //修改密码成功,给出提示信息,跳转至登录界面
                                Looper.prepare();
                                Toast.makeText(UpdatePWActivity.this, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UpdatePWActivity.this, LoginActivity.class));
                                UpdatePWActivity.this.finish();
                                Looper.loop();
                            }
                        }
                    }).start();
                } else{
                    Toast.makeText(UpdatePWActivity.this, "两次填写的密码不一致", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("修改登录密码");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }

    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp()
    {
        startActivity(new Intent(UpdatePWActivity.this, SafeActivity.class));
        UpdatePWActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

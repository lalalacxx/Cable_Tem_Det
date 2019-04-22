package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
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
 * Created by cxx on 2019/4/21.
 */
public class AddDeviceActivity  extends ActionBarActivity{

    private EditText textDeviceNum = null;
    private EditText textDeviceCode = null;
    private Button btnAddDevice = null;
    //监听手机的返回键,注册界面按下返回键返回至登录界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至登录界面
            startActivity(new Intent(AddDeviceActivity.this, NULLDeviceActivity.class));
            AddDeviceActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddevice);
        textDeviceNum = (EditText)findViewById(R.id.device_num);
        textDeviceCode = (EditText)findViewById(R.id.device_code);
        btnAddDevice = (Button)findViewById(R.id.btn_add_device);
        //确认添加按钮事件
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                final String deviceNum = textDeviceNum.getText().toString();
                final String deviceCode = textDeviceCode.getText().toString();
                if (deviceNum.equals("") || deviceCode.equals("")) {
                    //if (false) {
                    Toast.makeText(AddDeviceActivity.this, "设备号或验证码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Map<String, String> requestMap = new HashMap<String, String>();
                            requestMap.put("uid", Util.uid);
                            requestMap.put("did", deviceNum);
                            requestMap.put("verifyCode", deviceCode);
                            String requestData = Util.json_encode(requestMap);
                            String response = Util.sendJsonPost(requestData, urlEnum.AddDevice_URL);
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            responseMap = Util.json_decode(data, response);
                            String code = responseMap.get("code");
                            System.err.println("code:" + code);
                            if (code != null) {//添加设备失败,给出原因
                                Looper.prepare();
                                Toast.makeText(AddDeviceActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
                            } else//添加设备成功,跳转至我的设备界面
                            {
                                startActivity(new Intent(AddDeviceActivity.this, MyDeviceActivity.class));
                                AddDeviceActivity.this.finish();
                            }
                        }
                    }).start();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("添加设备");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }

    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp()
    {
        startActivity(new Intent(AddDeviceActivity.this, NULLDeviceActivity.class));
        AddDeviceActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

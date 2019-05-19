package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import bsEnum.urlEnum;
import common.MyApplication;
import common.Util;

/**
 * Created by cxx on 2019/4/21.
 */
public class DeviceAddActivity  extends ActionBarActivity{

    private EditText textDeviceNum = null;
    private EditText textDeviceCode = null;
    private Button btnAddDevice = null;
    //监听手机的返回键,添加界面按下返回键返回至我的设备界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至登录界面
            startActivity(new Intent(DeviceAddActivity.this, MyDeviceActivity.class));
            DeviceAddActivity.this.finish();
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
                    Toast.makeText(DeviceAddActivity.this, "设备号或验证码不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    //向服务端传输数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.err.println("********开始添加设备***********");
                            Map<String, String> requestMap = new HashMap<String, String>();
                            MyApplication myApplication = (MyApplication)getApplication();
                            String uid = myApplication.getUid();
                            requestMap.put("uid",  uid);
                            requestMap.put("did", deviceNum);
                            requestMap.put("verifyCode", deviceCode);
                            String requestData = Util.json_encode(requestMap);
                            System.err.println("********调用添加设备接口***********");
                            String response = Util.sendJsonPost(requestData, urlEnum.AddDevice_URL);
                            System.err.println("********调用完成***********");
                            Map<String, String> responseMap = new HashMap<String, String>();
                            String[] data = {"code", "reason"};
                            System.err.println("********开始处理json串***********");
                            responseMap = Util.json_decode(data, response);
                            System.err.println("********处理完成***********");
                            String code = responseMap.get("code");
                            if (code == null) {//添加设备成功
                                //向服务端传输数据
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Map<String, String> requestMap = new HashMap<String, String>();
                                        MyApplication myApplication = (MyApplication)getApplication();
                                        String uid =  myApplication.getUid();
                                        requestMap.put("uid", uid);
                                        String requestData = Util.json_encode(requestMap);
                                        String response = Util.sendJsonPost(requestData, urlEnum.GetUserDevice_URL);
                                        Map<String, String> responseMap = new HashMap<String, String>();
                                        String[] data = {"code", "reason"};
                                        responseMap = Util.json_decode(data, response);
                                        String code = responseMap.get("code");
                                        System.err.println("code:" + code);
                                        if (code != null) {
                                            //请求失败,给出原因
                                            Looper.prepare();
                                            Toast.makeText(DeviceAddActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                            Looper.loop();// 进入loop中的循环，查看消息队列
                                        } else {//请求成功,查看result是否为空
                                            String[] result = Util.getResult(response);
                                            if (result.length == 0) {//result没有元素表示没有可供查看的设备
                                                System.err.println("result.length:" + result.length);
                                                startActivity(new Intent(DeviceAddActivity.this, NULLDeviceActivity.class));
                                                DeviceAddActivity.this.finish();
                                            } else {//result不为空,则取出每一个数据
                                                Intent intent = new Intent(DeviceAddActivity.this, MyDeviceActivity.class);
                                                Bundle bundle_path = new Bundle();
                                                bundle_path.putSerializable("DATA", result);
                                                intent.putExtras(bundle_path);
                                                startActivity(intent);
                                            }
                                        }
                                    }
                                }).start();
                            } else
                            {
                                Looper.prepare();
                                Toast.makeText(DeviceAddActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                Looper.loop();// 进入loop中的循环，查看消息队列
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
        startActivity(new Intent(DeviceAddActivity.this, MyDeviceActivity.class));
        DeviceAddActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

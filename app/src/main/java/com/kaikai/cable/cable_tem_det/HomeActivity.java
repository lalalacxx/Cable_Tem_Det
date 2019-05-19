package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import bsEnum.urlEnum;
import common.MyApplication;
import common.SerializableMap;
import common.Util;

/**
 * Created by cxx on 2019/4/10.
 */
public class HomeActivity extends ActionBarActivity {


    private Button btnUser = null;
    private Button btnData = null;
    private Button btnWarn = null;
    private Button btnDevice = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回键按下则返回至登录界面
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            HomeActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnUser = (Button) findViewById(R.id.btn_user);
        btnData = (Button)findViewById(R.id.btn_data);
        btnWarn = (Button)findViewById(R.id.btn_warn);
        btnDevice = (Button) findViewById(R.id.btn_device);
        //个人中心按钮事件监听
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UserActivity.class));
                HomeActivity.this.finish();
            }
        });
        //设备管理按钮事件监听
        btnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
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
                            Toast.makeText(HomeActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        } else {//请求成功,查看result是否为空
                            String[] result = Util.getResult(response);
                            if (result.length == 0) {//result没有元素表示没有可供查看的设备
                                System.err.println("result.length:" + result.length);
                                startActivity(new Intent(HomeActivity.this, NULLDeviceActivity.class));
                                HomeActivity.this.finish();
                            } else {//result不为空,则取出每一个数据
                                Intent intent = new Intent(HomeActivity.this, MyDeviceActivity.class);
                                Bundle bundle_path = new Bundle();
                                bundle_path.putSerializable("DATA", result);
                                intent.putExtras(bundle_path);
                                startActivity(intent);
                            }
                        }
                    }
                }).start();
            }
        });
        //数据查询按钮事件监听
        btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, HomeDateShowChooseTDActivity.class));
                HomeActivity.this.finish();
            }
        });
        //温度预警按钮事件监听
        btnWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                //向服务端传输数据
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> requestMap = new HashMap<String, String>();
                        MyApplication myApplication = (MyApplication)getApplication();
                        String uid =  myApplication.getUid();
                        requestMap.put("uid",  uid);
                        String requestData = Util.json_encode(requestMap);
                        String response = Util.sendJsonPost(requestData, urlEnum.GetWarnTem_URL);
                        Map<String, String> responseMap = new HashMap<String, String>();
                        String[] data = {"code", "reason"};
                        responseMap = Util.json_decode(data, response);
                        String code = responseMap.get("code");
                        System.err.println("code:" + code);
                        if (code != null) {
                            //请求失败,给出原因
                            Looper.prepare();
                            Toast.makeText(HomeActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        } else  {//请求成功,查看result是否为空
                            String[][] result = Util.getResult2(response);
                            System.err.println("result.length:"+result.length);
                            if(result.length==0) {//result没有元素表示没有预警设备
                                startActivity(new Intent(HomeActivity.this, NULLWarnActivity.class));
                                HomeActivity.this.finish();
                            }else {//result不为空,则取出每一个数据
                                Intent intent = new Intent(HomeActivity.this,WarnTemActivity.class);
                                Bundle bundle_path = new Bundle();
                                //bundle_path.putSerializable("DATA", result);
                                SerializableMap tmpmap=new SerializableMap();
                                tmpmap.setData(result);
                                bundle_path.putSerializable("DATA", tmpmap);
                                //Bundle bundle = new Bundle();
                                intent.putExtras(bundle_path);
                                startActivity(intent);
                            }
                        }
                    }
                }).start();
            }
        });
    }
}
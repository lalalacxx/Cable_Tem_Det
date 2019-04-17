package com.kaikai.cable.cable_tem_det;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
 * Created by cxx on 2019/4/12.
 */
public class SafeActivity extends ActionBarActivity {

    private Button btnPW = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至个人中心
            startActivity(new Intent(SafeActivity.this, UserActivity.class));
            SafeActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);
        ActionBar actionBar=getSupportActionBar();

        btnPW = (Button)findViewById(R.id.btn_pw);
        //登录密码按钮事件监听
        btnPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SafeActivity.this, UpdatePWActivity.class));
                SafeActivity.this.finish();
            }
        });

    }

    //更改用户名的对话框
    public void alert_edit_username(View view){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("修改用户名")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et.getText().toString().equals("")) {
                            //未输入用户名,,给出提示信息返回账户与安全界面
                            Toast.makeText(SafeActivity.this, "用户名错误", Toast.LENGTH_SHORT).show();
                        } else {
                            //按下确定键后的事件:将更改后的新数据发送给服务器端
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> requestMap = new HashMap<String, String>();
                                    requestMap.put("uid", Util.uid);
                                    requestMap.put("username", et.getText().toString());
                                    String requestData = Util.json_encode(requestMap);
                                    String response = Util.sendJsonPost(requestData, urlEnum.ChangeUserName_URL);
                                    Map<String, String> responseMap = new HashMap<String, String>();
                                    String[] data = {"code", "reason"};
                                    responseMap = Util.json_decode(data, response);
                                    String code = responseMap.get("code");
                                    System.err.println("code:" + code);
                                    if (code != null) {
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                        Looper.loop();// 进入loop中的循环，查看消息队列
                                    } else {
                                        //修改用户名成功,给出提示信息返回账户与安全界面
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, "用户名已修改", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }
    //更改邮箱的对话框
    public void alert_edit_email(View view){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("修改邮件地址")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et.getText().toString().equals("")) {
                            //未输入邮箱地址,给出提示信息返回账户与安全界面
                            Toast.makeText(SafeActivity.this, "邮箱地址错误", Toast.LENGTH_SHORT).show();
                        } else {
                            //按下确定键后的事件:将更改后的新数据发送给服务器端
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> requestMap = new HashMap<String, String>();
                                    requestMap.put("uid", Util.uid);
                                    requestMap.put("email", et.getText().toString());
                                    String requestData = Util.json_encode(requestMap);
                                    String response = Util.sendJsonPost(requestData, urlEnum.ChangeEmail_URL);
                                    Map<String, String> responseMap = new HashMap<String, String>();
                                    String[] data = {"code", "reason"};
                                    responseMap = Util.json_decode(data, response);
                                    String code = responseMap.get("code");
                                    System.err.println("code:" + code);
                                    if (code != null) {
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                        Looper.loop();// 进入loop中的循环，查看消息队列
                                    } else {
                                        //修改邮箱成功,给出提示信息返回账户与安全界面
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, "邮箱已修改", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }
    //更改手机号的对话框
    public void alert_edit_mobile(View view){
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("修改手机号")
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (et.getText().toString().equals("")) {
                            //未输入手机号,给出提示信息
                            Toast.makeText(SafeActivity.this, "手机号错误", Toast.LENGTH_SHORT).show();
                        } else {
                            //按下确定键后的事件:将更改后的新数据发送给服务器端
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Map<String, String> requestMap = new HashMap<String, String>();
                                    requestMap.put("uid", Util.uid);
                                    requestMap.put("mobile", et.getText().toString());
                                    String requestData = Util.json_encode(requestMap);
                                    String response = Util.sendJsonPost(requestData, urlEnum.ChangeMobile_URL);
                                    Map<String, String> responseMap = new HashMap<String, String>();
                                    String[] data = {"code", "reason"};
                                    responseMap = Util.json_decode(data, response);
                                    String code = responseMap.get("code");
                                    System.err.println("code:" + code);
                                    if (code != null) {
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                        Looper.loop();// 进入loop中的循环，查看消息队列
                                    } else {
                                        //修改手机号成功,给出提示信息返回账户与安全界面
                                        Looper.prepare();
                                        Toast.makeText(SafeActivity.this, "手机号已修改", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                            }).start();
                        }
                    }
                }).setNegativeButton("取消", null).show();
    }

    //添加actionbar(顶部导航栏)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("账号与安全");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // (导航栏左端的返回箭头)返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(SafeActivity.this, UserActivity.class));
        SafeActivity.this.finish();
        return super.onSupportNavigateUp();
    }

}

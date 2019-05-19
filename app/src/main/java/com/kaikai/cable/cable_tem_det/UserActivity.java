package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;

import common.MyApplication;
import common.Util;

/**
 * Created by cxx on 2019/4/11.
 */
public class UserActivity extends ActionBarActivity {

    private Button btnSafe = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至功能选择界面
            startActivity(new Intent(UserActivity.this, HomeActivity.class));
            UserActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ActionBar actionBar=getSupportActionBar();
        btnSafe = (Button)findViewById(R.id.btn_safe);


        //账户与安全按钮事件监听
        btnSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication myApplication = (MyApplication)getApplication();
                String uid =  myApplication.getUid();
                System.err.println("账户拿到uid: " +  uid);
                startActivity(new Intent(UserActivity.this, SafeActivity.class));
                UserActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_time, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人中心");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(UserActivity.this, HomeActivity.class));
        UserActivity.this.finish();
        return super.onSupportNavigateUp();
    }

}

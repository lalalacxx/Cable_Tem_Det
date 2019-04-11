package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by cxx on 2019/4/10.
 */
public class HomeActivity extends ActionBarActivity {

    private Button btnUser = null;
   // private Button btnData = null;
  //  private Button btnWarn = null;
  //  private Button btnDevice = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
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
        btnUser = (Button)findViewById(R.id.btn_user);
     //   btnData = (Button)findViewById(R.id.btn_data);
       // btnWarn = (Button)findViewById(R.id.btn_warn);
       // btnDevice = (Button)findViewById(R.id.btn_device);
        //个人中心按钮事件监听
        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, UserActivity.class));
                HomeActivity.this.finish();
            }
        });
        //设备管理按钮事件监听
       /* btnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DeviceActivity.class));
                HomeActivity.this.finish();
            }
        });*/
        //数据查询按钮事件监听
        /*btnData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DataActivity.class));
                HomeActivity.this.finish();
            }
        });
        //温度预警按钮事件监听
        btnWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WarnActivity.class));
                HomeActivity.this.finish();
            }
        });*/
    }
}
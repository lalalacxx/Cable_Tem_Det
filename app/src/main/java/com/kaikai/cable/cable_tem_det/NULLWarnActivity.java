package com.kaikai.cable.cable_tem_det;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by cxx on 2019/5/7.
 */
public class NULLWarnActivity extends ActionBarActivity {
    private Button btnTurnTem = null;
    //监听手机的返回键,注册界面按下返回键返回至登录界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至登录界面
            startActivity(new Intent(NULLWarnActivity.this, HomeActivity.class));
            NULLWarnActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nullwarn);
        btnTurnTem = (Button)findViewById(R.id.btn_nullWarn);
        //"跳转至温度查询"按钮事件
        btnTurnTem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NULLWarnActivity.this,DataShowActivity.class));
                NULLWarnActivity.this.finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_bar, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("温度预警");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }

    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp()
    {
        startActivity(new Intent(NULLWarnActivity.this, HomeActivity.class));
        NULLWarnActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

package com.kaikai.cable.cable_tem_det;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by cxx on 2019/4/20.
 */
public class NULLDeviceActivity extends Activity{

    private Button btnAddDevice = null;

    //监听手机的返回键,注册界面按下返回键返回至登录界面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回键按下则返回至登录界面
            startActivity(new Intent(NULLDeviceActivity.this, HomeActivity.class));
            NULLDeviceActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nulldevice);
        btnAddDevice = (Button)findViewById(R.id.btn_nullDevice_add);
        //"点我添加"按钮事件
        btnAddDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NULLDeviceActivity.this,AddDeviceActivity.class));
                NULLDeviceActivity.this.finish();
            }
        });
    }
}

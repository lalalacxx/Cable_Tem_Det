package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

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
        btnSafe = (Button)findViewById(R.id.btn_safe);
        //账户与安全按钮事件监听
        btnSafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserActivity.this, SafeActivity.class));
                UserActivity.this.finish();
            }
        });
    }
}

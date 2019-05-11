package com.kaikai.cable.cable_tem_det;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsEnum.urlEnum;
import common.Util;

/**
 * Created by cxx on 2019/4/20.
 */
public class MyDeviceActivity extends ActionBarActivity{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemain);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        String[] result = bundle.getStringArray("DATA");//getString()返回指定key的值

        List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();

        for(int i = 0;i < result.length;i++) {
            map.put("did", result[i]);
            ls.add(map);
            System.err.println("呵呵呵:"+result[i]);
        }
        //使用标签ListView存放
        ListView lvList = (ListView) findViewById(R.id.lv);
        //将list中每个对象虚拟为一个Item，然后再存入Grid中的每一行,listitemlayout就相当于一个item
        ListAdapter adapter = new SimpleAdapter(this, ls, R.layout.activity_deviceitem,
                new String[] {"did"}, new int[] {R.id.did});
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position 点击的Item位置，从0开始算
                Intent intent = new Intent();
                intent.putExtra("xx", "");//传递给下一个Activity的值
                startActivity(intent);//启动Activity
            }
        });

       // System.err.println("str.length:"+result.length);
    }

   /* public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        System.out.println("当前值："+position);
        Map<String, String> temp = map.get(position);
//temp是该选项的内容*/


    //点击设备号弹出对话框(可供选择:删除,查看数据)
    public void check_del(View view) {
        final TextView did = new TextView(this);
        //Map<String, String> temp = map.get(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> requestMap = new HashMap<String, String>();
                requestMap.put("uid", Util.uid);
                requestMap.put("did", did.getText().toString());
                System.err.println("did_did:"+did.getText().toString());
                String requestData = Util.json_encode(requestMap);
                String response = Util.sendJsonPost(requestData, urlEnum.LOGIN_URL);
                Map<String, String> responseMap = new HashMap<String, String>();
                String[] data = {"code", "reason"};
                responseMap = Util.json_decode(data, response);
                String code = responseMap.get("code");
                System.err.println("code:" + code);
                if (code != null) {//查看数据失败,给出原因
                    Looper.prepare();
                    Toast.makeText(MyDeviceActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } else {
                    //查看数据成功,跳转至数据展示界面
                    startActivity(new Intent(MyDeviceActivity.this, DataShowActivity.class));
                    MyDeviceActivity.this.finish();
                }
            }
        }).start();
    }


    //ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("我的设备");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(MyDeviceActivity.this, HomeActivity.class));
        MyDeviceActivity.this.finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(MyDeviceActivity.this, AddDeviceActivity.class));
                MyDeviceActivity.this.finish();
                finish();
                return true;
            case R.id.menu_del:
                Toast.makeText(this, "menu_del", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_add:
                startActivity(new Intent(MyDeviceActivity.this, AddDeviceActivity.class));
                MyDeviceActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

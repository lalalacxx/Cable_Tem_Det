package com.kaikai.cable.cable_tem_det;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bsEnum.urlEnum;
import common.MyApplication;
import common.Util;

/**
 * Created by cxx on 2019/4/20.
 */
public class MyDeviceActivity extends ActionBarActivity {
    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回键按下则返回至登录界面
            startActivity(new Intent(MyDeviceActivity.this, HomeActivity.class));
            MyDeviceActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemain);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        String[] result = bundle.getStringArray("DATA");//getString()返回指定key的值
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
        for(int i = 0;i < result.length;i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("did", result[i]);
            ls.add(map);
        }
        //使用标签ListView存放
        ListView MyDeviceList = (ListView) findViewById(R.id.lv);
        //将list中每个对象虚拟为一个Item，然后再存入Grid中的每一行,listitemlayout就相当于一个item
        ListAdapter adapter = new SimpleAdapter(this, ls, R.layout.activity_deviceitem,
                new String[] {"did"}, new int[] {R.id.did});
        MyDeviceList.setAdapter(adapter);
        //监听item
        MyDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) parent
                        .getItemAtPosition(position);
                final String did = map.get("did");
                Intent intent = new Intent(MyDeviceActivity.this, ChooseTimeActivity.class);
                Bundle bundle_path = new Bundle();
                bundle_path.putSerializable("DID", did);
                intent.putExtras(bundle_path);
                startActivity(intent);
            }
        });
        MyDeviceList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                HashMap<String, String> map = (HashMap<String, String>) arg0
                        .getItemAtPosition(arg2);
                final String did = map.get("did");
                System.err.println("长按获得DID" + did);
                builder.setMessage("真的要删除该设备么?").setPositiveButton("是",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which){
                        //删除成功,向服务端传输数据
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Map<String, String> requestMap = new HashMap<String, String>();
                                MyApplication myApplication = (MyApplication) getApplication();
                                String uid = myApplication.getUid();
                                requestMap.put("uid", uid);
                                requestMap.put("did", did);
                                String requestData = Util.json_encode(requestMap);
                                String response = Util.sendJsonPost(requestData, urlEnum.DelDevice_URL);
                                Map<String, String> responseMap = new HashMap<String, String>();
                                String[] data = {"code", "reason"};
                                responseMap = Util.json_decode(data, response);
                                String code = responseMap.get("code");
                                System.err.println("code:" + code);
                                if (code != null) {
                                    Looper.prepare();
                                    Toast.makeText(MyDeviceActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                    Looper.loop();// 进入loop中的循环，查看消息队列
                                } else {//删除设备成功//向服务端传输数据
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Map<String, String> requestMap = new HashMap<String, String>();
                                            MyApplication myApplication = (MyApplication)getApplication();
                                            String uid =  myApplication.getUid();
                                            requestMap.put("uid",  uid);
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
                                            Toast.makeText(MyDeviceActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                                            Looper.loop();// 进入loop中的循环，查看消息队列
                                        } else {//请求成功,查看result是否为空
                                            String[] result = Util.getResult(response);
                                            if (result.length == 0) {//result没有元素表示没有可供查看的设备
                                                System.err.println("result.length:" + result.length);
                                                startActivity(new Intent(MyDeviceActivity.this, NULLDeviceActivity.class));
                                                MyDeviceActivity.this.finish();
                                            } else {//result不为空,则取出每一个数据
                                                Intent intent = new Intent(MyDeviceActivity.this, MyDeviceActivity.class);
                                                Bundle bundle_path = new Bundle();
                                                bundle_path.putSerializable("DATA", result);
                                                intent.putExtras(bundle_path);
                                                startActivity(intent);
                                            }
                                            }
                                        }
                                    }).start();
                                }
                            }
                        }).start();
                    }
                }).setNegativeButton("否",null).show();
                return true;
            }
        });
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
                startActivity(new Intent(MyDeviceActivity.this, HomeActivity.class));
                MyDeviceActivity.this.finish();
                finish();
                return true;
            case R.id.menu_add:
                startActivity(new Intent(MyDeviceActivity.this, DeviceAddActivity.class));
                MyDeviceActivity.this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

package com.kaikai.cable.cable_tem_det;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
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
import common.MyApplication;
import common.SerializableMap;
import common.Util;

/**
 * Created by cxx on 2019/5/7.
 */
public class WarnTemActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicemain);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        SerializableMap result = (SerializableMap) bundle.get("DATA");
        List<Map<String, String>> ls = new ArrayList<Map<String, String>>();
        System.err.println("result.LENGTHhah:"+result.size());
        for (int i = 0; i < result.size();i++) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("did", result.showData(i,0));
            map.put("temperature", result.showData(i,1));
            map.put("update_time", result.showData(i,2));
            ls.add(map);
            System.err.println("设备:" + result.showData(i, 0)+"  温度" + result.showData(i, 1)+"  时间" + result.showData(i, 2));
        }
        //使用标签ListView存放
        ListView warnList = (ListView) findViewById(R.id.WarnList);
        //将list中每个对象虚拟为一个Item，然后再存入Grid中的每一行,listitemlayout就相当于一个item
        ListAdapter adapter = new SimpleAdapter(this, ls, R.layout.activity_warnitem,
                new String[] {"did","temperature","update_time"}, new int[] {R.id.warnDid,R.id.warnTem,R.id.warnDate});
        warnList.setAdapter(adapter);
        //监听item
        warnList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HashMap<String, String> map = (HashMap<String, String>) parent
                        .getItemAtPosition(position);
                final String did = map.get("did");
                final String date = map.get("update_time");
                //拆分获取到的日期date(拆分出year,month,day,hour)
                //final String year =
                //final String month =
                //final String day =
                //final String hour =
                Intent intent = new Intent(WarnTemActivity.this, DataShowActivity.class);
                Bundle bundle_path = new Bundle();
                bundle_path.putSerializable("DID", did);
                //bundle_path.putSerializable("YEAR",year );
               // bundle_path.putSerializable("MONTH", month);
               // bundle_path.putSerializable("DAY", day);
                //bundle_path.putSerializable("HOUR", hour);
                intent.putExtras(bundle_path);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_time,menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("温度预警");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(WarnTemActivity.this, HomeActivity.class));
        WarnTemActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

package com.kaikai.cable.cable_tem_det;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import bsEnum.urlEnum;
import common.MyApplication;
import common.Util;

/**
 * Created by cxx on 2019/5/17.
 */
public class HomeDateShowChooseTDActivity extends ActionBarActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    private Context context;
    private LinearLayout llDevice,llDate, llTime;
    private TextView tvDevice,tvDate, tvTime;
    private int year, month, day, hour, minute;
    //在TextView上显示的字符
    private StringBuffer device,date, time;
    private Button butSureTD = null;
    String did = "";
    String[] items = {};

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回键按下则返回至登录界面
            startActivity(new Intent(HomeDateShowChooseTDActivity.this, HomeActivity.class));
            HomeDateShowChooseTDActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_td);
        butSureTD = (Button) findViewById(R.id.btn_sure_td);
        context = this;
        device = new StringBuffer();
        date = new StringBuffer();
        time = new StringBuffer();
        initView();
        initDateTime();
        butSureTD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View var1) {
                final String Year = String.valueOf(year);
                final String Month = String.valueOf(month);
                final String Day = String.valueOf(day);
                final String Hour = String.valueOf(hour);
                Intent intent = new Intent(HomeDateShowChooseTDActivity.this,DataShowActivity.class);
                Bundle bundle_path = new Bundle();
                bundle_path.putSerializable("DID", did);
                bundle_path.putSerializable("YEAR",Year);
                bundle_path.putSerializable("MONTH", Month);
                bundle_path.putSerializable("DAY", Day);
                bundle_path.putSerializable("HOUR", Hour);
                intent.putExtras(bundle_path);
                startActivity(intent);
            }
        });
    }
    /**
     * 初始化控件
     */
    private void initView() {
        llDevice = (LinearLayout) findViewById(R.id.ll_device);
        tvDevice = (TextView) findViewById(R.id.tv_device);
        llDate = (LinearLayout) findViewById(R.id.ll_date);
        tvDate = (TextView) findViewById(R.id.tv_date);
        llTime = (LinearLayout) findViewById(R.id.ll_time);
        tvTime = (TextView) findViewById(R.id.tv_time);
        llDevice.setOnClickListener(this);
        llDate.setOnClickListener(this);
        llTime.setOnClickListener(this);
    }
    /**
     * 获取当前的日期和时间
     */
    private void initDateTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) { //清除上次记录的日期
                            date.delete(0, date.length());
                        }
                        tvDate.setText(date.append(String.valueOf(year)).append("年").append(String.valueOf(month)).append("月").append(day).append("日"));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                View dialogView = View.inflate(context, R.layout.activity_dialog_date, null);
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("请选择日期");
                dialog.setView(dialogView);
                dialog.show();
                //初始化日期监听事件
                datePicker.init(year, month+1, day, this);
                break;
            case R.id.ll_time:
                AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (time.length() > 0) { //清除上次记录的日期
                            time.delete(0, time.length());
                        }
                        tvTime.setText(time.append(String.valueOf(hour)).append("时").append(String.valueOf(minute)).append("分"));
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog2 = builder2.create();
                View dialogView2 = View.inflate(context, R.layout.activity_dialog_time, null);
                TimePicker timePicker = (TimePicker) dialogView2.findViewById(R.id.timePicker);
                timePicker.setCurrentHour(hour);
                timePicker.setCurrentMinute(minute);
                timePicker.setIs24HourView(true); //设置24小时制
                timePicker.setOnTimeChangedListener(this);
                dialog2.setTitle("请选择时间");
                dialog2.setView(dialogView2);
                dialog2.show();
                break;
            case R.id.ll_device:
                //向服务端传输数据
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
                            Toast.makeText(HomeDateShowChooseTDActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                            Looper.loop();// 进入loop中的循环，查看消息队列
                        } else {//请求成功,查看result是否为空
                            String[] result = Util.getResult(response);
                            if (result.length == 0) {//result没有元素表示没有可供查看的设备
                                System.err.println("result.length:" + result.length);
                                startActivity(new Intent(HomeDateShowChooseTDActivity.this, NULLDeviceActivity.class));
                                HomeDateShowChooseTDActivity.this.finish();
                            } else {//result不为空,则取出每一个数据
                                items = new String[result.length] ;
                                items = result;
                            }
                        }
                    }
                }).start();
                for(int i = 0;i < items.length;i++) {
                    System.err.println("items"+i+":"+items[i]);
                }
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setTitle("请选择设备");
                alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        did = items[i];
                    }
                });
                alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        if (device.length() > 0) { //清除上次记录的日期
                            device.delete(0, device.length());
                        }
                        tvDevice.setText(device.append(did));
                        dialog.dismiss();
                    }
                });
                alertBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog3 = alertBuilder.create();
                dialog3.show();
        }
    }
    /**
     * 日期改变的监听事件
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear+1;
        this.day = dayOfMonth;
    }
    /**
     * 时间改变的监听事件
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_time, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//添加返回箭头
        getSupportActionBar().setTitle("选择设备及时间");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(HomeDateShowChooseTDActivity.this, HomeActivity.class));
        HomeDateShowChooseTDActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

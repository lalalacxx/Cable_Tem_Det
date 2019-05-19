package com.kaikai.cable.cable_tem_det;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;

/**
 * Created by cxx on 2019/5/14.
 */
public class ChooseTimeActivity   extends ActionBarActivity implements View.OnClickListener, DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener{
    private Context context;
    private LinearLayout llDate, llTime;
    private TextView tvDate, tvTime;
    private int year, month, day, hour, minute;
    //在TextView上显示的字符
    private StringBuffer date, time;
    private Button butSureTime = null;

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回键按下则返回至设备管理界面
            startActivity(new Intent(ChooseTimeActivity.this, MyDeviceActivity.class));
            ChooseTimeActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        final String did = bundle.getString("DID");//getString()返回指定key的值

        context = this;
        date = new StringBuffer();
        time = new StringBuffer();
        initView();
        initDateTime();

        butSureTime = (Button) findViewById(R.id.btn_sure_time);
        //"确认时间"按钮监听事件
        butSureTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View var1) {
                //选择好时间以后,将时间信息以及设备号传递给温度数据展示界面以供调用接口查询温度数据
                    final String Year = String.valueOf(year);
                    final String Month = String.valueOf(month);
                    final String Day = String.valueOf(day);
                    final String Hour = String.valueOf(hour);
                    Intent intent = new Intent(ChooseTimeActivity.this, DataShowActivity.class);
                    Bundle bundle_path = new Bundle();
                    bundle_path.putSerializable("DID", did);
                    bundle_path.putSerializable("YEAR", Year);
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
            llDate = (LinearLayout) findViewById(R.id.ll_date);
            tvDate = (TextView) findViewById(R.id.tv_date);
            llTime = (LinearLayout) findViewById(R.id.ll_time);
            tvTime = (TextView) findViewById(R.id.tv_time);
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
        getSupportActionBar().setTitle("选择时间");  //设置Title文字
        return super.onCreateOptionsMenu(menu);
    }
    // 返回上一界面
    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(ChooseTimeActivity.this, MyDeviceActivity.class));
        ChooseTimeActivity.this.finish();
        return super.onSupportNavigateUp();
    }
}

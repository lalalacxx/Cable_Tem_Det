package com.kaikai.cable.cable_tem_det;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bsEnum.urlEnum;
import common.MyApplication;
import common.Util;

/**
 * Created by cxx on 2019/4/23.
 */
public class DataShowActivity extends Activity {
    private Button btn_choose_TD = null;

    String did;  //设备号
    String uid;//用户
    String year;//用户
    String month;//用户
    String day;//用户
    String hour;//用户

    LineChart lc;

    //float[] ys1;
    float[] history_data_day = new float[24];
    float[] history_data_week = new float[12];
    float[] history_data_month = new float[30];

    //监听手机的返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //返回键按下则返回至首页
            startActivity(new Intent(DataShowActivity.this, HomeActivity.class));
            DataShowActivity.this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = getIntent().getExtras();//.getExtras()得到intent所附带的额外数据
        did = bundle.getString("DID");//getString()返回指定key的值
        year = bundle.getString("YEAR");
        month = bundle.getString("MONTH");
        day = bundle.getString("DAY");
        hour = bundle.getString("HOUR");
        lc = (LineChart) findViewById(R.id.data_chart);
        MyApplication myApplication = (MyApplication) getApplication();
        uid = myApplication.getUid();

        setTitle();//设置标题  xx的室内xx情况
        showChart(history_data_day);
        showData_D();   //获取并显示数据

        btn_choose_TD = (Button) findViewById(R.id.choose_time_device);
        //选择设备及时间按钮事件监听
        btn_choose_TD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DataShowActivity.this, ChooseTDActivity.class));
                DataShowActivity.this.finish();
            }
        });
    }
    private void setXAxis() {
        // X轴
        XAxis xAxis = lc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 在底部
        xAxis.setDrawGridLines(false); // 不绘制网格线
        xAxis.setLabelCount(20); // 设置标签数量
        xAxis.setTextColor(Color.WHITE); // 文本颜色为灰色
        xAxis.setTextSize(12f); // 文本大小为12dp
        xAxis.setGranularity(3f); // 设置间隔尺寸
        xAxis.setAxisMinimum(0f); // 设置X轴最小值
        xAxis.setAxisMaximum(24f); // 设置X轴最大值
        // 设置标签的显示格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == 0 ? "℃" : value == 63 ? "(S)" : value < 10 ? "0" + (int) value : (int) value + "";
            }
        });
    }

    private void setYAxis() {
        // 左边Y轴
        final YAxis yAxisLeft = lc.getAxisLeft();
        yAxisLeft.setAxisMaximum(40); // 设置Y轴最大值
        yAxisLeft.setAxisMinimum(-10); // 设置Y轴最小值
        yAxisLeft.setGranularity(2f); // 设置间隔尺寸
        yAxisLeft.setTextSize(12f); // 文本大小为12dp
        yAxisLeft.setTextColor(Color.WHITE); // 文本颜色为灰色
        yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return value == yAxisLeft.getAxisMinimum() ? (int) value + "" : (int) value + "";
            }
        });
        // 右侧Y轴
        lc.getAxisRight().setEnabled(false); // 不启用
    }

    public void setChartData(float[] history_data) {
        // 1. 获取一或多组Entry对象集合的数据
        // 模拟数据1
        List<Entry> yVals1 = new ArrayList<>();
        for (int i = 0; i < history_data.length; i++) {
            yVals1.add(new Entry(i, history_data[i]));
        }
        // 2. 分别通过每一组Entry对象集合的数据创建折线数据集
        LineDataSet lineDataSet1 = new LineDataSet(yVals1, "设备温度数据");
        //lineDataSet1.setColor(Color.GREEN); // 设置折线为红色
        lineDataSet1.setDrawCircles(true);//在点上画圆 默认true
        //lineDataSet1.setDrawCircleHole(false); // 不绘制圆洞，即为实心圆点
        lineDataSet1.setHighLightColor(Color.GREEN); // 设置点击某个点时，横竖两条线的颜色
        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置为贝塞尔曲线
        lineDataSet1.setCubicIntensity(0.15f); // 强度
        lineDataSet1.setCircleColor(Color.WHITE); // 设置圆点为颜色
        lineDataSet1.setCircleRadius(5f);
        lineDataSet1.setLineWidth(1f); // 设置线宽为2

        // 3.将每一组折线数据集添加到折线数据中
        LineData lineData = new LineData(lineDataSet1);
        lineData.setDrawValues(false);
        // 4.将折线数据设置给图表
        lc.setData(lineData);
    }

    //显示图表
    private void showChart(float[] history_data) {
        //向LineChart插入数据
        setXAxis();
        setYAxis();
        setChartData(history_data);
    }

    //设置标题
    private void setTitle() {
        TextView title = (TextView) findViewById(R.id.TemTitle);
        Intent i = this.getIntent();
        title.setText(did+ "的历史温度");
    }
    //从服务器获取待查看的设备的温度数据，并显示到UI，制作图标
    private void showData_D() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> requestMap = new HashMap<String, String>();

                MyApplication myApplication = (MyApplication) getApplication();
                String uid = myApplication.getUid();
                requestMap.put("uid", uid);

                requestMap.put("did", did);
                requestMap.put("year", year);
                requestMap.put("month", month);
                requestMap.put("day", day);
                requestMap.put("hour", hour);

                String requestData = Util.json_encode(requestMap);
                String response = Util.sendJsonPost(requestData, urlEnum.CheckDeviceTem_URL);
                Map<String, String> responseMap = new HashMap<String, String>();
                String[] data = {"code", "reason"};
                responseMap = Util.json_decode(data, response);
                String code = responseMap.get("code");
                System.err.println("code:" + code);
                if (code != null) {
                    Looper.prepare();
                    Toast.makeText(DataShowActivity.this, responseMap.get("reason"), Toast.LENGTH_SHORT).show();
                    Looper.loop();// 进入loop中的循环，查看消息队列
                } else {//处理拿到的温度数据,展示数据在折现图上

                }
            }
        }).start();
    }


}

package com.example.huangcl.batteryinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import static com.example.huangcl.batteryinfo.MainActivity.*;

public class BatteryInfoReceiver extends BroadcastReceiver {

    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {

        mContext=context;
        String action = intent.getAction();

        //如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()

        if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
            BatteryN = intent.getIntExtra("level", 0);    //目前电量（0~100）
            BatteryV = intent.getIntExtra("voltage", 0);  //电池电压(mv)
            BatteryT = intent.getIntExtra("temperature", 0);  //电池温度(数值)
            BatteryT = BatteryT / 10.0; //电池摄氏温度，默认获取的非摄氏温度值，需做一下运算转换
            BatteryStyle=intent.getStringExtra("technology"); //电池类型
            BatteryScale=intent.getIntExtra("scale",0);//电池容量
            BatteryMcc=intent.getStringExtra("max_charging_current");//最大充电电流
            BatteryMcv=intent.getStringExtra("max_charging_voltage");//最大充电电压
            BatteryCc=intent.getStringExtra("charge_counter");//累计充电次数
            BatterySeq=intent.getStringExtra("seq");//当前更新序列号
            BatteryNow=intent.getIntExtra("BATTERY_PROPERTY_CURRENT_NOW",0);//瞬时电流
            BatteryAverage=intent.getIntExtra("BATTERY_PROPERTY_CURRENT_AVERAGE",0);//平均电流

            switch (intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    BatteryStatus = "充电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    BatteryStatus = "放电状态";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    BatteryStatus = "未充电";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    BatteryStatus = "充满电";

                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                    BatteryStatus = "未知道状态";
                    break;
            }

            switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                    BatteryTemp = "未知错误";
                    break;
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    BatteryTemp = "状态良好";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD:
                    BatteryTemp = "电池没有电";

                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    BatteryTemp = "电池电压过高";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    BatteryTemp = "电池过热";
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD:
                    BatteryTemp = "电池过冷";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    BatteryTemp = "未指定故障";
                    break;
            }

            switch (intent.getIntExtra("plugged", 0)) {
                case BatteryManager.BATTERY_PLUGGED_AC:
                    BatteryPlugged = "AC充电";
                    break;
                case BatteryManager.BATTERY_PLUGGED_USB:
                    BatteryPlugged = "USB充电";
                    break;
                case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                    BatteryPlugged = "无线充电";
                    break;
            }

            if(intent.getBooleanExtra("present",false)) {
                BatteryPresent="存在电池";
            }
            else {
                BatteryPresent="不存在电池";
            }

            dataList.clear();
            initDataList();
        }
        setAadapterForListView();
    }

    private void initDataList() {
        Data data1=new Data("BatteryN(剩余电量)："+BatteryN);
        dataList.add(data1);
        Data data2=new Data("BatteryV(电压)："+BatteryV);
        dataList.add(data2);
        Data data3=new Data("BatteryT(温度)："+BatteryT);
        dataList.add(data3);
        Data data4=new Data("BatteryStatus(状态)："+BatteryStatus);
        dataList.add(data4);
        Data data5=new Data("BatteryTemp(使用情况)："+BatteryTemp);
        dataList.add(data5);
        Data data6=new Data("BatteryStyle(类型)："+BatteryStyle);
        dataList.add(data6);
        Data data7=new Data("BatteryPlugged(充电方式)："+BatteryPlugged);
        dataList.add(data7);
        Data data8=new Data("BatteryPresent(是否存在电池)："+BatteryPresent);
        dataList.add(data8);
        Data data9=new Data("BatteryScale(电池容量)："+BatteryScale);
        dataList.add(data9);
        Data data10=new Data("BatteryAverage(平均电流)："+BatteryAverage);
        dataList.add(data10);
        Data data11=new Data("BatteryNow(瞬时电流)："+BatteryNow);
        dataList.add(data11);
    }

    public void setAadapterForListView() {
        adapter=new BatteryAdapter(mContext, R.layout.item_layout, dataList);
        lv.setAdapter(adapter);
    }
}

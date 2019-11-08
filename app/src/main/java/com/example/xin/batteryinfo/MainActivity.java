package com.example.xin.batteryinfo;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    public static int BatteryN;       //目前电量
    public static int BatteryV;       //电池电压
    public static double BatteryT;        //电池温度
    public static String BatteryStatus;   //电池状态
    public static String BatteryTemp;     //电池使用情况
    public static String BatteryStyle;  //电池类型
    public static String BatteryPlugged;  //充电方式
    public static String BatteryPresent;//是否存在电池
    public static int BatteryScale;//电池容量
    public static String BatteryMcc;//最大充电电流
    public static String BatteryMcv;//最大充电电压
    public static String BatteryCc;//累计充电次数
    public static String BatterySeq;//累计充电次数
    public static int BatteryNow;//瞬时电流
    public static int BatteryAverage;//平均电流
    public BatteryInfoReceiver batteryInfoReceiver;

    public static ListView lv;

    public static List<Data> dataList=new ArrayList<>();
    public static BatteryAdapter adapter;
    public BatteryInfoGet mBatteryInfoGet;
    public ShellUtils mShellUtils;
    private static Context instance;
    private int mMessageCount = 0;
    private Thread myThread;
    private Timer timer;

    private  String TAG = "xin.qi";

    class MyRunnable implements Runnable {
        public void run () {
            mBatteryInfoGet.DisplayDataList();
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    mBatteryInfoGet.DisplayDataList();
                    break;
            }
        };
    };

    private class MyTask extends TimerTask {

        private Activity context;
        MyTask(Activity context)
        {
            this.context = context;
        }

        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermisson();

        lv = (ListView)findViewById(R.id.lv_battery_info);

        instance = getApplicationContext();

        String[] commands = new String[] { "setenforce 0"};
        ShellUtils.execCommand(commands, true);
        //第一种通过接收ACTION_BATTERY_CHANGED事件来更新显示
        /*
        batteryInfoReceiver = new BatteryInfoReceiver();
        registerReceiver(batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        */

        MyRunnable myRunnable = new MyRunnable();

        mBatteryInfoGet = new BatteryInfoGet();

        mBatteryInfoGet.start();

        timer = new Timer();

        timer.scheduleAtFixedRate(new MyTask(this), 1, 500);
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(batteryInfoReceiver);
        timer.cancel();
        mBatteryInfoGet.exit();
        super.onDestroy();
    }

    private void checkPermisson() {

        int REQUEST_CODE_CONTACT = 101;
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //验证是否许可权限
        for (String str : permissions) {
            if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                return;
            }
        }
    }
}

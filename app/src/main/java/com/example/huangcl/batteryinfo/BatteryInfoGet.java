package com.example.huangcl.batteryinfo;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.huangcl.batteryinfo.MainActivity.adapter;
import static com.example.huangcl.batteryinfo.MainActivity.dataList;
import static com.example.huangcl.batteryinfo.MainActivity.lv;


public class BatteryInfoGet extends Thread {

    private Thread t;
    private static int cnt = 0;
    private static long current_time = 0;
    private String file_name = "power_history.txt";
    public int voltage, capacity, tempeture;
    private String batterystatus, usb_type, usb_online, usb_present, usb_voltage, usb_input_current_now, usb_real_type;
    private String bms_voltage_ocv, bms_capacity_raw;

    private String TAG = "xin.qi";
    private static int mMessageCount = 0;
    private String file_path = Environment.getExternalStoragePublicDirectory("") + "/getBatteryInfo/";

    public void start(){
        if (t == null) {
            setAadapterForListView();
            t = new Thread (this);
            t.start ();
        }
    }

    public void exit() {
        t.interrupt();
    }

    public void setAadapterForListView() {
        adapter = new BatteryAdapter(MainActivity.getContext(), R.layout.item_layout, dataList);
        lv.setAdapter(adapter);
    }

    public void initDataList() {
        Data data1=new Data("显示Battery节点信息： /sys/class/power_supply/battery");
        dataList.add(data1);
        Data data2=new Data("BatteryS(状态)："+ batterystatus);
        dataList.add(data2);
        Data data3=new Data("BatteryC(剩余电量)："+ capacity);
        dataList.add(data3);
        Data data4=new Data("BatteryV(电压)："+ voltage);
        dataList.add(data4);
        Data data5=new Data("BatteryT(温度)："+ tempeture);
        dataList.add(data5);
        Data data6=new Data("显示USB节点信息: /sys/class/power_supply/usb");
        dataList.add(data6);
        Data data7 = new Data("Usb_type(充电器类型): " + usb_type);
        dataList.add(data7);
        Data data8 = new Data("Usb_IN_Voltage(USB_IN电压): " + usb_voltage);
        dataList.add(data8);
        Data data9 = new Data("Usb_Online: " + usb_online);
        dataList.add(data9);
        Data data10 = new Data("Usb_Present(USB状态): " + usb_present);
        dataList.add(data10);
        Data data11 = new Data("Usb_input_current_now(输入电流): " + usb_input_current_now);
        dataList.add(data11);
        Data data12 = new Data("显示BMS节点信息: /sys/class/power_supply/bms");
        dataList.add(data12);
        Data data13 = new Data("Bms_voltage_ocv(OCV电压): " + bms_voltage_ocv);
        dataList.add(data13);
        Data data14 = new Data("Bms_capacity_raw: " + bms_capacity_raw);
        dataList.add(data14);
    }

    public void DisplayDataList() {
        dataList.clear();
        initDataList();
        setAadapterForListView();
    }

    @Override
    public void run() {
        try {
            while (true) {
                cnt++;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String o = getbatteryinfo();
                getusbinfo();
                getbmsinfo();
                FileUtils.writeTxtToFile(cnt + "---" + simpleDateFormat.format(date) + o, file_path,file_name);

                Thread.sleep( 500);
            }
        } catch (InterruptedException e) {
            System.out.println("Thread "+" interrupted.");
        }
    }

    private String getbatteryinfo() {
        String result = "null";
        try {
            capacity = readFile("/sys/class/power_supply/battery/capacity", 100);
            tempeture = readFile("/sys/class/power_supply/battery/temp", 25) / 10;
            voltage = readFile("/sys/class/power_supply/battery/voltage_now", 0) / 1000;
            batterystatus = readFile("/sys/class/power_supply/battery/status");
            result = " --capacity--" + (capacity) + "--voltage--" + voltage + "mv" + "--tempeture --" + tempeture + "℃";
            System.out.println("capacity " + capacity + " voltage " + voltage + " tempetrue " + tempeture + " status " + batterystatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getusbinfo() {
        String result = "null";

        try {
            usb_type    = readFile("/sys/class/power_supply/usb/type");
            usb_voltage = readFile("/sys/class/power_supply/usb/voltage_now");
            usb_online  = readFile("/sys/class/power_supply/usb/online");
            usb_present  = readFile("/sys/class/power_supply/usb/present");
            usb_input_current_now  = readFile("/sys/class/power_supply/usb/input_current_now");
            usb_real_type  = readFile("/sys/class/power_supply/usb/real_type");
            System.out.println(" Usb_type " + usb_type + "Usb_voltage " + usb_voltage + "Usb_online " + usb_online);
            System.out.println(" Usb_present " + usb_present + "Usb_input_current_now" + usb_input_current_now + "Usb_real_type" + usb_real_type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String getbmsinfo() {
        String result = "null";

        try {
            bms_voltage_ocv    = readFile("/sys/class/power_supply/bms/voltage_ocv");
            bms_capacity_raw    = readFile("/sys/class/power_supply/bms/capacity_raw");

            System.out.println(" bms_voltage_ocv " + bms_voltage_ocv + " bms_capacity_raw " + bms_capacity_raw);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private int readFile(String path, int defaultValue) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    path));
            int i = Integer.parseInt(bufferedReader.readLine(), 10);
            bufferedReader.close();
            return i;
        } catch (Exception localException) {
        }
        return defaultValue;
    }

    private String readFile(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    path));
            String buffer = bufferedReader.readLine();
            bufferedReader.close();
            return buffer;
        } catch (Exception localException) {
        }
        return null;
    }
}

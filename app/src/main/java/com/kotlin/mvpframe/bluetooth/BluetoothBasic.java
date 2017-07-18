package com.kotlin.mvpframe.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.Set;

/**
 * Created by Zijin on 2017/7/4.
 */

public class BluetoothBasic {
    public static final String TAG = BluetoothBasic.class.getSimpleName();

    /**
     * 获取蓝牙适配器
     *
     * @return
     */
    public static BluetoothAdapter getBluetoothAdapter() {
        return BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 是否支持蓝牙
     *
     * @return
     */
    public static boolean supportBluetooth() {
        return getBluetoothAdapter() != null;
    }

    /**
     * 判断蓝牙是否打开
     *
     * @return
     */
    public static boolean isBluetoothEnable() {
        return getBluetoothAdapter().isEnabled();
    }

    /**
     * 启用蓝牙
     *
     * @param activity
     */
    public static void startBluetoothEnable(Activity activity) {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableIntent, 1);
    }

    /**
     * 获取已配对的设备
     *
     * @return
     */
    public static Set<BluetoothDevice> getPairedDevices() {
        Set<BluetoothDevice> pairedDevices = getBluetoothAdapter().getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                Log.d(TAG, "getBluetoothDevices: name:" + device.getName() + " address:" + device.getAddress());
            }
        }
        return pairedDevices;
    }

    /**
     * 修改蓝牙设备可见时间
     * @param context
     */
    public static void discoverBluetooth(Context context){
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        context.startActivity(discoverableIntent);
    }

    /**
     * 发现其他蓝牙设备
     * @param context
     */
    public static void registerReceiver(Context context){
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//当前设备发现属性改变
        filter.addAction(BluetoothDevice.ACTION_FOUND);//发现其他设备
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "onReceive: " + action);
                if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)){
                    int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE,BluetoothAdapter.SCAN_MODE_NONE);//当前模式
                    int preMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE,BluetoothAdapter.SCAN_MODE_NONE);//上一个模式
                }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, "onReceive: name:" + device.getName() + " address:" + device.getAddress());
                }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    //搜索完成
                }
            }
        },filter);
    }
}

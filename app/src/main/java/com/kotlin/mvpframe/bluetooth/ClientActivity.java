package com.kotlin.mvpframe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.utils.ClosableUtil;

import java.io.IOException;
import java.util.Set;

/**
 * Created by Zijin on 2017/7/5.
 */

public class ClientActivity extends AppCompatActivity {
    public static final String TAG = ClientActivity.class.getSimpleName();

    private final BluetoothAdapter mBluetoothAdapter = BluetoothBasic.getBluetoothAdapter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_client);

        findViewById(R.id.btnConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBluetooth();
            }
        });


        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//当前设备发现属性改变
        filter.addAction(BluetoothDevice.ACTION_FOUND);//发现其他设备
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "onReceive: " + action);
                if (BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                    int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);//当前模式
                    int preMode = intent.getIntExtra(BluetoothAdapter.EXTRA_PREVIOUS_SCAN_MODE, BluetoothAdapter.SCAN_MODE_NONE);//上一个模式
                } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, "onReceive: name:" + device.getName() + " address:" + device.getAddress());
                    if ("OnePlus 3T".equals(device.getName())) {
                        connectThread = new ConnectThread(device);
                        connectThread.start();
                    }
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //搜索完成
                }
            }
        }, filter);
    }

    void checkBluetooth() {
        if (!BluetoothBasic.supportBluetooth()) {
            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else if (!BluetoothBasic.isBluetoothEnable()) {//未启用蓝牙
            BluetoothBasic.startBluetoothEnable(this);
        }else{
            BluetoothBasic.discoverBluetooth(this);
        }
    }

    ConnectThread connectThread;

    /**
     * 连接请求
     */
    private class ConnectThread extends Thread {
        private final BluetoothDevice mDevice;
        private final BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice mDevice) {
            this.mDevice = mDevice;

            BluetoothSocket tmpSocket = null;
            try {
                tmpSocket = mDevice.createRfcommSocketToServiceRecord(ServerActivity._UUID);
            } catch (IOException e) {
            }
            mSocket = tmpSocket;
        }

        @Override
        public void run() {
            super.run();
            mBluetoothAdapter.cancelDiscovery();
            try {
                mSocket.connect();
                Log.d(TAG, "run: ");
            } catch (IOException e) {
                cancel();
                return;
            }
        }

        public void cancel() {
            ClosableUtil.closeObject(mSocket);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1://请求打开蓝牙
                    BluetoothBasic.discoverBluetooth(this);
                    break;
            }
        }
    }
}

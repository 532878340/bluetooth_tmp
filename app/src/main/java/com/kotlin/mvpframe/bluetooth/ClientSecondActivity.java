package com.kotlin.mvpframe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.base.BaseListAdapter;
import com.kotlin.mvpframe.utils.ClosableUtil;
import com.kotlin.mvpframe.utils.ViewHolderUtil;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zijin on 2017/7/10.
 */

public class ClientSecondActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    public static final String TAG = ClientSecondActivity.class.getSimpleName();

    Switch mSwitch;
    ListView listview;//搜索到的设备
    TextView tvLoading;//搜索视图
    private Context mCtx;

    DevicesAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器

    private BluetoothDevice mBluetoothDevice;//需要连接的远程设备

    public static final int REQUEST_ENABLE = 1;//打开蓝牙

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_client_second);
        setTitle("蓝牙");

        mCtx = this;
        mSwitch = findViewById(R.id.switchView);
        listview = findViewById(R.id.listview);
        tvLoading = findViewById(R.id.tvLoading);
        init();
    }

    void init(){
        listview.setEmptyView(findViewById(R.id.emptyView));
        listview.setOnItemClickListener(this);

        mAdapter = new DevicesAdapter(this,new ArrayList<Device>(),R.layout.item_list);
        listview.setAdapter(mAdapter);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mSwitch.setChecked(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());

        findViewById(R.id.emptyView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBluetoothAdapter.isEnabled()){
                    //蓝牙开启，默认进行扫描
                    startDiscovery();
                }
            }
        });

        if(mBluetoothAdapter == null){
            Toast.makeText(mCtx, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            mSwitch.setEnabled(false);
            return;
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(!mBluetoothAdapter.isEnabled()){
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE);
                    }
                }else{
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothAdapter.disable();
                }
            }
        });

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(receiver,intentFilter);

        startDiscovery();
    }

    //扫描蓝牙设备
    void startDiscovery(){
        if(mBluetoothAdapter.isEnabled()){
            if(mBluetoothAdapter.isDiscovering()){
                mBluetoothAdapter.cancelDiscovery();
            }
            mBluetoothAdapter.startDiscovery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE){
            if(resultCode == RESULT_OK){//蓝牙打开
                mSwitch.setChecked(true);
                //蓝牙开启，扫描
                startDiscovery();
            }else{
                mSwitch.setChecked(false);
            }
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);

            if(BluetoothDevice.ACTION_FOUND.equals(action)){//搜索到设备
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Device device = new Device();
                device.name = bluetoothDevice.getName();
                device.address = bluetoothDevice.getAddress();
                mAdapter.addData(device);
            }else if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){//开始搜索
                tvLoading.setVisibility(View.VISIBLE);
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){//结束搜索
                tvLoading.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mAdapter.getItem(i).address);
        new ConnectThread(mBluetoothDevice).start();
    }

    class DevicesAdapter extends BaseListAdapter<Device>{

        public DevicesAdapter(Context context, List<Device> list, int resId) {
            super(context, list, resId);
        }

        @Override
        protected void convertView(int position, View view, Device type) {
            TextView name = ViewHolderUtil.get(view,R.id.name);
            TextView address = ViewHolderUtil.get(view,R.id.macAddress);

            name.setText(TextUtils.isEmpty(type.name) ? "Unknown" : type.name);//名称可能为空
            address.setText(type.address);
        }
    }

    /**
     * 客户端连接线程
     */
    private class ConnectThread extends Thread{
        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        private ConnectedThread mThread;

        public ConnectThread(BluetoothDevice blueDevice) {
            this.mmDevice = blueDevice;

            try {
                mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(ServerActivity._UUID);
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread: 通道创建失败 " + e.getMessage());
            }
        }

        @Override
        public void run() {
            super.run();

            mBluetoothAdapter.cancelDiscovery();
            if(mmSocket == null) return;

            try {
                mmSocket.connect();
                Log.d(TAG, "客户端: 连接成功");
            } catch (IOException e) {
                Log.d(TAG, "客户端: 连接失败");
                cancel();
            }

            manageConnectedSocket();
        }

        /**
         * 数据传输
         */
        void manageConnectedSocket(){
            mThread = new ConnectedThread(mmSocket);
            mThread.start();

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String s = "我是客户端123213231";
                    mThread.write(s.getBytes());
                }
            }.start();
        }

        public void cancel(){
            ClosableUtil.closeObject(mmSocket);
        }

        // 在尝试连接设备时，您的应用无需担心设备是否已配对。
        // 您的 RFCOMM 连接尝试将被阻塞，直至用户成功完成配对或配对失败（包括用户拒绝配对、配对失败或超时）
        void pair(){
            try {
                if(mmDevice.getBondState() == BluetoothDevice.BOND_NONE){//未配对，则先进行配对
                    Log.d(TAG, "onItemClick: 开始配对");
                    Method creMethod = BluetoothDevice.class.getMethod("createBond");
                    creMethod.invoke(mmDevice);//配对
                }else{//已经配对
                    Log.d(TAG, "onItemClick: 已经配对");
                }
            } catch (Exception e) {
                Log.d(TAG, "onItemClick: 无法进行配对:" + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

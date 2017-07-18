package com.kotlin.mvpframe.material;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothHealthAppConfiguration;
import android.bluetooth.BluetoothHealthCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.bluetooth.BluetoothBasic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ScrollingActivity extends AppCompatActivity {
    public static final String TAG = ScrollingActivity.class.getSimpleName();

    Switch switchView;
    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                BluetoothBasic.discoverBluetooth(ScrollingActivity.this);
            }
        });

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        switchView = findViewById(R.id.switchView);
        switchView.setChecked(mBluetoothAdapter.isEnabled());

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switchView.setChecked(BluetoothAdapter.getDefaultAdapter().isEnabled());
        }
    }


    public static final String NAME = "com.kotlin.mvpframe";
    public static final UUID _UUID = UUID.randomUUID();

    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME,_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mServerSocket = tmp;
        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket socket = null;
            while (true){
                try {
                    socket = mServerSocket.accept();
                    if(socket != null){

                        //管理链接
                        mServerSocket.close();
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void cancel(){
            try {
                mServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectThread extends Thread{
        private final BluetoothDevice mDevice;
        private final BluetoothSocket mSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;

            mDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mSocket = tmp;
        }

        @Override
        public void run() {
            super.run();
            mBluetoothAdapter.cancelDiscovery();

            try {
                mSocket.connect();
            } catch (IOException e) {
                try {
                    mSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return;
            }

            //管理链接
        }

        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mSocket;
        private final InputStream mInputStream;
        private final OutputStream mOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            this.mSocket = socket;

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }
            mInputStream = tmpIn;
            mOutStream = tmpOut;
        }

        @Override
        public void run() {
            super.run();
            byte[] butter = new byte[1024];
            int bytes;

            while (true){
                try {
                    bytes = mInputStream.read(butter);

                    //发送通知到主线程
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(byte[] bytes){
            try {
                mOutStream.write(bytes);
            } catch (IOException e) { }
        }

        public void cancel(){
            try {
                mSocket.close();
            } catch (IOException e) { }
        }
    }

    //蓝牙耳机连接
    BluetoothHeadset mBluetoothHeadset;

    void connectHeadset(){
        //建立代理连接
        mBluetoothAdapter.getProfileProxy(this,mProfileListener,BluetoothProfile.HEADSET);

        mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET,mBluetoothHeadset);
    }

    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int profile, BluetoothProfile bluetoothProfile) {
            if(profile == BluetoothProfile.HEADSET){
                mBluetoothHeadset = (BluetoothHeadset) bluetoothProfile;
            }
        }

        @Override
        public void onServiceDisconnected(int i) {
            if(i == BluetoothProfile.HEADSET){
                mBluetoothHeadset = null;
            }
        }
    };

    //健康设备
    BluetoothHealth mBluetoothHealth;

    void connectHealth(){
        mBluetoothAdapter.getProfileProxy(this,mHealthProfileListener,BluetoothProfile.HEALTH);

        mBluetoothHealth.registerSinkAppConfiguration(NAME, BluetoothHealth.CHANNEL_TYPE_STREAMING, new BluetoothHealthCallback() {
            @Override
            public void onHealthAppConfigurationStatusChange(BluetoothHealthAppConfiguration config, int status) {
                super.onHealthAppConfigurationStatusChange(config, status);
            }

            @Override
            public void onHealthChannelStateChange(BluetoothHealthAppConfiguration config, BluetoothDevice device, int prevState, int newState, ParcelFileDescriptor fd, int channelId) {
                super.onHealthChannelStateChange(config, device, prevState, newState, fd, channelId);
            }
        });
    }

    private BluetoothProfile.ServiceListener mHealthProfileListener = new BluetoothProfile.ServiceListener() {
        @Override
        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            if(i == BluetoothProfile.HEALTH){
                mBluetoothHealth = (BluetoothHealth) bluetoothProfile;
            }
        }

        @Override
        public void onServiceDisconnected(int i) {
            if(i == BluetoothProfile.HEALTH){
                mBluetoothHealth = null;
            }
        }
    };
}

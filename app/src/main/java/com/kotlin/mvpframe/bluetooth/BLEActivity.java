package com.kotlin.mvpframe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import com.kotlin.mvpframe.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zijin on 2017/7/11.
 */

public class BLEActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Switch mSwitch;
    ListView listview;//搜索到的设备
    TextView tvLoading;//搜索视图
    private Context mCtx;

    DevicesAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;//蓝牙适配器

    private BluetoothDevice mBluetoothDevice;//需要连接的远程设备

    public static final int REQUEST_ENABLE = 1;//打开蓝牙

    public static final long SCAN_DELAY = 12000;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_client_second);
        setTitle("BLE");

        mCtx = this;
        mSwitch = findViewById(R.id.switchView);
        listview = findViewById(R.id.listview);
        tvLoading = findViewById(R.id.tvLoading);

        init();
    }

    void init() {
        listview.setEmptyView(findViewById(R.id.emptyView));
        listview.setOnItemClickListener(this);

        mAdapter = new DevicesAdapter(this, new ArrayList<BluetoothDevice>(), R.layout.item_list);
        listview.setAdapter(mAdapter);

        if (Build.VERSION.SDK_INT < 18) {
            //sdk版本不够高，无法使用BLE
            Log.d(TAG, "init: sdk版本低了");
            return;
        }

        mBluetoothAdapter = ((BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
        if (mBluetoothAdapter != null) {
            //没有蓝牙模块
            if (!mBluetoothAdapter.isEnabled()) {
                Log.d(TAG, "init: 蓝牙开启中....");
                //蓝牙未开启
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0);
            } else {
                mSwitch.setChecked(true);
                scan();//搜索设备
            }
        } else {
            Toast.makeText(mCtx, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            mSwitch.setEnabled(false);
            Log.d(TAG, "init: 没有蓝牙模块");
        }
    }

    private static final String TAG = "BLEActivity";

    void scan() {
        Log.d(TAG, "scan: 搜索蓝牙设备...");

        //4.3-5.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "scan: 搜索...startLeScan");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {//5.0+
            Log.d(TAG, "scan: 搜索...startScan");
            final BluetoothLeScanner bluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
            bluetoothLeScanner.stopScan(mScanCallback);

            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        //没有蓝牙模块
                        if (mBluetoothAdapter.isEnabled()) {
                            scan();//搜索设备
                        }
                    } else {
                        bluetoothLeScanner.stopScan(mScanCallback);
                    }
                }
            });

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothLeScanner.stopScan(mScanCallback);
                }
            }, SCAN_DELAY);

            bluetoothLeScanner.startScan(mScanCallback);
        }
    }

    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
            // device 就是扫描到的蓝牙对象，里面各种跟蓝牙有关的信息
            // rssi信号强度，这个值是个负数，范围一般为0到-100，负数越大，代表信号越弱，一般如果超过-90，连接会出现不理想的情况
            // scanRecord广播数据，里面的数据就是蓝牙设备希望手机在连接它之前，让手机知道的信息
        }
    };

    ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            // callbackType：确定这个回调是如何触发的
            // result：包括4.3版本的蓝牙信息，信号强度rssi，和广播数据scanRecord

            BluetoothDevice device = result.getDevice();


            if (!mAdapter.getData().contains(device)) {
                mAdapter.addData(device);
            }

            Log.d(TAG, "onScanResult: " + device.getName() + "------" + device.getAddress() + "---" + result.getScanRecord().getBytes());
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            // 批量回调，一般不推荐使用，使用上面那个会更灵活
            Log.d(TAG, "onBatchScanResults: ");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            // 扫描失败，并且失败原因
            Log.d(TAG, "onScanFailed: ");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                mSwitch.setChecked(true);
                scan();
            } else {//开启被取消
                Log.d(TAG, "onActivityResult: 蓝牙未开启");
                mSwitch.setChecked(false);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        connect(mAdapter.getItem(i));
    }

    void connect(BluetoothDevice device) {
        BluetoothGatt bluetoothGatt = device.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                if (newState == BluetoothProfile.STATE_CONNECTED) {//连接成功，开始搜索服务
                    gatt.discoverServices();//必须调用
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicRead: " + gatt.getDevice().getName() + " value:" + new String(characteristic.getValue()));
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
                Log.d(TAG, "onCharacteristicWrite: " + gatt.getDevice().getName() + " write successful");

            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);

                Log.d(TAG, "onCharacteristicChanged: the response is " + new String(characteristic.getValue()));
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                if (status != BluetoothGatt.GATT_SUCCESS) {//发现服务失败
                    return;
                }

                Log.d(TAG, "onServicesDiscovered: ");
                List<BluetoothGattService> services = gatt.getServices();

                for (BluetoothGattService service : services) {
                    Log.d(TAG, "onServicesDiscovered: BluetoothGattService " + service.getUuid());

                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    for (BluetoothGattCharacteristic characteristic : characteristics) {
                        Log.d(TAG, "onServicesDiscovered: BluetoothGattCharacteristic " + characteristic.getUuid());
                    }
                }
                Log.d(TAG, "onServicesDiscovered: " + services.size());
            }
        });
    }

    class DevicesAdapter extends BaseListAdapter<BluetoothDevice> {

        public DevicesAdapter(Context context, List<BluetoothDevice> list, int resId) {
            super(context, list, resId);
        }

        @Override
        protected void convertView(int position, View view, BluetoothDevice type) {
            TextView name = ViewHolderUtil.get(view, R.id.name);
            TextView address = ViewHolderUtil.get(view, R.id.macAddress);

            name.setText(TextUtils.isEmpty(type.getName()) ? "Unknown" : type.getName());//名称可能为空
            address.setText(type.getAddress());
        }
    }
}

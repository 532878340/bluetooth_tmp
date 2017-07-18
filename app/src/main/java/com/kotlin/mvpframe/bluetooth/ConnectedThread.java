package com.kotlin.mvpframe.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.kotlin.mvpframe.utils.ClosableUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Zijin on 2017/7/10.
 */

public class ConnectedThread extends Thread{
    public static final String TAG = ConnectedThread.class.getSimpleName();

    private BluetoothSocket mSocket;
    private InputStream mmInputStream;
    private OutputStream mmOutStream;
    private int index;

    public ConnectedThread(BluetoothSocket mSocket) {
        this.mSocket = mSocket;

        try {
            mmInputStream = mSocket.getInputStream();
            mmOutStream = mSocket.getOutputStream();
        } catch (IOException e) {
            ClosableUtil.closeObject(mmInputStream);
            ClosableUtil.closeObject(mmOutStream);
        }
    }

    @Override
    public void run() {
        super.run();
        while (true){
            try {
                byte[] buffer = new byte[1024];
                mmInputStream.read(buffer);

                String s = new String(buffer);
                Log.d(TAG, "读数据 : " + mSocket.getRemoteDevice().getName() + s);
                Thread.sleep(3000);

                index ++;
                s = index + s;

                write(s.getBytes());
            } catch (IOException e) {
                ClosableUtil.closeObject(mmInputStream);
                ClosableUtil.closeObject(mmOutStream);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] buffer){
        try {
            mmOutStream.write(buffer);
            Log.d(TAG, "write: 写数据 " + new String(buffer));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

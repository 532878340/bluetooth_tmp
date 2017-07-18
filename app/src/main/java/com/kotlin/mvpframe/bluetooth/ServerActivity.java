package com.kotlin.mvpframe.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.utils.ClosableUtil;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Zijin on 2017/7/5.
 */

public class ServerActivity extends AppCompatActivity {
    public static final String TAG = ServerActivity.class.getSimpleName();

    public static final String SERVER_NAME = TAG;
    public static final UUID _UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mBluetoothAdapter = BluetoothBasic.getBluetoothAdapter();

    AcceptThread acceptThread;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_server);

        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mServerSocket;
        private boolean isConnecting = true;

        public AcceptThread() {
            BluetoothServerSocket tmpSocket = null;

            try {
                tmpSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(SERVER_NAME, _UUID);
            } catch (IOException e) {
            }
            mServerSocket = tmpSocket;
        }

        @Override
        public void run() {
            super.run();
            BluetoothSocket socket = null;

            while (isConnecting) {
                try {
                    socket = mServerSocket.accept();
                    Log.d(TAG, "服务端开始连接: " + socket);
                } catch (IOException e) {
                    Log.d(TAG, "服务端连接失败 ");
                    break;
                }
                if (socket != null) {
                    Log.d(TAG, "run: 服务端连接成功：");
                    Toast.makeText(ServerActivity.this, "connect is successful!", Toast.LENGTH_SHORT).show();
                    manageConnectedSocket(socket);

                    cancel();
                    break;
                }
            }
        }

        /**
         * 取消socket
         */
        public void cancel() {
            ClosableUtil.closeObject(mServerSocket);
        }

        /**
         * 设置是否允许连接
         */
        public void setConnecting(boolean isConnecting){
            this.isConnecting = isConnecting;
        }

        /**
         * 数据传输
         * @param socket
         */
        void manageConnectedSocket(BluetoothSocket socket){
            new ConnectedThread(socket).start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(acceptThread != null){
            acceptThread.setConnecting(false);
        }
    }
}

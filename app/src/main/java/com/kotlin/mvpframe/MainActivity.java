package com.kotlin.mvpframe;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.kotlin.mvpframe.base.steady.SteadyActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);

                    mTextMessage.post(new Runnable() {
                        @Override
                        public void run() {
//                            startActivity(new Intent(MainActivity.this, com.kotlin.mvpframe.demo.MainActivity.class));
//                            startActivity(new Intent(MainActivity.this, ClientSecondActivity.class));
//                            startActivity(new Intent(MainActivity.this, ServerActivity.class));
//                            startActivity(new Intent(MainActivity.this, BLEActivity.class));
//                            startActivity(new Intent(MainActivity.this, RemindActivity.class));
                            startActivity(new Intent(MainActivity.this, SteadyActivity.class));
                        }
                    });
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: " + UUID.randomUUID());

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}

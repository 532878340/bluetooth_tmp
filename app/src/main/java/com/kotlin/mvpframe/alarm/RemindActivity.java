package com.kotlin.mvpframe.alarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Switch;
import android.widget.TextView;

import com.kotlin.mvpframe.R;
import com.kotlin.mvpframe.base.BaseListAdapter;
import com.kotlin.mvpframe.utils.ListViewForScrollView;
import com.kotlin.mvpframe.utils.ViewHolderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zijin on 2017/7/11.
 */

public class RemindActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    Switch mSwitch;
    ListViewForScrollView listview;//搜索到的设备
    Context mCtx;

    AlarmAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_remind);

        mCtx = this;
        mSwitch = findViewById(R.id.switchView);
        listview = findViewById(R.id.listview);
        findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加
                startActivityForResult(new Intent(RemindActivity.this,AlarmDetailActivity.class),1);
            }
        });

        init();
    }

    private void init() {
        listview.setEmptyView(findViewById(R.id.emptyView));
        listview.setOnItemClickListener(this);

        mAdapter = new AlarmAdapter(this, new ArrayList<AlarmEntity>(), R.layout.item_alarm);
        listview.setAdapter(mAdapter);

        List<AlarmEntity> list = AlarmRemindDao.queryAll();
        mAdapter.addData(list);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this,AlarmDetailActivity.class);
        intent.putExtra("entity",mAdapter.getItem(i));
        startActivityForResult(intent,1);
    }

    class AlarmAdapter extends BaseListAdapter<AlarmEntity> {

        public AlarmAdapter(Context context, List<AlarmEntity> list, int resId) {
            super(context, list, resId);
        }

        @Override
        protected void convertView(int position, View view, AlarmEntity type) {
            TextView tvTitle = ViewHolderUtil.get(view,R.id.tvTitle);
            Switch switchView = ViewHolderUtil.get(view,R.id.switchView);

            tvTitle.setText(type.getTitle());
            switchView.setChecked(type.getStatus());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){//更新成功
            mAdapter.clearAdapter();

            List<AlarmEntity> list = AlarmRemindDao.queryAll();
            mAdapter.addData(list);
        }
    }
}

package com.kotlin.mvpframe.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kotlin.mvpframe.R;

/**
 * Created by Zijin on 2017/7/11.
 */

public class AlarmDetailActivity extends AppCompatActivity {
    private AlarmEntity alarmEntity;
    private boolean isEdit;//标识是否为编辑

    EditText edtTitle;
    EditText edtContent;
    RadioGroup radioGroup;
    Switch status;//开关
    TextView tvTime;//时间
    TimePicker timePicker;
    Button btnDel;//删除


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_edit);

        alarmEntity = (AlarmEntity) getIntent().getSerializableExtra("entity");

        isEdit = alarmEntity != null;

        edtTitle = findViewById(R.id.edtTitle);
        edtContent = findViewById(R.id.edtContent);
        radioGroup = findViewById(R.id.radioGroup);
        status = findViewById(R.id.status);
        tvTime = findViewById(R.id.tvTime);
        timePicker = findViewById(R.id.timePicker);
        btnDel = findViewById(R.id.btnDel);

        if (isEdit) {
            btnDel.setVisibility(View.VISIBLE);

            edtTitle.setText(alarmEntity.getTitle());
            edtContent.setText(alarmEntity.getContent());
            radioGroup.check(alarmEntity.getCycle() == 0 ? R.id.rbOnce : R.id.rbRepeat);
            status.setChecked(alarmEntity.getStatus());
            tvTime.setText(alarmEntity.getTime());

            String[] timeArr = alarmEntity.getTime().split(":");
            timePicker.setCurrentHour(Integer.parseInt(timeArr[0]));
            timePicker.setCurrentMinute(Integer.parseInt(timeArr[1]));

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlarmRemindDao.deleteRemind(alarmEntity.getId());
                    AlarmRemind.stopRemind(AlarmDetailActivity.this,alarmEntity.getId());
                    setResult(RESULT_OK);
                    AlarmDetailActivity.this.finish();
                }
            });
        }

        findViewById(R.id.btnCfm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isEdit) {
                    alarmEntity = new AlarmEntity();
                }

                alarmEntity.setTitle(edtTitle.getText().toString());
                alarmEntity.setContent(edtContent.getText().toString());
                alarmEntity.setCycle(radioGroup.getCheckedRadioButtonId() == R.id.rbOnce ? 0 : 1);
                alarmEntity.setStatus(status.isChecked());
                alarmEntity.setTime(tvTime.getText().toString());

                if (isEdit) {
                    AlarmRemindDao.updateRemind(alarmEntity);
                } else {
                    AlarmRemindDao.insertRemind(alarmEntity);
                }

                AlarmRemind.startRemind(AlarmDetailActivity.this,timePicker.getCurrentHour(),timePicker.getCurrentMinute(),alarmEntity.getId(),alarmEntity);

                setResult(RESULT_OK);
                AlarmDetailActivity.this.finish();
            }
        });

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                tvTime.setText(i + ":" + i1);
            }
        });
    }
}

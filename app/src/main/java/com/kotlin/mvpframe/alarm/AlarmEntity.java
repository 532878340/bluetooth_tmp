package com.kotlin.mvpframe.alarm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Zijin on 2017/7/11.
 */

@Entity
public class AlarmEntity implements Serializable{
    public static final long serialVersionUID = -1L;

    @Id(autoincrement = true)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    //提醒时间
    private String time;

    //期限 0 表示一次 1表示每天
    private int cycle;

    //状态 true开 false关
    private boolean status;

    @Generated(hash = 1901594250)
    public AlarmEntity(Long id, String title, String content, String time,
            int cycle, boolean status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.cycle = cycle;
        this.status = status;
    }

    @Generated(hash = 163591880)
    public AlarmEntity() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCycle() {
        return this.cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

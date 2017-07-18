package com.kotlin.mvpframe.entity;

/**
 * Created by Zijin on 2017/7/3.
 */

public class Course {

    public Course(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

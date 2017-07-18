package com.kotlin.mvpframe.entity;

import java.util.List;

/**
 * Created by Zijin on 2017/7/3.
 */

public class Student {
    private String name;
    private List<Course> course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourse() {
        return course;
    }

    public void setCourse(List<Course> course) {
        this.course = course;
    }
}

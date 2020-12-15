package com.example.xiaoqi.news;

import android.graphics.drawable.Drawable;

public class Project {
    private Drawable picture;
    private String name;
    private String text;
    private String time;
    private String str;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getPicture() {
        return picture;
    }

    public void setPicture(Drawable picture) {
        this.picture = picture;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Project(Drawable picture, String name,String text, String time,String str) {
        this.picture = picture;
        this.text = text;
        this.time = time;
        this.name = name;
        this.str = str;
    }
    public Project() {
    }
}

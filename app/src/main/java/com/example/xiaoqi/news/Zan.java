package com.example.xiaoqi.news;

import android.graphics.drawable.Drawable;

public class Zan {
    private Drawable touxiang;
    private String name;
    private String time;
    private Drawable img;

    public Zan(Drawable touxiang, String name, String time, Drawable img) {
        this.touxiang = touxiang;
        this.name = name;
        this.time = time;
        this.img = img;
    }

    public Zan(Drawable touxiang, String name, Drawable img) {
        this.touxiang = touxiang;
        this.name = name;
        this.img = img;
    }

    public Zan(Drawable touxiang, String name) {
        this.touxiang = touxiang;
        this.name = name;
    }

    public Zan(Drawable touxiang, String name, String time) {
        this.touxiang = touxiang;
        this.name = name;
        this.time = time;
    }

    public Drawable getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(Drawable touxiang) {
        this.touxiang = touxiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Drawable getImg() {
        return img;
    }

    public void setImg(Drawable img) {
        this.img = img;
    }
}
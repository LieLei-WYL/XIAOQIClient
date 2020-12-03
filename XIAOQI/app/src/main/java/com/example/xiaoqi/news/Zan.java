package com.example.xiaoqi.news;

import android.graphics.drawable.Drawable;
import android.widget.Button;

public class Zan {
    private Drawable zantouxiang;
    private String zanname;
    private String zanle;
    private String zantime;
    private  Drawable zanimg;
    private Button zanbut1;

    public Zan(Drawable zantouxiang, String zanname, String zanle, String zantime, Drawable zanimg) {
        this.zantouxiang = zantouxiang;
        this.zanname = zanname;
        this.zanle = zanle;
        this.zantime = zantime;
        this.zanimg = zanimg;
    }
    public Zan(Drawable zantouxiang, String zanname, String zanle, String zantime) {
        this.zantouxiang = zantouxiang;
        this.zanname = zanname;
        this.zanle = zanle;
        this.zantime = zantime;
    }

    public Drawable getZantouxiang() {
        return zantouxiang;
    }

    public void setZantouxiang(Drawable zantouxiang) {
        this.zantouxiang = zantouxiang;
    }

    public String getZanname() {
        return zanname;
    }

    public void setZanname(String zanname) {
        this.zanname = zanname;
    }

    public String getZanle() {
        return zanle;
    }

    public void setZanle(String zanle) {
        this.zanle = zanle;
    }

    public String getZantime() {
        return zantime;
    }

    public void setZantime(String zantime) {
        this.zantime = zantime;
    }

    public Drawable getZanimg() {
        return zanimg;
    }

    public void setZanimg(Drawable zanimg) {
        this.zanimg = zanimg;
    }
}

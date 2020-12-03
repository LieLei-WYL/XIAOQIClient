package com.example.xiaoqi.home;

import android.app.Application;

public class Global extends Application {
    private int userState;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    public int getUserState() {
        return userState;
    }

    public void setUserState(int userState) {
        this.userState = userState;
    }
}

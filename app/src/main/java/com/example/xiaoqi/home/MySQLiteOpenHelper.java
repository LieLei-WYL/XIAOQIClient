package com.example.xiaoqi.home;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 当数据库不存在，第一次创建数据库时自动被调用
     * 并且只被调用一次
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //执行建表语句
        db.execSQL("create table User(phone char(11) primary key,state int)");
    }

    /**
     * 当数据库版本有更新时会被自动调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

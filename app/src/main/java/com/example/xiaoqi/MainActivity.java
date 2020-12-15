package com.example.xiaoqi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.MyCustomAppIntro;
import com.example.xiaoqi.home.MySQLiteOpenHelper;
import com.example.xiaoqi.home.Note;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //创建数据库对象属性
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    private ArrayList<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //获取数据库对象
        dbHelper = new MySQLiteOpenHelper(this,"UserDB.db",null,1);
        db = dbHelper.getReadableDatabase();

        Global global = (Global) getApplication();
        //查询是否存在已登录的用户
        Cursor cursor = queryData();
//        cursor.moveToFirst();
        String phone = "";
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            phone = cursor.getString(cursor.getColumnIndex("phone"));
            Log.e("phone",phone);
            global.setCurrentUserPhone(phone);
            Log.e("CurrentUserPhone",global.getCurrentUserPhone()+"");
            global.setUserState(1);
        }else {
            global.setUserState(0);
        }
        Log.e("state",global.getUserState()+"");

        //AppIntro
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, MyCustomAppIntro.class);
        startActivity(intent);

    }

    /**
     * 查询是否存在已登录的用户
     * @return
     */
    private Cursor queryData() {
//        String[] str1 = new String[]{"state"};
//        String[] str2 = new String[]{"1"};
//        return db.query("User",str1,"state=?",str2,null,null,null,null);
        String where = "state=1";
        return db.query("User",null,where,null,null,null,null,null);
    }

}

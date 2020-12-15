package com.example.xiaoqi.me;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.MySQLiteOpenHelper;
import com.suke.widget.SwitchButton;

public class SettingActivity extends AppCompatActivity {
    private ImageView ivBacktoMe;
    private RelativeLayout backtologin;

    //创建数据库对象属性
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //获取数据库对象
        dbHelper = new MySQLiteOpenHelper(this,"UserDB.db",null,1);
        db = dbHelper.getReadableDatabase();

        final SwitchButton switchButton = (SwitchButton) findViewById(R.id.switch_button);

        switchButton.setChecked(false);
        switchButton.isChecked();
        switchButton.toggle();//switch state切换状态
        switchButton.toggle(true);//switch without animation有无动画
        switchButton.setShadowEffect(true);//disable shadow effect阴影效果
        switchButton.setEnabled(true);//disable button启用/禁用按钮
        switchButton.setEnableEffect(true);//disable the switch animation启用/禁用切换动画
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(switchButton.isChecked()){
                    Toast.makeText(getApplication(),"已开启深色模式",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplication(),"已关闭深色模式",Toast.LENGTH_SHORT).show();
                }

            }
        });
        ivBacktoMe = findViewById(R.id.iv_backtome);
        ivBacktoMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this,MeActivity.class);
                startActivity(intent);
            }
        });
        backtologin = findViewById(R.id.backtologin);
        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
    }

    /**
     * AlertDialog对话框
     */
    private void showAlertDialog() {
        //创建Builder对象
        //方法链调用
        new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("确定要退出这个应用程序吗")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //更改SQLiteDatabase用户登录状态数据
                        String where = "phone=?";
                        Global global = (Global) getApplication();
                        Log.e("CurrentUserPhone",global.getCurrentUserPhone()+"");
                        String[] whereValue = new String[]{global.getCurrentUserPhone()};
                        //使用ContentValues来封装更新封装数据
                        ContentValues cv = new ContentValues();
                        cv.put("state",0);
                        int flag = db.update("User",cv,where,whereValue);
                        Log.e("cnm",flag+"");
                        if (flag > 0){
                            global.setUserState(0);
                            global.setCurrentUserPhone("");
                        }else{
                            Toast.makeText(getApplication(),"error",Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent();
                        intent.setClass(SettingActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}

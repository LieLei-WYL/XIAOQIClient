package com.example.xiaoqi.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.suke.widget.SwitchButton;

public class SettingActivity extends AppCompatActivity {
    private ImageView ivBacktoMe;
    private RelativeLayout backtologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
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
                        Intent intent = new Intent();
                        intent.setClass(SettingActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                }).create().show();
    }
}

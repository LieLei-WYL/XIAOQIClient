package com.example.xiaoqi.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

public class RegistrateActivity extends AppCompatActivity {
    private EditText etRname;
    private EditText etRpassword;
    private EditText etPhone;
    private Button btnRegistrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);
        findViews();
        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rName = etRname.getText().toString();
                String rPassword = etRpassword.getText().toString();
                String rPhone = etPhone.getText().toString();
                if(((rPassword.length()>=6&&rPassword.length()<=24)&&(rPhone.length()==11))){
                    Toast.makeText(getApplicationContext(),"注册成功!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(RegistrateActivity.this,MeActivity.class);
                    startActivity(intent);
                    Log.i("hz",rName+":"+rPassword+rPhone);
                }else if ((rPassword.length()<6||rPassword.length()>24)){
                    Toast.makeText(getApplicationContext(),"密码长度需在6-24位之间!",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"手机号错误!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void findViews() {
        etRname = findViewById(R.id.et_rname);
        etPhone = findViewById(R.id.et_rphone);
        etRpassword = findViewById(R.id.et_rpassword);
        btnRegistrate = findViewById(R.id.btn_registrate);
    }
}

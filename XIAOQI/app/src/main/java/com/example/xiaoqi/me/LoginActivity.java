package com.example.xiaoqi.me;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

public class LoginActivity extends AppCompatActivity {
    private EditText etUphone;
    private EditText etUpassword;
    private Button btnToRegistrate;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MeActivity.class);
                startActivity(intent);
                String uPhone = etUphone.getText().toString();
                String uPassword = etUpassword.getText().toString();
                Log.i("hz",uPhone+":"+uPassword);
            }
        });
        btnToRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegistrateActivity.class);
                startActivity(intent);
            }
        });
    }

    private void findViews() {
        etUphone = findViewById(R.id.et_uphone);
        etUpassword = findViewById(R.id.et_upassword);
        btnLogin = findViewById(R.id.btn_login);
        btnToRegistrate = findViewById(R.id.btn_toRegistrate);
    }
}

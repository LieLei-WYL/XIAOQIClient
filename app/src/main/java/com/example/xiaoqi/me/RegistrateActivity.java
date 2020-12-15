package com.example.xiaoqi.me;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RegistrateActivity extends AppCompatActivity {
    private EditText etRname;
    private EditText etRphone;
    private EditText etRpassword;
    private Button btnRegistrate;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler(){
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getApplicationContext(),"该手机号已被注册",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(),"注册错误，请重试",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrate);
        findViews();
        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rName = etRname.getText().toString().trim();
                String rPhone = etRphone.getText().toString().trim();
                String rPassword = etRpassword.getText().toString().trim();
                if((rPhone.length()==11) && (rPassword.length()>=6 && rPassword.length()<=24)){

                    translateDateToServer(rName,rPhone,rPassword);

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
        etRphone = findViewById(R.id.et_rphone);
        etRpassword = findViewById(R.id.et_rpassword);
        btnRegistrate = findViewById(R.id.btn_registrate);
    }

    /**
     * 将正在注册用户信息的信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateDateToServer(final String rName, final String rPhone, final String rPassword) {
        final String registInfo = rName+":"+rPhone+":"+rPassword;
        Log.i("registInfo",registInfo);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/RegistServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(registInfo);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);
                    if("EXIST".equals(result)){
                        Message message = new Message();
                        message.what = 0;//区分不同的消息本身
                        myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
                    }else if("ERROR".equals(result)){
                        Message message = new Message();
                        message.what = 1;//区分不同的消息本身
                        myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
                    }else if("OK".equals(result)){
                        Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setClass(RegistrateActivity.this,LoginActivity.class);
                        startActivity(intent);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

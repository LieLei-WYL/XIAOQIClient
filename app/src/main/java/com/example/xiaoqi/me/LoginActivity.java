package com.example.xiaoqi.me;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.MySQLiteOpenHelper;

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

public class LoginActivity extends AppCompatActivity {
    private EditText etUphone;
    private EditText etUpassword;
    private Button btnToRegistrate;
    private Button btnLogin;

    //创建数据库对象属性
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler(){
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    Toast.makeText(getApplicationContext(),"用户名或密码输入错误",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //获取数据库对象
        dbHelper = new MySQLiteOpenHelper(this,"UserDB.db",null,1);
        db = dbHelper.getReadableDatabase();

        findViews();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,MeActivity.class);
                startActivity(intent);
                String uPhone = etUphone.getText().toString().trim();
                String uPassword = etUpassword.getText().toString().trim();
                Log.i("uPhone:uPassword",uPhone+":"+uPassword);
                translateDateToServer(uPhone,uPassword);
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

    /**
     * 将正在登录用户信息的信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateDateToServer(final String uPhone, final String uPassword) {
        final String loginInfo = uPhone+":"+uPassword;
        Log.i("uPhone:uPassword",loginInfo);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/LoginServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(loginInfo);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);
                    if("YES".equals(result)){
                        Log.i("result", "用户身份" + result);
                        global.setUserState(1);
                        global.setCurrentUserPhone(uPhone);

                        //更改SQLiteDatabase用户登录状态数据
                        String where = "phone='"+uPhone+"'";
                        Cursor cursor = db.query("User",null,where,null,null,null,null,null);
                        if(cursor.getCount()>0){//存在phone为uPhone的用户
                            cursor.moveToFirst();
//                            cursor.moveToNext();
                            String where1 = "phone=?";
                            String[] whereValue = new String[]{uPhone};
                            //使用ContentValues来封装更新封装数据
                            ContentValues cv = new ContentValues();
                            cv.put("state",1);
                            int flag = db.update("User",cv,where1,whereValue);
                            if (flag > 0){
                                global.setUserState(1);
                                global.setCurrentUserPhone(uPhone);
                            }else{
                                Toast.makeText(getApplication(),"error",Toast.LENGTH_SHORT).show();
                            }
                        }else{//不存在phone为uPhone的用户
                            ContentValues values=new ContentValues();
                            values.put("phone",uPhone);
                            values.put("state",1);
                            //返回新添记录的行号，该行号是一个内部直，与主键id无关，发生错误返回-1
                            long rowid = db.insert("User",null,values);
                            if(rowid == -1){
                                Toast.makeText(getApplication(),"error",Toast.LENGTH_SHORT).show();
                            }
                        }

                        intent.setClass(LoginActivity.this, MeActivity.class);
                        startActivity(intent);
                    }else if("NO".equals(result)){
                        Log.i("result", "用户身份错误");
                        Message message = new Message();
                        message.what = 0;//区分不同的消息本身
                        message.obj = "error";//发送的数据本身
                        myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
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

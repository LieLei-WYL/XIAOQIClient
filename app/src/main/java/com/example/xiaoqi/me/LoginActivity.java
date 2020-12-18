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

import com.example.xiaoqi.MainActivity;
import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.MyCustomAppIntro;
import com.example.xiaoqi.home.MySQLiteOpenHelper;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText etUphone;
    private EditText etUpassword;
    private Button btnToRegistrate;
    private Button btnLogin;

    //定义存储数据的UserInfo对象
    private NoteInfo noteInfo;

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
                case 1:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setAttentionNoteList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("attentionNoteList",noteInfo.getNotes().toString());
                    }
                    break;
                case 2:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setNearbyNoteList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("nearbyNoteList",noteInfo.getNotes().toString());
                    }
                    break;
                case 3:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setMyNoteList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("myNoteList",noteInfo.getNotes().toString());
                    }
                    break;
                case 4:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setMyLikeList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("myLikeList",noteInfo.getNotes().toString());
                    }
                    break;
                case 5:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setMyCollectList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("myCollectList",noteInfo.getNotes().toString());
                    }
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

                        translateAttentionDateToServer(uPhone);
                        translateNearbyDateToServer(uPhone);
                        translateMyNoteDateToServer(uPhone);
                        translateMyLikeDateToServer(uPhone);
                        translateMyCollectDateToServer(uPhone);

                        intent.setClass(LoginActivity.this, MeActivity.class);
                        startActivity(intent);

//                        //AppIntro
////                        Intent intent = new Intent();
////                        intent.setClass(LoginActivity.this, MyCustomAppIntro.class);
////                        startActivity(intent);

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

    /**
     * 将当前用户手机号信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateAttentionDateToServer(final String currentUserPhone) {
        Log.i("attention",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/AttentionServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("attentionResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 1;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将当前用户手机号信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateNearbyDateToServer(final String currentUserPhone) {
        Log.i("nearby",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/NearbyServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("nearbyResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 2;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将当前用户手机号信息字符串传输给服务端，请求返回当前用户的所有笔记数据
     * @param
     * @return
     */
    private void translateMyNoteDateToServer(final String currentUserPhone) {
        Log.i("MyNote",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/MyNoteServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("myNoteResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 3;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将当前用户手机号信息字符串传输给服务端，请求返回当前用户的所有喜欢的笔记数据
     * @param
     * @return
     */
    private void translateMyLikeDateToServer(final String currentUserPhone) {
        Log.i("MyLike",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/MyLikeServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("myLikeResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 4;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 将当前用户手机号信息字符串传输给服务端，请求返回当前用户的所有收藏的笔记数据
     * @param
     * @return
     */
    private void translateMyCollectDateToServer(final String currentUserPhone) {
        Log.i("MyCollect",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/MyCollectServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("myCollectResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 5;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

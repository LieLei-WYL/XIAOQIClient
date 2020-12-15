package com.example.xiaoqi.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteActivity;
import com.example.xiaoqi.home.NoteAdapter;
import com.example.xiaoqi.home.NoteInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToMeActivity extends AppCompatActivity {
    private String toUserPhone;
    private int followFlag = 0;

    private ImageView ivToMeBack;
    private LinearLayout llToMeBg;
    private CircleImageView ivToMeCircle;
    private TextView tvToMeName;
    private ImageView ivToMeSex;
    private TextView tvToMeAge;
    private TextView tvToMeArea;
    private TextView tvToMeIntro;
    private LinearLayout tomeAttention;
    private TextView tomeAttentionNum;
    private LinearLayout tomeChasers;
    private TextView tomeChasersNum;
    private TextView tomePraisedNum;
    private Button btnFollow;
    private GridView gvToMeNotes;

    private NoteAdapter noteAdapter;
    private ArrayList<Note> toUserNoteList;
    private NoteInfo noteInfo;
    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler() {
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    String str = (String) msg.obj;
                    String[] strs = str.split("、");
                    //msg.obj = name+"、"+gender+"、"+birthday+"、"+area+"、"+profile+"、"+attentionNum+"、"+followerNum+"、"+praisedNum;
                    tvToMeName.setText(strs[0]);
                    if(strs[1].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        ivToMeSex.setBackgroundDrawable(button_shape4);
                    }else{
                        if (strs[1].equals("man")) {
                            ivToMeSex.setImageResource(R.mipmap.man);
                        } else {
                            ivToMeSex.setImageResource(R.mipmap.woman);
                        }
                    }
                    if(strs[2].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        tvToMeAge.setBackgroundDrawable(button_shape4);
                    }else {
                        try {
                            tvToMeAge.setText(getAge(strs[2])+"");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(strs[3].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        tvToMeArea.setBackgroundDrawable(button_shape4);
                    }else {
                        tvToMeArea.setText(strs[3]);
                    }
                    if(strs[4].equals("")) {
                        tvToMeIntro.setText("此人很懒，还没有写个人简介~");
                    }else {
                        tvToMeIntro.setText(strs[4]);
                    }
                    tomeAttentionNum.setText(strs[5]);
                    tomeChasersNum.setText(strs[6]);
                    tomePraisedNum.setText(strs[7]);
                    break;
                case 1:
                    //加载头像
                    String avatarStr = (String) msg.obj;
                    if(fileIsExists(avatarStr)){
                        Bitmap avatar = BitmapFactory.decodeFile(avatarStr);
                        ivToMeCircle.setImageBitmap(avatar);
                    }else{
                        ivToMeCircle.setImageResource(R.drawable.avatardefault1);
                    }
                    break;
                case 2:
                    //加载背景图
                    String backgroundStr = (String) msg.obj;
                    if(fileIsExists(backgroundStr)){
                        Bitmap bg = BitmapFactory.decodeFile(backgroundStr);
                        BitmapDrawable bd = new BitmapDrawable(bg);
                        bd.setAlpha(200);
                        llToMeBg.setBackground(bd);
                    }else{
                        llToMeBg.setBackground(getDrawable(R.drawable.cloud));
                    }
                    break;
                case 3:
                    String result = (String) msg.obj;
                    if("YES".equals(result)){
                        followFlag = 1;
                        btnFollow.setText("已关注");
                    }else if("NO".equals(result)){
                        followFlag = 0;
                        btnFollow.setText("关注");
                    }
                    break;
                case 4:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        toUserNoteList = noteInfo.getNotes();
                        Log.e("toUserNoteList",toUserNoteList.toString());
                        noteAdapter = new NoteAdapter(getApplicationContext(), toUserNoteList, R.layout.note_list_item);
                        gvToMeNotes.setAdapter(noteAdapter);
                        downloadData();
                    }
                    break;
                case 5:
                    //更新适配器
                    noteAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_me);

        findViews();

        ivToMeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToMeActivity.this.finish();
            }
        });
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followFlag == 0) {
                    followFlag = 1;
                    btnFollow.setText("已关注");
                    Global global = (Global) getApplication();
                    translateFollowChangedToServer(followFlag + ":" + global.getCurrentUserPhone() + ":" + toUserPhone);
                } else if (followFlag == 1) {
                    //弹出框询问是否取关
                    //创建Builder对象
                    AlertDialog.Builder builder = new AlertDialog.Builder(ToMeActivity.this);
                    //设置对话框属性
                    builder.setTitle("温馨提示");//对话框标题
                    builder.setMessage("确定不再关注？");//显示的具体提示内容
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            followFlag = 0;
                            btnFollow.setText("关注");
                            Global global = (Global) getApplication();
                            translateFollowChangedToServer(followFlag + ":" + global.getCurrentUserPhone() + ":" + toUserPhone);
                        }
                    });//设置确定按钮
                    builder.setNegativeButton("取消", null);//设置取消按钮，点击效果默认为退出对话框
                    //创建对话框对象
                    AlertDialog alertDialog = builder.create();
                    //显示对话框
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorRed));
                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorRed));
                }
            }
        });

        Intent intent = getIntent();
        toUserPhone = intent.getStringExtra("toUserPhone");
        Global global = (Global) getApplication();
        applyFollowDateFromServer(global.getCurrentUserPhone(),toUserPhone);
        translateDateToServer(toUserPhone);
        applyNoteDateFromServer(toUserPhone);
    }

    private void findViews() {
        ivToMeBack = findViewById(R.id.iv_to_me_back);
        llToMeBg = findViewById(R.id.ll_to_me_bg);
        ivToMeCircle = findViewById(R.id.iv_to_me_circle);
        ivToMeSex = findViewById(R.id.iv_to_me_sex);
        tvToMeName = findViewById(R.id.tv_to_me_name);
        tvToMeAge = findViewById(R.id.tv_to_me_age);
        tvToMeArea = findViewById(R.id.tv_to_me_area);
        tvToMeName = findViewById(R.id.tv_to_me_name);
        tvToMeIntro = findViewById(R.id.tv_to_me_intro);
        tomeAttention = findViewById(R.id.tome_attention);
        tomeAttentionNum = findViewById(R.id.tome_attention_num);
        tomeChasers = findViewById(R.id.tome_chasers);
        tomeChasersNum = findViewById(R.id.tome_chasers_num);
        tomePraisedNum = findViewById(R.id.tome_praised_num);
        btnFollow = findViewById(R.id.btn_follow);
        gvToMeNotes = findViewById(R.id.gv_to_me_notes);
    }

    /**
     * 将目标用户手机号字符串传输给服务端，请求获得该用户的所有信息
     *
     * @param toUserPhone
     */
    private void translateDateToServer(final String toUserPhone) {
        Log.i("toUserPhone", toUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/UserInfoServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(toUserPhone);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();

                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void applyDateToServer() {
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    //从服务端下载所有蛋糕信息，并通过Message发布出去
                    //1、通过网络请求下载数据(图片要下载到本地，还要修改图片地址为本地地址)
                    //创建URL对象
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/UserInfoServlet");
                    //通过URL对象获取网络输入流
                    InputStream in = url.openStream();
                    //读数据（Json串）循环读写方式
                    byte[] bytes = new byte[512];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes, 0, bytes.length)) != -1) {
                        buffer.append(new String(bytes, 0, len));
                    }
//                    String reslut = buffer.toString();
//                    String reslut = URLDecoder.decode(buffer.toString(), "GBK");
                    String reslut = new String(buffer.toString().getBytes(), "utf-8");
                    Log.e("wyl", reslut);
                    in.close();
                    //先将json串解析成外部CakeInfo对象
                    //创建外层JsonObject对象
                    JSONObject jUser = new JSONObject(reslut);
                    String phone = jUser.optString("phone");
                    String password = jUser.optString("password");
                    String avatar = jUser.optString("avatar");
                    String name = jUser.optString("name");
                    String gender = jUser.optString("gender");
                    String birthday = jUser.optString("birthday");
                    String area = jUser.optString("area");
                    String profile = jUser.optString("profile");
                    String background = jUser.optString("background");
                    String search = jUser.optString("search");
                    String attentionNum = jUser.optString("attentionNum");
                    String followerNum = jUser.optString("followerNum");
                    String praisedNum = jUser.optString("praisedNum");

                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = name+"、"+gender+"、"+birthday+"、"+area+"、"+profile+"、"+attentionNum+"、"+followerNum+"、"+praisedNum;
                    //发送Message对象
                    myHandler.sendMessage(msg);

                    String str1 = load(global.getPath(),avatar);
                    //通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg1 = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg1.what = 1;
                    msg1.obj = str1;
                    //发送Message对象
                    myHandler.sendMessage(msg1);

                    String str2 = load(global.getPath(),background);
                    //通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg2 = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg2.what = 2;
                    msg2.obj = str2;
                    //发送Message对象
                    myHandler.sendMessage(msg2);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public String load(String path, String avatar) throws IOException {
        //拼接服务端地址
        String netPhotoId = path + avatar;
        Log.e("netPhotoId",netPhotoId);
        //通过网络请求下载
        URL imgUrl = new URL(netPhotoId);
        //获取网络输入流
        InputStream imgIn = imgUrl.openStream();
        //获取本地file目录
        String files = getFilesDir().getAbsolutePath();
        String imgs = files + "/imgs";
        //判断imgs目录是否存在
        File dirImgs = new File(imgs);
        if (!dirImgs.exists()) {
            //如果目录不存在，则创建
            dirImgs.mkdir();
        }
        //获取图片的名称（不包含服务端路径的图片名称）
        String[] strs = avatar.split("/");
        String imgName = strs[strs.length - 1];
        String imgPath = imgs + "/" + imgName;
        Log.e("wyl", "拼接头像/背景图名称：" + imgPath);
        //修改cake对象的图片地址（修改cake的图片属性为本地图片地址）
        avatar = imgPath;

        //判断图片是否已经存在
        if(fileIsExists(imgPath)){

        }else {
            //获取本地文件输出流
            OutputStream out = new FileOutputStream(imgPath);
            //循环读写
            int b = -1;
            while ((b = imgIn.read()) != -1) {
                out.write(b);
                out.flush();
            }
            //关闭流
            out.close();
        }

        return avatar;
    }

    private void applyFollowDateFromServer(final String currentUserPhone, final String toUserPhone) {
        final String followInfo = currentUserPhone+":"+toUserPhone;
        Log.i("followInfo",followInfo);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/FollowInfoServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(followInfo);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);
                    Message message = new Message();
                    message.what = 3;//区分不同的消息本身
                    message.obj = result;//发送的数据本身
                    myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void translateFollowChangedToServer(final String string) {
        Log.i("string", string);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/FollowServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(string);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();

//                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void applyNoteDateFromServer(final String toUserPhone) {
        Log.i("toUserPhone",toUserPhone);
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
                    writer.write(toUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("toUserResult",result);

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


    private void downloadData() {
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //通过网络下载note的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for(int i = 0; i < toUserNoteList.size(); i++){
                        toUserNoteList.get(i).getImages()[0] = downloadImage(toUserNoteList.get(i).getImages()[0]);
                        Message msg1 = myHandler.obtainMessage();
                        msg1.what = 5;
                        myHandler.sendMessage(msg1);
                        toUserNoteList.get(i).setAuthorAvatar(downloadImage(toUserNoteList.get(i).getAuthorAvatar()));
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        myHandler.sendMessage(msg2);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private String downloadImage(String image) throws IOException, IOException {
        //拼接服务端地址
        //通过网络请求下载
        Global global = (Global) getApplication();
        URL imgUrl = new URL(global.getPath() + image);
        //获取网络输入流
        InputStream imgIn = imgUrl.openStream();
        //获取本地file目录
        String files = getFilesDir().getAbsolutePath();
        String imgs = files + "/imgs";
        //判断imgs目录是否存在
        File dirImgs = new File(imgs);
        if (!dirImgs.exists()) {
            //如果目录不存在，则创建
            dirImgs.mkdir();
        }
        //获取图片的名称（不包含服务端路径的图片名称）
        String[] strs = image.split("/");
        String imgName = strs[strs.length - 1];
        String imgPath = imgs + "/" + imgName;
        Log.e("wyl", "笔记图片名称：" + imgPath);
        //修改note对象的图片地址（修改note的图片属性为本地图片地址）
        image = imgPath;

        //判断图片是否已经存在
        if(fileIsExists(imgPath)){

        }else {
            //获取本地文件输出流
            OutputStream out = new FileOutputStream(imgPath);
            //循环读写
            int b = -1;
            while ((b = imgIn.read()) != -1) {
                out.write(b);
                out.flush();
            }
            //关闭流
            out.close();
        }

        return image;
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static int getAge(String str) throws Exception{

        java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = formatter.parse(str);

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay))
        {
            Log.e("age","The birthDay is before Now.It's unbelievable!" );
        }
        int  yearNow = cal.get(Calendar.YEAR);
        int  monthNow = cal.get(Calendar.MONTH);
        int  dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        Log.e("now",yearNow+"-"+monthNow+"-"+dayOfMonthNow);
        cal.setTime(birthDay);
        int  yearBirth = cal.get(Calendar.YEAR);
        int  monthBirth = cal.get(Calendar.MONTH)+1;
        int  dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        Log.e("now",yearBirth+"-"+monthBirth+"-"+dayOfMonthBirth);
        int  age = yearNow - yearBirth;
        if (monthNow <= monthBirth)
        {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            }
            else {
                age--;
            }
        }
        return  age;

    }
}
package com.example.xiaoqi.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteInfo;
import com.example.xiaoqi.me.AFList;
import com.example.xiaoqi.me.ListAdapter;
import com.example.xiaoqi.me.LoginActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivHome3;
    private ImageView ivPoetry3;
    private ImageView ivPublish3;
    private ImageView ivMe3;

    private ListView listView;
    private LinearLayout llzan;
    private LinearLayout llguan;
    private LinearLayout llping;
    private List<Project> projectList = new ArrayList<Project>();

    private List<DialogueMain> dialogueMains = new ArrayList<DialogueMain>();
    private ChatAdapter chatAdapter;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    dialogueMains = (ArrayList<DialogueMain>) msg.obj;
                    Log.e("dialogueMains",dialogueMains.toString());
                    if (dialogueMains != null) {
                        chatAdapter = new ChatAdapter(NewsActivity.this,dialogueMains,R.layout.xiaoxi_item1);
                        listView.setAdapter(chatAdapter);
                        applyDateToServer();
                    }
                    break;
                case 1:
                    chatAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Global global = (Global) getApplication();
        if(global.getUserState() == 0){

            setContentView(R.layout.activity_prompt3);

            Button button = findViewById(R.id.btn_toLogin);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ImageView ivHome6 = findViewById(R.id.iv_home6);
            ImageView ivPoetry6 = findViewById(R.id.iv_poetry6);
            ImageView ivPublish6 = findViewById(R.id.iv_publish6);
            ImageView ivMe6 = findViewById(R.id.iv_me6);
            ivHome6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewsActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ivPoetry6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewsActivity.this, PoetryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ivPublish6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
//                        intent.setClass(MeActivity.this, PromptActivity.class);
                        Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(NewsActivity.this, PublishActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                }
            });
            ivMe6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewsActivity.this, MeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });

        }else {
            setContentView(R.layout.activity_news);

            ivHome3 = findViewById(R.id.iv_home3);
            ivPoetry3 = findViewById(R.id.iv_poetry3);
            ivPublish3 = findViewById(R.id.iv_publish3);
            ivMe3 = findViewById(R.id.iv_me3);

            llzan = findViewById(R.id.zan);
            llguan = findViewById(R.id.guan);
            llping = findViewById(R.id.ping);
            listView = findViewById(R.id.list0);

//        //从数据库取出最近聊天的对话记录
//        // 对话dialogue
//        // 消息sentence
//        Intent response = getIntent();
//        String text = response.getStringExtra("text");
//        String name = response.getStringExtra("name");
//        String time = response.getStringExtra("time");
//        String str = response.getStringExtra("str");
//        Drawable drawable =getResources().getDrawable(R.drawable.cake19);
//        Project pro1 = new Project(drawable,name,text,time,str);
//        projectList.add(pro1);
//
//        ChatAdapter adapter = new ChatAdapter(NewsActivity.this,projectList,R.layout.xiaoxi_item1);
////        ProjectAdpter
//        listView.setAdapter(adapter);

            ivHome3.setOnClickListener(this);
            ivPoetry3.setOnClickListener(this);
            ivPublish3.setOnClickListener(this);
            ivMe3.setOnClickListener(this);

            llzan.setOnClickListener(this);
            llguan.setOnClickListener(this);
            llping.setOnClickListener(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Project project = projectList.get(position);
//                String str = project.getStr();
                    Intent intent = new Intent();
                    intent.setClass(NewsActivity.this, ChatActivity.class);
                    intent.putExtra("phone", dialogueMains.get(position).getPhone());
                    intent.putExtra("name", dialogueMains.get(position).getName());
                    intent.putExtra("avatar", dialogueMains.get(position).getAvatar());
                    intent.putExtra("currentAvatar", dialogueMains.get(position).getCurrentAvatar());
                    startActivity(intent);
                }
            });

            String phone = global.getCurrentUserPhone();
            translateDateToServer(phone);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.iv_home3:
                intent = new Intent(NewsActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.iv_poetry3:
                intent = new Intent(NewsActivity.this, PoetryActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.iv_publish3:
                Global global = (Global) getApplication();
                if(global.getUserState() == 0){
                    Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(NewsActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
            case R.id.iv_me3:
                intent = new Intent(NewsActivity.this, MeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;

            case R.id.zan:
                intent = new Intent(NewsActivity.this,ZanAndShou.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.guan:
                intent = new Intent(NewsActivity.this,GuanZhu.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.ping:
                intent = new Intent(NewsActivity.this,PingLun.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    /**
     * 将信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateDateToServer(final String currentUserPhone) {
        Log.i("currentUserPhone",currentUserPhone);
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/DialogueMainServlet");
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
                    Log.i("result", "result:" + result);

                    //先将json串解析成外部NoteInfo对象
                    List<DialogueMain> dialogueMains = new ArrayList<DialogueMain>();
                    //创建外层JsonObject对象
                    JSONObject jDialogueMains = new JSONObject(result);
                    JSONArray jArray = jDialogueMains.getJSONArray("dialogueMains");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        //获取当前的JsonObject对象
                        JSONObject jDialogueMain = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        String phone  = jDialogueMain.optString("phone");
                        String avatar = jDialogueMain.optString("avatar");
                        String currentAvatar = jDialogueMain.optString("currentAvatar");
                        String name = jDialogueMain.optString("name");
                        String lastTime = jDialogueMain.optString("lastTime");
                        String lastSentence = jDialogueMain.optString("lastSentence");
                        //给Note对象赋值
                        DialogueMain dialogueMain = new DialogueMain(phone,avatar,currentAvatar,name,lastTime,lastSentence);
                        //把当前的cake对象添加到集合中
                        dialogueMains.add(dialogueMain);
                    }


//                    //通过网络下载note的图片，保存到本地
//                    //拼接图片的服务端资源路径，进行下载
//                    for(int j=0;j < notes.size();j++){
//                        notes.get(j).getImages()[0] = downloadImage(notes.get(j).getImages()[0]);
//                        notes.get(j).setAuthorAvatar(downloadImage(notes.get(j).getAuthorAvatar()));
//                    }


                    //2、通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = dialogueMains;
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

    private void applyDateToServer() {
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //通过网络下载note的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for(int i = 0; i < dialogueMains.size(); i++){
                        dialogueMains.get(i).setAvatar(downloadImage(dialogueMains.get(i).getAvatar()));
                        dialogueMains.get(i).setCurrentAvatar(downloadImage(dialogueMains.get(i).getCurrentAvatar()));
                        Message msg1 = myHandler.obtainMessage();
                        msg1.what = 1;
                        myHandler.sendMessage(msg1);
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

    /**
     * 将当前用户手机号信息字符串传输给服务端，请求返回当前用户的所有喜欢的笔记数据
     * @param
     * @return
     */
    private void translateLikeAndCollectListDateToServer(final String currentUserPhone) {
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
}
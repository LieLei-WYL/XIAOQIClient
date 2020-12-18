package com.example.xiaoqi.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ScrollView sv;
    private ListView msgListView;
    private EditText inputText;
    private ImageView send;
    private ImageView ivBacktolist;
    private TextView chatName;

    private String targetAvatar;
    private String currentAvatar;
    private List<Dialogue> dialogues = new ArrayList<Dialogue>();
    private DialogueAdapter dialogueAdapter;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    dialogues = (ArrayList<Dialogue>) msg.obj;
                    Log.e("dialogues", dialogues.toString());
                    if (dialogues != null) {
                        dialogueAdapter = new DialogueAdapter(ChatActivity.this, dialogues, R.layout.chat_list, targetAvatar, currentAvatar);
                        msgListView.setAdapter(dialogueAdapter);
                    }
                    break;
                case 1:
//                    msgAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口没有标题栏
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Global global = (Global) getApplication();
        String targetPhone = intent.getStringExtra("phone");
        String currentPhone = global.getCurrentUserPhone();
        String name = intent.getStringExtra("name");
        targetAvatar = intent.getStringExtra("avatar");
        currentAvatar = intent.getStringExtra("currentAvatar");

//        initMsg();
        chatName = findViewById(R.id.chat_name);
        chatName.setText(name);

        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        sv = findViewById(R.id.sv);
        msgListView = findViewById(R.id.msg_list_view);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                if (!"".equals(content)) {
                    Dialogue dialogue = new Dialogue(content,str,0);
                    translateMsgDateToServer(dialogue,currentPhone,targetPhone);
                    dialogues.add(dialogue);
                    dialogueAdapter.notifyDataSetChanged();
                    sv.fullScroll(ScrollView.FOCUS_DOWN);
                    inputText.setText("");//清空输入框的内容
                }
            }

        });
        ivBacktolist = findViewById(R.id.iv_backtolist);
        ivBacktolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        Log.e("phones",targetPhone + ":" + currentPhone);
        translateDateToServer(targetPhone + ":" + currentPhone);

    }


//    private void initMsg() {
//        Msg msg1 = new Msg("I miss you!", Msg.RECEIVED);
//        msgList.add(msg1);
//
//        Msg msg2 = new Msg("I miss you,too!", Msg.SENT);
//        msgList.add(msg2);
//
//        Msg msg3 = new Msg("I will come back soon!", Msg.RECEIVED);
//        msgList.add(msg3);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * 将信息字符串传输给服务端
     *
     * @param
     * @return
     */
    private void translateDateToServer(final String phones) {
        Log.i("phones", phones);
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/DialogueServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(phones);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);

                    //先将json串解析成外部NoteInfo对象
                    List<Dialogue> dialogueList = new ArrayList<Dialogue>();
                    //创建外层JsonObject对象
                    JSONObject jDialogueList = new JSONObject(result);
                    JSONArray jArray = jDialogueList.getJSONArray("dialogueList");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for (int i = 0; i < jArray.length(); i++) {
                        //获取当前的JsonObject对象
                        JSONObject jDialogue = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        String sentence = jDialogue.optString("sentence");
                        String time = jDialogue.optString("time");
                        int type = jDialogue.optInt("type");
                        //给Note对象赋值
                        Dialogue dialogue = new Dialogue(sentence, time, type);
                        //把当前的cake对象添加到集合中
                        dialogueList.add(dialogue);
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
                    msg.obj = dialogueList;
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

    private void translateMsgDateToServer(Dialogue dialogue,String currentPhone,String targetPhone) {
        String string = currentPhone+";"+targetPhone+";"+dialogue.getSentence()+";"+dialogue.getTime();
        Log.i("MsgString", string);
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/AddDialogueServlet");
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
                    Log.i("result", "result:" + result);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
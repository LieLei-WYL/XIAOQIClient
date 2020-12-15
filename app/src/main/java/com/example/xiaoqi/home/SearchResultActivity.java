package com.example.xiaoqi.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

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
import java.util.Collections;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private ArrayList<Note> searchResultNoteList = new ArrayList<>();
    private ImageView ivSearchResultBack;
    private EditText edtSearchResultWrite;
    private TextView tvSearchResultSearch;
    private TextView tvSearchResultNum;
    private GridView gvSearchResult;

    private NoteAdapter noteAdapter;
    private NoteInfo noteInfo;
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null) {
                        //把获取的数据添加到searchResultNoteList中
                        searchResultNoteList.clear();
                        searchResultNoteList = noteInfo.getNotes();
                        tvSearchResultNum.setText(searchResultNoteList.size()+"");
                        Log.e("searchResultNoteList", searchResultNoteList.toString());

                        noteAdapter = new NoteAdapter(getApplicationContext(), searchResultNoteList, R.layout.note_list_item);
                        gvSearchResult.setAdapter(noteAdapter);

//                        noteAdapter.notifyDataSetChanged();
                        applyDateToServer();
                    }
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        translateDateToServer(search);

        ivSearchResultBack = findViewById(R.id.iv_search_result_back);
        edtSearchResultWrite = findViewById(R.id.edt_search_result_write);
        tvSearchResultSearch = findViewById(R.id.tv_search_result_search);
        tvSearchResultNum = findViewById(R.id.tv_search_result_num);
        gvSearchResult = findViewById(R.id.gv_search_result);

        noteAdapter = new NoteAdapter(getApplicationContext(), searchResultNoteList, R.layout.note_list_item);
        gvSearchResult.setAdapter(noteAdapter);

        tvSearchResultSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtSearchResultWrite.getText().toString().trim();
                if(str.equals("")){
                    Toast.makeText(getApplication(),"还没有输入要搜索的关键词呢",Toast.LENGTH_SHORT).show();
                }else {
                    translateDateToServer(str);
                }
            }
        });

        ivSearchResultBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 将搜索信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateDateToServer(final String search) {
        Log.i("search",search);
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/SearchResultServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(search);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);




//                    writer.flush();
//                    //通过URL对象获取网络输入流
//                    //读数据（Json串）循环读写方式
//                    byte[] bytes = new byte[1024];
//                    StringBuffer buffer = new StringBuffer();
//                    int len = -1;
//                    while ((len = in.read(bytes,0,bytes.length)) != -1){
//                        buffer.append(new String(bytes,0,len));
//                    }
//                    String reslut = new String(buffer.toString().getBytes(),"utf-8");
//                    Log.e("result",reslut);
//
//                    out.close();







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

    private void applyDateToServer() {
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //通过网络下载note的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for(int i = 0; i < searchResultNoteList.size(); i++){
                        searchResultNoteList.get(i).getImages()[0] = downloadImage(searchResultNoteList.get(i).getImages()[0]);
                        Message msg1 = myHandler.obtainMessage();
                        msg1.what = 0;
                        myHandler.sendMessage(msg1);
                        searchResultNoteList.get(i).setAuthorAvatar(downloadImage(searchResultNoteList.get(i).getAuthorAvatar()));
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 0;
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
}


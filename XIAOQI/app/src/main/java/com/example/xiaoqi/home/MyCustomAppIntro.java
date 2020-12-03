package com.example.xiaoqi.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.xiaoqi.R;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroCustomLayoutFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class MyCustomAppIntro extends AppIntro {
    private ArrayList<Note> noteList = new ArrayList<>();
    //定义存储数据的UserInfo对象
    private NoteInfo noteInfo;
    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler(){
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        noteList = noteInfo.getNotes();
                        Log.e("wyl",noteList.toString());
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.custom_appintro1));
        addSlide(AppIntroCustomLayoutFragment.newInstance(R.layout.custom_appintro2));
        setDoneText("关闭");
        setColorDoneText(getResources().getColor(R.color.colorBlack));
        setSkipText("跳过");
        applyDateToServer();
    }

    @Override
    protected void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
//        try {
//            sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(MyCustomAppIntro.this,HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("noteList",noteList);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
//        try {
//            sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(MyCustomAppIntro.this,HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("noteList",noteList);
        intent.putExtra("bundle",bundle);
        startActivity(intent);
    }

    private void applyDateToServer() {
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //从服务端下载所有笔记信息，并通过Message发布出去
                    //1、通过网络请求下载数据(图片要下载到本地，还要修改图片地址为本地地址)
                    //创建URL对象
                    URL url = new URL("http://10.7.89.113:8080/XIAOQI/HomeServlet");
                    //通过URL对象获取网络输入流
                    InputStream in = url.openStream();
                    //读数据（Json串）循环读写方式
                    byte[] bytes = new byte[512];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes,0,bytes.length)) != -1){
                        buffer.append(new String(bytes,0,len));
                    }
                    String reslut = new String(buffer.toString().getBytes(),"utf-8");
                    Log.e("wyl",reslut);
                    in.close();
                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(reslut);
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
                    //通过网络下载note的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for(int j=0;j < notes.size();j++){
                        notes.get(j).getImages()[0] = downloadImage(notes.get(j).getImages()[0]);
                        notes.get(j).setAuthorAvatar(downloadImage(notes.get(j).getAuthorAvatar()));
                    }
                    //2、通过发送Message对象将数据发布出去
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

    private String downloadImage(String image) throws IOException {
        //拼接服务端地址
        String netPhotoId = "http://10.7.89.113:8080" + image;
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
        String[] strs = image.split("/");
        String imgName = strs[strs.length - 1];
        String imgPath = imgs + "/" + imgName;
        Log.e("wyl", "笔记图片名称：" + imgPath);
        //修改note对象的图片地址（修改note的图片属性为本地图片地址）
        image = imgPath;
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

        return image;
    }


}
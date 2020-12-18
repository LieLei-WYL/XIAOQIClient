package com.example.xiaoqi.me;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

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
import java.util.ArrayList;

public class FollowerListActivity extends AppCompatActivity {
    private ImageButton ibReturn;
    private ListView lvFollowerList;
    private ArrayList<AFList> afLists = new ArrayList<>();
    private ListAdapter listAdapter;

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    afLists = (ArrayList<AFList>) msg.obj;
                    Log.e("afList",afLists.toString());
                    if (afLists != null) {
                        listAdapter = new ListAdapter(getApplication(), afLists, R.layout.aflist_item);
                        lvFollowerList.setAdapter(listAdapter);
                        applyDateToServer();
                    }
                    break;
                case 1:
                    listAdapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_list);

        ibReturn = findViewById(R.id.followerlist_return);
        lvFollowerList = findViewById(R.id.followerlist);

        ibReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FollowerListActivity.this.finish();
            }
        });
        //绑定Adapter
//        listAdapter = new ListAdapter(getApplication(), afLists, R.layout.aflist_item);
//        lvAttentionlist.setAdapter(listAdapter);

        Intent intent = getIntent();
        String phone = intent.getStringExtra("phone");
        translateDateToServer(phone);
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
                    URL url = new URL(global.getPath() + "/XIAOQI/FollowerListServlet");
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
                    ArrayList<AFList> AFs = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jAFs = new JSONObject(result);
                    JSONArray jArray = jAFs.getJSONArray("afs");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        AFList afList = new AFList();
                        //获取当前的JsonObject对象
                        JSONObject jAF = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        String phone  = jAF.optString("phone");
                        String avatar = jAF.optString("avatar");
                        String name = jAF.optString("name");
                        String profile = jAF.optString("profile");
                        int followFlag = jAF.optInt("followFlag");
                        //给Note对象赋值
                        afList.setPhone(phone);
                        afList.setAvatar(avatar);
                        afList.setName(name);
                        afList.setProfile(profile);
                        afList.setFollowFlag(followFlag);
                        //把当前的cake对象添加到集合中
                        AFs.add(afList);
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
                    msg.obj = AFs;
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
                    for(int i = 0; i < afLists.size(); i++){
                        afLists.get(i).setAvatar(downloadImage(afLists.get(i).getAvatar()));
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
}

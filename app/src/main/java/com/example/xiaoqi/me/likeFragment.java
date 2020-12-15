package com.example.xiaoqi.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class likeFragment extends Fragment {
    private ArrayList<Note> noteList;
    private GridView gvNotes;
    private NoteAdapter noteAdapter;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler(){
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    //更新适配器
                    noteAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater布局填充器
        //加载内容页面的布局文件（将内容页面的XML布局文件转换成View类型的对象）
        View view = inflater.inflate(R.layout.me_fragment_layout,//内容页面的布局文件
                container,//根视图对象
                false);//false表示需要手动调用addView方法将view添加到container中,true表示自动调用addView方法
        //在哪里加载文件，就在哪里获取内容页面当中控件的引用
        gvNotes = view.findViewById(R.id.gv_me_notes);
        //获取Activity传来的Bundle数据
        noteList = (ArrayList<Note>) getArguments().getSerializable("myLikeList");

        //绑定Adapter
        noteAdapter = new NoteAdapter(getContext(), noteList, R.layout.note_list_item);
        gvNotes.setAdapter(noteAdapter);

        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //通过网络下载note的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for(int i = 0; i < noteList.size(); i++){
                        noteList.get(i).getImages()[0] = downloadImage(noteList.get(i).getImages()[0]);
                        Message msg1 = myHandler.obtainMessage();
                        msg1.what = 0;
                        myHandler.sendMessage(msg1);
                        noteList.get(i).setAuthorAvatar(downloadImage(noteList.get(i).getAuthorAvatar()));
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

        return view;
    }

    private String downloadImage(String image) throws IOException, IOException {
        //拼接服务端地址
        //通过网络请求下载
        Global global = (Global) getActivity().getApplication();
        URL imgUrl = new URL(global.getPath() + image);
        //获取网络输入流
        InputStream imgIn = imgUrl.openStream();
        //获取本地file目录
        String files = getActivity().getFilesDir().getAbsolutePath();
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

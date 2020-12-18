package com.example.xiaoqi.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import java.util.ArrayList;
import java.util.List;

public class PingLun extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pinglun);
        listView=(ListView) findViewById(R.id.list3);
        imageButton1 =(ImageButton)findViewById(R.id.ping_return);
        Drawable drawable1 =getResources().getDrawable(R.drawable.avatar1);
        Drawable drawable2 =getResources().getDrawable(R.drawable.avatar3);
        Drawable drawable3 =getResources().getDrawable(R.drawable.avatar8);

        Drawable drawable4 =getResources().getDrawable(R.drawable.note1img1);
        Drawable drawable5 =getResources().getDrawable(R.drawable.note4img1);
        Zan zan = new Zan(drawable2,"休息休息一下","求原图，爱了爱了",drawable4);
        Zan zan1 = new Zan(drawable3,"人生得意须尽欢","赞",drawable4);
        Zan zan2 = new Zan(drawable1,"浮生若梦","好美",drawable5);
        Zan zan3 = new Zan(drawable3,"人生得意须尽欢","赞",drawable5);
        Zan zan4 = new Zan(drawable2,"休息休息一下","大大画画好好看哦",drawable5);
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);
        list.add(zan2);
        list.add(zan3);
        list.add(zan4);

        ZiAdapter adapter =new ZiAdapter(PingLun.this,list, R.layout.pinglun_item1,3);
        listView.setAdapter(adapter);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(PingLun.this,NewsActivity.class);
                startActivity(intent);
            }
        });
    }
}

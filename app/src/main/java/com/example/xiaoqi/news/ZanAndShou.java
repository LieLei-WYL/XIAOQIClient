package com.example.xiaoqi.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.me.MeActivity;

import java.util.ArrayList;
import java.util.List;

public class ZanAndShou extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zanandshou);
        listView=(ListView) findViewById(R.id.list4);
        imageButton1=(ImageButton)findViewById(R.id.zan_return);
        Drawable drawable1 =getResources().getDrawable(R.drawable.avatar1);
        Drawable drawable2 =getResources().getDrawable(R.drawable.avatar3);
        Drawable drawable3 =getResources().getDrawable(R.drawable.avatar8);

        Drawable drawable4 =getResources().getDrawable(R.drawable.note1img1);
        Drawable drawable5 =getResources().getDrawable(R.drawable.note4img1);
        Zan zan = new Zan(drawable2,"休息休息一下","",drawable4);
        Zan zan1 = new Zan(drawable2,"休息休息一下","",drawable5);
        Zan zan2 = new Zan(drawable1,"浮生若梦","",drawable4);
        Zan zan3 = new Zan(drawable1,"浮生若梦","",drawable5);
        Zan zan4 = new Zan(drawable3,"人生得意须尽欢","",drawable4);
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);
        list.add(zan2);
        list.add(zan3);
        list.add(zan4);

        ZiAdapter adapter = new ZiAdapter(ZanAndShou.this,list, R.layout.zanandshou_item1,4);
        listView.setAdapter(adapter);
        Intent response =  getIntent();
        String id = response.getStringExtra("id");
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZanAndShou.this.finish();
            }
        });
    }
}
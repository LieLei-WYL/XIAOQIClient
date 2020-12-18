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

public class GuanZhu extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guanzhu);
        listView=(ListView) findViewById(R.id.list2);
        imageButton1=(ImageButton)findViewById(R.id.guan_return);
        Drawable drawable1 =getResources().getDrawable(R.drawable.avatar1);
        Drawable drawable2 =getResources().getDrawable(R.drawable.avatar8);

        Drawable drawable3 =getResources().getDrawable(R.drawable.followed);
        Drawable drawable4 =getResources().getDrawable(R.drawable.follow);
        Zan zan = new Zan(drawable1,"浮生若梦","",drawable3);
        Zan zan1 = new Zan(drawable2,"人生得意须尽欢","",drawable4);
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);

        ZiAdapter adapter =new ZiAdapter(GuanZhu.this,list, R.layout.guanzhu_item1,2);
        listView.setAdapter(adapter);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuanZhu.this.finish();
            }
        });
    }
}

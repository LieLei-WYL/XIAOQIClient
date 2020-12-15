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
        Drawable drawable =getResources().getDrawable(R.drawable.cake19);
        Zan zan = new Zan(drawable,"XiaoQi","11-18 16:18",drawable);
        Zan zan1 = new Zan(drawable,"XiaoQi","11-18 16:18",drawable);
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);

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

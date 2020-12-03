package com.example.xiaoqi.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {
    private ImageView ivHome;
    private ImageView ivPoetry;
    private ImageView ivPublish;
    private ImageView ivMe;
    private Intent intent = new Intent();

    private ListView listView;
    private ImageButton imageButton1;
    private ImageButton imageButton2;
    private ImageButton imageButton3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        findViews();
        setListener();

        Drawable drawable = getDrawable(R.drawable.cake19);
        Zan zan = new Zan(drawable,"XiaoQi","你好呀","16:18");
        Zan zan1 = new Zan(drawable,"XiaoQi","你好么","16:18");
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);

        ZiAdapter adapter =new ZiAdapter(NewsActivity.this,list,R.layout.xiaoxi_item1,2);
        listView.setAdapter(adapter);
    }

    private void findViews() {
        ivHome =findViewById(R.id.iv_home3);
        ivPoetry =findViewById(R.id.iv_poetry3);
        ivPublish =findViewById(R.id.iv_publish3);
        ivMe =findViewById(R.id.iv_me3);

        imageButton1=(ImageButton)findViewById(R.id.zan);
        imageButton2=(ImageButton)findViewById(R.id.guan);
        imageButton3=(ImageButton)findViewById(R.id.ping);
        listView=(ListView) findViewById(R.id.list1);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivHome.setOnClickListener(myListener);
        ivPoetry.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivMe.setOnClickListener(myListener);

        imageButton1.setOnClickListener(myListener);
        imageButton2.setOnClickListener(myListener);
        imageButton3.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
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
                    intent = new Intent(NewsActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_me3:
                    intent = new Intent(NewsActivity.this, MeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;

                case R.id.zan:
                    intent.setClass(NewsActivity.this,ZanAndShou.class);
                    startActivity(intent);
                    break;
                case R.id.guan:
                    intent.setClass(NewsActivity.this,GuanZhu.class);
                    startActivity(intent);
                    break;
                case R.id.ping:
                    intent.setClass(NewsActivity.this,PingLun.class);
                    startActivity(intent);
                    break;
            }
        }
    }
}

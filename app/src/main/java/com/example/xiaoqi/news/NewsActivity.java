package com.example.xiaoqi.news;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView ivHome3;
    private ImageView ivPoetry3;
    private ImageView ivPublish3;
    private ImageView ivMe3;

    private ListView listView;
    private LinearLayout llzan;
    private LinearLayout llguan;
    private LinearLayout llping;
    private List<Project> projectList = new ArrayList<Project>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        ivHome3=findViewById(R.id.iv_home3);
        ivPoetry3=findViewById(R.id.iv_poetry3);
        ivPublish3=findViewById(R.id.iv_publish3);
        ivMe3=findViewById(R.id.iv_me3);

        llzan=findViewById(R.id.zan);
        llguan=findViewById(R.id.guan);
        llping=findViewById(R.id.ping);
        listView=findViewById(R.id.list0);
        Intent response = getIntent();
        String text = response.getStringExtra("text");
        String name = response.getStringExtra("name");
        String time = response.getStringExtra("time");
        String str = response.getStringExtra("str");
        Drawable drawable =getResources().getDrawable(R.drawable.cake19);
        Project pro1 = new Project(drawable,name,text,time,str);
        projectList.add(pro1);

        ChatAdapter adapter =new ChatAdapter(NewsActivity.this,projectList,R.layout.xiaoxi_item1);
//        ProjectAdpter
        listView.setAdapter(adapter);
        ivHome3.setOnClickListener(this);
        ivPoetry3.setOnClickListener(this);
        ivPublish3.setOnClickListener(this);
        ivMe3.setOnClickListener(this);

        llzan.setOnClickListener(this);
        llguan.setOnClickListener(this);
        llping.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Project project = projectList.get(position);
//                String str = project.getStr();
                Intent intent = new Intent();
                intent.setClass(NewsActivity.this,ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
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
                Global global = (Global) getApplication();
                if(global.getUserState() == 0){
                    Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                }else {
                    intent.setClass(NewsActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
                break;
            case R.id.iv_me3:
                intent = new Intent(NewsActivity.this, MeActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;

            case R.id.zan:
                intent = new Intent(NewsActivity.this,ZanAndShou.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.guan:
                intent = new Intent(NewsActivity.this,GuanZhu.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.ping:
                intent = new Intent(NewsActivity.this,PingLun.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }

    }
}

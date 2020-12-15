package com.example.xiaoqi.poetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.NoteActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.publish.PublishActivity;

public class Xiangqing extends AppCompatActivity{
    private ScrollView scrollView;

    private ImageView Iv;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private int position;
    private Global global;
    private Poetry poetry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing);

        scrollView=findViewById(R.id.sc);
        Iv=findViewById(R.id.Iv);
        tv=findViewById(R.id.tv_type);
        tv1=findViewById(R.id.tv_name);
        tv2=findViewById(R.id.tv_dynasty);
        tv3=findViewById(R.id.tv_author);
        tv4=findViewById(R.id.tv_content);
        ImageButton fanhui = findViewById(R.id.fanhui);
        LinearLayout shang = findViewById(R.id.pre);
        LinearLayout xia = findViewById(R.id.next);
        MyListener myListener = new MyListener();
        fanhui.setOnClickListener(myListener);
        shang.setOnClickListener(myListener);
        xia.setOnClickListener(myListener);

        position = getIntent().getIntExtra("position",0);
        Log.e("position",position+"");
        global = (Global) getApplication();
        poetry = global.getPoetryList().get(position);
        show(poetry);
    }

    private void show(Poetry poetry) {
        Iv.setImageResource(poetry.getImageid());
        tv.setText(poetry.getType());
        tv1.setText(poetry.getName());
        tv2.setText(poetry.getDynasty());
        tv3.setText(poetry.getAuthor());
        String content = poetry.getContent();
        content = content.replaceAll("。","。\n");
        tv4.setText(content);
    }

    class MyListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fanhui:
                    Intent intent = new Intent(Xiangqing.this,PoetryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.pre:
                    if(position == 0) {
                        Toast.makeText(getApplicationContext(),"这是第一首",Toast.LENGTH_SHORT).show();
                    }else {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        int item = global.getCurItem()-1;
                        Log.e("item",item+"");
                        global.setCurItem(item);
                        position--;
                        poetry = global.getPoetryList().get(position);
                        show(poetry);
                    }
                    break;
                case R.id.next:
                    if(position == global.getPoetryList().size()-1) {
                        Toast.makeText(getApplicationContext(),"请返回刷新加载更多",Toast.LENGTH_LONG).show();
                    }else {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        int item = global.getCurItem()+1;
                        Log.e("item",item+"");
                        global.setCurItem(item);
                        position++;
                        poetry = global.getPoetryList().get(position);
                        show(poetry);
                    }
                    break;
            }
        }
    }
}
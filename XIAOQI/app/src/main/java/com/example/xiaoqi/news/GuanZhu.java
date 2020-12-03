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

public class GuanZhu extends AppCompatActivity {
    private ListView listView;
    private ImageButton imageButton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guanzhu);
        listView=(ListView) findViewById(R.id.list1);
        imageButton1=(ImageButton)findViewById(R.id.return1);
        Drawable drawable = getDrawable(R.drawable.cake19);
        Zan zan = new Zan(drawable,"XiaoQi","关注了你","11-18 16:18");
        Zan zan1 = new Zan(drawable,"XiaoQi","关注了你","11-18 16:18");
        List list =new ArrayList();
        list.add(zan);
        list.add(zan1);

        ZiAdapter adapter =new ZiAdapter(GuanZhu.this,list,R.layout.guanzhu_item1,2);
        listView.setAdapter(adapter);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuanZhu.this,NewsActivity.class);
                startActivity(intent);
            }
        });
    }
}

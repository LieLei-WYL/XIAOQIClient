package com.example.xiaoqi.poetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoqi.R;

public class Xiangqing extends AppCompatActivity implements View.OnClickListener{
    int index;
    ImageView Iv;
    TextView tv;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    final int[] imageid =new int[]{R.drawable.mb,R.drawable.mb1,R.drawable.mb2,R.drawable.mb3,R.drawable.mb4,R.drawable.mb5,R.drawable.mb6};
    final String[] type = new String[]{"唐诗","宋诗","宋词","论语","诗经","近代诗","现代诗"};
    final String[] name = new String[]{"静夜思","春晓","宋词","论语","诗经","近代诗","现代诗"};
    final String[] dynasty = new String[]{"唐","宋","战国","春秋","清","近代","现代"};
    final String[] author = new String[]{"李白","杜甫","白居易","王红伟","时红阳","王艳蕾","韩卓"};
    final String[] content = new String[]{"床前明月光，疑是地上霜。举头望明月，低头思故乡。","春眠不觉晓，处处蚊子咬。夜来杀虫剂，不知死多少","宋词","论语","诗经","近代诗","现代诗"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xiangqing);

        ImageButton fanhui = (ImageButton) findViewById(R.id.fanhui);
        Button shang = (Button)findViewById(R.id.shang);
        Button xia = (Button)findViewById(R.id.xia);
//        shang.setOnClickListener(this);
//        xia.setOnClickListener((View.OnClickListener) this);
        Bundle bundle=getIntent().getExtras();
        index = bundle.getInt("id");
        int id=bundle.getInt("imageid");
        String type=bundle.getString("type");
        String name=bundle.getString("name");
        String dynasty=bundle.getString("dynasty");
        String author=bundle.getString("author");
        String content=bundle.getString("content");
        Iv=(ImageView) findViewById(R.id.Iv);
        Iv.setImageResource(id);
        tv=(TextView) findViewById(R.id.tv_type);
        tv.setText(type);
        tv1=(TextView) findViewById(R.id.tv_name);
        tv1.setText(name);
        tv2=(TextView) findViewById(R.id.tv_dynasty);
        tv2.setText(dynasty);
        tv3=(TextView) findViewById(R.id.tv_author);
        tv3.setText(author);
        tv4=(TextView) findViewById(R.id.tv_content);
        tv4.setText(content);
        fanhui.setOnClickListener(this);
        shang.setOnClickListener(this);
        xia.setOnClickListener(this);

    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fanhui:
                Intent intent = new Intent(Xiangqing.this,PoetryActivity.class);
                startActivity(intent);
                break;
            case R.id.shang:
                if (index == 0){
                    index = index;
                }
                else {
                    index = index - 1;
                }
                break;
            case R.id.xia:
                if(index == 6){
                    index = index;
                }
                else {
                    index = index + 1;
                }
                break;
            default:
                break;
        }
//        Log.e("asdsaddad",index+"");
        Iv.setImageResource(imageid[index]);
        tv.setText(type[index]);
        tv1.setText(name[index]);
        tv2.setText(dynasty[index]);
        tv3.setText(author[index]);
        tv4.setText(content[index]);
    }
}
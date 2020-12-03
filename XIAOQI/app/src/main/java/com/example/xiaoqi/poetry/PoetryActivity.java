package com.example.xiaoqi.poetry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.jinrishici.sdk.android.JinrishiciClient;
import com.jinrishici.sdk.android.factory.JinrishiciFactory;
import com.jinrishici.sdk.android.listener.JinrishiciCallback;
import com.jinrishici.sdk.android.model.JinrishiciRuntimeException;
import com.jinrishici.sdk.android.model.PoetySentence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoetryActivity extends AppCompatActivity {
    private ImageView ivHome;
    private ImageView ivPublish;
    private ImageView ivNews;
    private ImageView ivMe;
    private Intent intent;

    private Button hyj;
    private ListView lv;
//    private TextView jrsc;
    private ImageView gscb;
    private List<Gushici> gushiciList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private JinrishiciClient client;
    int i = 0;
    final int[] gscbj = new int[]{R.drawable.gscbj1,R.drawable.gscbj2,R.drawable.gscbj3,R.drawable.gscbj4,R.drawable.gscbj5,R.drawable.gscbj6,R.drawable.gscbj7,R.drawable.gscbj8,R.drawable.gscbj9,R.drawable.gscbj10,R.drawable.gscbj11,R.drawable.gscbj12,  };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry);

        JinrishiciFactory.init(PoetryActivity.this);
        client = new JinrishiciClient();
        Shi();

        findViews();
        setListener();


        final int[] imageid =new int[]{R.drawable.mb,R.drawable.mb1,R.drawable.mb2,R.drawable.mb3,R.drawable.mb4,R.drawable.mb5,R.drawable.mb6};
        final String[] type = new String[]{"唐诗","宋诗","宋词","论语","诗经","近代诗","现代诗"};
        final String[] name = new String[]{"静夜思","春晓","宋词","论语","诗经","近代诗","现代诗"};
        final String[] dynasty = new String[]{"唐","宋","战国","春秋","清","近代","现代"};
        final String[] author = new String[]{"李白","杜甫","白居易","王红伟","时红阳","王艳蕾","韩卓"};
        final String[] content = new String[]{"床前明月光，疑是地上霜。举头望明月，低头思故乡。","春眠不觉晓，处处蚊子咬。夜来杀虫剂，不知死多少","床前明月光，疑是地上霜。举头望明月，低头思故乡。","床前明月光，疑是地上霜。举头望明月，低头思故乡。","床前明月光，疑是地上霜。举头望明月，低头思故乡。","床前明月光，疑是地上霜。举头望明月，低头思故乡。","床前明月光，疑是地上霜。举头望明月，低头思故乡。"};
        List<Map<String, Object>> gushiciList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("imageid", R.drawable.mb);
        map1.put("type","唐诗");
        map1.put("name", "静夜思");
        map1.put("dynasty","唐");
        map1.put("author", "李白");
        map1.put("content","床前明月光，疑是地上霜，举头望明月，低头思故乡。");
        gushiciList.add(map1);

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("imageid", R.drawable.mb1);
        map2.put("type","宋诗");
        map2.put("name", "春晓");
        map2.put("dynasty","宋");
        map2.put("author", "杜甫");
        map2.put("content","春眠不觉晓，处处蚊子咬。夜来杀虫剂，不知死多少。");
        gushiciList.add(map2);

        lv.setAdapter(new SimpleAdapter(this,gushiciList,R.layout.shici_item,new String[]{"imageid","type","name","dynasty","author","content"},new int []{R.id.gsc_image,R.id.gsc_type,R.id.gsc_name,R.id.gsc_dynasty,R.id.gsc_author,R.id.gsc_content}));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                Bundle bundle = new Bundle();
                bundle.putInt("id",arg2);
                bundle.putInt("imageid", imageid[arg2]);
                bundle.putString("type",type[arg2]);
                bundle.putString("name",name[arg2]);
                bundle.putString("dynasty",dynasty[arg2]);
                bundle.putString("author",author[arg2]);
                bundle.putString("content",content[arg2]);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(PoetryActivity.this, Xiangqing.class);
                startActivity(intent);
            }
        });

    }

    private void findViews() {
        ivHome =findViewById(R.id.iv_home2);
        ivPublish =findViewById(R.id.iv_publish2);
        ivNews =findViewById(R.id.iv_news2);
        ivMe =findViewById(R.id.iv_me2);

        hyj = findViewById(R.id.hyj);
        lv = findViewById(R.id.list_gushici);
//        jrsc = findViewById(R.id.jrsc);
        gscb = findViewById(R.id.gscbj);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivHome.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivNews.setOnClickListener(myListener);
        ivMe.setOnClickListener(myListener);

        hyj.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_home2:
                    intent = new Intent(PoetryActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_publish2:
                    intent = new Intent(PoetryActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_news2:
                    intent = new Intent(PoetryActivity.this, NewsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_me2:
                    intent = new Intent(PoetryActivity.this, MeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;

                case R.id.hyj:
                    Shi();
                    if(i<12) {
                        Resources resources =getResources();
                        Drawable drawable = resources.getDrawable(gscbj[i]);
                        gscb.setBackground(drawable);
                        i++;
                    }
                    else{
                        i=0;
                        Resources resources =getResources();
                        Drawable drawable = resources.getDrawable(gscbj[i]);
                        gscb.setBackground(drawable);
                    }
                    break;
            }
        }
    }

    private  void  Shi(){
        client.getOneSentenceBackground(new JinrishiciCallback() {
            @Override
            public void done(PoetySentence poetySentence) {
                //TODO do something
                ((TextView) (findViewById(R.id.nihao))).setText(poetySentence.getData()
                        .getContent());
            }

            @Override
            public void error(JinrishiciRuntimeException e) {
                //TODO do something else
                ((TextView) (findViewById(R.id.nihao))).setText(e.getMessage());
            }
        });

    }
}


package com.example.xiaoqi.poetry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.jinrishici.sdk.android.JinrishiciClient;
import com.jinrishici.sdk.android.factory.JinrishiciFactory;
import com.jinrishici.sdk.android.listener.JinrishiciCallback;
import com.jinrishici.sdk.android.model.JinrishiciRuntimeException;
import com.jinrishici.sdk.android.model.PoetySentence;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PoetryActivity extends AppCompatActivity {
    private ImageView ivHome;
    private ImageView ivRefresh;
    private ImageView ivPublish;
    private ImageView ivNews;
    private ImageView ivMe;
    private Intent intent;

    private Button change;
    private ListView lv;
    private ImageView gscb;
    int i = 0;
    final int[] imageid =new int[]{R.drawable.i1,R.drawable.i2,R.drawable.i3,R.drawable.i4,R.drawable.i5,R.drawable.i6,R.drawable.i7,R.drawable.i8,R.drawable.i9,R.drawable.i10,R.drawable.i11,R.drawable.i12,R.drawable.i13,R.drawable.i14,R.drawable.i15,R.drawable.i16,R.drawable.i17,R.drawable.i18,R.drawable.i19,R.drawable.i20};
    final int[] gscbj = new int[]{R.drawable.gscbj1,R.drawable.gscbj2,R.drawable.gscbj3,R.drawable.gscbj4,R.drawable.gscbj5,R.drawable.gscbj6,R.drawable.gscbj7,R.drawable.gscbj8,R.drawable.gscbj9,R.drawable.gscbj10,R.drawable.gscbj11,R.drawable.gscbj12};

    private String text;
    private Poetry poetry;
    private PoetryAdapter poetryAdapter;
    private ArrayList<Poetry> list = new ArrayList<>();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    Global global = (Global) getApplication();
                    list = global.getPoetryList();
                    Log.e("poetryAdapterTest:",list.toString());
                    poetryAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    ivRefresh.setImageResource(R.mipmap.refreshingpng);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poetry);

        findViews();
        setListener();

        Global global = (Global) getApplication();
        list = global.getPoetryList();
        if(list.size() == 0){
            getJson();
        }
        poetryAdapter = new PoetryAdapter(getApplicationContext(),list,R.layout.shici_item);
        lv.setAdapter(poetryAdapter);
        lv.setDivider(null);
        lv.setSelection(global.getCurItem());

        lv.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                Log.e("firstVisibleItem",firstVisibleItem+"");
                global.setCurItem(firstVisibleItem);
            }
            @Override
            public void onScrollStateChanged(AbsListView view , int scrollState){
                //正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
                //回调顺序如下
                //第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
                //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
                //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
            }
        });
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                // 于对选中的项进行处理
//                //将上一次滚动时的第一条信息，重新展示为第一条信息，即：实现点击后点击条目的位置不变；
//                lv.setSelection(global.getCurItem());
//            }
//        });

        JinrishiciFactory.init(PoetryActivity.this);
        Jinrishiju();
//        getJson();
    }

    private void findViews() {
        ivHome = findViewById(R.id.iv_home2);
        ivRefresh = findViewById(R.id.gif_refresh);
        ivPublish = findViewById(R.id.iv_publish2);
        ivNews = findViewById(R.id.iv_news2);
        ivMe = findViewById(R.id.iv_me2);

        change = findViewById(R.id.change);
        lv = findViewById(R.id.list_gushici);
        gscb = findViewById(R.id.gscbj);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivHome.setOnClickListener(myListener);
        ivRefresh.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivNews.setOnClickListener(myListener);
        ivMe.setOnClickListener(myListener);

        change.setOnClickListener(myListener);
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
                case R.id.gif_refresh:
                    getJson();
                    break;
                case R.id.iv_publish2:
                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
//                        intent.setClass(PoetryActivity.this, PromptActivity.class);
                        Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                    }else {
                        intent.setClass(PoetryActivity.this, PublishActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
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

                case R.id.change:
                    Jinrishiju();
                    break;
            }
        }
    }

    private  void  Jinrishiju(){
        Class jclient = JinrishiciClient.class;
        try {
            Constructor constructor = jclient.getDeclaredConstructor();
            constructor.setAccessible(true);
            JinrishiciClient client = (JinrishiciClient)constructor.newInstance();
            client.getOneSentenceBackground(new JinrishiciCallback() {
                @Override
                public void done(PoetySentence poetySentence) {
                    //TODO do something
                    ((TextView) (findViewById(R.id.jrsj))).setText(poetySentence.getData()
                            .getContent());
                }

                @Override
                public void error(JinrishiciRuntimeException e) {
                    //TODO do something else
                    ((TextView) (findViewById(R.id.jrsj))).setText(e.getMessage());
                }
            });
            if(i < 12) {
                Resources resources = getResources();
                Drawable drawable = resources.getDrawable(gscbj[i]);
                drawable.setAlpha(200);
                gscb.setBackground(drawable);
                i++;
            }
            else{
                i = 0;
                Resources resources = getResources();
                Drawable drawable = resources.getDrawable(gscbj[i]);
                drawable.setAlpha(200);
                gscb.setBackground(drawable);
                i++;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public void  getJson() {
        ivRefresh.setImageResource(R.mipmap.refreshinggif);
        new Thread() {
            @Override
            public void run() {
                Global global = (Global) getApplication();
                for (int i=0; i<20; ++i) {
                    try {
                        //你的URL
                        String url_s = "https://api.nextrt.com/V1/Gushi/";
                        URL url = new URL(url_s);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        //设置连接属性。不喜欢的话直接默认也阔以
                        conn.setConnectTimeout(5000);//设置超时
                        conn.setUseCaches(false);//数据不多不用缓存了
                        //这里连接了
                        conn.connect();
                        //这里才真正获取到了数据
                        InputStream inputStream = conn.getInputStream();
                        InputStreamReader input = new InputStreamReader(inputStream);
                        BufferedReader buffer = new BufferedReader(input);
                        if (conn.getResponseCode() == 200) {//200意味着返回的是"OK"
                            String inputLine;
                            StringBuffer resultData = new StringBuffer();//StringBuffer字符串拼接很快
                            while ((inputLine = buffer.readLine()) != null) {
                                resultData.append(inputLine);
                            }
                            text = resultData.toString();
                            JSONObject jsonObject = new JSONObject(text);
                            JSONObject jsonDatas = jsonObject.getJSONObject("data");
                            String title = jsonDatas.getString("subject");
                            String dynasty = jsonDatas.getString("dynasty");
                            String author = jsonDatas.getString("author");
                            String content = jsonDatas.getString("content");
                            Log.e("标题", title);
                            poetry = new Poetry();
                            poetry.setImageid(imageid[i]);
                            poetry.setAuthor(author);
                            poetry.setContent(content);
                            poetry.setDynasty(dynasty);
                            poetry.setType(dynasty + "诗");
                            poetry.setName(title);
                            //这里的text就是上边获取到的数据，一个String.
                            Log.e("作者", poetry.getAuthor());
                            Log.e("out---->", text);
                            global.getPoetryList().add(poetry);
                            Message msg1 = handler.obtainMessage();
                            msg1.what = 0;
                            handler.sendMessage(msg1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Message msg2 = handler.obtainMessage();
                msg2.what = 1;
                handler.sendMessage(msg2);
            }
        }.start();
    }
}


package com.example.xiaoqi.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private Map<String, ImageView> imageViewMap = new HashMap<>();
    private ImageView ivPoetry;
    private ImageView ivPublish;
    private ImageView ivNews;
    private ImageView ivMe;
    private ImageView ivHomeSearch;
    private Intent intent;
//    private ArrayList<Note> noteList;

    //创建数据库对象属性
    private SQLiteDatabase db;
    private MySQLiteOpenHelper dbHelper;

    private ArrayList<Note> noteList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //获取数据库对象
        dbHelper = new MySQLiteOpenHelper(this,"UserDB.db",null,1);
        db = dbHelper.getReadableDatabase();

        findViews();
        setListener();

        Intent intent = getIntent();
        Bundle b = intent.getBundleExtra("bundle");
        noteList = (ArrayList<Note>) b.getSerializable("noteList");

//        noteList = new ArrayList<>();
//        int[] imageId1 = new int[]{R.drawable.note1img1,R.drawable.note1img2,R.drawable.note1img3,R.drawable.note1img4,R.drawable.note1img5,R.drawable.note1img6,R.drawable.note1img7};
//        int[] imageId2 = new int[]{R.drawable.note2img1,R.drawable.note2img2,R.drawable.note2img3,R.drawable.note2img4,R.drawable.note2img5,R.drawable.note2img6,R.drawable.note2img7,R.drawable.note2img8,R.drawable.note2img9};
//        int[] imageId3 = new int[]{R.drawable.note3img1,R.drawable.note3img2,R.drawable.note3img3,R.drawable.note3img4,R.drawable.note3img5};
//        int[] imageId4 = new int[]{R.drawable.note4img1,R.drawable.note4img2,R.drawable.note4img3,R.drawable.note4img4,R.drawable.note4img5};
//        Note note1 = new Note("13513382833","插画分享 | 风雪待归人 古风美人系列","插画师：付璐\n颜料：水彩、颜彩、矿物岩彩色系等，来自涂鸦王国","插画分享","9-15",imageId1,950,603,24);
//        Note note2 = new Note("18931378558","当《诗经》遇上古风插画，也太美了吧！","画师：呼葱觅蒜","古风插画","5-1",imageId2,8410,4214,211);
//        Note note3 = new Note("17733870176","中式古风小可堂茶馆 费时半年打造完成","设计构想，在深圳这座创新城市中设计具有中国古风味道的茶室，快节奏的城市中设计出能小憩小聚的慢节奏写意空间。\n" +
//                "作为小可堂第一打卡景，门头的古灯笼与现代的小可堂招牌形成跨越年限的冲突美，入门玄关邀请余秋雨先生题字，除了意境还有派头。\n室内的空间五大元素:\n1沿用原始木头建筑结构\n2具有年代感的红木家具和原木门窗部件\n3搭配具有意境的花艺和绿植\n4古风灯笼\n5端景造景设计\n" +
//                "每一个元素结合到室内设计里都成为小可堂处于深圳这座城市独一无二的茶室体验感。\n与三五好友品上一杯好茶，分享盘中美食，随着时间的转移，欣赏窗户光影变化和室外的园林美景。懂生活懂享受就是那么美好。","茶室设计","5-12",imageId3,7012,4817,167);
//        Note note4 = new Note("15131400656","搬运 | 古风人物 | 插画分享","蜉蝣羽衣，朝生暮死\n作者：猫君大白","寻找小憩绘画大神","5-20",imageId4,436,437,6);
//        noteList.add(note1);
//        noteList.add(note2);
//        noteList.add(note3);
//        noteList.add(note4);

        Bundle bundle = new Bundle();
        bundle.putSerializable("noteList",noteList);

        //获取FragmentTabHost的引用
        FragmentTabHost fragmentTabHost = findViewById(android.R.id.tabhost);
        //初始化
        fragmentTabHost.setup(this,
                getSupportFragmentManager(),//管理多个Fragment对象的管理器
                android.R.id.tabcontent);//显示内容页面的控件的id
        //创建内容页面TabSpec对象
        TabHost.TabSpec tab1 = fragmentTabHost.newTabSpec("first_tab").setIndicator(getTabSpecView("first_tab",R.drawable.attention1));
        fragmentTabHost.addTab(tab1,
                AttentionFragment.class,//FirstFragment类的Class对象
                bundle);//传递数据时使用，不需要传递数据直接传null
        TabHost.TabSpec tab2 = fragmentTabHost.newTabSpec("second_tab").setIndicator(getTabSpecView("second_tab",R.drawable.find));
        fragmentTabHost.addTab(tab2, FindFragment.class, bundle);
        TabHost.TabSpec tab3 = fragmentTabHost.newTabSpec("third_tab").setIndicator(getTabSpecView("third_tab",R.drawable.nearby1));
        fragmentTabHost.addTab(tab3, NearbyFragment.class, bundle);

        //处理fragmentTabHost的选项切换事件
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //修改图片和文字的颜色
                switch(tabId){
                    case "first_tab":
                        imageViewMap.get("first_tab").setImageResource(R.drawable.attention);
                        imageViewMap.get("second_tab").setImageResource(R.drawable.find1);
                        imageViewMap.get("third_tab").setImageResource(R.drawable.nearby1);
                        break;
                    case "second_tab":
                        imageViewMap.get("first_tab").setImageResource(R.drawable.attention1);
                        imageViewMap.get("second_tab").setImageResource(R.drawable.find);
                        imageViewMap.get("third_tab").setImageResource(R.drawable.nearby1);
                        break;
                    case "third_tab":
                        imageViewMap.get("first_tab").setImageResource(R.drawable.attention1);
                        imageViewMap.get("second_tab").setImageResource(R.drawable.find1);
                        imageViewMap.get("third_tab").setImageResource(R.drawable.nearby);
                        break;
                }
            }
        });

        //默认选中的标签页:参数是下标
        fragmentTabHost.setCurrentTab(1);

    }

    private void findViews() {
        ivPoetry =findViewById(R.id.iv_poetry1);
        ivPublish =findViewById(R.id.iv_publish1);
        ivNews =findViewById(R.id.iv_news1);
        ivMe =findViewById(R.id.iv_me1);
        ivHomeSearch =findViewById(R.id.iv_home_search);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivPoetry.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivNews.setOnClickListener(myListener);
        ivMe.setOnClickListener(myListener);
        ivHomeSearch.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_poetry1:
                    intent = new Intent(HomeActivity.this, PoetryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_publish1:
                    intent = new Intent(HomeActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_news1:
                    intent = new Intent(HomeActivity.this, NewsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_me1:
                    intent = new Intent(HomeActivity.this, MeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_home_search:
                    intent = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
            }
        }
    }

    public View getTabSpecView(String tag, int drawable){
        View view = getLayoutInflater().inflate(R.layout.tab_spec_layout,null);
        //获取tab_spec_layout布局当中视图控件的引用
        ImageView icon = view.findViewById(R.id.icon);
        icon.setImageResource(drawable);
        //存储到map中
        imageViewMap.put(tag,icon);
        return view;
    }

}
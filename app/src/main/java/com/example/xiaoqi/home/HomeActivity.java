package com.example.xiaoqi.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTabHost;

import android.content.Intent;
import android.os.Bundle;
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

//    //创建数据库对象属性
//    private SQLiteDatabase db;
//    private MySQLiteOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        //获取数据库对象
//        dbHelper = new MySQLiteOpenHelper(this,"UserDB.db",null,1);
//        db = dbHelper.getReadableDatabase();

        findViews();
        setListener();

        Global global = (Global) getApplication();
        Bundle attentionBundle = new Bundle();
        attentionBundle.putSerializable("attentionNoteList",global.getAttentionNoteList());
        Log.e("AttentionNoteList()",global.getAttentionNoteList().toString());
        Bundle findBundle = new Bundle();
        findBundle.putSerializable("findNoteList",global.getFindNoteList());
        Log.e("FindNoteList()",global.getFindNoteList().toString());
        Bundle NearbyBundle = new Bundle();
        NearbyBundle.putSerializable("nearbyNoteList",global.getNearbyNoteList());

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
                null);//传递数据时使用，不需要传递数据直接传null
        TabHost.TabSpec tab2 = fragmentTabHost.newTabSpec("second_tab").setIndicator(getTabSpecView("second_tab",R.drawable.find));
        fragmentTabHost.addTab(tab2, FindFragment.class, findBundle);
        TabHost.TabSpec tab3 = fragmentTabHost.newTabSpec("third_tab").setIndicator(getTabSpecView("third_tab",R.drawable.nearby1));
        fragmentTabHost.addTab(tab3, NearbyFragment.class, NearbyBundle);

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
                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
//                        intent = new Intent(HomeActivity.this, PromptActivity.class);
                        Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                    }else {
                        intent = new Intent(HomeActivity.this, PublishActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
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
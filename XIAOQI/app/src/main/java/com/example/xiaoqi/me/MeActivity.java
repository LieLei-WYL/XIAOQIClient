package com.example.xiaoqi.me;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTabHost;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.me.android.CaptureActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MeActivity extends AppCompatActivity {
    private ImageView ivHome;
    private ImageView ivPoetry;
    private ImageView ivPublish;
    private ImageView ivNews;
    private Intent intent = new Intent();

    private ArrayList<Note> noteList;

    private Map<String, TextView> textViewMap = new HashMap<>();
    private Map<String , ImageView> imageViewMap =new HashMap<>();
    private ImageView ivMore;
    private ImageView ivScanningcod;
    private CircleImageView ivCircle;
    private TextView tvName;
    private ImageView ivSex;
    private LinearLayout attention;
    private TextView attentionnum;
    private LinearLayout followers;
    private TextView followersnum;
    private LinearLayout praised;
    private TextView praisednum;
    private LinearLayout llBackground;
    private LinearLayout llcode;
    private TextView tvMeIntro;
    private Button btnEdit;
    private ImageView ivSetting;
    private FlowingDrawer mDrawer;
    private FrameLayout containerMenu;
    private static final int REQUEST_CODE_SCAN = 100;
    private static final String DECODED_CONTENT_KEY = "a";
    private static final String DECODED_BITMAP_KEY = "b";
    private final int LOGIN_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        /**
         * 获取控件和监听器
         */
        findViews();
        setListener();
        /**
         * 获取PersonaldataActivity内容,利用SharedPreferences
         */
        showUserInfo();
        /**
         * Fragment部分
         */

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

        FragmentTabHost fragmentTabHost = findViewById(R.id.me_tabhost);
        fragmentTabHost.setup(this,getSupportFragmentManager(),android.R.id.tabcontent);
        TabHost.TabSpec tab1 = fragmentTabHost.newTabSpec("note_tab")
                .setIndicator(getTabSpecView("note_tab","笔记",R.mipmap.line1));
        fragmentTabHost.addTab(tab1, noteFragment.class,bundle);
        TabHost.TabSpec tab2 = fragmentTabHost.newTabSpec("collect_tab")
                .setIndicator(getTabSpecView("collect_tab","收藏",R.mipmap.line1));
        fragmentTabHost.addTab(tab2, collectFragment.class,bundle);
        TabHost.TabSpec tab3 = fragmentTabHost.newTabSpec("like_tab")
                .setIndicator(getTabSpecView("like_tab","赞过",R.mipmap.line1));
        fragmentTabHost.addTab(tab3, likeFragment.class,bundle);
        fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                switch(tabId){
                    case "note_tab":
                        imageViewMap.get("note_tab").setImageResource(R.mipmap.line2);
                        imageViewMap.get("collect_tab").setImageResource(R.mipmap.line1);
                        imageViewMap.get("like_tab").setImageResource(R.mipmap.line1);
                        break;
                    case "collect_tab":
                        imageViewMap.get("note_tab").setImageResource(R.mipmap.line1);
                        imageViewMap.get("collect_tab").setImageResource(R.mipmap.line2);
                        imageViewMap.get("like_tab").setImageResource(R.mipmap.line1);
                        break;
                    case "like_tab":
                        imageViewMap.get("note_tab").setImageResource(R.mipmap.line1);
                        imageViewMap.get("collect_tab").setImageResource(R.mipmap.line1);
                        imageViewMap.get("like_tab").setImageResource(R.mipmap.line2);
                        break;
                }
            }
        });
        fragmentTabHost.setCurrentTab(0);
        imageViewMap.get("note_tab").setImageResource(R.mipmap.line2);
        /**
         * FlowingDrawer部分
         */
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);
        mDrawer.setOnDrawerStateChangeListener(new ElasticDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int oldState, int newState) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("MeActivity", "Drawer STATE_CLOSED");
                }
            }
            @Override
            public void onDrawerSlide(float openRatio, int offsetPixels) {
                Log.i("MeActivity", "openRatio=" + openRatio + " ,offsetPixels=" + offsetPixels);
            }
        });
        String name =  tvName.getText().toString();
        Log.i("hz",name);
    }

    private void showUserInfo() {
        SharedPreferences userSp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String name = userSp.getString("name","用户109003");
        String intro = userSp.getString("intro","走过路过，点个关注不迷路");
        String ssex = userSp.getString("ssex","man");
        String bg = userSp.getString("pic","***");
        tvName.setText(name);
        tvMeIntro.setText(intro);
        if (ssex.equals("man")){
            ivSex.setImageResource(R.mipmap.man);
        }else {
            ivSex.setImageResource(R.mipmap.woman);
        }
        Bitmap bp = stringToBitmap(bg);
        BitmapDrawable db = new BitmapDrawable(getResources(),bp);
        llBackground.setBackgroundDrawable(db);
    }

    private void findViews() {
        ivHome =findViewById(R.id.iv_home4);
        ivPoetry =findViewById(R.id.iv_poetry4);
        ivPublish =findViewById(R.id.iv_publish4);
        ivNews =findViewById(R.id.iv_news4);

        ivMore = findViewById(R.id.iv_more);
        ivCircle = findViewById(R.id.iv_circle);
        ivScanningcod = findViewById(R.id.iv_scanningcod);
        ivSetting = findViewById(R.id.iv_setting);
        ivSex = findViewById(R.id.iv_sex);
        btnEdit = findViewById(R.id.btn_edit);
        tvName = findViewById(R.id.tv_name);
        attention = findViewById(R.id.attention);
        attentionnum = findViewById(R.id.attention_num);
        praised = findViewById(R.id.praised);
        praisednum = findViewById(R.id.praised_num);
        followers = findViewById(R.id.followers);
        followersnum = findViewById(R.id.followers_num);
        mDrawer = findViewById(R.id.drawerlayout);
        llBackground = findViewById(R.id.ll_background);
        tvMeIntro = findViewById(R.id.tv_meintro);
        llcode = findViewById(R.id.ll_code);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivHome.setOnClickListener(myListener);
        ivPoetry.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivNews.setOnClickListener(myListener);

        btnEdit.setOnClickListener(myListener);
        ivSetting.setOnClickListener(myListener);
        ivScanningcod.setOnClickListener(myListener);
        ivCircle.setOnClickListener(myListener);
        ivMore.setOnClickListener(myListener);
        llcode.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_home4:
                    intent = new Intent(MeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_poetry4:
                    intent = new Intent(MeActivity.this, PoetryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_publish4:
                    intent = new Intent(MeActivity.this, PublishActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_news4:
                    intent = new Intent(MeActivity.this, NewsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;

                case R.id.btn_edit:
                    intent.setClass(MeActivity.this,PersonaldataActivity.class);
                    String uname = tvName.getText().toString();
                    ivCircle.setDrawingCacheEnabled(true);
                    Bitmap bitmap1 = ivCircle.getDrawingCache();
                    String pic = bitmapToString(bitmap1);
                    String intro = tvMeIntro.getText().toString();
                    intent.putExtra("uname",uname);
                    intent.putExtra("pic",pic);
                    intent.putExtra("intr",intro);
                    ivSex.setDrawingCacheEnabled(false);
                    ivCircle.setDrawingCacheEnabled(false);
                    startActivity(intent);
                    break;
                case R.id.iv_circle:
                    intent.setClass(MeActivity.this,PortraitActivity.class);
                    //获取 ImageView 中已经加载好的图片：
                    ivCircle.setDrawingCacheEnabled(true);
                    Bitmap bitmap = ivCircle.getDrawingCache();
                    //bitmap图片转成string
                    String name = bitmapToString(bitmap);
                    //跳转携带数据
                    Bundle bundle = new Bundle();
                    bundle.putString("name",name);
                    intent.putExtra("bundle",bundle);
                    //跳转到新的Activity并且返回响应
                    startActivityForResult(intent,LOGIN_REQUEST);
                    ivCircle.setDrawingCacheEnabled(false);
                    break;
                case R.id.iv_more:
                    mDrawer.openMenu();
                    break;
                case R.id.iv_scanningcod:
                    //动态权限申请
                    if (ContextCompat.checkSelfPermission(MeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MeActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
                    } else {
                        //扫码
                        goScan();
                    }
                    break;
                case R.id.iv_setting:
                    intent.setClass(MeActivity.this,SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_code:
                    intent.setClass(MeActivity.this,CodeActivity.class);
                    String name1 = tvName.getText().toString();
                    ivCircle.setDrawingCacheEnabled(true);
                    Bitmap bitmap2 = ivCircle.getDrawingCache();
                    String por = bitmapToString(bitmap2);
                    intent.putExtra("name1",name1);
                    intent.putExtra("por",por);
                    ivCircle.setDrawingCacheEnabled(false);
                    startActivity(intent);
                    break;
            }
        }
    }
    /**
     * 图片转换成base64字符串
     *
     * @param bitmap
     * @return
     */
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
    //String转Bitmap
    private Bitmap stringToBitmap(String name) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(name, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * 跳转到扫码界面扫码
     */
    private void goScan() {
        Intent intent = new Intent(MeActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    /**
     * Activity携带数据跳回
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                //返回的文本内容
                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                //返回的BitMap图像
                Bitmap bitmap1 = data.getParcelableExtra(DECODED_BITMAP_KEY);
            }
        } else if(requestCode ==LOGIN_REQUEST&&resultCode == 2000){//从换头像的页面跳回
            //获得从PortraitActivity响应的数据
            String name = data.getStringExtra("name");
            //修改数据源
            Bitmap bitmap = stringToBitmap(name);
            ivCircle.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //扫码
                    goScan();
                } else {
                    Toast.makeText(this, "你拒绝了权限申请，无法打开相机扫码哟！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    /**
     * Fragment部分涉及到的方法
     * @param tag
     * @param title
     * @param pic
     * @return
     */
    private View getTabSpecView(String tag, String title,int pic) {
        View view = getLayoutInflater().inflate(R.layout.me_tab_spce_layout,null);
        TextView tvTitle = view.findViewById(R.id.me_title);
        tvTitle.setText(title);
        textViewMap.put(tag,tvTitle);
        ImageView meLine = view.findViewById(R.id.me_line);
        meLine.setImageResource(pic);
        imageViewMap.put(tag,meLine);
        return view;
    }
}


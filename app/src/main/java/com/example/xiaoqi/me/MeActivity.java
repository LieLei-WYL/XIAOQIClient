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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Comment;
import com.example.xiaoqi.home.CommentAdapter;
import com.example.xiaoqi.home.CommentInfo;
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteActivity;
import com.example.xiaoqi.me.android.CaptureActivity;
import com.example.xiaoqi.news.DraftBoxActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ImageView ivScanningCode;
    private CircleImageView ivCircle;

    private TextView tvMeName;
    private ImageView ivMeSex;
    private TextView tvMeAge;
    private TextView tvMeArea;
    private TextView tvMeIntro;

    private LinearLayout attention;
    private TextView attentionNum;
    private LinearLayout followers;
    private TextView followersNum;
    private TextView praisedNum;
    private LinearLayout llBackground;
    private LinearLayout llCode;
    private LinearLayout llDraft;
    private Button btnEdit;
    private ImageView ivSetting;
    private FlowingDrawer mDrawer;
    private FrameLayout containerMenu;
    private static final int REQUEST_CODE_SCAN = 100;
    private static final String DECODED_CONTENT_KEY = "a";
    private static final String DECODED_BITMAP_KEY = "b";
    private final int LOGIN_REQUEST = 2;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler() {
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    String str = (String) msg.obj;
                    String[] strs = str.split("、");
                    //msg.obj = name+"、"+gender+"、"+birthday+"、"+area+"、"+profile+"、"+attentionNum+"、"+followerNum+"、"+praisedNum;
                    tvMeName.setText(strs[0]);
                    if(strs[1].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        ivMeSex.setBackgroundDrawable(button_shape4);
                    }else{
                        if (strs[1].equals("man")) {
                            ivMeSex.setImageResource(R.mipmap.man);
                        } else {
                            ivMeSex.setImageResource(R.mipmap.woman);
                        }
                    }
                    if(strs[2].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        tvMeAge.setBackgroundDrawable(button_shape4);
                    }else {
                        try {
                            tvMeAge.setText(getAge(strs[2])+"");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(strs[3].equals("")) {
                        Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
                        tvMeArea.setBackgroundDrawable(button_shape4);
                    }else {
                        tvMeArea.setText(strs[3]);
                    }
                    if(strs[4].equals("")) {
                        tvMeIntro.setText("有趣的个人简介会吸引更多粉丝哦~");
                    }else {
                        tvMeIntro.setText(strs[4]);
                    }
                    attentionNum.setText(strs[5]);
                    followersNum.setText(strs[6]);
                    praisedNum.setText(strs[7]);
                    break;
                case 1:
                    //加载头像
                    String avatarStr = (String) msg.obj;
                    if(fileIsExists(avatarStr)){
                        Bitmap avatar = BitmapFactory.decodeFile(avatarStr);
                        ivCircle.setImageBitmap(avatar);
                    }else{
                        ivCircle.setImageResource(R.drawable.avatardefault1);
                    }
                    break;
                case 2:
                    //加载背景图
                    String backgroundStr = (String) msg.obj;
                    if(fileIsExists(backgroundStr)){
                        Bitmap bg = BitmapFactory.decodeFile(backgroundStr);
                        BitmapDrawable bd = new BitmapDrawable(bg);
                        bd.setAlpha(200);
                        llBackground.setBackground(bd);
                    }else{
                        llBackground.setBackground(getDrawable(R.drawable.cloud));
                    }
                    break;
            }
        }
    };

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public static int getAge(String str) throws Exception{

        java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDay = formatter.parse(str);

        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay))
        {
            Log.e("age","The birthDay is before Now.It's unbelievable!" );
        }
        int  yearNow = cal.get(Calendar.YEAR);
        int  monthNow = cal.get(Calendar.MONTH);
        int  dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        Log.e("now",yearNow+"-"+monthNow+"-"+dayOfMonthNow);
        cal.setTime(birthDay);
        int  yearBirth = cal.get(Calendar.YEAR);
        int  monthBirth = cal.get(Calendar.MONTH)+1;
        int  dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        Log.e("now",yearBirth+"-"+monthBirth+"-"+dayOfMonthBirth);
        int  age = yearNow - yearBirth;
        if (monthNow <= monthBirth)
        {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            }
            else {
                age--;
            }
        }
        return  age;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Global global = (Global) getApplication();
        if(global.getUserState() == 0){

            setContentView(R.layout.activity_prompt2);

            Button button = findViewById(R.id.btn_toLogin);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ImageView ivHome5 = findViewById(R.id.iv_home5);
            ImageView ivPoetry5 = findViewById(R.id.iv_poetry5);
            ImageView ivPublish5 = findViewById(R.id.iv_publish5);
            ImageView ivNews5 = findViewById(R.id.iv_news5);
            ivHome5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ivPoetry5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MeActivity.this, PoetryActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });
            ivPublish5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
//                        intent.setClass(MeActivity.this, PromptActivity.class);
                        Toast.makeText(getApplicationContext(),"请登录后再与小憩玩耍吧",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(MeActivity.this, PublishActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                    }
                }
            });
            ivNews5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MeActivity.this, NewsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            });

        }else {

            setContentView(R.layout.activity_me);

            /**
             * 获取控件和监听器
             */
            findViews();
            setListener();

//            if(global.getUser().getPhone().equals("")){
                translateDateToServer(global.getCurrentUserPhone());
//            }else{
//                String avatarStr = global.getUser().getAvatar();
//                if(fileIsExists(avatarStr)){
//                    Bitmap avatar = BitmapFactory.decodeFile(avatarStr);
//                    ivCircle.setImageBitmap(avatar);
//                }else{
//                    ivCircle.setImageResource(R.drawable.avatardefault1);
//                }
//
//                tvMeName.setText(global.getUser().getName());
//                if(global.getUser().getGender().equals("")) {
//                    Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
//                    ivMeSex.setBackgroundDrawable(button_shape4);
//                }else{
//                    if (global.getUser().getGender().equals("man")) {
//                        ivMeSex.setImageResource(R.mipmap.man);
//                    } else {
//                        ivMeSex.setImageResource(R.mipmap.woman);
//                    }
//                }
//                if(global.getUser().getGender().equals("")) {
//                    Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
//                    tvMeAge.setBackgroundDrawable(button_shape4);
//                }else {
//                    try {
//                        tvMeAge.setText(getAge(global.getUser().getBirthday())+"");
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                if(global.getUser().getArea().equals("")) {
//                    Drawable button_shape4 = getResources().getDrawable(R.drawable.button_shape4);
//                    tvMeArea.setBackgroundDrawable(button_shape4);
//                }else {
//                    tvMeArea.setText(global.getUser().getArea());
//                }
//                if(global.getUser().getBackground().equals("")) {
//                    tvMeIntro.setText("有趣的个人简介会吸引更多粉丝哦~");
//                }else {
//                    tvMeIntro.setText(global.getUser().getProfile());
//                }
//
//                String backgroundStr = global.getUser().getBackground();
//                if(fileIsExists(backgroundStr)){
//                    Bitmap bg = BitmapFactory.decodeFile(backgroundStr);
//                    BitmapDrawable bd = new BitmapDrawable(bg);
//                    llBackground.setBackground(bd);
//                }else{
//                    llBackground.setBackground(getDrawable(R.drawable.cloud));
//                }
//                attentionNum.setText(global.getUser().getAttentionNum());
//                followersNum.setText(global.getUser().getFollowerNum());
//                praisedNum.setText(global.getUser().getPraisedNum());
//            }

            /**
             * Fragment部分
             */
            Bundle myNoteBundle = new Bundle();
            myNoteBundle.putSerializable("myNoteList",global.getMyNoteList());
            Log.e("myNoteList()",global.getMyNoteList().toString());
            Bundle myLikeBundle = new Bundle();
            myLikeBundle.putSerializable("myLikeList",global.getMyLikeList());
            Log.e("myLikeList()",global.getMyLikeList().toString());
            Bundle myCollectBundle = new Bundle();
            myCollectBundle.putSerializable("myCollectList",global.getMyCollectList());
            Log.e("myCollectList()",global.getMyCollectList().toString());

            FragmentTabHost fragmentTabHost = findViewById(R.id.me_tabhost);
            fragmentTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
            TabHost.TabSpec tab1 = fragmentTabHost.newTabSpec("note_tab")
                    .setIndicator(getTabSpecView("note_tab", "笔记", R.mipmap.line1));
            fragmentTabHost.addTab(tab1, noteFragment.class, myNoteBundle);
            TabHost.TabSpec tab2 = fragmentTabHost.newTabSpec("collect_tab")
                    .setIndicator(getTabSpecView("collect_tab", "收藏", R.mipmap.line1));
            fragmentTabHost.addTab(tab2, collectFragment.class, myCollectBundle);
            TabHost.TabSpec tab3 = fragmentTabHost.newTabSpec("like_tab")
                    .setIndicator(getTabSpecView("like_tab", "赞过", R.mipmap.line1));
            fragmentTabHost.addTab(tab3, likeFragment.class, myLikeBundle);
            fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
                @Override
                public void onTabChanged(String tabId) {
                    switch (tabId) {
                        case "note_tab":
                            textViewMap.get("note_tab").setTextColor(getResources().getColor(R.color.colorRed));
                            imageViewMap.get("note_tab").setImageResource(R.mipmap.line2);
                            textViewMap.get("collect_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("collect_tab").setImageResource(R.mipmap.line1);
                            textViewMap.get("like_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("like_tab").setImageResource(R.mipmap.line1);
                            break;
                        case "collect_tab":
                            textViewMap.get("note_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("note_tab").setImageResource(R.mipmap.line1);
                            textViewMap.get("collect_tab").setTextColor(getResources().getColor(R.color.colorRed));
                            imageViewMap.get("collect_tab").setImageResource(R.mipmap.line2);
                            textViewMap.get("like_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("like_tab").setImageResource(R.mipmap.line1);
                            break;
                        case "like_tab":
                            textViewMap.get("note_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("note_tab").setImageResource(R.mipmap.line1);
                            textViewMap.get("collect_tab").setTextColor(getResources().getColor(R.color.colorGray));
                            imageViewMap.get("collect_tab").setImageResource(R.mipmap.line1);
                            textViewMap.get("like_tab").setTextColor(getResources().getColor(R.color.colorRed));
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
        }
    }

    /**
     * 将当前用户的手机号字符串传输给服务端，请求获得该用户的所有信息
     *
     * @param currentUserPhone
     */
    private void translateDateToServer(final String currentUserPhone) {
        Log.i("currentUserPhone", currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/UserInfoServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();

                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void applyDateToServer() {
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    //从服务端下载所有蛋糕信息，并通过Message发布出去
                    //1、通过网络请求下载数据(图片要下载到本地，还要修改图片地址为本地地址)
                    //创建URL对象
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/UserInfoServlet");
                    //通过URL对象获取网络输入流
                    InputStream in = url.openStream();
                    //读数据（Json串）循环读写方式
                    byte[] bytes = new byte[512];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes, 0, bytes.length)) != -1) {
                        buffer.append(new String(bytes, 0, len));
                    }
//                    String reslut = buffer.toString();
//                    String reslut = URLDecoder.decode(buffer.toString(), "GBK");
                    String reslut = new String(buffer.toString().getBytes(), "utf-8");
                    Log.e("wyl", reslut);
                    in.close();
                    //先将json串解析成外部CakeInfo对象
                    //创建外层JsonObject对象
                    JSONObject jUser = new JSONObject(reslut);
                    String phone = jUser.optString("phone");
                    String password = jUser.optString("password");
                    String avatar = jUser.optString("avatar");
                    String name = jUser.optString("name");
                    String gender = jUser.optString("gender");
                    String birthday = jUser.optString("birthday");
                    String area = jUser.optString("area");
                    String profile = jUser.optString("profile");
                    String background = jUser.optString("background");
                    String search = jUser.optString("search");
                    String attentionNum = jUser.optString("attentionNum");
                    String followerNum = jUser.optString("followerNum");
                    String praisedNum = jUser.optString("praisedNum");

                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = name+"、"+gender+"、"+birthday+"、"+area+"、"+profile+"、"+attentionNum+"、"+followerNum+"、"+praisedNum;
                    //发送Message对象
                    myHandler.sendMessage(msg);

                    String str1 = load(global.getPath(),avatar);
                    //通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg1 = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg1.what = 1;
                    msg1.obj = str1;
                    //发送Message对象
                    myHandler.sendMessage(msg1);

                    String str2 = load(global.getPath(),background);
                    //通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg2 = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg2.what = 2;
                    msg2.obj = str2;
                    //发送Message对象
                    myHandler.sendMessage(msg2);

                    User user = new User(phone,password,str1,name,gender,birthday,area,profile,str2,search);
                    global.setUser(user);

                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public String load(String path, String avatar) throws IOException {
        //拼接服务端地址
        String netPhotoId = path + avatar;
        //通过网络请求下载
        URL imgUrl = new URL(netPhotoId);
        //获取网络输入流
        InputStream imgIn = imgUrl.openStream();
        //获取本地file目录
        String files = getFilesDir().getAbsolutePath();
        String imgs = files + "/imgs";
        //判断imgs目录是否存在
        File dirImgs = new File(imgs);
        if (!dirImgs.exists()) {
            //如果目录不存在，则创建
            dirImgs.mkdir();
        }
        //获取图片的名称（不包含服务端路径的图片名称）
        String[] strs = avatar.split("/");
        String imgName = strs[strs.length - 1];
        String imgPath = imgs + "/" + imgName;
        Log.e("wyl", "拼接头像/背景图名称：" + imgPath);
        //修改cake对象的图片地址（修改cake的图片属性为本地图片地址）
        avatar = imgPath;

        //判断图片是否已经存在
        if(fileIsExists(imgPath)){

        }else {
            //获取本地文件输出流
            OutputStream out = new FileOutputStream(imgPath);
            //循环读写
            int b = -1;
            while ((b = imgIn.read()) != -1) {
                out.write(b);
                out.flush();
            }
            //关闭流
            out.close();
        }

        return avatar;
    }

    private void findViews() {
        ivHome =findViewById(R.id.iv_home4);
        ivPoetry =findViewById(R.id.iv_poetry4);
        ivPublish =findViewById(R.id.iv_publish4);
        ivNews =findViewById(R.id.iv_news4);

        ivMore = findViewById(R.id.iv_more);
        ivCircle = findViewById(R.id.iv_circle);
        ivScanningCode = findViewById(R.id.iv_scanningcode);
        ivSetting = findViewById(R.id.iv_setting);

        tvMeName = findViewById(R.id.tv_me_name);
        ivMeSex = findViewById(R.id.iv_me_sex);
        tvMeAge= findViewById(R.id.tv_me_age);
        tvMeArea = findViewById(R.id.tv_me_area);
        tvMeIntro = findViewById(R.id.tv_me_intro);

        btnEdit = findViewById(R.id.btn_edit);
        attention = findViewById(R.id.attention);
        attentionNum = findViewById(R.id.attention_num);
        praisedNum = findViewById(R.id.praised_num);
        followers = findViewById(R.id.followers);
        followersNum = findViewById(R.id.followers_num);
        mDrawer = findViewById(R.id.drawerlayout);
        llBackground = findViewById(R.id.ll_background);
        llCode = findViewById(R.id.ll_code);
        llDraft = findViewById(R.id.ll_draft);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivHome.setOnClickListener(myListener);
        ivPoetry.setOnClickListener(myListener);
        ivPublish.setOnClickListener(myListener);
        ivNews.setOnClickListener(myListener);

        attention.setOnClickListener(myListener);
        followers.setOnClickListener(myListener);

        btnEdit.setOnClickListener(myListener);
        ivSetting.setOnClickListener(myListener);
        ivScanningCode.setOnClickListener(myListener);
        ivCircle.setOnClickListener(myListener);
        ivMore.setOnClickListener(myListener);
        llCode.setOnClickListener(myListener);
        llCode.setOnClickListener(myListener);
        llDraft.setOnClickListener(myListener);
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

                case R.id.attention:
                    intent = new Intent(MeActivity.this, AttentionListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.followers:
                    intent = new Intent(MeActivity.this, FollowerListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;

                case R.id.btn_edit:
                    intent.setClass(MeActivity.this,PersonaldataActivity.class);
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
                case R.id.iv_scanningcode:
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
                    String name1 = tvMeName.getText().toString();
                    ivCircle.setDrawingCacheEnabled(true);
                    Bitmap bitmap2 = ivCircle.getDrawingCache();
                    String por = bitmapToString(bitmap2);
                    intent.putExtra("name1",name1);
                    intent.putExtra("por",por);
                    ivCircle.setDrawingCacheEnabled(false);
                    startActivity(intent);
                    break;
                case R.id.ll_draft:
                    intent.setClass(MeActivity.this, DraftBoxActivity.class);
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


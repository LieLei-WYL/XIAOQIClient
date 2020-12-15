package com.example.xiaoqi.publish;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.bumptech.glide.Glide;
import com.example.xiaoqi.MainActivity;
import com.example.xiaoqi.R;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.DraftBoxActivity;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import java.io.File;
import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PublishActivity extends AppCompatActivity {
    private int columnWidth;
    private ArrayList<String> imagePaths = null;
    private GridAdapter gridAdapter;
    private GridView gv;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    private ImageView ivPlus;
    private ImageView ivBackToHome;
    private EditText edText;
    private EditText edTitle;
    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private RelativeLayout rlTopic;
    private RelativeLayout rlPosition;
    private ImageView ivDraftBox;
    private Button btnRelease;
    private MapView mpView;
    private BaiduMap baiduMap;
    private TextView tvCity;
    private ImageView ivNew;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private int DRAFTBOX = 500;
    private MaterialProgressBar mpb;
    //定位客户端类
    private LocationClient locClient;
    //定位客户端选项类
    private LocationClientOption locOption;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_release);
        findViews();
        setListener();
        //得到GridView中每个ImageView宽高
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gv.setNumColumns(cols);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;
        //GridView item点击事件（浏览照片）
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishActivity.this);
                intent.setCurrentItem(position);
                intent.setPhotoPaths(imagePaths);
                startActivityForResult(intent, 22);
            }
        });
        ////取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
//        selectImage();
//        Intent response = getIntent();
//        String id = response.getStringExtra("id");
//        if (id.equals("update")){
//            showMessage();
//        }
    }

    private void showMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Message",MODE_PRIVATE);
        String title = sharedPreferences.getString("title","no title");
        String text = sharedPreferences.getString("text","null");
        String topic1 = sharedPreferences.getString("topic1","null");
        String topic2 = sharedPreferences.getString("topic2","null");
        String topic3 = sharedPreferences.getString("topic3","null");
        String position = sharedPreferences.getString("position","null");
        String pic1 = sharedPreferences.getString("pic1","***");
        String pic2 = sharedPreferences.getString("1","***");
        String pic3 = sharedPreferences.getString("2","***");
        edTitle.setText(title);
        edText.setText(text);
        tvCity.setText(position);
        t1.setText(topic1);
        t2.setText(topic2);
        t3.setText(topic3);
        ArrayList<String> list = new ArrayList<>();
        if ((!pic1.equals("***"))&&pic2.equals("***")&&pic3.equals("***")){
            list.add(pic1);
        }else if ((!pic1.equals("***"))&&(!pic2.equals("***"))&&pic3.equals("***")){
            list.add(pic1);
            list.add(pic2);
        }else if ((!pic1.equals("***"))&&(!pic2.equals("***"))&&(!pic3.equals("***"))){
            list.add(pic1);
            list.add(pic2);
            list.add(pic3);
        }
        GridAdapter gridAdapter = new GridAdapter(list);
        gv.setAdapter(gridAdapter);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivPlus.setOnClickListener(myListener);
        ivBackToHome.setOnClickListener(myListener);
        rlPosition.setOnClickListener(myListener);
        rlTopic.setOnClickListener(myListener);
        ivDraftBox.setOnClickListener(myListener);
        btnRelease.setOnClickListener(myListener);
        iv1.setOnClickListener(myListener);
        iv2.setOnClickListener(myListener);
        iv3.setOnClickListener(myListener);
    }

    private void findViews() {
        ivPlus = findViewById(R.id.iv_plus);
        ivBackToHome = findViewById(R.id.iv_backtohome);
        gv = findViewById(R.id.gv);
        edText = findViewById(R.id.ed_text);
        edTitle = findViewById(R.id.ed_title);
        rlTopic = findViewById(R.id.rl_topic);
        rlPosition = findViewById(R.id.rl_position);
        ivDraftBox = findViewById(R.id.iv_draftbox);
        btnRelease = findViewById(R.id.btn_release);
        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        t3 = findViewById(R.id.t3);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        mpb = findViewById(R.id.mpb);

    }
class MyListener implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_plus:
                //多选图片
                selectImage();
                break;
            case R.id.iv_backtohome:
                showAlertDialog();
                break;
            case R.id.rl_position:
                //定位
                showPosition();
                break;
            case R.id.rl_topic:
                //话题
                showTopic();
                addTopic();
                break;
            case R.id.btn_release:
                //发布作品操作
                saveMessage();
                mpb.setVisibility(View.VISIBLE);
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        Intent intent = new Intent();
                        intent.setClass(PublishActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    }
                };
                handler.sendEmptyMessageDelayed(1,3000);
                break;
            case R.id.iv_draftbox:
                addDraftBox();
                break;
            case R.id.iv1:
                t1.setText("");
                break;
            case R.id.iv2:
                t2.setText("");
                break;
            case R.id.iv3:
                t3.setText("");
                break;
        }
    }
}

    /**
     * 添加话题
     */
    private void addTopic() {
        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = tv1.getText().toString();
                if (t1.getText().equals("")){
                    t1.setText(t);
                }else if (t2.getText().equals("")){
                    t2.setText(t);
                }else if (t3.getText().equals("")){
                    t3.setText(t);
                }else {
                    Toast.makeText(getApplicationContext(),"最多可以添加三个话题哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = tv2.getText().toString();
                if (t1.getText().equals("")){
                    t1.setText(t);
                }else if (t2.getText().equals("")){
                    t2.setText(t);
                }else if (t3.getText().equals("")){
                    t3.setText(t);
                }else {
                    Toast.makeText(getApplicationContext(),"最多可以添加三个话题哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = tv3.getText().toString();
                if (t1.getText().equals("")){
                    t1.setText(t);
                }else if (t2.getText().equals("")){
                    t2.setText(t);
                }else if (t3.getText().equals("")){
                    t3.setText(t);
                }else {
                    Toast.makeText(getApplicationContext(),"最多可以添加三个话题哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = tv4.getText().toString();
                if (t1.getText().equals("")){
                    t1.setText(t);
                }else if (t2.getText().equals("")){
                    t2.setText(t);
                }else if (t3.getText().equals("")){
                    t3.setText(t);
                }else {
                    Toast.makeText(getApplicationContext(),"最多可以添加三个话题哦",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
/**
 * 话题
 */
    private void showTopic() {
        showPopupWindow();
    }
    private void showPopupWindow () {
        //创建PopupWindpw对象
        final PopupWindow popupWindow = new PopupWindow(this);
        //设置弹出窗口的宽度
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置他的视图
        View view = getLayoutInflater().inflate(R.layout.topic_pop, null);
        tv1 = view.findViewById(R.id.ttv1);
        tv2 = view.findViewById(R.id.ttv2);
        tv3 = view.findViewById(R.id.ttv3);
        tv4 = view.findViewById(R.id.ttv4);
        ivNew = view.findViewById(R.id.iv_new);
        popupWindow.setContentView(view);
        //显示PopupWindow(必须指定显示的位置)
        popupWindow.showAtLocation(rlTopic,Gravity.CENTER,0,700);
    }
    private void showPosition() {
        tvCity = findViewById(R.id.tv_city);
//获取定位客户端类的对象
        locClient = new LocationClient(getApplicationContext());
        //获取地图控件
        View view = getLayoutInflater().inflate(R.layout.map,null);
        mpView = view.findViewById(R.id.mv_map);
        showLocationInfo();
        Log.i("hz","这是定位");
    }
    /**
     * 定位并显示定位数据
     */
    private void showLocationInfo() {
        Log.i("hz","这是定位");
        //获取百度地图控制器
        baiduMap = mpView.getMap();
        //获取定位客户端选项类的对象
        locOption = new LocationClientOption();
        //1. 设置定位参数
        //打开GPS
        locOption.setOpenGps(true);
        // 坐标类型
        locOption.setCoorType("bd09ll");
        //设置扫描间隔时间
        locOption.setScanSpan(1000);
        //设置定位模式(高精度定位模式)
        locOption.setLocationMode(
                LocationClientOption.LocationMode.Hight_Accuracy);
        //设置坐标系
        locOption.setCoorType("wgs84");
        //设置是否需要地址信息
        locOption.setIsNeedAddress(true);
        locOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置高精度定位定位模式
        //2. 将设置的定位参数应用给定位客户端
        //开启图层定位
        baiduMap.setMyLocationEnabled(true);
        locClient.setLocOption(locOption);
        //4. 注册定位监听器（为了将定位的数据显示在地图上）
        locClient.registerLocationListener(
                new BDAbstractLocationListener() {
                    @Override
                    public void onReceiveLocation(BDLocation bdLocation) {
                        //获取纬度信息
                        double lat = bdLocation.getLatitude();
                        //获取经度信息
                        double lng = bdLocation.getLongitude();
                        //打印出当前位置
                        Log.i("hz", "location.getAddrStr()=" + bdLocation.getAddrStr());
                        //打印出当前城市
                        String city = bdLocation.getCity();
                        Log.i("hz", "location.getCity()=" + bdLocation.getCity());
                        tvCity.setText(city);
                        //返回码
                        int i = bdLocation.getLocType();
                    }
                });
        //3. 启动定位
        locClient.start();
    }

    /**
     * AlertDialog对话框
     */
    private void showAlertDialog() {
        //创建Builder对象
        //方法链调用
        new AlertDialog.Builder(this).setTitle("温馨提示")
                .setMessage("是否要保存到草稿箱？")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(PublishActivity.this, MeActivity.class);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //加入草稿箱操作
                        addDraftBox();
                    }
                }).create().show();
    }

    /**
     * 加入草稿箱操作
     */
    private void addDraftBox() {
        saveMessage();
        Intent intent = new Intent();
        intent.setClass(PublishActivity.this, DraftBoxActivity.class);
        startActivityForResult(intent,DRAFTBOX);
    }
    /**
     * 保存信息
     */
    private void saveMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Message",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String title = edTitle.getText().toString();
        String text = edText.getText().toString();
        String position = tvCity.getText().toString();
        String topic1 = t1.getText().toString();
        String topic2 = t2.getText().toString();
        String topic3 = t3.getText().toString();
        GridView gridView = (GridView)gv;
        GridAdapter gridAdapter = (GridAdapter) gridView.getAdapter();
        for(int i=0;i<gridAdapter.getCount();++i){
            String pic = gridAdapter.getItem(i);
            editor.putString(i+"",pic);
        }
//        String pic1 = gridAdapter.getItem(0);
//        String pic2 = gridAdapter.getItem(1);
//        String pic3 = gridAdapter.getItem(2);
        editor.putString("title",title);
        editor.putString("text",text);
        editor.putString("position",position);
        editor.putString("topic1",topic1);
        editor.putString("topic2",topic2);
        editor.putString("topic3",topic3);
        editor.commit();
    }

    /**
     * 多选图片
     */
    private void selectImage() {
        PhotoPickerIntent intent1 = new PhotoPickerIntent(PublishActivity.this);
        intent1.setSelectModel(SelectModel.MULTI);
        intent1.setShowCarema(true); // 是否显示拍照
        intent1.setMaxTotal(3); // 最多选择照片数量，默认为9
        intent1.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent1, REQUEST_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                //浏览照片
                case 22:
                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
                        loadAdpater(paths);
                    }
                    break;
            }
        }else if(requestCode == DRAFTBOX &&resultCode == 520) {
            //获得从LoginActivity相应的数据
            showMessage();
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths == null) {
            imagePaths = new ArrayList<>();
        }
        imagePaths.clear();
        imagePaths.addAll(paths);
        if (gridAdapter == null) {
            gridAdapter = new GridAdapter(imagePaths);
            gv.setAdapter(gridAdapter);
        } else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
            Glide.with(PublishActivity.this)
                    .load(new File(getItem(position)))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }

}

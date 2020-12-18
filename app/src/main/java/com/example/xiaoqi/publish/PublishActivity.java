package com.example.xiaoqi.publish;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
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

import androidx.annotation.NonNull;
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
import com.example.xiaoqi.home.Global;
import com.example.xiaoqi.home.HomeActivity;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteInfo;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.me.UploadUtil;
import com.example.xiaoqi.news.DraftBoxActivity;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.foamtrace.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class PublishActivity extends AppCompatActivity {
    private Bitmap bm = null;
    private String fileName;
    private int noteId;

    private int columnWidth;
    private ArrayList<String> imagePaths = null;
    private GridAdapter gridAdapter;
    private ImageView ivPublishImg;
//    private GridView gv;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private static final int REQUEST_CAMERA_CODE = 11;
    private ImageView ivPlus;
    private ImageView ivBackToHome;
    private EditText edTitle;
    private EditText edContent;
    private EditText edTopic;
    private RelativeLayout rlPosition;
    private ImageView ivDraftBox;
    private Button btnRelease;
    private MapView mpView;
    private BaiduMap baiduMap;
    private TextView tvCity;
    private int DRAFTBOX = 500;
    private MaterialProgressBar mpb;
    //定位客户端类
    private LocationClient locClient;
    //定位客户端选项类
    private LocationClientOption locOption;

    //定义存储数据的UserInfo对象
    private NoteInfo noteInfo;
    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler(){
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 0:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global = (Global) getApplication();
                        global.setFindNoteList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("findNoteList",noteInfo.getNotes().toString());
                        Intent intent = new Intent();
                        intent.setClass(PublishActivity.this , HomeActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_release);
        findViews();
        setListener();
//        //得到GridView中每个ImageView宽高
//        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
//        cols = cols < 3 ? 3 : cols;
//        gv.setNumColumns(cols);
//        int screenWidth = getResources().getDisplayMetrics().widthPixels;
//        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
//        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;
//        //GridView item点击事件（浏览照片）
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishActivity.this);
//                intent.setCurrentItem(position);
//                intent.setPhotoPaths(imagePaths);
//                startActivityForResult(intent, 22);
//            }
//        });
//        ////取消严格模式  FileProvider
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//            StrictMode.setVmPolicy(builder.build());
//        }


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
        String position = sharedPreferences.getString("position","null");
        String pic1 = sharedPreferences.getString("pic1","***");
        String pic2 = sharedPreferences.getString("1","***");
        String pic3 = sharedPreferences.getString("2","***");
        edTitle.setText(title);
        edContent.setText(text);
        tvCity.setText(position);
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
//        GridAdapter gridAdapter = new GridAdapter(list);
//        gv.setAdapter(gridAdapter);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivPlus.setOnClickListener(myListener);
        ivBackToHome.setOnClickListener(myListener);
        rlPosition.setOnClickListener(myListener);
        edTopic.setOnClickListener(myListener);
        ivDraftBox.setOnClickListener(myListener);
        btnRelease.setOnClickListener(myListener);
    }

    private void findViews() {
        ivPlus = findViewById(R.id.iv_plus);
        ivBackToHome = findViewById(R.id.iv_backtohome);
        ivPublishImg = findViewById(R.id.iv_publish_img);
//        gv = findViewById(R.id.gv);
        edContent = findViewById(R.id.ed_content);
        edTitle = findViewById(R.id.ed_title);
        edTopic = findViewById(R.id.ed_topic);
        tvCity = findViewById(R.id.tv_city);
        rlPosition = findViewById(R.id.rl_position);
        ivDraftBox = findViewById(R.id.iv_draftbox);
        btnRelease = findViewById(R.id.btn_release);
        mpb = findViewById(R.id.mpb);

    }
class MyListener implements View.OnClickListener{

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_plus:
//                //多选图片
//                selectImage();

                // 相册选取
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 103);

                break;
            case R.id.iv_backtohome:
                showAlertDialog();
                break;
            case R.id.rl_position:
                //定位
                showPosition();
                break;
            case R.id.btn_release:
                //发布作品操作

                applyNoteIdFromServer(bm);
//                applyDateToServer();

//                saveMessage();
                mpb.setVisibility(View.VISIBLE);
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        applyDateToServer();
//                        Intent intent = new Intent();
//                        intent.setClass(PublishActivity.this , HomeActivity.class);
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    }
                };
                handler.sendEmptyMessageDelayed(1,3000);
                break;
            case R.id.iv_draftbox:
                addDraftBox();
                break;
            }
        }
    }

    private void applyDateToServer() {
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    //从服务端下载所有笔记信息，并通过Message发布出去
                    //1、通过网络请求下载数据(图片要下载到本地，还要修改图片地址为本地地址)
                    //创建URL对象
                    Global global = (Global) getApplication();
                    Log.e("path",global.getPath());
                    URL url = new URL(global.getPath() + "/XIAOQI/HomeServlet");
                    //通过URL对象获取网络输入流
                    InputStream in = url.openStream();
                    //读数据（Json串）循环读写方式
                    byte[] bytes = new byte[4096];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes,0,bytes.length)) != -1){
                        buffer.append(new String(bytes,0,len));
                    }
                    String reslut = new String(buffer.toString().getBytes(),"utf-8");
                    Log.e("wyl",reslut);
                    in.close();
                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(reslut);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);


//                    //通过网络下载note的图片，保存到本地
//                    //拼接图片的服务端资源路径，进行下载
//                    for(int j=0;j < notes.size();j++){
//                        notes.get(j).getImages()[0] = downloadImage(notes.get(j).getImages()[0]);
//                        notes.get(j).setAuthorAvatar(downloadImage(notes.get(j).getAuthorAvatar()));
//                    }


                    //2、通过发送Message对象将数据发布出去
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void translateNoteToServer(final String string) {
        Log.i("string", string);
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/AddNoteServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(string);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void applyNoteIdFromServer(Bitmap bm) {
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/NoteIdServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write("");
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("noteid result", "result:" + result);
                    noteId = Integer.parseInt(result)+1;
                    saveImage(bm);

                    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                    Date curDate = new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);
                    String string = global.getCurrentUserPhone()+";"+fileName+
                            ";"+edTitle.getText().toString().trim()+
                            ";"+edContent.getText().toString().trim()+
                            ";"+edTopic.getText().toString().trim()+
                            ";"+str+";"+tvCity.getText().toString().trim();
                    translateNoteToServer(string);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void showPosition() {
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
//                        locClient.stop();
                        PublishActivity.this.finish();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //加入草稿箱操作
                        locClient.stop();
                        addDraftBox();
                    }
                }).create().show();
    }

    /**
     * 加入草稿箱操作
     */
    private void addDraftBox() {
//        saveMessage();
        Intent intent = new Intent();
        intent.setClass(PublishActivity.this, DraftBoxActivity.class);
        startActivityForResult(intent,DRAFTBOX);
    }
//    /**
//     * 保存信息
//     */
//    private void saveMessage() {
//        for (int i = 0;i < imagePaths.size(); i++) {
//            Log.e("wcnm",imagePaths.get(i));
//            Bitmap bitmap = convertStringToIcon(imagePaths.get(i));
////            Bitmap bitmap = BitmapUtils.stringToBitmap(imagePaths.get(i));
////            Bitmap bitmap = (Bitmap) imagePaths.get(i);
//            // 保存图片
//            try {
//                saveImage(bitmap,i+1);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 多选图片
     */
    private void selectImage() {
//        //相册选取
//        Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
//        intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent1, 103);

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
//                    loadAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                //浏览照片
                case 22:
//                    loadAdpater(data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT));
                    break;
                // 调用相机拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();

                        ArrayList<String> paths = new ArrayList<>();
                        paths.add(captureManager.getCurrentPhotoPath());
//                        loadAdpater(paths);
                    }
                    break;
                case 103:
//                    Bitmap bm = null;
                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();

                    try {
                        Uri originalUri = data.getData(); // 获得图片的uri

                        bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片

//                        applyNoteIdFromServer(bm);
//                        saveImage(bm);

                        // 这里开始的第二部分，获取图片的路径：

                        String[] proj = { MediaStore.Images.Media.DATA };

                        // 好像是android多媒体数据库的封装接口，具体的看Android文档
                        @SuppressWarnings("deprecation")
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        // 按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        // 最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        ivPublishImg.setImageURI(originalUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }else if(requestCode == DRAFTBOX &&resultCode == 520) {
            //获得从LoginActivity相应的数据
            showMessage();
        }
    }

//    private void loadAdpater(ArrayList<String> paths) {
//        if (imagePaths == null) {
//            imagePaths = new ArrayList<>();
//        }
//        imagePaths.clear();
//        imagePaths.addAll(paths);
//        if (gridAdapter == null) {
//            gridAdapter = new GridAdapter(imagePaths);
//            gv.setAdapter(gridAdapter);
//        } else {
//            gridAdapter.notifyDataSetChanged();
//        }
//    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st)
    {
        // OutputStream out;
        Bitmap bitmap = null;
        try
        {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.e("cnm","not null");
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("cnm","null");
            return null;
        }
    }

    public File saveImage(Bitmap bmp) throws MalformedURLException {
        //获取本地file目录
        String files1 = getFilesDir().getAbsolutePath();
        String imgs1 = files1 + "/imgs";
        //判断imgs目录是否存在
        File dirImgs1 = new File(imgs1);
        if (!dirImgs1.exists()) {
            //如果目录不存在，则创建
            dirImgs1.mkdir();
        }
        Global global = (Global) getApplication();
        fileName = "note" + noteId + ".png";
        File file = new File(dirImgs1, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = imgs1+"/"+fileName;
        File file1 = new File(path); //这里的path就是那个地址的全局变量
        new Thread(){
            @Override
            public void run() {
                String result = UploadUtil.uploadFile(file1, global.getPath() + "/XIAOQI/UploadShipServlet2");
//                Log.e("UploadShipServlet2",result);
            }
        }.start();

        return dirImgs1;
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

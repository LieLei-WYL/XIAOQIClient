package com.example.xiaoqi.me;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PersonaldataActivity extends AppCompatActivity {
    private Spinner spinnerSex;
    private ImageView ivBacktome;
    private EditText etPersonname;
    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;
    private EditText etPersonplace;
    private EditText etPersonintro;
    private ImageView ivErweima;
    private Button btnSave;
    private ImageView ivPersonCircle;
    private ImageView ivPersonBackground;
    private TextView tvPersonphone;

    private EditText etOldPwd;
    private EditText etNewPwd;

    private final int login = 1;
    private String ssex;

    private User user;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler() {
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(),"保存失败，请重试",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);

        findViews();
        setListener();

        spinnerSex.setOnItemSelectedListener(new SpinnerSelectedListener());

        Global global = (Global) getApplication();
        user = global.getUser();
        if(fileIsExists(user.getAvatar())){
            Bitmap avatar = BitmapFactory.decodeFile(user.getAvatar());
            ivPersonCircle.setImageBitmap(avatar);
        }else{
            ivPersonCircle.setImageResource(R.drawable.avatardefault1);
        }
        etPersonname.setText(user.getName());
        tvPersonphone.setText(user.getPhone());
        if((user.getGender().equals("man"))) {
            spinnerSex.setSelection(0);
        }else {
            spinnerSex.setSelection(1);
        }
        String[] birthday = user.getBirthday().split("-");
        etYear.setText(birthday[0]);
        etMonth.setText(birthday[1]);
        etDay.setText(birthday[2]);
        etPersonplace.setText(user.getArea());
        etPersonintro.setText(user.getProfile());
        if(fileIsExists(user.getBackground())){
            Bitmap bg = BitmapFactory.decodeFile(user.getBackground());
            ivPersonBackground.setImageBitmap(bg);
        }else{
            ivPersonBackground.setImageResource(R.drawable.cloud);
        }
    }

    private void findViews() {
        spinnerSex = findViewById(R.id.spinner_sex);
        ivBacktome = findViewById(R.id.iv_backtome);
        etDay = findViewById(R.id.et_day);
        etMonth = findViewById(R.id.et_month);
        etYear = findViewById(R.id.et_year);
        etPersonintro = findViewById(R.id.et_personintro);
        etPersonname = findViewById(R.id.et_personname);
        etPersonplace = findViewById(R.id.et_personplace);
        ivErweima = findViewById(R.id.iv_erweima);
        btnSave = findViewById(R.id.btn_save);
        ivPersonCircle = findViewById(R.id.iv_personcircle);
        ivPersonBackground = findViewById(R.id.iv_personbackground);
        tvPersonphone = findViewById(R.id.tv_personphone);

        etOldPwd = findViewById(R.id.et_old_pwd);
        etNewPwd = findViewById(R.id.et_new_pwd);
    }
    private void setListener() {
        MyListener myListener = new MyListener();
        ivPersonBackground.setOnClickListener(myListener);
        ivPersonCircle.setOnClickListener(myListener);
        ivBacktome.setOnClickListener(myListener);
        ivErweima.setOnClickListener(myListener);
        btnSave.setOnClickListener(myListener);
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.iv_backtome:
                    intent.setClass(PersonaldataActivity.this,MeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_erweima:
                    intent.setClass(PersonaldataActivity.this,CodeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_personbackground:
                    intent.setClass(PersonaldataActivity.this,BackgroundActivity.class);
                    intent.putExtra("background",user.getBackground());
                    //跳转到新的Activity并且返回响应
                    startActivity(intent);
                    break;
                case R.id.iv_personcircle:
                    intent.setClass(PersonaldataActivity.this,PortraitActivity.class);
                    intent.putExtra("avatar",user.getAvatar());
                    //跳转到新的Activity并且返回响应
                    startActivity(intent);
                    break;
                case R.id.btn_save:
                    Global global = (Global) getApplication();
                    String pwd = etNewPwd.getText().toString().trim();
                    if(!pwd.equals("")) {
                        String pwd1 = etOldPwd.getText().toString().trim();
                        String pwd2 = global.getUser().getPassword();
                        if (!pwd1.equals(pwd2)) {
                            Toast.makeText(getApplicationContext(), "原密码输入错误", Toast.LENGTH_SHORT).show();
                        } else {
                            String avatar1 = "imgPath";
                            String name1 = etPersonname.getText().toString().trim();
                            String phone1 = etPersonname.getText().toString().trim();
                            String gender1 = "";
                            if (spinnerSex.getSelectedItemPosition() == 0) {
                                gender1 = "man";
                            } else {
                                gender1 = "woman";
                            }
                            String birthday1 = etYear.getText().toString().trim() + "-" + etMonth.getText().toString().trim() + "-" + etDay.getText().toString().trim();
                            String area1 = etPersonplace.getText().toString().trim();
                            String intro1 = etPersonintro.getText().toString().trim();
                            String bg1 = "imgPath";
                            String newPwd = etNewPwd.getText().toString().trim();

                            User user = global.getUser();
                            user.setAvatar(avatar1);
                            user.setName(name1);
                            user.setPhone(phone1);
                            user.setGender(gender1);
                            user.setBirthday(birthday1);
                            user.setArea(area1);
                            user.setProfile(intro1);
                            user.setBackground(bg1);
                            user.setPassword(newPwd);
                            global.setUser(user);

                            String tran = avatar1 + ":" + name1 + ":" + phone1 + ":" + gender1 + ":" + birthday1 + ":" + area1 + ":" + intro1 + ":" + bg1 + ":" + newPwd;
                            translateDateToServer(tran);
                        }
                    }else{
                        String avatar1 = user.getAvatar();
                        String name1 = etPersonname.getText().toString().trim();
                        String phone1 = user.getPhone();
                        String gender1 = "";
                        if (spinnerSex.getSelectedItemPosition() == 0) {
                            gender1 = "man";
                        } else {
                            gender1 = "woman";
                        }
                        String birthday1 = etYear.getText().toString().trim() + "-" + etMonth.getText().toString().trim() + "-" + etDay.getText().toString().trim();
                        String area1 = etPersonplace.getText().toString().trim();
                        String intro1 = etPersonintro.getText().toString().trim();
                        String bg1 = user.getBackground();

                        User user = global.getUser();
                        user.setAvatar(avatar1);
                        user.setName(name1);
                        user.setPhone(phone1);
                        user.setGender(gender1);
                        user.setBirthday(birthday1);
                        user.setArea(area1);
                        user.setProfile(intro1);
                        user.setBackground(bg1);
                        global.setUser(user);

                        String tran = avatar1 + ":" + name1 + ":" + phone1 + ":" + gender1 + ":" + birthday1 + ":" + area1 + ":" + intro1 + ":" + bg1 + ":" + global.getUser().getPassword();
                        translateDateToServer(tran);
                    }
                    break;
            }
        }
    }


    /**
     * 将当前用户的手机号字符串传输给服务端，请求获得该用户的所有信息
     *
     * @param tran
     */
    private void translateDateToServer(final String tran) {
        Log.i("tran", tran);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/EditInfoServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(tran);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);
                    if("YES".equals(result)){
                        Log.i("save result", result+"");
                        Message message = new Message();
                        message.what = 0;//区分不同的消息本身
                        myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
                    }else if("NO".equals(result)){
                        Log.i("save result", result+"");
                        Message message = new Message();
                        message.what = 1;//区分不同的消息本身
                        myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
                    }
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

                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = name+"、"+gender+"、"+birthday+"、"+area+"、"+profile;
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

        return avatar;
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == login && resultCode == 2) {//从换头像的页面跳回
            //获得从Portrait1Activity响应的数据
            String name = data.getStringExtra("name");
            //修改数据源
            Bitmap bitmap = stringToBitmap(name);
            ivPersonCircle.setImageBitmap(bitmap);
        } else if ((requestCode == login && resultCode == 3)) {//从换背景图的页面跳回
            //获得从BackgroundActivity响应的数据
            String bg = data.getStringExtra("bg");
            //修改数据源
            Bitmap bitmap1 = stringToBitmap(bg);
            ivPersonBackground.setImageBitmap(bitmap1);
        }
    }

    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //获取内容
            String select = parent.getItemAtPosition(position).toString();
            Log.i("性别",select);
            switch (select){
                case "男":
                    ssex = "man";
                    break;
                case "女":
                    ssex = "woman";
                    break;
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}

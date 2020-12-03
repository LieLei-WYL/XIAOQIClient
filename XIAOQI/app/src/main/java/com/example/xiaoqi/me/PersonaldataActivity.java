package com.example.xiaoqi.me;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import java.io.ByteArrayOutputStream;

public class PersonaldataActivity extends AppCompatActivity {
    private Spinner spinnerSex;
    private ImageView ivBacktome;
    private EditText etPersonname;
    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;
    private EditText etPersonplace;
    private EditText etPersonintro;
    private Button btnSave;
    private ImageView ivPersonCircle;
    private ImageView ivPersonBackground;
    private TextView tvPersonphone;
    private final int login = 1;
    private String ssex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaldata);
        findViews();
        setListener();
        Intent response = getIntent();
        String uname = response.getStringExtra("uname");
        String intr = response.getStringExtra("intr");
        String pic = response.getStringExtra("pic");
        etPersonname.setText(uname);
        etPersonintro.setText(intr);
        Bitmap bp1 = stringToBitmap(pic);
        ivPersonCircle.setImageBitmap(bp1);
        spinnerSex.setOnItemSelectedListener(new SpinnerSelectedListener());
        showUserInfo();
    }

    private void showUserInfo() {
        SharedPreferences userSp = getSharedPreferences("UserInfo", MODE_PRIVATE);
        String phone = userSp.getString("phone","00000000000");
        String year = userSp.getString("year","2000");
        String month = userSp.getString("month","1");
        String day = userSp.getString("day","1");
        String place = userSp.getString("place","北京");
        String ssex = userSp.getString("ssex","男");
        etPersonplace.setText(place);
        etYear.setText(year);
        etMonth.setText(month);
        etDay.setText(day);
        if (ssex.equals("man")){
            spinnerSex.setSelection(0);
        }else {
            spinnerSex.setSelection(1);
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
        btnSave = findViewById(R.id.btn_save);
        ivPersonCircle = findViewById(R.id.iv_personcircle);
        ivPersonBackground = findViewById(R.id.iv_personbackground);
        tvPersonphone = findViewById(R.id.tv_personphone);
    }
    private void setListener() {
        MyListener myListener = new MyListener();
        ivPersonBackground.setOnClickListener(myListener);
        ivPersonCircle.setOnClickListener(myListener);
        ivBacktome.setOnClickListener(myListener);
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
            case R.id.iv_personbackground:
                intent.setClass(PersonaldataActivity.this,BackgroundActivity.class);
                ivPersonBackground.setDrawingCacheEnabled(true);
                Bitmap bitmap1 = ivPersonBackground.getDrawingCache();
                //bitmap图片转成string
                String bg = bitmapToString(bitmap1);
                //跳转携带数据
                intent.putExtra("bg",bg);
                //跳转到新的Activity并且返回响应
                startActivityForResult(intent,login);
                ivPersonBackground.setDrawingCacheEnabled(false);
                break;
            case R.id.iv_personcircle:
                intent.setClass(PersonaldataActivity.this,Portrait1Activity.class);
                //获取 ImageView 中已经加载好的图片：
                ivPersonCircle.setDrawingCacheEnabled(true);
                Bitmap bitmap = ivPersonCircle.getDrawingCache();
                //bitmap图片转成string
                String name = bitmapToString(bitmap);
                //跳转携带数据
                Bundle bundle = new Bundle();
                bundle.putString("name",name);
                intent.putExtra("bundle",bundle);
                //跳转到新的Activity并且返回响应
                startActivityForResult(intent,login);
                ivPersonCircle.setDrawingCacheEnabled(false);
                break;
            case R.id.btn_save:
                //利用SharedPreferences保存个人信息并且在跳转回MeActivity时在MeActivity显示
                saveInformation();
                Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

    /**
     * 利用SharedPreferences保存个人信息并且在跳转回MeActivity时在MeActivity显示
     */
    private void saveInformation() {
        SharedPreferences userSp = getSharedPreferences("UserInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = userSp.edit();
        String name = etPersonname.getText().toString().trim();
        String phone = tvPersonphone.getText().toString().trim();
        String year = etYear.getText().toString().trim();
        String month = etMonth.getText().toString().trim();
        String day = etDay.getText().toString().trim();
        String place = etPersonplace.getText().toString().trim();
        String intro = etPersonintro.getText().toString().trim();
        ivPersonBackground.setDrawingCacheEnabled(true);
        Bitmap bitmap = ivPersonBackground.getDrawingCache();
        String pic = bitmapToString(bitmap);
        editor.putString("name",name);
        editor.putString("phone",phone);
        editor.putString("year",year);
        editor.putString("month",month);
        editor.putString("day",day);
        editor.putString("place",place);
        editor.putString("intro",intro);
        editor.putString("pic",pic);
        editor.putString("ssex",ssex);
        editor.commit();
        ivPersonBackground.setDrawingCacheEnabled(false);
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

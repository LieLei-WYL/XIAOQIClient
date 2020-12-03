package com.example.xiaoqi.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import java.io.ByteArrayOutputStream;

public class BackgroundActivity extends AppCompatActivity {
    private ImageView ivBacktoperson1;
    private ImageView ivBackground;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);
        //接收从MeActivity传递的图片
        Intent request = getIntent();
        String bg = request.getStringExtra("bg");
        Bitmap bitmap = stringToBitmap(bg);
        ivBackground = findViewById(R.id.iv_background);
        ivBackground.setImageBitmap(bitmap);
        ivBacktoperson1 = findViewById(R.id.iv_backtoperson1);
        ivBacktoperson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回数据
                Intent response = new Intent();
                Bitmap bitmap = ((BitmapDrawable) ivBackground.getDrawable()).getBitmap();
                //bitmap图片转成string
                String bg = bitmapToString(bitmap);
                response.putExtra("bg",bg);
                //响应
                setResult(3,response);
                //结束NewActivity
                finish();
            }
        });
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
    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
}

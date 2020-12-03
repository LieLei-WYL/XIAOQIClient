package com.example.xiaoqi.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import java.io.ByteArrayOutputStream;


public class PortraitActivity extends AppCompatActivity {
    private ImageView ivPortraitimg;
    private ImageView ivBacktome1;
    private Button btnPortrait;
    private LinearLayout root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portrait);
        //接收从MeActivity传递的图片
        Intent request = getIntent();
        Bundle bundle = request.getBundleExtra("bundle");
        String name = bundle.getString("name");
        Bitmap bitmap = stringToBitmap(name);
        ivPortraitimg = findViewById(R.id.iv_portraitimg);
        ivPortraitimg.setImageBitmap(bitmap);
        ivBacktome1 = findViewById(R.id.iv_backtome1);
        ivBacktome1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回数据
                Intent response = new Intent();
                Bitmap bitmap = ((BitmapDrawable) ivPortraitimg.getDrawable()).getBitmap();
                //bitmap图片转成string
                String name = bitmapToString(bitmap);
                response.putExtra("name",name);
                //响应
                setResult(2000,response);
                //结束NewActivity
                finish();
            }
        });
        btnPortrait = findViewById(R.id.btn_portrait);
        root = findViewById(R.id.root);
        btnPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出PopupWindow
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        //创建PopupWindpw对象
        final PopupWindow popupWindow = new PopupWindow(this);
        //设置弹出窗口的宽度
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置他的视图
        View view = getLayoutInflater().inflate(R.layout.popupwindow,null);
        //设置视图当中控件的属性和监听器
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭弹出框
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        //显示PopupWindow(必须指定显示的位置)
        LinearLayout root = findViewById(R.id.root);
        popupWindow.showAtLocation(root, Gravity.CENTER,0,0);
//        //指定弹出窗口在某个控件的下方
//        popupWindow.showAsDropDown(btnPortrait);

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

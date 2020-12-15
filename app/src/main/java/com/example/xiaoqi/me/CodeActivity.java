package com.example.xiaoqi.me;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.xiaoqi.me.encode.CodeCreator.createQRCode;

public class CodeActivity extends AppCompatActivity {
    private CircleImageView civ;
    private TextView cname;
    private ImageView code;
    private ImageView ivBacktoMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        civ = findViewById(R.id.civ);
        cname = findViewById(R.id.cname);
        Intent response = getIntent();
        String name = response.getStringExtra("name1");
        cname.setText(name);
        String por = response.getStringExtra("por");
        Bitmap bitmap = stringToBitmap(por);
        civ.setImageBitmap(bitmap);
        ivBacktoMenu = findViewById(R.id.iv_backtomenu);
        ivBacktoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CodeActivity.this.finish();
            }
        });
        Bitmap bitmap1 = createQRCode("http://XIAOQI/pikaqiu");
        code = findViewById(R.id.code);
        code.setImageBitmap(bitmap1);
        Log.i("hz",name);
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

}

package com.example.xiaoqi.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xiaoqi.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ToMeActivity extends AppCompatActivity {
    private LinearLayout llToMeBg;
    private CircleImageView ivToMeCircle;
    private TextView tvToMeName;
    private ImageView ivToMeSex;
    private TextView tvToMeintro;
    private LinearLayout tomeAttention;
    private TextView tomeAttentionNum;
    private LinearLayout tomeChasers;
    private TextView tomeChasersNum;
    private LinearLayout tomePraised;
    private TextView tomePraisedNum;
    private Button btnFollow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_me);
        findViews();
    }

    private void findViews() {
        llToMeBg = findViewById(R.id.ll_to_me_bg);
        ivToMeCircle = findViewById(R.id.iv_to_me_circle);
        ivToMeSex = findViewById(R.id.iv_to_me_sex);
        tvToMeName = findViewById(R.id.tv_to_me_name);
        tvToMeintro = findViewById(R.id.tv_to_meintro);
        tomeAttention = findViewById(R.id.tome_attention);
        tomeAttentionNum = findViewById(R.id.tome_attention_num);
        tomeChasers = findViewById(R.id.tome_chasers);
        tomeChasersNum = findViewById(R.id.tome_chasers_num);
        tomePraised = findViewById(R.id.tome_praised);
        tomePraisedNum = findViewById(R.id.tome_praised_num);
        btnFollow = findViewById(R.id.btn_follow);
    }
}
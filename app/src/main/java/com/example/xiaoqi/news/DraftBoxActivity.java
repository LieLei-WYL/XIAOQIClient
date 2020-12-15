package com.example.xiaoqi.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xiaoqi.MainActivity;
import com.example.xiaoqi.R;

import java.io.File;
import java.util.ArrayList;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class DraftBoxActivity extends AppCompatActivity {
    private int columnWidth;
    private GridView gv1;
    private TextView tvTitle;
    private TextView tvText;
    private TextView t4;
    private TextView t5;
    private TextView t6;
    private TextView tvCity1;
    private Button btnRelease1;
    private ImageView ivBacktoRe;
    private MaterialProgressBar mpb1;
    private ArrayList<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draft_box);
        findViews();
        showMessage();
        //得到GridView中每个ImageView宽高
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gv1.setNumColumns(cols);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols - 1)) / cols;
        ivBacktoRe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent response = new Intent();
                setResult(520,response);
                finish();
                list.clear();
            }
        });
        btnRelease1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpb1.setVisibility(View.VISIBLE);
                Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {

                        Intent intent = new Intent();
                        intent.setClass(DraftBoxActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"发布成功",Toast.LENGTH_SHORT).show();
                    }
                };
                handler.sendEmptyMessageDelayed(1,3000);
            }
        });
    }

    private void showMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Message",MODE_PRIVATE);
        String title = sharedPreferences.getString("title","no title");
        String text = sharedPreferences.getString("text","null");
        String topic1 = sharedPreferences.getString("topic1","null");
        String topic2 = sharedPreferences.getString("topic2","null");
        String topic3 = sharedPreferences.getString("topic3","null");
        String position = sharedPreferences.getString("position","null");
        String pic1 = sharedPreferences.getString("0","***");
        String pic2 = sharedPreferences.getString("1","***");
        String pic3 = sharedPreferences.getString("2","***");
        tvTitle.setText(title);
        tvText.setText(text);
        tvCity1.setText(position);
        t4.setText(topic1);
        t5.setText(topic2);
        t6.setText(topic3);
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
        gv1.setAdapter(gridAdapter);
    }
    private void findViews() {
        gv1 = findViewById(R.id.gv1);
        tvTitle = findViewById(R.id.tv_title);
        tvText = findViewById(R.id.tv_text);
        tvCity1 = findViewById(R.id.tv_city1);
        btnRelease1 = findViewById(R.id.btn_release1);
        t4 = findViewById(R.id.t4);
        t5 = findViewById(R.id.t5);
        t6 = findViewById(R.id.t6);
        ivBacktoRe = findViewById(R.id.iv_backtore);
        mpb1 = findViewById(R.id.mpb1);
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
            Glide.with(DraftBoxActivity.this)
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

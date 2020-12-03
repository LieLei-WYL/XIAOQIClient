package com.example.xiaoqi.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private int foldFlag = -1;
    private List<String> strings = new ArrayList<>();
    private ArrayList<String> hotSearchList;
    private ImageView ivSearchBack;
    private EditText edtSearchWriteComment;
    private TextView tvSearchSearch;
    private RelativeLayout rlSearch;
    private ImageView ivSearchClear;
    private ImageView ivSearchUnfold;
    private TagFlowLayout tflSearchHistory;
    private GridView gvSearchHot;
    //布局管理器
    private LayoutInflater mInflater;
    //流式布局的子布局
    private TextView tv;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Collections.reverse(strings);
                    tflSearchHistory.setAdapter(new TagAdapter<String>(strings) {
                        @Override
                        public View getView(FlowLayout parent, int position, String s) {
                            tv = (TextView) mInflater.inflate(R.layout.tv,
                                    tflSearchHistory, false);
                            tv.setText(s);
                            return tv;
                        }
                    });
                    Collections.reverse(strings);
                    break;
            }
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ivSearchBack = findViewById(R.id.iv_search_back);
        edtSearchWriteComment = findViewById(R.id.edt_search_write_comment);
        tvSearchSearch = findViewById(R.id.tv_search_search);
        rlSearch = findViewById(R.id.rl_search);
        ivSearchUnfold = findViewById(R.id.iv_search_unfold);
        ivSearchClear = findViewById(R.id.iv_search_clear);
        tflSearchHistory = findViewById(R.id.tfl_search_history);
        gvSearchHot = findViewById(R.id.gv_search_hot);

        mInflater = LayoutInflater.from(this);
        tvSearchSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = edtSearchWriteComment.getText().toString().trim();
                strings.add(str);
                if(foldFlag == 1){
                    int height = tflSearchHistory.getHeight();
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                    linearParams.height = height+240;
                    rlSearch.setLayoutParams(linearParams);
                }
                //通知handler更新UI
                handler.sendEmptyMessageDelayed(1, 0);

                setFold();
            }
        });
        //流式布局tag的点击方法
        tflSearchHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Collections.reverse(strings);
                Toast.makeText(SearchActivity.this, strings.get(position), Toast.LENGTH_SHORT).show();
                Collections.reverse(strings);
                return true;
            }
        });

        ivSearchUnfold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(foldFlag == 0){
                    ivSearchUnfold.setImageResource(R.drawable.fold);
                    foldFlag = 1;

                    int height = tflSearchHistory.getHeight();
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                    linearParams.height = height+240;
                    rlSearch.setLayoutParams(linearParams);

                }else if(foldFlag == 1){
                    ivSearchUnfold.setImageResource(R.drawable.unfold);
                    foldFlag = 0;

                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                    linearParams.height = 450;
                    rlSearch.setLayoutParams(linearParams);

                }
            }
        });

        ivSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        ivSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strings.clear();
                handler.sendEmptyMessageDelayed(1, 0);
                ivSearchUnfold.setImageResource(R.drawable.white);
                foldFlag = -1;
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                linearParams.height = 450;
                rlSearch.setLayoutParams(linearParams);
            }
        });

        hotSearchList = new ArrayList<>();
        hotSearchList.add("国风合伙人");
        hotSearchList.add("陈情令");
        hotSearchList.add("汉尚华莲");
        hotSearchList.add("插画");
        hotSearchList.add("敦煌壁画");
        hotSearchList.add("京剧脸谱");
        hotSearchList.add("国潮");
        hotSearchList.add("云肩");
        HotSearchAdapter hotSearcheAdapter = new HotSearchAdapter(this, hotSearchList, R.layout.hotsearch_list_item);
        gvSearchHot.setAdapter(hotSearcheAdapter);

    }

    public void setFold(){
        if(tflSearchHistory.getHeight() > 230 && foldFlag == -1){
            ivSearchUnfold.setImageResource(R.drawable.unfold);
            foldFlag = 0;
        }
    }
}

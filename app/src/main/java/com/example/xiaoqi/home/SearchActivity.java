package com.example.xiaoqi.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.example.xiaoqi.me.LoginActivity;
import com.example.xiaoqi.me.MeActivity;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class SearchActivity extends AppCompatActivity {
    private int foldFlag = -1;
    private List<String> strings = new ArrayList<>();
    private ArrayList<String> hotSearchList = new ArrayList<>();
    private ImageView ivSearchBack;
    private EditText edtSearchWrite;
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
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    String[] str = result.split(":");
                    String[] strs1 = str[0].split("、");
                    for(String str1 : strs1){
                        hotSearchList.add(str1);
                    }
                    HotSearchAdapter hotSearchAdapter = new HotSearchAdapter(SearchActivity.this, hotSearchList, R.layout.hotsearch_list_item);
                    gvSearchHot.setAdapter(hotSearchAdapter);

                    if(str.length > 1){
                        String[] strs2 = str[1].split("、");
                        for(String str2 : strs2){
                            strings.add(str2);
                        }
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

                        setFold();
                    }
                    break;
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
        edtSearchWrite = findViewById(R.id.edt_search_write);
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
                String str = edtSearchWrite.getText().toString().trim();
                if(str.equals("")){
                    Toast.makeText(getApplication(),"还没有输入要搜索的关键词呢",Toast.LENGTH_SHORT).show();
                }else {
                    strings.add(str);

                    Global global = (Global) getApplication();
                    translateSearchDateToServer(strings, global.getCurrentUserPhone());

                    if (foldFlag == 1) {
                        int height = tflSearchHistory.getHeight();
                        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                        linearParams.height = height + 240;
                        rlSearch.setLayoutParams(linearParams);
                    }
                    //通知handler更新UI
                    myHandler.sendEmptyMessageDelayed(1, 0);

                    Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                    intent.putExtra("search",str);
                    startActivity(intent);
                }
            }
        });
        //流式布局tag的点击方法
        tflSearchHistory.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Collections.reverse(strings);
                Intent intent = new Intent(SearchActivity.this,SearchResultActivity.class);
                intent.putExtra("search",strings.get(position));
                Collections.reverse(strings);
                startActivity(intent);
//                Toast.makeText(SearchActivity.this, strings.get(position), Toast.LENGTH_SHORT).show();
//                Collections.reverse(strings);
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

                Global global = (Global) getApplication();
                translateSearchDateToServer(strings,global.getCurrentUserPhone());

                myHandler.sendEmptyMessageDelayed(1, 0);
                ivSearchUnfold.setImageResource(R.drawable.white);
                foldFlag = -1;
                LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) rlSearch.getLayoutParams();
                linearParams.height = 450;
                rlSearch.setLayoutParams(linearParams);
            }
        });

        Global global = (Global) getApplication();
        translateDateToServer(global.getCurrentUserPhone());
    }

    public void setFold(){
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        tflSearchHistory.measure(w, h);
        int height = tflSearchHistory.getMeasuredHeight();
        int width = tflSearchHistory.getMeasuredWidth();
        Log.e("Height",height+"");
        if(height > 230 && foldFlag == -1){
            ivSearchUnfold.setImageResource(R.drawable.unfold);
            foldFlag = 0;
        }
    }

    /**
     * 将正在登录用户信息的信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateDateToServer(final String currentUserPhone) {
        Log.i("currentUserPhone",currentUserPhone);
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/HotSearchServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    Log.i("result", "result:" + result);
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = result;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void translateSearchDateToServer(List<String> strings,String currentUserPhone) {
        String str = "";
        String finalStr;
        if(strings.size() != 0) {
            int i;
            for (i = 0; i < strings.size() - 1; i++) {
                str = str + strings.get(i) + "、";
            }
            str = str + strings.get(i);
            str = str + ":" + currentUserPhone;
            finalStr = str;
        }else{
            str = str + ":" + currentUserPhone;
            Log.i("str", str);
            finalStr = str;
        }
        Log.i("str", str);
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/SearchServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(finalStr);
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
}

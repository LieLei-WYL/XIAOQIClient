package com.example.xiaoqi.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class HistorySearchAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> historySearchs = new ArrayList<>();
    private int itemLayoutRes;

    public HistorySearchAdapter(Context mContext, ArrayList<String> historySearchs, int itemLayoutRes) {
        this.mContext = mContext;
        this.historySearchs = historySearchs;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != historySearchs){
            return historySearchs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != historySearchs){
            return historySearchs.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {//获取每个item的id的值
        return position;
    }

    @Override
    //获取每个item的视图对象
    public View getView(final int position, View convertView, ViewGroup parent) {
        //convertView每个item的视图对象
        //加载item的布局文件
        if(convertView == null) {//加载过一次就不用再次加载了（小小的优化）
            LayoutInflater inflater = LayoutInflater.from(mContext);//布局填充器
            convertView = inflater.inflate(itemLayoutRes, null);
        }

        //获取item控件的引用
        TextView tvListTitle = convertView.findViewById(R.id.tv_history_search_content);
        //设置控件内容
        tvListTitle.setText(historySearchs.get(position));
        //设置监听器
        tvListTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(mContext,NoteActivity.class);
//                intent.putExtra("search",search);
//                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

}

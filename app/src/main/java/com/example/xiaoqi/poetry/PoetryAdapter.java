package com.example.xiaoqi.poetry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Note;
import com.example.xiaoqi.home.NoteActivity;

import java.util.ArrayList;

class PoetryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Poetry> poetrys;
    private int itemLayoutRes;

    public PoetryAdapter(Context mContext, ArrayList<Poetry> poetrys, int itemLayoutRes) {
        this.mContext = mContext;
        this.poetrys = poetrys;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != poetrys){
            return poetrys.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != poetrys){
            return poetrys.get(position);
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
        RelativeLayout xq = convertView.findViewById(R.id.xiangqing);
        ImageView ivImage = convertView.findViewById(R.id.gsc_image);
        TextView tvType = convertView.findViewById(R.id.gsc_type);
        TextView tvName = convertView.findViewById(R.id.gsc_name);
        TextView tvDynasty = convertView.findViewById(R.id.gsc_dynasty);
        TextView tvAuthor = convertView.findViewById(R.id.gsc_author);
        TextView tvContent = convertView.findViewById(R.id.gsc_content);

        //设置控件内容
        ivImage.setImageResource(poetrys.get(position).getImageid());
        tvType.setText(poetrys.get(position).getType());
        tvName.setText(poetrys.get(position).getName());
        tvDynasty.setText("["+poetrys.get(position).getDynasty()+"]");
        tvAuthor.setText(poetrys.get(position).getAuthor());
        tvContent.setText(poetrys.get(position).getContent());

        //设置监听器
        xq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, Xiangqing.class);
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

}

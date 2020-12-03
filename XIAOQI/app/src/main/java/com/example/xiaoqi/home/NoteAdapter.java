package com.example.xiaoqi.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Note> notes = new ArrayList<>();
    private int itemLayoutRes;

    public NoteAdapter(Context mContext, ArrayList<Note> notes, int itemLayoutRes) {
        this.mContext = mContext;
        this.notes = notes;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != notes){
            return notes.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != notes){
            return notes.get(position);
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
        LinearLayout llListEntirety = convertView.findViewById(R.id.ll_list_entirety);
        ImageView ivListImage = convertView.findViewById(R.id.iv_list_image);
        TextView tvListTitle = convertView.findViewById(R.id.tv_list_title);
        ImageView ivListAvatar = convertView.findViewById(R.id.iv_list_avatar);
        TextView tvListName = convertView.findViewById(R.id.tv_list_name);

        //设置控件内容
        Bitmap image = BitmapFactory.decodeFile(notes.get(position).getImages()[0]);
        ivListImage.setImageBitmap(image);
        tvListTitle.setText(notes.get(position).getTitle());
        Bitmap avatar = BitmapFactory.decodeFile(notes.get(position).getAuthorAvatar());
        ivListAvatar.setImageBitmap(avatar);
        tvListName.setText(notes.get(position).getAuthorPhone());

        //设置监听器
        llListEntirety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("note",notes.get(position));
                Intent intent = new Intent();
                intent.setClass(mContext,NoteActivity.class);
                intent.putExtra("bundle",bundle);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

}

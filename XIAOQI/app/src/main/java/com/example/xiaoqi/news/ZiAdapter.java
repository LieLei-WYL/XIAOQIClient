package com.example.xiaoqi.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoqi.R;

import java.util.List;

import cn.gavinliu.android.lib.shapedimageview.ShapedImageView;

public class ZiAdapter extends BaseAdapter {
    private Context context;
    private List<Zan> zans;
    private int itemLayoutRes;
    private ShapedImageView zantouxiang;
    private TextView zanname;
    private TextView zantime;
    private TextView zanle;
    private ImageView zanimg;
    private int type;

    public ZiAdapter(Context context, List<Zan> zans, int itemLayoutRes,int type) {
        this.context = context;
        this.zans = zans;
        this.itemLayoutRes = itemLayoutRes;
        this.type=type;
    }
    /**
     * 获得数据条数
     * @return
     */
    @Override
    public int getCount() {
        if(null != zans){
            return zans.size();
        }
        return 0;
    }

    /**
     * 获取item显示的数据对象
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        if(null != zans){
            return zans.get(i);
        }
        return null;
    }

    /**
     * 获取item的id值
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    //获取每个item的视图对象
    public View getView(final int position, View convertView, ViewGroup parent) {
        //convertView每个item的视图对象
        //加载item的布局文件
        if(convertView == null) {//加载过一次就不用再次加载了（小小的优化）
            LayoutInflater inflater = LayoutInflater.from(context);//布局填充器
            convertView = inflater.inflate(itemLayoutRes, null);
        }
        //获取控件引用
        zantouxiang = convertView.findViewById(R.id.tu1);
        zanname=convertView.findViewById(R.id.tit1);
        zanle=convertView.findViewById(R.id.tit2);
        zantime=convertView.findViewById(R.id.tit3);
        Zan zan = zans.get(position);
        switch (type){
            case 1:
                zanimg=convertView.findViewById(R.id.tu2);
                BitmapDrawable bd1 =(BitmapDrawable)zan.getZanimg();
                Bitmap bitmap1=bd1.getBitmap();
                zanimg.setImageBitmap(bitmap1);
                break;
        }

        //设置控件内容
        zanname.setText(zan.getZanname());
        zanle.setText(zan.getZanle());
        zantime.setText(zan.getZantime());

        return convertView;
    }
}


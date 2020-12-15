package com.example.xiaoqi.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xiaoqi.R;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class HotSearchAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> hotSearchs = new ArrayList<>();
    private int itemLayoutRes;

    public HotSearchAdapter(Context mContext, ArrayList<String> hotSearchs, int itemLayoutRes) {
        this.mContext = mContext;
        this.hotSearchs = hotSearchs;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != hotSearchs){
            return hotSearchs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != hotSearchs){
            return hotSearchs.get(position);
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
        TextView tvHotSearchList = convertView.findViewById(R.id.tv_hotsearch_list);
        ImageView ivHotSearchListHot = convertView.findViewById(R.id.iv_hotsearch_list_hot);

        tvHotSearchList.setText(hotSearchs.get(position));
        if(position < 3){
            ivHotSearchListHot.setImageResource(R.drawable.hot);
        }

        //设置监听器
        tvHotSearchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("note",notes.get(position));
//                Intent intent = new Intent();
//                intent.setClass(mContext,InfoActivity.class);
//                intent.putExtra("bundle",bundle);
//                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

}

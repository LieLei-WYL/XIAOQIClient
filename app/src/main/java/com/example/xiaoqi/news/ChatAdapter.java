package com.example.xiaoqi.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiaoqi.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<Project> projects;
    private int itemLayoutRes;
    private TextView tvtv4;
    private TextView tvtv5;
    private TextView tvtime;
    private CircleImageView iviv2;

    public ChatAdapter(Context context, List<Project> projects, int itemLayoutRes) {
        this.context = context;
        this.projects = projects;
        this.itemLayoutRes = itemLayoutRes;
    }

    /**
     * 获得数据条数
     * @return
     */
    @Override
    public int getCount() {
        if(null != projects){
            return projects.size();
        }
        return 0;
    }

    /**
     * 获取item显示的数据对象
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        if(null != projects){
            return projects.get(position);
        }
        return null;
    }

    /**
     * 获取item的id值
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(null == view){
            //加载布局
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(itemLayoutRes,null);
        }
        //获取控件引用
        iviv2 = view.findViewById(R.id.iviv2);
        tvtv4 = view.findViewById(R.id.tvtv4);
        tvtv5 = view.findViewById(R.id.tvtv5);
        tvtime = view.findViewById(R.id.tv_time);
        //添加内容
        iviv2 .setImageDrawable(projects.get(position).getPicture());
        tvtv4.setText(projects.get(position).getName());
        tvtv5.setText(projects.get(position).getText());
        tvtime.setText(projects.get(position).getTime());
        return view;
    }
}


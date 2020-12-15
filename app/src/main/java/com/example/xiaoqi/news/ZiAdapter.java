package com.example.xiaoqi.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoqi.R;
import com.example.xiaoqi.news.Zan;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ZiAdapter extends BaseAdapter {
    private Context context;
    private List<Zan> list;
    private int itemLayoutRes;
    private ZQImageViewRoundOval touxiang;
    private TextView name;
    private TextView time;
    private ImageView img;
    private int type;

    public ZiAdapter(Context context, List<Zan> list, int itemLayoutRes, int type) {
        this.context = context;
        this.list = list;
        this.itemLayoutRes = itemLayoutRes;
        this.type = type;
    }

    /**
     * 获得数据条数
     *
     * @return
     */
    @Override
    public int getCount() {
        if (null != list) {
            return list.size();
        }
        return 0;
    }

    /**
     * 获取item显示的数据对象
     *
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
        if (null != list) {
            return list.get(i);
        }
        return null;
    }

    /**
     * 获取item的id值
     *
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //convertView每个item的视图对象
        //加载item的布局文件
        if(view == null) {//加载过一次就不用再次加载了（小小的优化）
            LayoutInflater inflater = LayoutInflater.from(context);//布局填充器
            view = inflater.inflate(itemLayoutRes, null);
        }
        switch (type) {
            case 1:
                //加载布局
//                    view = inflater.inflate(itemLayoutRes, null);
                //获取控件引用
                touxiang = view.findViewById(R.id.iv_att);
                name = view.findViewById(R.id.att_name);
                touxiang.setImageDrawable(list.get(i).getTouxiang());
                name.setText(list.get(i).getName());
                break;
            case 2:
                //加载布局
//                    view = inflater.inflate(itemLayoutRes, null);
                //获取控件引用
                touxiang = view.findViewById(R.id.iv_guan);
                name = view.findViewById(R.id.guan_name);
                time = view.findViewById(R.id.guan_time);
                touxiang.setImageDrawable(list.get(i).getTouxiang());
                name.setText(list.get(i).getName());
                time.setText(list.get(i).getTime());
                break;
            case 3:
                //加载布局
//                    view = inflater.inflate(itemLayoutRes, null);
                //获取控件引用
                touxiang = view.findViewById(R.id.iv_ping);
                name = view.findViewById(R.id.ping_name);
                time = view.findViewById(R.id.ping_time);
                img = view.findViewById(R.id.ping_iv);
                touxiang.setImageDrawable(list.get(i).getTouxiang());
                name.setText(list.get(i).getName());
                time.setText(list.get(i).getTime());
                img.setImageDrawable(list.get(i).getImg());
                break;
            case 4:
                //加载布局
//                    view = inflater.inflate(itemLayoutRes, null);
                //获取控件引用
                touxiang = view.findViewById(R.id.iv_zan);
                name = view.findViewById(R.id.zan_name);
                time = view.findViewById(R.id.zan_time);
                img = view.findViewById(R.id.zan_iv);
                touxiang.setImageDrawable(list.get(i).getTouxiang());
                name.setText(list.get(i).getName());
                time.setText(list.get(i).getTime());
                img.setImageDrawable(list.get(i).getImg());
                break;
        }

        return view;
    }
}


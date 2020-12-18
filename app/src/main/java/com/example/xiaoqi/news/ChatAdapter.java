package com.example.xiaoqi.news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.xiaoqi.R;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<DialogueMain> dialogueMains;
    private int itemLayoutRes;
    private TextView tvtv4;
    private TextView tvtv5;
    private TextView tvtime;
    private CircleImageView iviv2;

    public ChatAdapter(Context context, List<DialogueMain> dialogueMains, int itemLayoutRes) {
        this.context = context;
        this.dialogueMains = dialogueMains;
        this.itemLayoutRes = itemLayoutRes;
    }

    /**
     * 获得数据条数
     * @return
     */
    @Override
    public int getCount() {
        if(null != dialogueMains){
            return dialogueMains.size();
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
        if(null != dialogueMains){
            return dialogueMains.get(position);
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
        if(fileIsExists(dialogueMains.get(position).getAvatar())){
            Bitmap avatar = BitmapFactory.decodeFile(dialogueMains.get(position).getAvatar());
            iviv2.setImageBitmap(avatar);
        }else{
            iviv2.setImageResource(R.drawable.avatardefault1);
        }
        tvtv4.setText(dialogueMains.get(position).getName());
        tvtv5.setText(dialogueMains.get(position).getLastSentence());
        tvtime.setText(dialogueMains.get(position).getLastTime());
        return view;
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}


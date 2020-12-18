package com.example.xiaoqi.news;

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
import com.example.xiaoqi.home.NoteActivity;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogueAdapter extends BaseAdapter {
    private Context context;
    private List<Dialogue> dialogueList;
    private int itemLayoutRes;
    private String targetAvatar;
    private String currentAvatar;

    public DialogueAdapter(Context context, List<Dialogue> dialogueList, int itemLayoutRes, String targetAvatar, String currentAvatar) {
        this.context = context;
        this.dialogueList = dialogueList;
        this.itemLayoutRes = itemLayoutRes;
        this.targetAvatar = targetAvatar;
        this.currentAvatar = currentAvatar;
    }

    /**
     * 获得数据条数
     * @return
     */
    @Override
    public int getCount() {
        if(null != dialogueList){
            return dialogueList.size();
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
        if(null != dialogueList){
            return dialogueList.get(position);
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

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
        viewHolder.rightLayout = (LinearLayout) view.findViewById(R.id.right_Layout);
        viewHolder.leftMsg = (TextView) view.findViewById(R.id.left_msg);
        viewHolder.rightMsg = (TextView) view.findViewById(R.id.right_msg);
        viewHolder.leftAvatar = (ImageView) view.findViewById(R.id.left_avatar);
        viewHolder.rightAvatar = (ImageView) view.findViewById(R.id.right_avatar);
        viewHolder.leftTime = (TextView) view.findViewById(R.id.left_time);
        viewHolder.rightTime = (TextView) view.findViewById(R.id.right_time);
        view.setTag(viewHolder);

        if (dialogueList.get(position).getType() == 1) {
            //如果是收到的消息，则显示左边消息布局，将右边消息布局隐藏
            viewHolder.leftLayout.setVisibility(View.VISIBLE);
            viewHolder.rightLayout.setVisibility(View.GONE);

            if(fileIsExists(targetAvatar)){
                Bitmap avatar = BitmapFactory.decodeFile(targetAvatar);
                viewHolder.leftAvatar.setImageBitmap(avatar);
            }else{
                viewHolder.leftAvatar.setImageResource(R.drawable.avatardefault1);
            }
            viewHolder.leftMsg.setText(dialogueList.get(position).getSentence());
            viewHolder.leftTime.setText(dialogueList.get(position).getTime());
        } else if (dialogueList.get(position).getType() == 0) {
            //如果是发出去的消息，显示右边布局的消息布局，将左边的消息布局隐藏
            viewHolder.rightLayout.setVisibility(View.VISIBLE);
            viewHolder.leftLayout.setVisibility(View.GONE);

            if(fileIsExists(currentAvatar)){
                Bitmap avatar = BitmapFactory.decodeFile(currentAvatar);
                viewHolder.rightAvatar.setImageBitmap(avatar);
            }else{
                viewHolder.rightAvatar.setImageResource(R.drawable.avatardefault2);
            }
            viewHolder.rightMsg.setText(dialogueList.get(position).getSentence());
            viewHolder.rightTime.setText(dialogueList.get(position).getTime());
        }

        return view;
    }

    class ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;
        ImageView leftAvatar;
        ImageView rightAvatar;
        TextView leftTime;
        TextView rightTime;
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


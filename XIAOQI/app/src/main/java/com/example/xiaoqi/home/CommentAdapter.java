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

public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int itemLayoutRes;

    public CommentAdapter(Context mContext, ArrayList<Comment> comments, int itemLayoutRes) {
        this.mContext = mContext;
        this.comments = comments;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != comments){
            return comments.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != comments){
            return comments.get(position);
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
        ImageView ivCommentAvatar = convertView.findViewById(R.id.iv_comment_avatar);
        TextView tvCommentName = convertView.findViewById(R.id.tv_comment_name);
        TextView tvCommentContent = convertView.findViewById(R.id.tv_comment_content);
        TextView tvCommentDate = convertView.findViewById(R.id.tv_comment_date);
        LikeButton lbCommentLike = convertView.findViewById(R.id.lb_comment_like);
        TextView tvCommentLike = convertView.findViewById(R.id.tv_comment_like);

//        //设置控件内容
//        //获取图片
//        Bitmap photo = BitmapFactory.decodeFile(notes.get(position).getPhotoId());
//        //显示图片
//        ivPhoto.setImageBitmap(photo);
        ivCommentAvatar.setImageResource(R.drawable.avatar);
        tvCommentName.setText(comments.get(position).getAuthorPhone());
        tvCommentContent.setText(comments.get(position).getContent());
        tvCommentDate.setText(comments.get(position).getDate());
        lbCommentLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(mContext.getApplicationContext(),"Liked!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(mContext.getApplicationContext(),"Unliked!",Toast.LENGTH_SHORT).show();
            }
        });
        tvCommentLike.setText(comments.get(position).getLike()+"");

        //设置监听器
        ivCommentAvatar.setOnClickListener(new View.OnClickListener() {
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

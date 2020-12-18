package com.example.xiaoqi.me;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<AFList> afLists = new ArrayList<>();
    private int itemLayoutRes;

    public ListAdapter(Context mContext, ArrayList<AFList> afLists, int itemLayoutRes) {
        this.mContext = mContext;
        this.afLists = afLists;
        this.itemLayoutRes = itemLayoutRes;
    }

    @Override
    public int getCount() {//获得数据的条数
        if(null != afLists){
            return afLists.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {//获取每个item显示的数据对象
        if (null != afLists){
            return afLists.get(position);
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
        ImageView ivAFListAvatar = convertView.findViewById(R.id.iv_afList_avatar);
        TextView ivAFListName = convertView.findViewById(R.id.iv_afList_name);
        TextView ivAFListProfile = convertView.findViewById(R.id.iv_afList_profile);
        Button btnAFListFollow = convertView.findViewById(R.id.btn_afList_follow);

        if(fileIsExists(afLists.get(position).getAvatar())){
            Bitmap avatar = BitmapFactory.decodeFile(afLists.get(position).getAvatar());
            ivAFListAvatar.setImageBitmap(avatar);
        }else{
            ivAFListAvatar.setImageResource(R.drawable.defaultavatar);
        }
        ivAFListName.setText(afLists.get(position).getName());
        if(afLists.get(position).getProfile().equals("")){
            ivAFListProfile.setText("此人很懒，还没有写个人简介");
        }else{
            ivAFListProfile.setText(afLists.get(position).getProfile());
        }
        if(afLists.get(position).getFollowFlag() == 1){
            btnAFListFollow.setText("已关注");
        }

        //设置监听器
        ivAFListAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, ToMeActivity.class);
                intent.putExtra("toUserPhone",afLists.get(position).getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                mContext.startActivity(intent);
            }
        });
        btnAFListFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (afLists.get(position).getFollowFlag() == 0) {
                    afLists.get(position).setFollowFlag(1);
                    btnAFListFollow.setText("已关注");
                    Global global = (Global) mContext.getApplicationContext();
                    translateFollowChangedToServer(afLists.get(position).getFollowFlag() + ":" + global.getCurrentUserPhone() + ":" + afLists.get(position).getPhone());
                } else if (afLists.get(position).getFollowFlag() == 1) {
//                    //弹出框询问是否取关
//                    //创建Builder对象
//                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext.getApplicationContext(),R.style.Theme_AppCompat_Light_Dialog_Alert);
//                    //设置对话框属性
//                    builder.setTitle("温馨提示");//对话框标题
//                    builder.setMessage("确定不再关注？");//显示的具体提示内容
//                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
                            afLists.get(position).setFollowFlag(0);
                            btnAFListFollow.setText("关注");
                            Global global = (Global) mContext.getApplicationContext();
                            translateFollowChangedToServer(afLists.get(position).getFollowFlag() + ":" + global.getCurrentUserPhone() + ":" + afLists.get(position).getPhone());
//                        }
//                    });//设置确定按钮
//                    builder.setNegativeButton("取消", null);//设置取消按钮，点击效果默认为退出对话框
//                    //创建对话框对象
//                    AlertDialog alertDialog = builder.create();
//                    //显示对话框
//                    alertDialog.show();
//                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.colorRed));
//                    alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.colorRed));
                }
            }
        });

        return convertView;
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

    private void translateFollowChangedToServer(final String string) {
        Log.i("string", string);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) mContext.getApplicationContext();
                    URL url = new URL(global.getPath() + "/XIAOQI/FollowServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(string);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();

//                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}

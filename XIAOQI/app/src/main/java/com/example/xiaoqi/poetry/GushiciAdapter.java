package com.example.xiaoqi.poetry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xiaoqi.R;

import java.util.List;

public class GushiciAdapter extends ArrayAdapter<Gushici> {
    private int resourceId;
    public GushiciAdapter(Context context, int textViewResourceId, List<Gushici> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Gushici gushici=getItem(position);   //获取当前项的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView gushiciImage=(ImageView)view.findViewById(R.id.gsc_image);
        TextView gushiciType=(TextView) view.findViewById(R.id.gsc_type);
        TextView gushiciName=(TextView) view.findViewById(R.id.gsc_name);
        TextView gushiciDynasty=(TextView) view.findViewById(R.id.gsc_dynasty);
        TextView gushiciAuthor=(TextView) view.findViewById(R.id.gsc_author);
        TextView gushiciContent=(TextView) view.findViewById(R.id.gsc_content);
        gushiciImage.setImageResource(gushici.getImageid());
        gushiciType.setText(gushici.getType());
        gushiciName.setText(gushici.getName());
        gushiciDynasty.setText(gushici.getDynasty());
        gushiciAuthor.setText(gushici.getAuthor());
        gushiciContent.setText(gushici.getContent());
        return view;
    }
}


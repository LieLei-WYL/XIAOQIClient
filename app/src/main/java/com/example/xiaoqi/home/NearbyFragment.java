package com.example.xiaoqi.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.xiaoqi.R;
import com.example.xiaoqi.me.LoginActivity;
import com.example.xiaoqi.me.PromptActivity;

import java.util.ArrayList;

public class NearbyFragment extends Fragment {
    private ArrayList<Note> noteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Global global = (Global) getActivity().getApplication();
        if(global.getUserState() == 0){
//            Intent intent = new Intent(getContext(), PromptActivity.class);
//            startActivity(intent);

            View view = inflater.inflate(R.layout.activity_prompt1,//内容页面的布局文件
                    container,//根视图对象
                    false);
            Button button = view.findViewById(R.id.btn_toLogin);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(0,0);
                }
            });

            return view;

        }else {

            //inflater布局填充器
            //加载内容页面的布局文件（将内容页面的XML布局文件转换成View类型的对象）
            View view = inflater.inflate(R.layout.fragment_nearby,//内容页面的布局文件
                    container,//根视图对象
                    false);//false表示需要手动调用addView方法将view添加到container中,true表示自动调用addView方法
            //在哪里加载文件，就在哪里获取内容页面当中控件的引用
            GridView gvNotes = view.findViewById(R.id.gv_nearby_notes);
            //获取Activity传来的Bundle数据
            noteList = (ArrayList<Note>) getArguments().getSerializable("nearbyNoteList");
            //绑定Adapter
//        Global global = (Global) getActivity().getApplication();
            String path = global.getPath();
            NoteAdapter noteAdapter = new NoteAdapter(getContext(), noteList, R.layout.note_list_item);
            gvNotes.setAdapter(noteAdapter);

            return view;

        }
    }
}

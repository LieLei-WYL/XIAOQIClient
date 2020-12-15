package com.example.xiaoqi.me;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.xiaoqi.R;

public class AttentionListActivity extends AppCompatActivity {
    private ImageButton ibReturn;
    private ListView lvAttentionlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_list);

        ibReturn = findViewById(R.id.attentionlist_return);
        lvAttentionlist = findViewById(R.id.attentionlist);

        ibReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttentionListActivity.this.finish();
            }
        });
//        lvAttentionlist.setAdapter();
    }
}

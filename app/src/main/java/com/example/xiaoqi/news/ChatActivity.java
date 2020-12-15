package com.example.xiaoqi.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapter;
    final private List<Msg> msgList = new ArrayList<Msg>();
    private ImageView ivBacktolist;
    private Msg msg1;
    private Msg msg2;
    private int i=0;
    private String text;
    private TextView chatName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置窗口没有标题栏
        setContentView(R.layout.activity_chat);
//        initMsg();
        chatName = findViewById(R.id.chat_name);
        adapter = new MsgAdapter(ChatActivity.this, R.layout.chat_list, msgList);
        inputText = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapter);
//        if (msgList!=null){
//            msg1 = adapter.getItem(msgList.size());
//        }
        showMessage();
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if(!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.SENT);
                    msgList.add(msg);
                    msg2 = adapter.getItem(msgList.size() - 1);
                    adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
                    msgListView.setSelection(msgList.size());//将ListView定位到最后一行
                    inputText.setText("");//清空输入框的内容
                }
                i++;
            }

        });
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        final String time = df.format(new Date());
        ivBacktolist = findViewById(R.id.iv_backtolist);
        ivBacktolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ChatActivity.this, NewsActivity.class);
//                if (i>0){
//                    text = msg2.getContent();
//                }else {
//                    text = msg1.getContent();
//                }
                text = msg2.getContent();

                String name = chatName.getText().toString();
                String str = "1";
                intent.putExtra("name",name);
                intent.putExtra("text",text);
                intent.putExtra("time",time);
                intent.putExtra("str",str);
                startActivity(intent);
                Log.i("text",text);
                saveMessage();

            }
        });
    }

    private void saveMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Msg",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int j=0;j<msgList.size();++j){
            Msg msg = adapter.getItem(j);
            String m = msg.getContent();
            int type = msg.getType();
            editor.putString(i+"",m);
            editor.putString(i+":1",type+"");
        }
        editor.commit();
    }

    private void showMessage() {
        SharedPreferences sharedPreferences = getSharedPreferences("Msg",MODE_PRIVATE);
        for (int n=0;n<msgList.size();++n){
            String m = sharedPreferences.getString(n+"","");
            String type = sharedPreferences.getString(n+":1","1");
            int type1 = Integer.parseInt(type);
            Msg msg = new Msg(m,type1);
            msgList.add(msg);
        }
        adapter.notifyDataSetChanged();//有新消息时，刷新ListView中的显示
        msgListView.setSelection(msgList.size());//将ListView定位到最后一行
    }

//    private void initMsg() {
//        Msg msg1 = new Msg("I miss you!",Msg.RECEIVED);
//        msgList.add(msg1);
//
//        Msg msg2 = new Msg("I miss you,too!",Msg.SENT);
//        msgList.add(msg2);
//
//        Msg msg3 = new Msg("I will come back soon!",Msg.RECEIVED);
//        msgList.add(msg3);
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    public class Msg{

        public static final int RECEIVED = 0;//收到一条消息

        public static final int SENT = 1;//发出一条消息

        private String  content;//消息的内容

        private int type;//消息的类型

        public  Msg(String content,int type){
            this.content = content;
            this.type = type;
        }
        public String getContent(){
            return content;
        }

        public int getType(){
            return type;
        }
    }

    public class MsgAdapter extends ArrayAdapter<Msg> {
        private int resourceId;

        public MsgAdapter(Context context, int textViewresourceId, List<Msg> objects) {
            super(context, textViewresourceId, objects);
            resourceId = textViewresourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Msg msg = getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView == null){
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
                viewHolder.rightLayout = (LinearLayout)view.findViewById(R.id.right_Layout);
                viewHolder.leftMsg = (TextView)view.findViewById(R.id.left_msg);
                viewHolder.rightMsg = (TextView)view.findViewById(R.id.right_msg);
                view.setTag(viewHolder);
            }else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            if(msg.getType()== Msg.RECEIVED){
                //如果是收到的消息，则显示左边消息布局，将右边消息布局隐藏
                viewHolder.leftLayout.setVisibility(View.VISIBLE);
                viewHolder.rightLayout.setVisibility(View.GONE);
                viewHolder.leftMsg.setText(msg.getContent());
            }else if(msg.getType()== Msg.SENT){
                //如果是发出去的消息，显示右边布局的消息布局，将左边的消息布局隐藏
                viewHolder.rightLayout.setVisibility(View.VISIBLE);
                viewHolder.leftLayout.setVisibility(View.GONE);
                viewHolder.rightMsg.setText(msg.getContent());
            }
            return view;
        }

        class ViewHolder{
            LinearLayout leftLayout;
            LinearLayout rightLayout;
            TextView leftMsg;
            TextView rightMsg;
        }
    }

}

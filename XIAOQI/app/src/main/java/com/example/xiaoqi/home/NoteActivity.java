package com.example.xiaoqi.home;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.xiaoqi.R;
import com.example.xiaoqi.me.MeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    private Intent intent;
    private Note note;
    private ArrayList<Comment> commentList;

    private ImageView ivNoteBack;
    private ImageView ivNoteAvatar;
    private TextView tvNoteName;
    private ImageView ivNoteFollow;
    private SliderLayout slNotePhoto;
    private TextView tvNoteTitle;
    private TextView tvNoteContent;
    private TextView tvNoteTopic;
    private TextView tvNoteDate;
    private TextView tvNoteComment;
    private ListView lvNoteComment;
    private TextView tvNoteWriteComment;
    private LikeButton lbNoteLike;
    private TextView tvNoteLike;
    private LikeButton lbNoteCollect;
    private TextView tvNoteCollect;

    private InputTextMsgDialog inputTextMsgDialog;

    private int followFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        findViews();
        setListener();

        //获取NoteAdapter传来的Bundle数据
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        note = (Note) bundle.getSerializable("note");
        Log.e("wyl",note.toString());

        Bitmap photo = BitmapFactory.decodeFile(note.getAuthorAvatar());
        ivNoteAvatar.setImageBitmap(photo);
        tvNoteName.setText(note.getAuthorName());

        DefaultSliderView defaultSliderView = new DefaultSliderView(this);//默认的滑块视图，只能显示图像。
//        Bitmap image = BitmapFactory.decodeFile(note.getImages()[0]);
        File file = new File(note.getImages()[0]);
        defaultSliderView.image(file);
        defaultSliderView.getView();
        slNotePhoto.addSlider(defaultSliderView);
//        for(int i = 0;i<note.getImages().length;i++){
//            DefaultSliderView defaultSliderView = new DefaultSliderView(this);//默认的滑块视图，只能显示图像。
//            defaultSliderView.image(note.getImages()[i]);
////            Bitmap image = BitmapFactory.decodeFile(note.getImages()[i]);
////            ivListImage.setImageBitmap(image);
//            defaultSliderView.getView();
//            slNotePhoto.addSlider(defaultSliderView);
//        }
        //其他设置
        slNotePhoto.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        slNotePhoto.stopAutoCycle();//停止自动循环

        tvNoteTitle.setText(note.getTitle());
        tvNoteContent.setText(note.getContent());
        tvNoteTopic.setText(note.getTopic());
        tvNoteDate.setText(note.getDate());
//        tvNoteComment.setText(note.getComment()+"");

        commentList = new ArrayList<>();
        Comment comment1 = new Comment("12345","爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了","2020-11-1",4);
        Comment comment2 = new Comment("67890","大大画画好好看哦,爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了","2020-11-4",9);
        Comment comment3 = new Comment("45678","求原图,爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了爱了","2020-11-8",0);
        Comment comment4 = new Comment("12390","想要原图~","2020-11-11",1);
        commentList.add(comment1);
        commentList.add(comment2);
        commentList.add(comment3);
        commentList.add(comment4);
        //绑定Adapter
        CommentAdapter commentAdapter = new CommentAdapter(this, commentList, R.layout.comment_list_item);
        lvNoteComment.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(lvNoteComment);

//        tvNoteLike.setText(note.getLike()+"");
//        tvNoteCollect.setText(note.getCollect()+"");

        inputTextMsgDialog = new InputTextMsgDialog(NoteActivity.this, R.style.dialog_center);
        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                //点击发送按钮后，回调此方法，msg为输入的值
                Log.e("wyl",msg);
            }
        });

    }

    private void findViews() {
        ivNoteBack = findViewById(R.id.iv_note_back);
        ivNoteAvatar = findViewById(R.id.iv_note_avatar);
        tvNoteName = findViewById(R.id.tv_note_name);
        ivNoteFollow = findViewById(R.id.iv_note_follow);
        slNotePhoto = findViewById(R.id.sl_note_photo);
        tvNoteTitle = findViewById(R.id.tv_note_title);
        tvNoteContent = findViewById(R.id.tv_note_content);
        tvNoteTopic = findViewById(R.id.tv_note_topic);
        tvNoteDate = findViewById(R.id.tv_note_date);
        tvNoteComment = findViewById(R.id.tv_note_comment);
        lvNoteComment = findViewById(R.id.lv_note_comment);
        tvNoteWriteComment = findViewById(R.id.tv_note_write_comment);
        lbNoteLike = findViewById(R.id.lb_note_like);
        tvNoteLike = findViewById(R.id.tv_note_like);
        lbNoteCollect = findViewById(R.id.lb_note_collect);
        tvNoteCollect = findViewById(R.id.tv_note_collect);
    }

    private void setListener() {
        MyListener myListener = new MyListener();
        ivNoteBack.setOnClickListener(myListener);
        ivNoteAvatar.setOnClickListener(myListener);
        ivNoteFollow.setOnClickListener(myListener);
        tvNoteWriteComment.setOnClickListener(myListener);
        lbNoteLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(),"喜欢",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(),"取消喜欢",Toast.LENGTH_SHORT).show();
            }
        });
        lbNoteCollect.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(),"收藏",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Toast.makeText(getApplicationContext(),"取消收藏",Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_note_back:
                    intent = new Intent(NoteActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_note_avatar:
//                    intent = new Intent(NoteActivity.this, InfoActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_note_follow:
                    if(followFlag == 0) {
                        ivNoteFollow.setImageResource(R.drawable.followed);
                        followFlag = 1;
                    }else if(followFlag == 1){
                        //弹出框询问是否取关
                        //创建Builder对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                        //设置对话框属性
                        builder.setTitle("温馨提示");//对话框标题
                        builder.setMessage("确定不再关注？");//显示的具体提示内容
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                followFlag = 0;
                                ivNoteFollow.setImageResource(R.drawable.follow);
                            }
                        });//设置确定按钮
                        builder.setNegativeButton("取消",null);//设置取消按钮，点击效果默认为退出对话框
                        //创建对话框对象
                        AlertDialog alertDialog = builder.create();
                        //显示对话框
                        alertDialog.show();
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorRed));
                        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorRed));
                    }
                    break;
                case R.id.tv_note_write_comment:
                    inputTextMsgDialog.show();
                    break;
            }
        }
    }

    /**
     * 解决listview只显示一条以及高度显示不正确的问题
     * @param listView
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *(listAdapter.getCount()))+100;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}

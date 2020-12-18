package com.example.xiaoqi.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.xiaoqi.me.PromptActivity;
import com.example.xiaoqi.me.ToMeActivity;
import com.example.xiaoqi.news.NewsActivity;
import com.example.xiaoqi.poetry.PoetryActivity;
import com.example.xiaoqi.publish.PublishActivity;
import com.like.LikeButton;
import com.like.OnLikeListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.System.in;
import static java.lang.System.out;

public class NoteActivity extends AppCompatActivity {
    private String cAvatar;
    private String cName;

    private CommentAdapter commentAdapter;

    private Intent intent;
    private Note note;
    private ArrayList<Comment> commentList;

    private ImageView ivNoteBack;
    private ImageView ivNoteAvatar;
    private TextView tvNoteName;
    private ImageView ivNoteArea;
    private TextView tvNoteArea;
    private ImageView ivNoteFollow;
    private SliderLayout slNotePhoto;
    private TextView tvNoteTitle;
    private TextView tvNoteContent;
    private LinearLayout llNoteTopic;
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

    //定义存储数据的UserInfo对象
    private CommentInfo commentInfo;
    private NoteInfo noteInfo;

    private DefaultSliderView defaultSliderView;

    //主线程中创建Handler类的匿名的子类对象
    private Handler myHandler = new Handler() {
        @Override
        //处理Message
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 0:
                    String str = (String) msg.obj;
                    String[] strs = str.split("、");
                    int flag = Integer.parseInt(strs[0]);
                    if(flag == 1){
                        ivNoteFollow.setImageResource(R.drawable.followed);
                        followFlag = 1;
                    }
                    flag = Integer.parseInt(strs[1]);
                    if(flag == 1){
                        lbNoteLike.setLiked(true);
                    }
                    flag = Integer.parseInt(strs[2]);
                    if(flag == 1){
                        lbNoteCollect.setLiked(true);
                    }
                    tvNoteLike.setText(strs[3]);
                    tvNoteCollect.setText(strs[4]);
                    tvNoteComment.setText(strs[5]);
                    break;
                case 1:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    commentList = (ArrayList<Comment>) msg.obj;
                    if (commentList != null) {
                        Log.e("wyl", commentList.toString());
                        //绑定Adapter
                        commentAdapter = new CommentAdapter(NoteActivity.this, commentList, R.layout.comment_list_item);
                        lvNoteComment.setAdapter(commentAdapter);
                        setListViewHeightBasedOnChildren(lvNoteComment);
                    }
                    break;
                case 2:
                    String i = (String) msg.obj;
                    defaultSliderView = new DefaultSliderView(NoteActivity.this);//默认的滑块视图，只能显示图像。
                    File file = new File(i);
                    defaultSliderView.image(file);
//                    defaultSliderView.getView();
                    slNotePhoto.addSlider(defaultSliderView);
                    break;
                case 3:
                    String s = (String) msg.obj;
                    String[] strings = s.split(":");
                    int commentId = Integer.parseInt(strings[0]);
                    String message = strings[1];
                    String date = strings[2];
                    Global global = (Global) getApplication();
                    Comment comment = new Comment(commentId,note.getNoteId(),global.getCurrentUserPhone(),cAvatar,cName,message,date,0+"");
                    commentList.add(comment);
                    commentAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(lvNoteComment);
                    break;
                case 4:
                    String str4 = (String) msg.obj;
                    String[] strs4 = str4.split(":");
                    cAvatar = strs4[0];
                    cName = strs4[1];
                    break;
                case 5:
                    //下载数据完成，将下载好的数据显示在界面上
                    //获取下载完成的数据
                    noteInfo = (NoteInfo) msg.obj;
                    if (noteInfo != null){
                        //把获取的数据添加到cakeList中
                        Global global1 = (Global) getApplication();
                        global1.setAttentionNoteList(noteInfo.getNotes());
//                        noteList = noteInfo.getNotes();
                        Log.e("attentionNoteList",noteInfo.getNotes().toString());
                    }
                    break;
            }
        }
    };

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
        Log.e("wyl", note.toString());

        Bitmap photo = BitmapFactory.decodeFile(note.getAuthorAvatar());
        ivNoteAvatar.setImageBitmap(photo);
        tvNoteName.setText(note.getAuthorName());
        if(note.getArea().equals("")){
            ivNoteArea.setImageResource(R.drawable.white);
            tvNoteArea.setText(note.getArea());
        }else{
            tvNoteArea.setText(note.getArea());
        }
        ivNoteFollow.setImageResource(R.drawable.follow);

        defaultSliderView = new DefaultSliderView(NoteActivity.this);//默认的滑块视图，只能显示图像。
        File file = new File(note.getImages()[0]);
        defaultSliderView.image(file);
        defaultSliderView.getView();
        slNotePhoto.addSlider(defaultSliderView);

        //其他设置
        slNotePhoto.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        slNotePhoto.stopAutoCycle();//停止自动循环

        tvNoteTitle.setText(note.getTitle());
        tvNoteContent.setText(note.getContent());
        tvNoteTopic.setText(note.getTopic());
        tvNoteDate.setText(note.getDate());

        //传输数据
        final Global global = (Global) getApplication();
        translateDateToServer(note.getNoteId()+":"+global.getCurrentUserPhone());

        inputTextMsgDialog = new InputTextMsgDialog(NoteActivity.this, R.style.dialog_center);
        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                //点击发送按钮后，回调此方法，msg为输入的值
                Log.e("wyl", msg);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH)+1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String date = year+"-"+month+"-"+day;
                translateCommentToServer(note.getNoteId(),global.getCurrentUserPhone(),msg,date);
            }
        });

    }

    private void findViews() {
        ivNoteBack = findViewById(R.id.iv_note_back);
        ivNoteAvatar = findViewById(R.id.iv_note_avatar);
        tvNoteName = findViewById(R.id.tv_note_name);
        ivNoteArea = findViewById(R.id.iv_note_area);
        tvNoteArea = findViewById(R.id.tv_note_area);
        ivNoteFollow = findViewById(R.id.iv_note_follow);
        slNotePhoto = findViewById(R.id.sl_note_photo);
        tvNoteTitle = findViewById(R.id.tv_note_title);
        tvNoteContent = findViewById(R.id.tv_note_content);
        llNoteTopic = findViewById(R.id.ll_note_topic);
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
        llNoteTopic.setOnClickListener(myListener);
        tvNoteWriteComment.setOnClickListener(myListener);
        lbNoteLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Global global = (Global) getApplication();
                if(global.getUserState() == 0){
                    Intent intent = new Intent(NoteActivity.this, PromptActivity.class);
                    startActivity(intent);
                }else{
                    int num = Integer.parseInt(tvNoteLike.getText().toString().trim());
                    num++;
                    tvNoteLike.setText(num+"");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int num = Integer.parseInt(tvNoteLike.getText().toString().trim());
                num--;
                tvNoteLike.setText(num+"");
            }
        });
        lbNoteCollect.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Global global = (Global) getApplication();
                if(global.getUserState() == 0){
                    Intent intent = new Intent(NoteActivity.this, PromptActivity.class);
                    startActivity(intent);
                }else {
                    int num = Integer.parseInt(tvNoteCollect.getText().toString().trim());
                    num++;
                    tvNoteCollect.setText(num+"");
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                int num = Integer.parseInt(tvNoteCollect.getText().toString().trim());
                num--;
                tvNoteCollect.setText(num+"");
            }
        });
    }

    class MyListener implements View.OnClickListener {
        Global global = (Global) getApplication();

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_note_back:
                    NoteActivity.this.finish();
                    break;
                case R.id.iv_note_avatar:
                    intent = new Intent(NoteActivity.this, ToMeActivity.class);
                    intent.putExtra("toUserPhone",note.getAuthorPhone());
                    startActivity(intent);
                    overridePendingTransition(0,0);
                    break;
                case R.id.iv_note_follow:
//                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
                        Intent intent = new Intent(NoteActivity.this, PromptActivity.class);
                        startActivity(intent);
                    }else {
                        if (followFlag == 0) {
                            followFlag = 1;
                            ivNoteFollow.setImageResource(R.drawable.followed);
                            translateFollowChangedToServer(followFlag + ":" + global.getCurrentUserPhone() + ":" + note.getAuthorPhone());
                            Log.e("follow",followFlag+"");
                            translateAttentionDateToServer(global.getCurrentUserPhone());
                        } else if (followFlag == 1) {
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
                                    Global global = (Global) getApplication();
                                    translateFollowChangedToServer(followFlag + ":" + global.getCurrentUserPhone() + ":" + note.getAuthorPhone());
                                    Log.e("follow",followFlag+"");
                                    translateAttentionDateToServer(global.getCurrentUserPhone());
                                }
                            });//设置确定按钮
                            builder.setNegativeButton("取消", null);//设置取消按钮，点击效果默认为退出对话框
                            //创建对话框对象
                            AlertDialog alertDialog = builder.create();
                            //显示对话框
                            alertDialog.show();
                            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorRed));
                            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorRed));
                        }
                    }
                    break;
                case R.id.ll_note_topic:
                    Intent intent = new Intent(NoteActivity.this,SearchResultActivity.class);
                    intent.putExtra("search",tvNoteTopic.getText().toString().trim());
                    startActivity(intent);
                    break;
                case R.id.tv_note_write_comment:
//                    Global global = (Global) getApplication();
                    if(global.getUserState() == 0){
                        Intent intent1 = new Intent(NoteActivity.this, PromptActivity.class);
                        startActivity(intent1);
                    }else {
                        inputTextMsgDialog.show();
                    }
                    break;
            }
        }
    }

    /**
     * 将当前用户手机号信息字符串传输给服务端
     * @param
     * @return
     */
    private void translateAttentionDateToServer(final String currentUserPhone) {
        Log.i("attention",currentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread(){
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/AttentionServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(currentUserPhone);
                    writer.flush();

                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();
                    in.close();
                    Log.e("attentionResult",result);

                    //先将json串解析成外部NoteInfo对象
                    //创建NoteInfo对象和Note集合对象
                    NoteInfo noteInfo = new NoteInfo();
                    ArrayList<Note> notes = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jNotes = new JSONObject(result);
                    JSONArray jArray = jNotes.getJSONArray("notes");
                    //遍历JSONArray对象，解析其中的每个元素（Note）(解析内部Note集合)
                    for(int i = 0;i < jArray.length();i++){
                        Note note = new Note();
                        //获取当前的JsonObject对象
                        JSONObject jNote = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        int noteId = jNote.optInt("note_id");
                        String phone = jNote.optString("phone");
                        String avatar = jNote.optString("avatar");
                        String name = jNote.optString("name");
                        String images = jNote.optString("images");
                        String title = jNote.optString("title");
                        String content = jNote.optString("content");
                        String topic = jNote.optString("topic");
                        String date = jNote.optString("date");
                        String area = jNote.optString("area");
                        //给Note对象赋值
                        note.setNoteId(noteId);
                        note.setAuthorPhone(phone);
                        note.setAuthorAvatar(avatar);
                        note.setAuthorName(name);
                        note.setImages(images.split("、"));
                        note.setTitle(title);
                        note.setContent(content);
                        note.setTopic(topic);
                        note.setDate(date);
                        note.setArea(area);
                        //把当前的cake对象添加到集合中
                        notes.add(note);
                    }
                    //给NoteInfo对象赋值
                    noteInfo.setNotes(notes);
                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 5;
                    msg.obj = noteInfo;
                    //发送Message对象
                    myHandler.sendMessage(msg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 解决listview只显示一条以及高度显示不正确的问题
     *
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount())) + 100;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 将当前笔记的id值字符串传输给服务端，请求获得该笔记的所有图片信息和点赞数、收藏数、评论数，以及评论集合
     *
     * @param noteIdAndCurrentUserPhone
     */
    private void translateDateToServer(final String noteIdAndCurrentUserPhone) {
        Log.i("wyl", noteIdAndCurrentUserPhone);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/NoteServlet");
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    //获取输入流和输出流
                    OutputStream out = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
                    writer.write(noteIdAndCurrentUserPhone);
                    writer.flush();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
                    String result = reader.readLine();
                    reader.close();
                    out.close();

                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void applyDateToServer() {
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    //从服务端下载所有蛋糕信息，并通过Message发布出去
                    //1、通过网络请求下载数据(图片要下载到本地，还要修改图片地址为本地地址)
                    //创建URL对象
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/NoteServlet");
                    //通过URL对象获取网络输入流
                    InputStream in = url.openStream();
                    //读数据（Json串）循环读写方式
                    byte[] bytes = new byte[512];
                    StringBuffer buffer = new StringBuffer();
                    int len = -1;
                    while ((len = in.read(bytes, 0, bytes.length)) != -1) {
                        buffer.append(new String(bytes, 0, len));
                    }
//                    String reslut = buffer.toString();
//                    String reslut = URLDecoder.decode(buffer.toString(), "GBK");
                    String reslut = new String(buffer.toString().getBytes(), "utf-8");
                    Log.e("wyl", reslut);
                    in.close();
                    //先将json串解析成外部CakeInfo对象
                    //创建CakeInfo对象和Cake集合对象
                    CommentInfo commentInfo = new CommentInfo();
                    ArrayList<Comment> commentList = new ArrayList<>();
                    //创建外层JsonObject对象
                    JSONObject jCommentList = new JSONObject(reslut);
                    JSONObject jAvatar = jCommentList.getJSONObject("avatar");
                    JSONObject jName = jCommentList.getJSONObject("name");
                    JSONObject jImages = jCommentList.getJSONObject("images");
                    JSONObject jFollowFlag = jCommentList.getJSONObject("followFlag");
                    JSONObject jLikeFlag = jCommentList.getJSONObject("likeFlag");
                    JSONObject jCollectionFlag = jCommentList.getJSONObject("collectionFlag");
                    JSONObject jLikes = jCommentList.getJSONObject("likes");
                    JSONObject jCollections = jCommentList.getJSONObject("collections");
                    JSONObject jComments = jCommentList.getJSONObject("comments");
                    String currentAvatar = jAvatar.optString("avatar");
                    String currentName = jName.optString("name");
                    String image = jImages.optString("images");
                    int followFlag = jFollowFlag.optInt("followFlag");
                    int likeFlag = jLikeFlag.optInt("likeFlag");
                    int collectionFlag = jCollectionFlag.optInt("collectionFlag");
                    String[] images = image.split("、");
                    String likes = jLikes.optString("likes");
                    String collections = jCollections.optString("collections");
                    String comments = jComments.optString("comments");

                    //获取Message对象
                    Message msg = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg.what = 0;
                    msg.obj = followFlag+"、"+likeFlag+"、"+collectionFlag+"、"+likes+"、"+collections+"、"+comments;
                    //发送Message对象
                    myHandler.sendMessage(msg);

                    //拼接服务端地址
                    String netPhotoId1 = global.getPath() + currentAvatar;
                    //通过网络请求下载
                    URL imgUrl1 = new URL(netPhotoId1);
                    //获取网络输入流
                    InputStream imgIn1 = imgUrl1.openStream();
                    //获取本地file目录
                    String files1 = getFilesDir().getAbsolutePath();
                    String imgs1 = files1 + "/imgs";
                    //判断imgs目录是否存在
                    File dirImgs1 = new File(imgs1);
                    if (!dirImgs1.exists()) {
                        //如果目录不存在，则创建
                        dirImgs1.mkdir();
                    }
                    //获取图片的名称（不包含服务端路径的图片名称）
                    String[] strs1 = currentAvatar.split("/");
                    String imgName1 = strs1[strs1.length - 1];
                    String imgPath1 = imgs1 + "/" + imgName1;
                    Log.e("wyl", "拼接头像名称：" + imgPath1);
                    //修改cake对象的图片地址（修改cake的图片属性为本地图片地址）
                    currentAvatar = imgPath1;

                    //判断图片是否已经存在
                    if(fileIsExists(imgPath1)){

                    }else {
                        //获取本地文件输出流
                        OutputStream out1 = new FileOutputStream(imgPath1);
                        //循环读写
                        int b1 = -1;
                        while ((b1 = imgIn1.read()) != -1) {
                            out1.write(b1);
                            out1.flush();
                        }
                        //关闭流
                        out1.close();
                    }

                    Message msg4 = myHandler.obtainMessage();
                    //设置Message对象的属性（what，obj）
                    msg4.what = 4;
                    msg4.obj = currentAvatar+":"+currentName;
                    //发送Message对象
                    myHandler.sendMessage(msg4);

                    JSONArray jArray = jCommentList.getJSONArray("commentList");
                    //遍历JSONArray对象，解析其中的每个元素（Cake）(解析内部Cake集合)
                    for (int i = 0; i < jArray.length(); i++) {
                        Comment comment = new Comment();
                        //获取当前的JsonObject对象
                        JSONObject jComment = jArray.getJSONObject(i);
                        //获取当前元素中的属性
                        String commentId = jComment.optString("comment_id");
                        String noteId = jComment.optString("note_id");
                        String phone = jComment.optString("phone");
                        String avatar = jComment.optString("avatar");
                        String name = jComment.optString("name");
                        String content = jComment.optString("content");
                        String date = jComment.optString("date");
                        String commentLikes = jComment.optString("likes");
                        //给Cake对象赋值
                        comment.setCommentId(Integer.parseInt(commentId));
                        comment.setNoteId(Integer.parseInt(noteId));
                        comment.setAuthorPhone(phone);

                        //拼接服务端地址
                        String netPhotoId = global.getPath() + avatar;
                        //通过网络请求下载
                        URL imgUrl = new URL(netPhotoId);
                        //获取网络输入流
                        InputStream imgIn = imgUrl.openStream();
                        //获取本地file目录
                        String files = getFilesDir().getAbsolutePath();
                        String imgs = files + "/imgs";
                        //判断imgs目录是否存在
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            //如果目录不存在，则创建
                            dirImgs.mkdir();
                        }
                        //获取图片的名称（不包含服务端路径的图片名称）
                        String[] strs = avatar.split("/");
                        String imgName = strs[strs.length - 1];
                        String imgPath = imgs + "/" + imgName;
                        Log.e("wyl", "拼接头像名称：" + imgPath);
                        //修改cake对象的图片地址（修改cake的图片属性为本地图片地址）
                        avatar = imgPath;

                        //判断图片是否已经存在
                        if(fileIsExists(avatar)){

                        }else {
                            //获取本地文件输出流
                            OutputStream out = new FileOutputStream(imgPath);
                            //循环读写
                            int b = -1;
                            while ((b = imgIn.read()) != -1) {
                                out.write(b);
                                out.flush();
                            }
                            //关闭流
                            out.close();
                        }

                        comment.setAuthorAvatar(avatar);
                        comment.setAuthorName(name);
                        comment.setContent(content);
                        comment.setDate(date);
                        comment.setLikes(commentLikes);
                        //把当前的cake对象添加到集合中
                        commentList.add(comment);
                        //2、通过发送Message对象将数据发布出去
                        //获取Message对象
                        Message msg1 = myHandler.obtainMessage();
                        //设置Message对象的属性（what，obj）
                        msg1.what = 1;
                        msg1.obj = commentList;
                        //发送Message对象
                        myHandler.sendMessage(msg1);
                    }

                    //通过网络下载images的图片，保存到本地
                    //拼接图片的服务端资源路径，进行下载
                    for (int j = 1; j < images.length; j++) {
                        Log.e("wyl", images[j]);
                        //拼接服务端地址
                        String netPhotoId = global.getPath() + images[j];
                        //通过网络请求下载
                        URL imgUrl = new URL(netPhotoId);
                        //获取网络输入流
                        InputStream imgIn = imgUrl.openStream();
                        //获取本地file目录
                        String files = getFilesDir().getAbsolutePath();
                        String imgs = files + "/imgs";
                        //判断imgs目录是否存在
                        File dirImgs = new File(imgs);
                        if (!dirImgs.exists()) {
                            //如果目录不存在，则创建
                            dirImgs.mkdir();
                        }
                        //获取图片的名称（不包含服务端路径的图片名称）
                        String[] strs = images[j].split("/");
                        String imgName = strs[strs.length - 1];
                        String imgPath = imgs + "/" + imgName;
                        Log.e("wyl", "拼接头像名称：" + imgPath);
                        //修改cake对象的图片地址（修改cake的图片属性为本地图片地址）
                        images[j] = imgPath;

                        //判断图片是否已经存在
                        if(fileIsExists(imgPath)){

                        }else {
                            //获取本地文件输出流
                            OutputStream out = new FileOutputStream(imgPath);
                            //循环读写
                            int b = -1;
                            while ((b = imgIn.read()) != -1) {
                                out.write(b);
                                out.flush();
                            }
                            //关闭流
                            out.close();
                        }

                        //获取Message对象
                        Message msg2 = myHandler.obtainMessage();
                        //设置Message对象的属性（what，obj）
                        msg2.what = 2;
                        msg2.obj = images[j];
                        //发送Message对象
                        myHandler.sendMessage(msg2);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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
                    Global global = (Global) getApplication();
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

    private void translateCommentToServer(final int noteId,final String currentUserPhone,final String msg,final String date) {
        final String string = noteId+":"+currentUserPhone+":"+msg+":"+date;
        Log.i("string", string);
        final Intent intent = new Intent();
        //创建线程传输数据
        new Thread() {
            @Override
            public void run() {
                //进行网络请求
                try {
                    Global global = (Global) getApplication();
                    URL url = new URL(global.getPath() + "/XIAOQI/AddCommentServlet");
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
                    Log.i("result", "result:" + result);

                    String str = result+":"+msg+":"+date;
                    Message message = new Message();
                    message.what = 3;//区分不同的消息本身
                    message.obj = str;//发送的数据本身
                    myHandler.sendMessage(message);//发送消息，进入主线程的消息队列
//                    applyDateToServer();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

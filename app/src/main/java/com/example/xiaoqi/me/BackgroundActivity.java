package com.example.xiaoqi.me;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xiaoqi.R;
import com.example.xiaoqi.home.Global;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

public class BackgroundActivity extends AppCompatActivity {
    private ImageView ivBacktoperson1;
    private ImageView ivBackground;
    private Button btnBackgroundPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        //接收从MeActivity传递的图片
        Intent intent = getIntent();
        String backgroundStr = intent.getStringExtra("background");

        ivBackground = findViewById(R.id.iv_portraitimg);
        if (fileIsExists(backgroundStr)) {
            Bitmap avatar = BitmapFactory.decodeFile(backgroundStr);
            ivBackground.setImageBitmap(avatar);
        } else {
            ivBackground.setImageResource(R.drawable.cloud);
        }
        ivBacktoperson1 = findViewById(R.id.iv_backtoperson1);
        ivBacktoperson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.setClass(BackgroundActivity.this,MeActivity.class);
            }
        });
        btnBackgroundPortrait = findViewById(R.id.btn_background_portrait);
        btnBackgroundPortrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出PopupWindow
                showPopupWindow();
            }
        });
    }

    public boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (f.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void showPopupWindow() {
        //创建PopupWindpw对象
        final PopupWindow popupWindow = new PopupWindow(this);
        //设置弹出窗口的宽度
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置他的视图
        View view = getLayoutInflater().inflate(R.layout.popupwindow, null);
        //设置视图当中控件的属性和监听器
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnTakePictures = view.findViewById(R.id.pop_take_pictures);
        Button btnPhotoAlbum = view.findViewById(R.id.pop_photo_album);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭弹出框
                popupWindow.dismiss();
            }
        });
        btnTakePictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                /**
                 * 我们使用Intent的方式打开系统相机
                 * 1.直接跳转相机包名，前提是你知道这个应用的包名
                 * 2.就是使用隐式Intent了，在这里我们就使用隐式intent
                 */
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 指定拍照
                // 拍照返回图片
                startActivityForResult(intent, 102);
            }
        });
        btnPhotoAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                // 相册选取
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 103);
            }
        });
        popupWindow.setContentView(view);
        //显示PopupWindow(必须指定显示的位置)
        LinearLayout root = findViewById(R.id.root);
        popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
//        //指定弹出窗口在某个控件的下方
//        popupWindow.showAsDropDown(btnPortrait);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 判断是否返回值
        if (resultCode == RESULT_OK) {
//            // 判断返回值是否正确
//            if (requestCode == CODE) {
//                // 获取图片
//                Bundle bundle = data.getExtras();
//                // 转换图片的二进制流
//                Bitmap bitmap = (Bitmap) bundle.get("data");
//                // 设置图片
//                ivPortraitimg.setImageBitmap(bitmap);
//                // 保存图片
//                saveImage(bitmap);
//            }
            switch(requestCode){
                case 102:
                    // 获取图片
                    Bundle bundle = data.getExtras();
                    // 转换图片的二进制流
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    // 设置图片
                    ivBackground.setImageBitmap(bitmap);
                    // 保存图片
                    try {
                        saveImage(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case 103:
                    Bitmap bm = null;
                    // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                    ContentResolver resolver = getContentResolver();

                    try {
                        Uri originalUri = data.getData(); // 获得图片的uri

                        bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
                        saveImage(bm);

                        // 这里开始的第二部分，获取图片的路径：

                        String[] proj = { MediaStore.Images.Media.DATA };

                        // 好像是android多媒体数据库的封装接口，具体的看Android文档
                        @SuppressWarnings("deprecation")
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        // 按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        // 最后根据索引值获取图片路径
                        String path = cursor.getString(column_index);
                        ivBackground.setImageURI(originalUri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }


    public File saveImage(Bitmap bmp) throws MalformedURLException {
        //获取本地file目录
        String files1 = getFilesDir().getAbsolutePath();
        String imgs1 = files1 + "/imgs";
        //判断imgs目录是否存在
        File dirImgs1 = new File(imgs1);
        if (!dirImgs1.exists()) {
            //如果目录不存在，则创建
            dirImgs1.mkdir();
        }
        Global global = (Global) getApplication();
        String fileName = global.getCurrentUserPhone() + "bg.jpg";
        File file = new File(dirImgs1, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = imgs1+"/"+fileName;
        File file1 = new File(path); //这里的path就是那个地址的全局变量
        new Thread(){
            @Override
            public void run() {
                String result = UploadUtil.uploadFile(file1, global.getPath() + "/XIAOQI/UploadShipServlet");
                Log.e("UploadShipServlet",result);
            }
        }.start();

        return dirImgs1;
    }
}

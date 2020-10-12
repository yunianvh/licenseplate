package com.cdjysd.licenseplatedemo;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdjysd.licenseplatedemo.adapter.NumAdapter;
import com.cdjysd.licenseplatedemo.utils.ImgUtil;
import com.cdjysd.licenseplatelib.utils.LPalte;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private final int LICESECODE = 666;

    private TextView result, nva1, nva2, post;
    private GridView numGv;
    private ImageView imageView;

    private Uri imageUri;//拍照时使用
    private File tempFile;
    private String base64Img;//图片数据

    List<String> list = new ArrayList<>();
    private int listIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.tv_info);
        nva1 = findViewById(R.id.tv_nva_1);
        nva2 = findViewById(R.id.tv_nva_2);
        post = findViewById(R.id.tv_post);
        post.setOnClickListener(click);
        nva2.setOnClickListener(click);
        nva1.setOnClickListener(click);
        imageView = findViewById(R.id.img_car);
        imageView.setOnClickListener(click);
        numGv = findViewById(R.id.gv_num);
        numGv.setOnItemClickListener(this);
        for (int i = 0; i < 50; i++) {
            list.add("A" + (i + 1));
        }
        NumAdapter numAdapter = new NumAdapter(this, list);
        numGv.setAdapter(numAdapter);

        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        } else {
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

//        if (!(checkSelfPermission(Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED)) {
//            lackedPermission.add(Manifest.permission.VIBRATE);
//        }

        // 权限都已经有了，那么直接
        if (lackedPermission.size() == 0) {

        } else {
            // 请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限，如果获得权限就可以调用SDK，否则不要调用SDK。
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {

        } else {
            Toast.makeText(this, "没有权限请开启", Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_nva_1:
                    list.clear();
                    for (int i = 0; i < 50; i++) {
                        list.add("A" + (i + 1));
                    }
                    NumAdapter numAdapter = new NumAdapter(MainActivity.this, list);
                    numGv.setAdapter(numAdapter);
                    break;
                case R.id.tv_nva_2:
                    list.clear();
                    for (int i = 50; i < 100; i++) {
                        list.add("A" + (i + 1));
                    }
                    NumAdapter numAdapter2 = new NumAdapter(MainActivity.this, list);
                    numGv.setAdapter(numAdapter2);
                    break;

                case R.id.img_car:
                    openCamera();
                    break;

                case R.id.tv_post:
                    Toast.makeText(MainActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        listIndex = i;
        LPalte.openScanPlate(MainActivity.this, LICESECODE);
    }


    /**
     * 打开相机
     */
    public void openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else {
            //系統版本
            int currentapiVersion = Build.VERSION.SDK_INT;
            // 激活相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 判断存储卡是否可以用，可用进行存储
            if (ImgUtil.hasSdcard()) {
                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String filename = timeStampFormat.format(new Date());
                tempFile = new File(Environment.getExternalStorageDirectory(), filename + ".jpg");
                if (currentapiVersion < 24) {
                    // 从文件中创建uri
                    imageUri = Uri.fromFile(tempFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                } else {
                    //兼容android7.0 使用共享文件的形式
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                    //检查是否有存储权限，以免崩溃
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //申请WRITE_EXTERNAL_STORAGE权限
                        Toast.makeText(this, "请开启存储权限", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                }
            }
            // 开启一个带有返回值的Activity，请求码为1
            startActivityForResult(intent, 1);
        }
    }


    /**
     * 打开图库
     */
    private void openPhones() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //识别后的回调
        if (resultCode == RESULT_OK && requestCode == LICESECODE && data != null) {
            String hphm = data.getCharSequenceExtra("number").toString();
            String hpzl = data.getCharSequenceExtra("hpzl").toString();
            String color = data.getCharSequenceExtra("color").toString();

            result.setText("车位号：" + list.get(listIndex) + "\n车牌号：" + hphm + "\n号牌种类：" + hpzl + "\n号牌颜色：" + color + "\n开始时间：2020-10-12 11:09" + "\n当前时间：2020-10-12 17:01" + "\n离开时间：使用中");
        }

        //监听打图片相关
        switch (requestCode) {
            //图库
            case 0:
                switch (resultCode) {
                    case RESULT_OK:
                        Uri uri = data.getData();
                        String img_src = uri.getPath();//这是本机的图片路径
                        Log.i(TAG, "onActivityResult: " + img_src);

                        ContentResolver cr = MainActivity.this.getContentResolver();
                        try {
                            Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                            /* 将Bitmap设定到ImageView */
                            imageView.setImageBitmap(bitmap);
//                            base64Img = ImgUtil.bitmapToBase64(bitmap);
                        } catch (FileNotFoundException e) {
                            Log.e("Exception", e.getMessage(), e);
                        }
                        break;
                }
                break;

            //相机
            case 1:
                Log.i(TAG, "onActivityResult: 拍照进来了");
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 2); // 启动裁剪程序
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
//                        base64Img = ImgUtil.bitmapToBase64(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

}

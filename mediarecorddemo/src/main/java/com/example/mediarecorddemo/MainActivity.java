package com.example.mediarecorddemo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {


    private Button goCameraBtn;

    private ImageView showCameraIv;

    private static final int CAMERA_CODE = 1;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // check Android 6 permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i("TEST","Granted");
            //init(barcodeScannerView, getIntent(), null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);//1 can be another integer
        }
        setContentView(R.layout.activity_main);
        goCameraBtn = (Button) this.findViewById(R.id.id_go_camera_btn);
        goCameraBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                processGoCamera();
            }
        });
        showCameraIv = (ImageView) this.findViewById(R.id.id_show_camera_iv);
        showCameraIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                processShowCamera(v);
            }
        });
    }

    /**
     * 45.
     * 处理进入camera事件
     * 46.
     */
    private void processGoCamera() {
        Intent intent = new Intent();
        intent.setClass(this, CameraActivity.class);
        startActivityForResult(intent, CAMERA_CODE);
    }

    /**
     * 54.
     * 处理图片跳转进入预览界面
     * 55.
     */
    private void processShowCamera(View v) {

        Intent intent = new Intent();
        intent.setClass(this, PreviewActivity.class);
/**
 60.
 * 将图片url传给PreviewActivity
 61.
 */
        intent.putExtra("cameraUrl", v.getContentDescription().toString());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            if (CAMERA_CODE == requestCode) {
/**
 73.
 * 获取activity返回的url
 74.
 */
                Uri uri = data.getData();
                String url = uri.toString().substring(uri.toString().indexOf("///") + 2);
                if (url != null && !TextUtils.isEmpty(url)) {
                    showCameraIv.setContentDescription(url);
                    showCameraIv.setImageBitmap(HelpUtil.getBitmapByUrl(url));
                }
            }
        }

    }

}
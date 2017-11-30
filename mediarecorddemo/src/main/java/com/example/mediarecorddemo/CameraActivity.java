package com.example.mediarecorddemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class CameraActivity extends Activity implements OnClickListener,
        SurfaceHolder.Callback {

    private static final String TAG = CameraActivity.class.getSimpleName();
    private static final int MEDIA_TYPE_IMAGE = 1;
    private Button switchCameraBtn, captureBtn;
    private SurfaceView surfaceSv;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    // 0表示后置，1表示前置
    private int cameraPosition = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// 不显示标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);
        findById();
        initData();
    }

    /**
     * 055.
     * 初始化view
     * 056.
     */
    private void findById() {
        switchCameraBtn = (Button) this.findViewById(R.id.id_switch_camera_btn);
        captureBtn = (Button) this.findViewById(R.id.id_capture_btn);
        surfaceSv = (SurfaceView) this.findViewById(R.id.id_area_sv);

        switchCameraBtn.setOnClickListener(this);
        captureBtn.setOnClickListener(this);
    }

    /**
     * 067.
     * 初始化相关data
     * 068.
     */
    private void initData() {
// 获得句柄
        mHolder = surfaceSv.getHolder();
// 添加回调
        mHolder.addCallback(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (this.checkCameraHardware(this) && (mCamera == null)) {
// 打开camera
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
    }

    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
// Camera is not available (in use or does not exist)
            camera = null;
            Log.e(TAG, "Camera is not available (in use or does not exist)");
        }
        return camera;
    }

    @Override
    public void onPause() {
        super.onPause();
/**
 104.
 * 记得释放camera，方便其他应用调用
 105.
 */
        releaseCamera();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 115.
     * 释放mCamera
     * 116.
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();// 停掉原来摄像头的预览
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_switch_camera_btn:
                // 切换前后摄像头
                int cameraCount = 0;
                CameraInfo cameraInfo = new CameraInfo();
                cameraCount = Camera.getNumberOfCameras();// 得到摄像头的个数

                for (int i = 0; i < cameraCount; i++) {
                    Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
                    if (cameraPosition == 1) {
                        // 现在是后置，变更为前置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                            /**
                             141.
                             * 记得释放camera，方便其他应用调用
                             142.
                             */
                            releaseCamera();
                            // 打开当前选中的摄像头
                            mCamera = Camera.open(i);
                            // 通过surfaceview显示取景画面
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 0;
                            break;
                        }
                    } else {
                        // 现在是前置， 变更为后置
                        if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            /**
                             155.
                             * 记得释放camera，方便其他应用调用
                             156.
                             */
                            releaseCamera();
                            mCamera = Camera.open(i);
                            setStartPreview(mCamera, mHolder);
                            cameraPosition = 1;
                            break;
                        }
                    }

                }
                break;
            case R.id.id_capture_btn:
// 拍照,设置相关参数
                Camera.Parameters params = mCamera.getParameters();

                params.setPictureFormat(ImageFormat.JPEG);

                params.setPreviewSize(800, 400);

// 自动对焦

                params.setFocusMode(Parameters.FOCUS_MODE_AUTO);

                mCamera.setParameters(params);

                mCamera.takePicture(null, null, picture);

                break;

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
// If your preview can change or rotate, take care of those events here.
// Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
// preview surface does not exist
            return;
        }

// stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
// ignore: tried to stop a non-existent preview
        }

// set preview size and make any resize, rotate or
// reformatting changes here

// start preview with new settings
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
// 当surfaceview关闭时，关闭预览并释放资源
/**
 215.
 * 记得释放camera，方便其他应用调用
 216.
 */
        //TODO
        Toast.makeText(this, "surfaceDestroyed", Toast.LENGTH_SHORT).show();
        releaseCamera();
        holder = null;
        surfaceSv = null;
    }

    /**
     * 223.
     * 创建png图片回调数据对象
     * 224.
     */
    PictureCallback picture = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (pictureFile == null) {
                Log.d(TAG,
                        "Error creating media file, check storage permissions: ");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                returnResult(pictureFile);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    /**
     * 248.
     * Create a File for saving an image or video
     * 249.
     */
    private static File getOutputMediaFile(int type) {
// To be safe, you should check that the SDCard is mounted
// using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
// This location works best if you want the created images to be shared
// between applications and persist after your app has been uninstalled.

// Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

// Create a media file name
        String timeStamp = HelpUtil.getDateFormatString(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".png");
        } else {
            return null;
        }
        return mediaFile;
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
// this device has a camera
            return true;
        } else {
// no camera on this device
            return false;
        }
    }

    /**
     * 294.
     * activity返回式返回拍照图片路径
     * 295.
     *
     * @param mediaFile 296.
     */
    private void returnResult(File mediaFile) {
        Intent intent = new Intent();
        intent.setData(Uri.fromFile(mediaFile));
        this.setResult(RESULT_OK, intent);
        this.finish();
    }

    /**
     * 305.
     * 设置camera显示取景画面,并预览
     * 306.
     *
     * @param camera 307.
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}
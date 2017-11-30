package com.example.mediarecorddemo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HelpUtil {
    /**
     * 18.
     * 根据图片路径获取本地图片的Bitmap
     * 19.
     * <p>
     * 20.
     *
     * @param url 21.
     * @return 22.
     */
    public static Bitmap getBitmapByUrl(String url) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            fis = new FileInputStream(url);
            bitmap = BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            bitmap = null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
                fis = null;
            }
        }

        return bitmap;
    }

    /**
     * 50.
     * bitmap旋转90度
     * 51.
     * <p>
     * 52.
     *
     * @param bitmap 53.
     * @return 54.
     */
    public static Bitmap createRotateBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            try {
                m.setRotate(90, bitmap.getWidth() / 2, bitmap.getHeight() / 2);// 90就是我们需要选择的90度
                Bitmap bmp2 = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), m, true);
                bitmap.recycle();
                bitmap = bmp2;
            } catch (Exception ex) {
                System.out.print("创建图片失败！" + ex);
            }
        }
        return bitmap;
    }

    public static Bitmap getBitmapByUri(Uri uri, ContentResolver cr) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(cr
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 85.
     * 获取格式化日期字符串
     * 86.
     *
     * @param date 87.
     * @return 88.
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDateFormatString(Date date) {
        if (date == null)
            date = new Date();
        String formatStr = new String();
        SimpleDateFormat matter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        formatStr = matter.format(date);
        return formatStr;
    }
}
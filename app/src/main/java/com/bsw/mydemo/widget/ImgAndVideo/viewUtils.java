package com.bsw.mydemo.widget.ImgAndVideo;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Created by jinzifu on 15/11/19.
 */
public class viewUtils {

    private static long mLastClickTime = 0;

    /**
     *   * 读取图片属性：旋转的角度
     *  * @param path 图片绝对路径
     *  * @return degree旋转的角度
     *  
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (null != bitmap) {
            bitmap.recycle();
        }
        return bmp;
    }

    /**
     * 防止重复点击
     *为false 说明点击太快
     * @return
     */
    public static boolean singleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
            return false;
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }
    public static boolean singleClickforshort() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 300)
            return false;
        mLastClickTime = SystemClock.elapsedRealtime();
        return true;
    }

    /**
     * 点击缩略图显示大图
     *
     * @param context
     * @param imgView
     */
    public static void showBigImage(Context context, ImageView imgView) {
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setView(imgView);
        dialog.show();

        // 全屏显示的方法
//        final Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        ImageView imgView = getView();
//        dialog.setContentView(imgView);
//        dialog.show();

        // 点击图片消失
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 获取本地图片的 ImageView格式
     *
     * @param context
     * @return
     */
    public static ImageView getView(Context context, int id) {
        ImageView imgView = new ImageView(context);
        imgView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setImageResource(id);
        return imgView;

    }
}

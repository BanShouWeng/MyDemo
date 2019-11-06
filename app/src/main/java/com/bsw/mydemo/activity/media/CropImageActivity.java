package com.bsw.mydemo.activity.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.BitmapUtil;
import com.bsw.mydemo.utils.ViewUtils;
import com.bsw.mydemo.widget.cropImange.ClipLayout;

/*
 * 截取图片界面
 */
public class CropImageActivity extends BaseActivity {

    private ClipLayout mClipImageLayout;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("截取图片");

        // 设置要裁切的图片资源
        int degree = ViewUtils.readPictureDegree(path);
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);

            byte[] bytes = BitmapUtil.decodeBitmap(path);
            if (Const.notEmpty(bytes)) {

                Bitmap mbitmap = BitmapFactory.decodeByteArray(
                        bytes, 0,
                        bytes.length);
                mClipImageLayout.setImageBitmap(ViewUtils.rotateBitmap(mbitmap, degree));
            } else {
                toast(R.string.crop_picture_error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast(R.string.crop_picture_error);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:// 取消，返回到上一级
                setResult(RESULT_CANCELED);
                break;
            case R.id.ok:// 确定，裁切并上传'
                //progressDialog.show();
                String path = mClipImageLayout.clip();
                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_crop_homepage;
    }

    @Override
    protected void findViews() {
        mClipImageLayout = getView(R.id.id_clipImageLayout);
    }

    @Override
    protected void formatViews() {
        setClickView(R.id.cancel, R.id.ok);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        path = bundle.getString("bitmap");
    }
}

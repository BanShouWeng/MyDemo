package com.bsw.mydemo.activity.media;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.ImgAndVideo.BitmapUtil;
import com.bsw.mydemo.widget.ImgAndVideo.crophomepage.ClipHomepageLayout;
import com.bsw.mydemo.widget.ImgAndVideo.viewUtils;

/*
 * 截取图片界面
 */
public class CropDiaryHomepageActivity extends BaseActivity {

    private ClipHomepageLayout mClipImageLayout;
    private TextView mTvCancel, mTvSelect;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("截取图片");
        // 实例化控件
        mClipImageLayout = (ClipHomepageLayout) findViewById(R.id.id_clipImageLayout);
        mTvCancel = (TextView) findViewById(R.id.cancel);
        mTvSelect = (TextView) findViewById(R.id.ok);
        // 设置按钮监听
        mTvCancel.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
        // 设置要裁切的图片资源
        int degree = viewUtils.readPictureDegree(path);
        try {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);

            Bitmap mbitmap = BitmapFactory.decodeByteArray(
                    BitmapUtil.decodeBitmap(path), 0,
                    BitmapUtil.decodeBitmap(path).length);
            mClipImageLayout.setImageBitmap(viewUtils.rotateBitmap(mbitmap, degree));
        } catch (Exception e) {
            e.printStackTrace();
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

    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {
        path = bundle.getString("bitmap");
    }
}

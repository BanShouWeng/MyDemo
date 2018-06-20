package com.bsw.mydemo.activity.media;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.Utils.BitmapUtil;

import java.io.File;
import java.io.IOException;

public class PicCropActivity extends BaseActivity {

    private ImageView picCrop;
    private String mPhotoPath;
    private final int IMG_CODE = 0x50;
    private final int PHOTO_CODE = 0x51;
    private final int PHOTO_CROP = 0x52;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.media_activity_btn_crop_pic);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pic_crop;
    }

    @Override
    protected void findViews() {
        picCrop = getView(R.id.pic_crop);
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.pic_crop, R.id.get_img, R.id.get_video);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pic_crop:
//                jumpTo(CropImageActivity.class, 505);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mPhotoPath = Environment.getExternalStorageDirectory() + "/"
                        + System.currentTimeMillis() + ".jpg";
               /* String mPhotoPath = Environment.getExternalStorageDirectory() + "/picture"
                        + "/" + System.currentTimeMillis() + ".jpg";*/

                try {
                    File mPhotoFile = new File(mPhotoPath);
                    if (! mPhotoFile.getParentFile().exists()) {
                        mPhotoFile.getParentFile().mkdirs();
                    }

                    if (! mPhotoFile.exists()) {
                        mPhotoFile.createNewFile();
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(mPhotoFile));
                    startActivityForResult(intent, PHOTO_CODE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.get_img:
                Intent in = new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in, IMG_CODE);
                break;

            case R.id.get_video:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMG_CODE:
                    Bundle bundleIC = new Bundle();
                    Uri uri = data.getData();
                    mPhotoPath = BitmapUtil.uri2StringPath(context, uri);
                    bundleIC.putString("bitmap", mPhotoPath);
                    jumpTo(CropImageActivity.class, PHOTO_CROP, bundleIC);
                    break;

                case PHOTO_CODE:
                    Bundle bundlePC = new Bundle();
                    bundlePC.putString("bitmap", mPhotoPath);
                    jumpTo(CropImageActivity.class, PHOTO_CROP, bundlePC);
                    break;

                case PHOTO_CROP:
                    mPhotoPath = data.getStringExtra("path");
                    byte[] bytes = BitmapUtil.decodeBitmap(mPhotoPath);
                    if (Const.notEmpty(bytes)) {
                        picCrop.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                    break;
            }
        }
    }
}

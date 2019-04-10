package com.bsw.mydemo.activity.view.gif;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bumptech.glide.Glide;

public class GifActivity extends BaseActivity {

    private ImageView gifIv;
    private ImageView svgIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_gif);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gif;
    }

    @Override
    protected void findViews() {
        gifIv = getView(R.id.gif_iv);
        svgIV = getView(R.id.svg);
    }

    @Override
    protected void formatViews() {
        svgIV.setImageResource(R.drawable.ic_fire);
        Glide.with(this).load(R.drawable.gif).into(gifIv);
        setOnClickListener(R.id.jumpPhotoViewGif);
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
            case R.id.jumpPhotoViewGif:
                jumpTo(PhotoViewAndGifActivity.class);
                break;
        }
    }
}

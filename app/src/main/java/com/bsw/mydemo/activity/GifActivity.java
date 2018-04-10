package com.bsw.mydemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bumptech.glide.Glide;

public class GifActivity extends BaseActivity {

    private ImageView gifIv;

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
    }

    @Override
    protected void formatViews() {
        Glide.with(this).load(R.drawable.gif).into(gifIv);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}

package com.bsw.mydemo.activity.view.pic;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.GlideUtils;

public class GlideActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_glide;
    }

    @Override
    protected void findViews() {
        String path = "https://43.243.138.182/static/images/upload/2019-08-27/e77af3e0-5bcc-4759-bea5-ef27ed1cbb27.png";
//        String path = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
        GlideUtils.loadImageView(this, path, (ImageView) getView(R.id.pic_iv));
    }

    @Override
    protected void formatViews() {

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

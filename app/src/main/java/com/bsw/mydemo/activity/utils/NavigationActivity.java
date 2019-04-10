package com.bsw.mydemo.activity.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.PackageUtils;
import com.bsw.mydemo.utils.TxtUtils;
import com.bsw.mydemo.base.BaseActivity;
/**
 * @author 半寿翁
 */
public class NavigationActivity extends BaseActivity {

    private EditText lng, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void findViews() {
        lat = getView(R.id.navigation_lat);
        lng = getView(R.id.navigation_lng);
    }

    @Override
    protected void formatViews() {
        setOnClickListener(R.id.navigation_baidu, R.id.navigation_gaode);
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
            case R.id.navigation_baidu:
                PackageUtils.jumpTo(context, PackageUtils.BAIDU_MAP, TxtUtils.getText(lng), TxtUtils.getText(lat));
                break;

            case R.id.navigation_gaode:
                PackageUtils.jumpTo(context, PackageUtils.GAODE_MAP, TxtUtils.getText(lng), TxtUtils.getText(lat));
                break;
        }
    }
}

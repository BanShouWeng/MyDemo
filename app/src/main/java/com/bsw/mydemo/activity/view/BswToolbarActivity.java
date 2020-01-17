package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.bswtoolbar.BswToolbar;
import com.bsw.mydemo.widget.bswtoolbar.OnToolbarBtnClickListener;

/**
 * 自定义Toolbar测试
 *
 * @author 半寿翁
 * @date 2019-12-31
 */
public class BswToolbarActivity extends BaseActivity {
    private BswToolbar bswToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bsw_toolbar;
    }

    @Override
    protected void findViews() {
        bswToolbar = getView(R.id.bsw_toolbar);
    }

    @Override
    protected void formatViews() {
        bswToolbar.setOnToolbarBtnClickListener(new OnToolbarBtnClickListener() {
            @Override
            public void onBackClick() {
                toast("点击返回了");
            }

            @Override
            public void onCloseClick() {
                toast("点击关闭了");
            }
        });
        bswToolbar.inflateMenu(R.menu.menu_main);
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

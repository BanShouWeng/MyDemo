package com.bsw.mydemo.activity.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.bean.JumpBean;
import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;

import java.util.Arrays;

/**
 * 工具Activity
 *
 * @author leiming
 * @date 2020-6-5
 */
public class UtilsActivity extends BaseActivity {
    private JumpBean[] jumpBeanList = {new JumpBean(R.string.main_activity_btn_file_download, FileDownloadActivity.class)
            , new JumpBean(R.string.main_activity_btn_bluetooth, BluetoothActivity.class)
            , new JumpBean(R.string.main_activity_btn_gps, GpsActivity.class)
            , new JumpBean(R.string.main_activity_btn_mobile, MobileActivity.class)
            , new JumpBean(R.string.main_activity_btn_navigation, NavigationActivity.class)
            , new JumpBean(R.string.main_activity_btn_time_zone, TimeActivity.class)
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_utils_layout;
    }

    @Override
    protected void findViews() {
        BswRecyclerView<JumpBean> bswRecyclerView = findViewById(R.id.utils_rv);
        bswRecyclerView.initAdapter(R.layout.item_main_layout, convertViewCallBack)
                .setDecoration()
                .setLayoutManager();
        bswRecyclerView.setData(Arrays.asList(jumpBeanList));
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

    private ConvertViewCallBack<JumpBean> convertViewCallBack = new ConvertViewCallBack<JumpBean>() {
        @Override
        public void convert(RecyclerViewHolder holder, final JumpBean jumpBean, int position, int layoutId) {
            holder.setText(R.id.jumpTo, jumpBean.getBtnTextId())
                    .setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(UtilsActivity.this, jumpBean.getJumpToClass()));
                        }
                    });
        }
    };
}

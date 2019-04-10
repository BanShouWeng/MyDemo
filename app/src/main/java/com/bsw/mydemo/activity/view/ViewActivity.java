package com.bsw.mydemo.activity.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;
import com.bsw.mydemo.R;
import com.bsw.mydemo.activity.WebViewActivity;
import com.bsw.mydemo.activity.utils.MobileActivity;
import com.bsw.mydemo.activity.view.gif.GifActivity;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.bean.JumpBean;

import java.util.Arrays;

public class ViewActivity extends BaseActivity {

    private BswRecyclerView<JumpBean> viewRv;

    private JumpBean[] jumpBeanList = {new JumpBean(R.string.main_activity_btn_mobile, MobileActivity.class)
            , new JumpBean(R.string.main_activity_btn_toolbar, ToolbarActivity.class)
            , new JumpBean(R.string.main_activity_btn_qrcode, ScanCodeActivity.class)
            , new JumpBean(R.string.main_activity_btn_gesture_lock, GestureLockActivity.class)
            , new JumpBean(R.string.main_activity_btn_gif, GifActivity.class)
            , new JumpBean(R.string.main_activity_btn_webview, WebViewActivity.class)
            , new JumpBean(R.string.main_activity_btn_BswRecyclerView, BswRecycleViewActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_view);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view;
    }

    @Override
    protected void findViews() {
        viewRv = getView(R.id.view_rv);
    }

    @Override
    protected void formatViews() {
        viewRv.initAdapter(R.layout.item_main_layout, convertViewCallBack)
                .setDecoration()
                .setLayoutManager();
        viewRv.setData(Arrays.asList(jumpBeanList));
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
        public JumpBean convert(RecyclerViewHolder holder, final JumpBean jumpBean, int position, int layoutId) {
            holder.setText(R.id.jumpTo, jumpBean.getBtnTextId())
                    .setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ViewActivity.this, jumpBean.getJumpToClass()));
                        }
                    });
            return jumpBean;
        }
    };
}

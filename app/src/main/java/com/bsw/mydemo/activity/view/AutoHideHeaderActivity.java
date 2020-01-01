package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.AutoHideHeadScrollView;
import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AutoHideHeaderActivity extends BaseActivity {
    private AutoHideHeadScrollView autoHideHeadSv;
    private BswRecyclerView<String> autoHideBodyRv;
    private TextView autoHideHeadTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_auto_hide_header;
    }

    @Override
    protected void findViews() {
        autoHideHeadSv = getView(R.id.auto_hide_head_sv);
    }

    @Override
    protected void formatViews() {
        autoHideHeadSv.initView(R.layout.item_ahhsv_header_layout
                , R.layout.item_ahhsv_body_layout
                , onScrollViewMoveListener);

        autoHideHeadTv = autoHideHeadSv.findChildById(R.id.auto_hide_head_tv);
        autoHideHeadTv.setTag(0);

        autoHideBodyRv = autoHideHeadSv.findChildById(R.id.auto_hide_body_rv);
        autoHideBodyRv.initAdapter(R.layout.item_main_layout, convertViewCallBack)
                .setLayoutManager()
                .setDecoration()
                .setData(getData(1));
    }

    private List<String> getData(int size) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            data.add(String.format(Locale.getDefault(), "我是第%d个小可爱", i));
        }
        return data;
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

    private ConvertViewCallBack<String> convertViewCallBack = new ConvertViewCallBack<String>() {
        @Override
        public void convert(RecyclerViewHolder holder, String s, int position, int layoutTag) {
            holder.setText(R.id.jumpTo, s);
        }
    };

    private AutoHideHeadScrollView.OnScrollViewMoveListener onScrollViewMoveListener = new AutoHideHeadScrollView.OnScrollViewMoveListener() {
        @Override
        public void scrollUp() {

        }

        @Override
        public void scrollDown() {

        }

        @Override
        public void scrollToTop() {

        }

        @Override
        public void scrollToBottom() {

        }

        @Override
        public void headHiddenChanged(boolean isHidden) {
            if (isHidden) {
                int times = (int) autoHideHeadTv.getTag();
                times++;
                autoHideHeadTv.setText(String.format(Locale.getDefault(), "我已经隐藏了%d次了", times));
            }
        }
    };
}

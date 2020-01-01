package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.StringFormatUtils;

import java.util.Map;

public class TextBulkFormatActivity extends BaseActivity {
    private SparseArray<TextView> textViewArray = new SparseArray<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_text_bulk_format;
    }

    @Override
    protected void findViews() {
        setTitle(R.string.main_activity_btn_bulk_format);

        textViewArray.append(R.id.bulk_format_1, (TextView) findViewById(R.id.bulk_format_1));
        textViewArray.append(R.id.bulk_format_2, (TextView) findViewById(R.id.bulk_format_2));
        textViewArray.append(R.id.bulk_format_3, (TextView) findViewById(R.id.bulk_format_3));
        textViewArray.append(R.id.bulk_format_4, (TextView) findViewById(R.id.bulk_format_4));
        textViewArray.append(R.id.bulk_format_5, (TextView) findViewById(R.id.bulk_format_5));
    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {
        String[] strings = {"我", "我的", "我的详", "我的详情", "我的详情页"};
//        String[] strings = {"aaaaaaaaaaa", "我的就时间就撒旦法收到撒旦法阿萨德", "使用ToolBar的setTitle()方法设置标题时", "我的手机号码：12345678901", "i'm joker"};
        Map<String, String> map = StringFormatUtils.formatStringLength(StringFormatUtils.LENGTH_FORMAT_RIGHT, false, strings);
        textViewArray.get(R.id.bulk_format_1).setText(map.get(strings[0]));
        textViewArray.get(R.id.bulk_format_2).setText(map.get(strings[1]));
        textViewArray.get(R.id.bulk_format_3).setText(map.get(strings[2]));
        textViewArray.get(R.id.bulk_format_4).setText(map.get(strings[3]));
        textViewArray.get(R.id.bulk_format_5).setText(map.get(strings[4]));
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}

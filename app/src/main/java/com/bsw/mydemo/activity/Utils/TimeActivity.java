package com.bsw.mydemo.activity.Utils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.DateFormatUtils;
import com.bsw.mydemo.base.BaseActivity;

import java.sql.Date;
import java.util.Locale;

public class TimeActivity extends BaseActivity {

    private TextView tvLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_time_zone);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_time;
    }

    @Override
    protected void findViews() {
        tvLocale = getView(R.id.tv_locale);

    }

    @Override
    protected void formatViews() {
//        Locale locale = context.getResources().getConfiguration().locale;
        Locale locale = Locale.getDefault();
        long exchangeTime = 1536544635000L;
        tvLocale.setText(String.format("language = %s country = %s locale time = %s china time = %s", locale.getLanguage(), locale.getCountry(), DateFormatUtils.format(exchangeTime, DateFormatUtils.NORMAL, locale), DateFormatUtils.formatCh(exchangeTime, DateFormatUtils.NORMAL)));
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

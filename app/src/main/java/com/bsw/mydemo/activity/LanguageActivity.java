package com.bsw.mydemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;

import java.util.Locale;

import static com.bsw.mydemo.R.string.main_activity_btn_language;
/**
 * @author 半寿翁
 */
public class LanguageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(main_activity_btn_language);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    protected void findViews() {
        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        ((TextView) getView(R.id.language_tv)).setText(language);
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
    public void onClick(View view) {

    }
}

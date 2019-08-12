package com.bsw.mydemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.base.BaseBean;
import com.bsw.mydemo.bean.UserAccountBean;
import com.bsw.mydemo.utils.TxtUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonInputActivity extends BaseActivity {

    private EditText jsonEt;
    private TextView showJson;

    private UserAccountBean userAccountBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gson_input;
    }

    @Override
    protected void findViews() {
        setTitle(R.string.main_activity_btn_gson_input);
        setBaseRightText(R.string.confirm);

        jsonEt = findViewById(R.id.json_et);
        showJson = findViewById(R.id.show_json);
    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {
        jsonEt.setText(new Gson().toJson(new UserAccountBean("晓明", "123456", false)));
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        try {
            userAccountBean = new Gson().fromJson(TxtUtils.getText(jsonEt), UserAccountBean.class);
            showJson.setText("用户名：".concat(userAccountBean.getAccount()).concat("\n")
                    .concat("密码：").concat(userAccountBean.getPassword()).concat("\n")
                    .concat("记住密码：").concat(userAccountBean.isRememberPW() ? "是" : "否").concat("\n"));
        } catch (JsonSyntaxException e) {
            showJson.setText("");
            toast("json 格式不正确");
        }
    }
}

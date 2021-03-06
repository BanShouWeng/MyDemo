package com.bsw.mydemo.activity.utils;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.IpAddressUtils;

public class MobileActivity extends BaseActivity {

    private TextView mobileContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("手机参数");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mobile;
    }

    @Override
    protected void findViews() {
        mobileContent = getView(R.id.mobile_content);
    }

    @Override
    protected void formatViews() {
//        OSUtils.ROM_TYPE osLevel = OSUtils.getRomType();
        String phoneInfo = "Product: " + android.os.Build.PRODUCT + "\n";
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI+ "\n";
        phoneInfo += ", TAGS: " + android.os.Build.TAGS+ "\n";
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE+ "\n";
        phoneInfo += ", MODEL: " + android.os.Build.MODEL+ "\n";
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK+ "\n";
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE+ "\n";
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE+ "\n";
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY+ "\n";
        phoneInfo += ", BRAND: " + android.os.Build.BRAND+ "\n";
        phoneInfo += ", BOARD: " + android.os.Build.BOARD+ "\n";
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT+ "\n";
        phoneInfo += ", ID: " + android.os.Build.ID+ "\n";
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER+ "\n";
        phoneInfo += ", USER: " + android.os.Build.USER;
        phoneInfo += ", IP: " + IpAddressUtils.getIpAddress(context);
//        mobileContent.setText("设备品牌:".concat(Build.BRAND).concat("\n").
//                concat("设备型号:").concat(Build.MODEL).concat("\n").
//                concat("设备版本号:").concat(Build.ID).concat("\n").
//                concat("系统版本:").concat(Build.VERSION.RELEASE).concat("\n").
//                concat("SDK版本:").concat(String.valueOf(Build.VERSION.SDK_INT)).concat("\n").
//                concat("定制版本"));
        mobileContent.setText(phoneInfo);
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

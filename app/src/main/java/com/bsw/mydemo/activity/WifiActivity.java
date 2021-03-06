package com.bsw.mydemo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.utils.StringFormatUtils;
import com.bsw.mydemo.utils.WifiAdminUtils;

public class WifiActivity extends BaseActivity {

    private EditText wifiSsid, wifiPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi;
    }

    @Override
    protected void findViews() {
        wifiSsid = getView(R.id.wifi_ssid);
        wifiPassword = getView(R.id.wifi_password);
    }

    @Override
    protected void formatViews() {
        setClickView(R.id.wifi_changed);

        WifiAdminUtils adminUtils = new WifiAdminUtils(context);
        if (adminUtils.isWifiConnect(this))
            wifiSsid.setText(adminUtils.getSSID());
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
//        WifiConnectUtils.WifiCipherType type = null;
//        if (scanResult.capabilities.toUpperCase().contains("WPA")) {
//            type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WPA;
//        } else if (scanResult.capabilities.toUpperCase()
//                .contains("WEP")) {
//            type = WifiConnectUtils.WifiCipherType.WIFICIPHER_WEP;
//        } else {
//            type = WifiConnectUtils.WifiCipherType.WIFICIPHER_NOPASS;
//        }
        // 去连接网络
        WifiAdminUtils mWifiAdmin = new WifiAdminUtils(context);
        /* 是否去连接了 */
//        if (WifiConnDialog.this != null) {
//            dismiss();
//        }
        boolean isConnect = mWifiAdmin.connect(StringFormatUtils.getString(wifiSsid), StringFormatUtils.getString(wifiPassword), WifiAdminUtils.WifiCipherType.WIFICIPHER_WPA);
        Log.d("WifiListActivity", isConnect + "是否去连接的值");
        if (isConnect) {
            Log.d("WifiListActivity", "去连接wifi了");
//            onNetworkChangeListener.onNetWorkConnect();
        } else {
            Log.d("WifiListActivity", "没有去连接wifi");
//            onNetworkChangeListener.onNetWorkConnect();
        }
    }
}

package com.bsw.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.activity.BluetoothActivity;
import com.bsw.mydemo.activity.DbActivity;
import com.bsw.mydemo.activity.LinkmanActivity;
import com.bsw.mydemo.activity.NavigationActivity;
import com.bsw.mydemo.activity.RTMPActivity;
import com.bsw.mydemo.activity.ShakeAndFlashActivity;
import com.bsw.mydemo.activity.VideoActivity;
import com.bsw.mydemo.activity.nfc.NFCActivity;
import com.bsw.mydemo.zxing.activity.CaptureActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.error);
        String error = App.getInstance().getSharedPreferencesInstance().getString("error", "");
        if (!TextUtils.isEmpty(error)) {
            textView.setText(error);
        }
        findViewById(R.id.jumpNFC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NFCActivity.class));
            }
        });

        findViewById(R.id.jumpVideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, VideoActivity.class));
            }
        });

        findViewById(R.id.jumpRtmp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RTMPActivity.class));
            }
        });

        findViewById(R.id.jumpBluetooth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BluetoothActivity.class));
            }
        });

        findViewById(R.id.jumpNavigation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NavigationActivity.class));
            }
        });

        findViewById(R.id.jumpDb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DbActivity.class));
            }
        });

        findViewById(R.id.jumpLinkman).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LinkmanActivity.class));
            }
        });

        findViewById(R.id.jumpShakeAndFlash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShakeAndFlashActivity.class));
            }
        });

        findViewById(R.id.jumpQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CaptureActivity.class));
            }
        });

        PermissionUtils.setRequestPermissions(this, new PermissionUtils.PermissionGrant() {
            @Override
            public Integer[] onPermissionGranted() {
                return new Integer[]{PermissionUtils.CODE_ALL_PERMISSION};
            }

            @Override
            public void onRequestResult(List<String> deniedPermission) {
                for (int i = 0; i < deniedPermission.size(); i++) {
                    Logger.i("deniedPermission", deniedPermission.get(i));
                }
            }
        });
    }
}

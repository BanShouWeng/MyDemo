package com.bsw.mydemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.activity.BluetoothActivity;
import com.bsw.mydemo.activity.DbActivity;
import com.bsw.mydemo.activity.GestureLockActivity;
import com.bsw.mydemo.activity.GifActivity;
import com.bsw.mydemo.activity.LanguageActivity;
import com.bsw.mydemo.activity.LinkmanActivity;
import com.bsw.mydemo.activity.NavigationActivity;
import com.bsw.mydemo.activity.RTMPActivity;
import com.bsw.mydemo.activity.ScanCodeActivity;
import com.bsw.mydemo.activity.media.MediaActivity;
import com.bsw.mydemo.activity.media.ShakeAndFlashActivity;
import com.bsw.mydemo.activity.VideoActivity;
import com.bsw.mydemo.activity.WifiActivity;
import com.bsw.mydemo.activity.nfc.NFCActivity;

import java.util.List;

/**
 * @author 半寿翁
 * @date 2017/11/1
 */
public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
                Toast.makeText(getApplicationContext(), "name = " + name + " age = " + age, Toast.LENGTH_LONG).show();
            }
        }

        textView = findViewById(R.id.error);
        String error = App.getInstance().getSharedPreferencesInstance().getString("error", "");
        if (! TextUtils.isEmpty(error)) {
            textView.setText(error);
        }
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "afsdfsfas", Toast.LENGTH_LONG).show();
                    }
                }).start();
            }
        });

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

        findViewById(R.id.jumpMedia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MediaActivity.class));
            }
        });

        findViewById(R.id.jumpQrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ScanCodeActivity.class));
            }
        });

        findViewById(R.id.jumpLanguage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LanguageActivity.class));
            }
        });

        findViewById(R.id.jumpGestureLock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GestureLockActivity.class));
            }
        });

        findViewById(R.id.jumpWifi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WifiActivity.class));
            }
        });

        findViewById(R.id.jumpGif).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GifActivity.class));
            }
        });

        PermissionUtils.setRequestPermissions(this, new PermissionUtils.PermissionGrant() {
            @Override
            public Integer[] onPermissionGranted() {
                return new Integer[] {PermissionUtils.CODE_ALL_PERMISSION};
            }

            @Override
            public void onRequestResult(List<String> deniedPermission) {
                for (int i = 0; i < deniedPermission.size(); i++) {
                    Logger.i("deniedPermission", deniedPermission.get(i));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

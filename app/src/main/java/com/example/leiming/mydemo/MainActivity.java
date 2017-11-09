package com.example.leiming.mydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.leiming.mydemo.activity.BluetoothActivity;
import com.example.leiming.mydemo.activity.NavigationActivity;
import com.example.leiming.mydemo.activity.RTMPActivity;
import com.example.leiming.mydemo.activity.VideoActivity;
import com.example.leiming.mydemo.activity.nfc.NFCActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;

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
    }
}

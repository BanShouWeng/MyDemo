package com.bsw.mydemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bsw.mydemo.Utils.JumpToUtils;
import com.bsw.mydemo.Utils.KeepAlive.KeepAliveActivity;
import com.bsw.mydemo.Utils.KeepAlive.ScreenBroadcastListener;
import com.bsw.mydemo.Utils.KeepAlive.ScreenManager;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.PermissionUtils;
import com.bsw.mydemo.activity.BluetoothActivity;
import com.bsw.mydemo.activity.DbActivity;
import com.bsw.mydemo.activity.GestureLockActivity;
import com.bsw.mydemo.activity.OSActivity;
import com.bsw.mydemo.activity.WebViewActivity;
import com.bsw.mydemo.activity.gif.GifActivity;
import com.bsw.mydemo.activity.LanguageActivity;
import com.bsw.mydemo.activity.LinkmanActivity;
import com.bsw.mydemo.activity.NavigationActivity;
import com.bsw.mydemo.activity.RTMPActivity;
import com.bsw.mydemo.activity.ScanCodeActivity;
import com.bsw.mydemo.activity.media.MediaActivity;
import com.bsw.mydemo.activity.VideoActivity;
import com.bsw.mydemo.activity.WifiActivity;
import com.bsw.mydemo.activity.nfc.NFCActivity;
import com.bsw.mydemo.activity.view.ToolbarActivity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author 半寿翁
 * @date 2017/11/1
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i_getvalue = getIntent();
        String action = i_getvalue.getAction();

        final ScreenManager screenManager = ScreenManager.getInstance(MainActivity.this);
        ScreenBroadcastListener listener = new ScreenBroadcastListener(this);
        listener.registerListener(new ScreenBroadcastListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Logger.e(getName(KeepAliveActivity.class), "开启了");
                screenManager.finishActivity();
            }

            @Override
            public void onScreenOff() {
                Logger.e(getName(KeepAliveActivity.class), "关闭了");
                screenManager.startActivity();
            }
        });

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
                Toast.makeText(getApplicationContext(), "name = " + name + " age = " + age, Toast.LENGTH_LONG).show();
            }
        }

        TextView textView = findViewById(R.id.error);
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

        findViewById(R.id.jumpOS).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OSActivity.class));
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

        findViewById(R.id.jumpWebView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });

        findViewById(R.id.jumpView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ToolbarActivity.class));
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

        Observable.just("On", "Off", "On", "On")
                //在传递过程中对事件进行过滤操作
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        return s != null;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                            @Override
                            public void onError(Throwable e) {
                                //出现错误会调用这个方法
                            }

                            @Override
                            public void onComplete() {
                                //被观察者的onCompleted()事件会走到这里;
                                Log.d("DDDDDD", "结束观察...\n");
                            }

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Boolean s) {
                                //处理传过来的onNext事件
                                Log.d("DDDDD", "handle this---" + s);
                            }
                        }
                );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected String getName(Class clazz) {
        return clazz.getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    JumpToUtils.jumpTo(getApplicationContext(), KeepAliveActivity.class);
                }
            }
        }).start();
    }
}

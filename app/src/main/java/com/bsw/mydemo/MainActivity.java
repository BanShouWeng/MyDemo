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

import com.bsw.mydemo.activity.DbActivity;
import com.bsw.mydemo.activity.GsonInputActivity;
import com.bsw.mydemo.activity.LanguageActivity;
import com.bsw.mydemo.activity.LinkmanActivity;
import com.bsw.mydemo.activity.RTMPActivity;
import com.bsw.mydemo.activity.SettingPageJumpActivity;
import com.bsw.mydemo.activity.VideoActivity;
import com.bsw.mydemo.activity.WebSocketActivity;
import com.bsw.mydemo.activity.WifiActivity;
import com.bsw.mydemo.activity.media.MediaActivity;
import com.bsw.mydemo.activity.nfc.NFCActivity;
import com.bsw.mydemo.activity.utils.BluetoothActivity;
import com.bsw.mydemo.activity.utils.FileDownloadActivity;
import com.bsw.mydemo.activity.utils.GpsActivity;
import com.bsw.mydemo.activity.utils.MobileActivity;
import com.bsw.mydemo.activity.utils.NavigationActivity;
import com.bsw.mydemo.activity.utils.TimeActivity;
import com.bsw.mydemo.activity.view.ViewActivity;
import com.bsw.mydemo.bean.JumpBean;
import com.bsw.mydemo.utils.JumpToUtils;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.PermissionUtils;
import com.bsw.mydemo.utils.keepAlive.KeepAliveActivity;
import com.bsw.mydemo.utils.keepAlive.ScreenBroadcastListener;
import com.bsw.mydemo.utils.keepAlive.ScreenManager;
import com.bsw.mydemo.utils.rxbus2.RxBus;
import com.bsw.mydemo.utils.rxbus2.Subscribe;
import com.bsw.mydemo.utils.rxbus2.ThreadMode;
import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import me.iwf.photopicker.PhotoPickerActivity;

/**
 * @author 半寿翁
 * @date 2017/11/1
 */
public class MainActivity extends AppCompatActivity {
    private JumpBean[] jumpBeanList = {new JumpBean(R.string.main_activity_btn_file_download, FileDownloadActivity.class)
            , new JumpBean(R.string.main_activity_btn_setting, SettingPageJumpActivity.class)
            , new JumpBean(R.string.main_activity_btn_websocket, WebSocketActivity.class)
            , new JumpBean(R.string.main_activity_btn_mobile, MobileActivity.class)
            , new JumpBean(R.string.main_activity_btn_view, ViewActivity.class)
            , new JumpBean(R.string.main_activity_btn_NFC, NFCActivity.class)
            , new JumpBean(R.string.main_activity_btn_video_play, VideoActivity.class)
            , new JumpBean(R.string.main_activity_btn_rtmp, RTMPActivity.class)
            , new JumpBean(R.string.main_activity_btn_navigation, NavigationActivity.class)
            , new JumpBean(R.string.main_activity_btn_bluetooth, BluetoothActivity.class)
            , new JumpBean(R.string.main_activity_btn_db, DbActivity.class)
            , new JumpBean(R.string.main_activity_btn_linkman, LinkmanActivity.class)
            , new JumpBean(R.string.main_activity_btn_media, MediaActivity.class)
            , new JumpBean(R.string.main_activity_btn_language, LanguageActivity.class)
            , new JumpBean(R.string.main_activity_btn_wifi, WifiActivity.class)
            , new JumpBean(R.string.main_activity_btn_time_zone, TimeActivity.class)
            , new JumpBean(R.string.main_activity_btn_gps, GpsActivity.class)
            , new JumpBean(R.string.main_activity_btn_gson_input, GsonInputActivity.class)
            , new JumpBean(R.string.main_activity_btn_photo_picker, PhotoPickerActivity.class)
    };

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

        BswRecyclerView<JumpBean> bswRecyclerView = findViewById(R.id.main_rv);
        bswRecyclerView.initAdapter(R.layout.item_main_layout, convertViewCallBack)
                .setDecoration()
                .setLayoutManager();
        bswRecyclerView.setData(Arrays.asList(jumpBeanList));

        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = i_getvalue.getData();
            if (uri != null) {
                String name = uri.getQueryParameter("name");
                String age = uri.getQueryParameter("age");
                Toast.makeText(getApplicationContext(), "name = " + name + " age = " + age, Toast.LENGTH_LONG).show();
            }
        }

        TextView textView = findViewById(R.id.error);
        final String error = App.getInstance().getSharedPreferencesInstance().getString("error", "");
        if (!TextUtils.isEmpty(error)) {
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

        Observable.just("On", "Off", "On", "On")
                //在传递过程中对事件进行过滤操作
                /*
                 * Function<传入类型，next接收类型>
                 */
                .map(new Function<String, String>() {
                    Map<String, Integer> map = new HashMap<>();

                    @Override
                    public String apply(String s) throws Exception {
                        StringBuilder stringBuilder = new StringBuilder(s);
                        Integer count = map.get(s);
                        if (null == count) {
                            map.put(s, 1);
                        } else {
                            map.put(s, ++count);
                        }
                        stringBuilder.append(s).append(" : 第").append(map.get(s)).append("次");
                        return stringBuilder.toString();
                    }
                })
                .subscribe(new Observer<String>() {
                               @Override
                               public void onError(Throwable e) {
                                   //出现错误会调用这个方法
                                   Log.d("DDDDDD", "异常..." + e.getMessage() + "\n");
                               }

                               @Override
                               public void onComplete() {
                                   //被观察者的onCompleted()事件会走到这里;
                                   Log.d("DDDDDD", "结束观察...\n");
                               }

                               @Override
                               public void onSubscribe(Disposable d) {
                                   Log.d("DDDDDD", d.toString());
                               }

                               @Override
                               public void onNext(String s) {
                                   //处理传过来的onNext事件
                                   Log.d("DDDDD", "handle this---" + s);
                               }
                           }
                );
        RxBus.get().register(this);
        RxBus.get().send(1000, new Person("sddsd", 15));
    }

    private ConvertViewCallBack<JumpBean> convertViewCallBack = new ConvertViewCallBack<JumpBean>() {
        @Override
        public void convert(RecyclerViewHolder holder, final JumpBean jumpBean, int position, int layoutId) {
            holder.setText(R.id.jumpTo, jumpBean.getBtnTextId())
                    .setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, jumpBean.getJumpToClass()));
                        }
                    });
        }
    };

    @Subscribe(code = 1000, threadMode = ThreadMode.MAIN)
    public void receive(Person person) {
        Logger.i(getName(), "我叫".concat(person.name).concat("今年").concat(person.age + "").concat("岁"));
    }

    private class Person {
        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
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

    private String getName() {
        return getClass().getSimpleName();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unRegister(this);
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

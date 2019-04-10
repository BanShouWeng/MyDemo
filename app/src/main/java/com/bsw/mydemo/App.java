package com.bsw.mydemo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.bsw.mydemo.activity.CrashHandler;
import com.bsw.mydemo.utils.rxbus2.RxBus;

import java.util.List;

/**
 * @author 半寿翁
 * @date 2017/11/1
 */
public class App extends Application {

    private static App app;
    private SharedPreferences sharedPreferences;

    public static App getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        RxBus.get().register(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        String a = getDirPath();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行

        super.onTerminate();
    }


    /**
     * 获取内部存储路径
     *
     * @return 路径
     */
    public String getDirPath() {
        return getApplicationContext().getFilesDir().getAbsolutePath();
    }

    public SharedPreferences getSharedPreferencesInstance() {
        if (sharedPreferences == null) {
            sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    /**
     * 获取当前应用程序的包名
     *
     * @return 返回包名
     */
    public String getAppProcessName() {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }

    /**
     * 获取Android版本号
     *
     * @return Android版本号
     */
    public String getAndroidVersion() {
        return android.os.Build.VERSION.SDK_INT + "";
    }

    /**
     * 获取手机运营商
     *
     * @return 手机运营商
     */
    public String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机生产厂家
     *
     * @return 手机生产厂家
     */
    public String getDeviceManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    /**
     * 获取手机机型
     *
     * @return 手机机型
     */
    public String getDeviceModel() {
        return android.os.Build.MODEL;
    }
}

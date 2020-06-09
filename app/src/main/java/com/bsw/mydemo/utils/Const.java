package com.bsw.mydemo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 半寿翁
 * @date 2017/11/1
 */
public class Const {

    /**
     * 线程池
     */
    private static ExecutorService threadPoolExecutor;

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static int judgeListNull(List list) {
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static int judgeListNull(Map list) {
        if (list == null || list.size() == 0) {
            return 0;
        } else {
            return list.size();
        }
    }

    /**
     * 启动线程池
     *
     * @param runnable 需要在子线程执行的程序
     */
    public static void threadPoolExecute(Runnable runnable) {
        if (isEmpty(threadPoolExecutor)) {
            threadPoolExecutor = Executors.newFixedThreadPool(3);
        }
        threadPoolExecutor.execute(runnable);
    }

    /**
     * 判断集合的长度
     *
     * @param list 索要获取长度的集合
     * @return 该集合的长度
     */
    public static <T extends Object> int judgeListNull(T[] list) {
        if (list == null || list.length == 0) {
            return 0;
        } else {
            return list.length;
        }
    }

    private static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            return pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断服务是否启动
     *
     * @param mContext 上下文对象
     * @param clazz    服务的类
     * @return 是否启动
     */
    public static boolean isServiceRunning(Context mContext, Class clazz) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (! (serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(clazz.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 当前对象为空判断
     *
     * @param o 被判断对象
     * @return 是否为空
     */
    public static boolean isEmpty(Object o) {
        return null == o;
    }

    /**
     * 当前对象存在判断
     *
     * @param o 被判断对象
     * @return 是否存在
     */
    public static boolean notEmpty(Object o) {
        return null != o;
    }

    /**
     * 判断服务是否启动
     *
     * @param mContext  上下文对象
     * @param className 服务的name
     * @return 是否启动
     */
    public static boolean isServiceRunning(Context mContext, String className) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(30);

        if (! (serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static void installApk(Context mContext, File apk) {

        Uri uri = null;
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            try {
                uri = FileProvider.getUriForFile(mContext, "com.bsw.mydemo", apk);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            uri = Uri.fromFile(apk);
        }
        //通过Intent安装APK文件
        Intent intents = new Intent();
        intents.setAction("android.intent.action.VIEW");
        intents.addCategory("android.intent.category.DEFAULT");
        intents.setDataAndType(uri, "application/vnd.android.package-archive");
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intents.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            android.os.Process.killProcess(android.os.Process.myPid());
        // 如果不加上这句的话在apk安装完成之后点击单开会崩溃
        mContext.startActivity(intents);
    }
}

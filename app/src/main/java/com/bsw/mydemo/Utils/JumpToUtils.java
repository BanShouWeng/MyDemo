package com.bsw.mydemo.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 界面跳转类
 *
 * @author 半寿翁
 * @date 2017/10/19
 */
public class JumpToUtils {
    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     */
    public static void jumpTo(Context context, Class<?> targetActivity) {
        context.startActivity(new Intent(context, targetActivity));
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public static void jumpTo(Context context, Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     * @param bundle         跳转所携带的信息
     */
    public static void receiverJumpTo(Context context, Class<?> targetActivity, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的目的Activity类
     */
    public static void receiverJumpTo(Context context, Class<?> targetActivity) {
        Intent intent = new Intent(context, targetActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode) {
        ((Activity) context).startActivityForResult(new Intent(context, targetActivity), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param context        当前上下文
     * @param targetActivity 所跳转的Activity类
     * @param bundle         跳转所携带的信息
     * @param requestCode    请求码
     */
    public static void jumpTo(Context context, Class<?> targetActivity, int requestCode, Bundle bundle) {
        Intent intent = new Intent(context, targetActivity);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }
}

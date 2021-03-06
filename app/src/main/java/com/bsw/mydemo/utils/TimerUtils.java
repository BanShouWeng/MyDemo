package com.bsw.mydemo.utils;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;

/**
 * @author 半寿翁
 * @date 2017/10/11
 */
public class TimerUtils extends CountDownTimer {

    private OnBaseTimerCallBack onBaseTimerCallBack;

    /**
     * @param millisInFuture    时间间隔是多长时间
     * @param countDownInterval 回调onTick方法，每隔多久执行一次
     */
    public TimerUtils(long millisInFuture, long countDownInterval, OnBaseTimerCallBack onBaseTimerCallBack) {
        super(millisInFuture, countDownInterval);
        this.onBaseTimerCallBack = onBaseTimerCallBack;
    }

    //间隔时间内执行的操作
    @Override
    public void onTick(long millisUntilFinished) {
        onBaseTimerCallBack.onTick(millisUntilFinished);
    }

    //间隔时间结束的时候才会调用
    @Override
    public void onFinish() {
        onBaseTimerCallBack.onFinish();
    }

    public void stop() {
        mHandler.sendEmptyMessage(0);
    }

    @SuppressLint("han")
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            cancel();
            return false;
        }
    });

    /**
     * 图片二点击回调接口
     */
    public interface OnBaseTimerCallBack {
        /**
         * 间隔时间内执行的操作
         *
         * @param millisUntilFinished 距离结束剩余时间
         */
        void onTick(long millisUntilFinished);

        /**
         * 间隔时间结束的时候才会调用
         */
        void onFinish();
    }
}

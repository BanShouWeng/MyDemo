package com.bsw.mydemo.event;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.bsw.mydemo.utils.NetJudgeUtil;
import com.bsw.mydemo.base.BaseActivity;

/**
 * @author 半寿翁
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetEvent event = BaseActivity.event;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int mobileNetState = NetJudgeUtil.getNetWorkState(context);
            // 接口回调传过去状态的类型
            event.onNetChanged(mobileNetState);
        }
    }

    // 自定义接口
    public interface NetEvent {
        void onNetChanged(int mobileNetState);
    }
}

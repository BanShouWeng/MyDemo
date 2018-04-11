package com.bsw.mydemo.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 电话拨打状态监听
 *
 * @author 半寿翁
 */
public class PhoneBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "PhoneBroadcastReceiver";
    private static String mIncomingNumber = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果是拨打电话
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "call OUT:" + phoneNumber);
        } else {
            // 如果是来电
            TelephonyManager tManager = (TelephonyManager) context
                    .getSystemService(Service.TELEPHONY_SERVICE);
            switch (tManager.getCallState()) {
                // 振铃
                case TelephonyManager.CALL_STATE_RINGING:
                    mIncomingNumber = intent.getStringExtra("incoming_number");
                    Log.i(TAG, "RINGING :" + mIncomingNumber);
                    break;

                // 接起电话
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.i(TAG, "incoming ACCEPT :" + mIncomingNumber);
                    break;

                // 挂电话
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.i(TAG, "incoming IDLE");
                    break;

                default:
                    Log.i(TAG, "incoming default");
                    break;
            }
        }
    }
}
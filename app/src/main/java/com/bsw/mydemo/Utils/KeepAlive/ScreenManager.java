package com.bsw.mydemo.Utils.KeepAlive;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * @author 半寿翁
 * @date 2018/7/23.
 */

public class ScreenManager {
    private Context mContext;

    private WeakReference<Activity> mActivityWref;

    public static ScreenManager gDefualt;

    public static ScreenManager getInstance(Context pContext) {
        if (gDefualt == null) {
            gDefualt = new ScreenManager(pContext.getApplicationContext());
        }
        return gDefualt;
    }

    private ScreenManager(Context pContext) {
        this.mContext = pContext;
    }

    public void setActivity(Activity pActivity) {
        mActivityWref = new WeakReference<Activity>(pActivity);
    }

    public void startActivity() {
        KeepAliveActivity.actionToSinglePixelActivity(mContext);
    }

    public void finishActivity() {
        //结束掉SinglePixelActivity
        if (mActivityWref != null) {
            Activity activity = mActivityWref.get();
            if (activity != null) {
                activity.finish();
            }
        }
    }
}

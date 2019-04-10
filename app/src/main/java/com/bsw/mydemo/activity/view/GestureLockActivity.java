package com.bsw.mydemo.activity.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.GestureLock.GestureLockViewGroup;
/**
 * @author 半寿翁
 */
public class GestureLockActivity extends BaseActivity {

    private GestureLockViewGroup mGestureLockViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_gesture_lock);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gesture_lock;
    }

    @Override
    protected void findViews() {
        mGestureLockViewGroup = getView(R.id.id_gestureLockViewGroup);
    }

    @Override
    protected void formatViews() {
        mGestureLockViewGroup.setAnswer(new int[] {1, 2, 3, 4, 5});
        mGestureLockViewGroup.setShowPath(true);
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary() {
                        toast("错误5次...");
                        mGestureLockViewGroup.setUnMatchExceedBoundary(5);
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        toast(matched + "");
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }
                });
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }
}

package com.bsw.mydemo.myTest;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.widget.BatteryView;
import com.bsw.mydemo.widget.BswRecyclerView.SwipeItemLayout;
import com.bsw.mydemo.R;

import java.lang.ref.WeakReference;

public class TestActivity extends AppCompatActivity {

    private View scaleTest;
    private MyHandler myHandler = new MyHandler(this);

    private final float offset = 300f;

    private boolean isScaleRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.test_bg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isScaleRun = !isScaleRun;
                if (isScaleRun) {
                    myHandler.sendEmptyMessage(0);
                }
            }
        });

        scaleTest = findViewById(R.id.scale_test);

//        FrameLayout testBg = findViewById(R.id.test_bg);
//        SwipeItemLayout swipeItemLayout = new SwipeItemLayout(this);
////        View view = swipeItemLayout.getChildAt(0);
//        View rightView = LayoutInflater.from(this).inflate(R.layout.user_right_layout, swipeItemLayout, false);
//        FrameLayout.LayoutParams lpR = new FrameLayout.LayoutParams(rightView.getLayoutParams());
//        lpR.gravity = Gravity.RIGHT;
//        rightView.setLayoutParams(lpR);
//        swipeItemLayout.addView(rightView);
//        View leftView = LayoutInflater.from(this).inflate(R.layout.user_left_layout, swipeItemLayout, false);
//        FrameLayout.LayoutParams lpL = new FrameLayout.LayoutParams(leftView.getLayoutParams());
//        lpL.gravity = Gravity.LEFT;
//        leftView.setLayoutParams(lpL);
//        swipeItemLayout.addView(leftView);
//        View mainView = LayoutInflater.from(this).inflate(R.layout.user_item_layout, swipeItemLayout, false);
//        ((TextView) mainView.findViewById(R.id.user_name)).setText("这是姓名");
//        ((TextView) mainView.findViewById(R.id.user_age)).setText("这是年纪");
//        swipeItemLayout.addView(mainView);
//        testBg.addView(swipeItemLayout);
    }

    private class MyHandler extends Handler {
        private WeakReference<TestActivity> weakReference;

        private boolean isZoomIn = true;

        MyHandler(TestActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final TestActivity activity = weakReference.get();
            if (null != activity) {
                if (isZoomIn) {
                    activity.scaleTest.setScaleX(scaleTest.getScaleX() - 0.005f);
                    activity.scaleTest.setScaleY(scaleTest.getScaleY() - 0.005f);
                    activity.scaleTest.setTranslationY(activity.scaleTest.getTranslationY() + offset / 200);
                } else {
                    activity.scaleTest.setScaleX(scaleTest.getScaleX() + 0.005f);
                    activity.scaleTest.setScaleY(scaleTest.getScaleY() + 0.005f);
                    activity.scaleTest.setTranslationY(activity.scaleTest.getTranslationY() - offset / 200);
                }
                if (activity.scaleTest.getScaleX() < 0 || activity.scaleTest.getScaleY() < 0) {
                    isZoomIn = false;
                } else if (activity.scaleTest.getScaleX() > 1 || activity.scaleTest.getScaleY() > 1) {
                    isZoomIn = true;
                }
                Logger.i("scaleX = " + activity.scaleTest.getScaleX()
                        + " scaleY = " + activity.scaleTest.getScaleY()
                        + " translationY = " + activity.scaleTest.getTranslationY()
                        + " isZoomIn = " + isZoomIn
                );

                Const.threadPoolExecute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            if (activity.isScaleRun) {
                                activity.myHandler.sendEmptyMessage(0);
                            }
                        }
                    }
                });
            }
        }
    }
}

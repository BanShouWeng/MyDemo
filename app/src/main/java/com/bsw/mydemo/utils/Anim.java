package com.bsw.mydemo.utils;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * @author leiming
 * @date 2020-6-16
 */
public class Anim {
    private MyHandler myHandler;
    private View scaleTest;

    private boolean isScaleRun = false;

    private final float offset = 300f;

    public Anim(View scaleTest) {
        myHandler = new MyHandler(this);
        this.scaleTest = scaleTest;
    }

    public boolean isRunning() {
        return isScaleRun;
    }

    public void start() {
        isScaleRun = true;
        myHandler.sendEmptyMessage(0);
    }

    public void stop() {
        isScaleRun = false;
    }

    public float getOffset() {
        return offset;
    }

    private class MyHandler extends Handler {
        private WeakReference<Anim> weakReference;

        private boolean isZoomIn = true;

        MyHandler(Anim anim) {
            weakReference = new WeakReference<>(anim);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Anim anim = weakReference.get();
            if (null != anim) {
                if (isZoomIn) {
                    anim.scaleTest.setScaleX(scaleTest.getScaleX() - 0.005f);
                    anim.scaleTest.setScaleY(scaleTest.getScaleY() - 0.005f);
                    anim.scaleTest.setTranslationY(anim.scaleTest.getTranslationY() + offset / 200);
                } else {
                    anim.scaleTest.setScaleX(scaleTest.getScaleX() + 0.005f);
                    anim.scaleTest.setScaleY(scaleTest.getScaleY() + 0.005f);
                    anim.scaleTest.setTranslationY(anim.scaleTest.getTranslationY() - offset / 200);
                }
                if (anim.scaleTest.getScaleX() < 0 || anim.scaleTest.getScaleY() < 0) {
                    isZoomIn = false;
                } else if (anim.scaleTest.getScaleX() > 1 || anim.scaleTest.getScaleY() > 1) {
                    isZoomIn = true;
                }
                Logger.i("scaleX = " + anim.scaleTest.getScaleX()
                        + " scaleY = " + anim.scaleTest.getScaleY()
                        + " translationY = " + anim.scaleTest.getTranslationY()
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
                            if (anim.isScaleRun) {
                                anim.myHandler.sendEmptyMessage(0);
                            }
                        }
                    }
                });
            }
        }
    }
}

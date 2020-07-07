package com.bsw.mydemo.utils.anim;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.FloatRange;
import android.view.View;

import com.bsw.mydemo.utils.Const;

import java.lang.ref.WeakReference;

/**
 * @author 半寿翁
 * @date 2020-6-16
 */
public class Anim {
    /**
     * 动画执行Handler
     */
    private MyHandler myHandler;
    /**
     * 缩放View
     */
    private View scaleView;

    /**
     * 动画是否进行中
     */
    private boolean isScaleRun = false;

    /**
     * 缩放中心的纵向偏移量
     */
    private float offsetY = 300f;
    /**
     * 缩放中心的横向偏移量
     */
    private float offsetX = 300f;

    /**
     * 启动时的View相对自身变化的大小(X轴)
     */
    private final float fromX;
    /**
     * 启动时的View相对自身变化的大小(Y轴)
     */
    private final float fromY;
    /**
     * 结束时的View相对自身的变化的大小(X轴)
     */
    private final float toX;
    /**
     * 结束时的View相对自身的变化的大小(Y轴)
     */
    private final float toY;
    /**
     * 缩放执行时长
     * 单位ms
     */
    private int duration = 500;

    /**
     * 初始化动画工具
     *
     * @param scaleView 缩放视图
     * @param from      缩放起始大小
     * @param to        缩放结束大小
     */
    public Anim(View scaleView, @FloatRange(from = 0) float from, @FloatRange(from = 0) float to) {
        myHandler = new MyHandler(this);
        this.scaleView = scaleView;
        fromX = from;
        fromY = from;
        toX = to;
        toY = to;
    }

    /**
     * 初始化动画工具
     *
     * @param scaleView 缩放视图
     * @param fromX     缩放起始大小(X轴)
     * @param fromY     缩放起始大小(Y轴)
     * @param toX       缩放结束大小(X轴)
     * @param toY       缩放结束大小(Y轴)
     */
    public Anim(View scaleView
            , @FloatRange(from = 0) float fromX
            , @FloatRange(from = 0) float fromY
            , @FloatRange(from = 0) float toX
            , @FloatRange(from = 0) float toY) {
        myHandler = new MyHandler(this);
        this.scaleView = scaleView;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }


    /**
     * 设置偏移量
     *
     * @param offsetX 横向偏移量
     * @param offsetY 纵向偏移量
     * @return 当前Anim动画
     */
    Anim setOffset(float offsetX, float offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        return this;
    }

    /**
     * 是否正在执行动画
     *
     * @return 动画执行状态
     */
    public boolean isRunning() {
        return isScaleRun;
    }

    /**
     * 启动切换动画
     *
     * @return 是否启动成功，若动画正在执行中，则返回false
     */
    public boolean start() {
        if (!isRunning()) {
            isScaleRun = true;
            scaleView.setScaleX(fromX);
            scaleView.setScaleY(fromY);
            myHandler.sendEmptyMessage(0);
            return true;
        }
        return false;
    }

    /**
     * 停止动画
     */
    public void stop() {
        isScaleRun = false;
    }

    /**
     * 获取中心纵向偏移量
     *
     * @return 缩放中心纵向偏移量
     */
    public float getOffsetY() {
        return offsetY;
    }

    /**
     * 获取中心纵向偏移量
     *
     * @return 缩放中心纵向偏移量
     */
    public float getOffsetX() {
        return offsetX;
    }

    /**
     * 缩放动画刷新Handler
     */
    @SuppressLint("HandlerLeak")
    private class MyHandler extends Handler {
        /**
         * 弱引用当前类，避免内存泄露
         */
        private WeakReference<Anim> weakReference;
        /**
         * 缩放步长（X轴）
         */
        private float scaleStepX;
        /**
         * 缩放步长（Y轴）
         */
        private float scaleStepY;
        /**
         * 位移步长（X轴）
         */
        private float offsetStepX;
        /**
         * 位移步长（Y轴）
         */
        private float offsetStepY;

        MyHandler(Anim anim) {
            weakReference = new WeakReference<>(anim);
            scaleStepX = (fromX - toX) / duration;
            scaleStepY = (fromY - toY) / duration;
            offsetStepX = offsetStepX / duration;
            offsetStepY = offsetStepY / duration;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Anim anim = weakReference.get();
            if (null != anim) {
                if (0 != scaleStepX) {
                    anim.scaleView.setScaleX(scaleView.getScaleX() - scaleStepX);
                }
                if (0 != scaleStepY) {
                    anim.scaleView.setScaleY(scaleView.getScaleY() - scaleStepY);
                }
                if (0 != offsetStepX) {
                    anim.scaleView.setTranslationX(anim.scaleView.getTranslationY() + offsetStepX);
                }
                if (0 != offsetStepY) {
                    anim.scaleView.setTranslationY(anim.scaleView.getTranslationY() + offsetStepY);
                }

                // 继续动画执行
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

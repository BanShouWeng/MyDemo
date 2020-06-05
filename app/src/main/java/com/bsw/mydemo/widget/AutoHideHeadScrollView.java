package com.bsw.mydemo.widget;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bsw.mydemo.utils.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class AutoHideHeadScrollView extends NestedScrollView {
    private static final int HEAD_HIDE = 22;
    private static final int HEAD_SHOW = 23;

    public static final int HEIGHT_PX = 5;
    public static final int HEIGHT_DP = 6;
    private Builder builder;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            HEIGHT_DP
            , HEIGHT_PX
    })
    @interface HeightUnit {
    }

    private Context mContext;

    /**
     * 滚动状态监听接口
     */
    private OnScrollViewMoveListener onScrollViewMoveListener;
    /**
     * head控件
     */
    private View headView;
    /**
     * body控件
     */
    private View bodyView;

    private SVHandler svHandler;

    public AutoHideHeadScrollView(@NonNull Context context) {
        this(context, null);
    }

    public AutoHideHeadScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoHideHeadScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setFillViewport(true);

        mContext = context;
        svHandler = new SVHandler(this);
        setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private boolean isHeadHidden = false;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (null != onScrollViewMoveListener) {
                    // 下滑
                    if (scrollY > oldScrollY) {
                        onScrollViewMoveListener.scrollUp();
                    }

                    // 上滑
                    if (scrollY < oldScrollY) {
                        onScrollViewMoveListener.scrollDown();

                    }

                    // 滑到顶部
                    if (scrollY == 0) {
                        onScrollViewMoveListener.scrollToTop();
                    }

                    // 滑到底部
                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        onScrollViewMoveListener.scrollToBottom();
                    }
                }

                if (null == builder) {
                    return;
                }

                if (headView.getBottom() <= builder.topBlank && !isHeadHidden) {        // head底部未显示且之前是非隐藏状态，则隐藏
                    isHeadHidden = true;
                    onScrollViewMoveListener.headHiddenChanged(isHeadHidden);
                }

                if (headView.getTop() >= builder.topBlank && isHeadHidden) {            // head顶部显示且之前是隐藏状态，则非隐藏
                    isHeadHidden = false;
                    onScrollViewMoveListener.headHiddenChanged(isHeadHidden);
                }
            }
        });
    }

    /**
     * 隐藏head
     */
    private void hideHead() {
        int topBlank;
        if (null == builder) {
            topBlank = 0;
        } else {
            topBlank = builder.topBlank;
        }

        int distance = headView.getBottom() + topBlank;
        if (null != headView && distance > 0) {
            Message message = new Message();
            message.arg1 = HEAD_HIDE;
            message.what = distance;
            svHandler.sendMessage(message);
        }
    }

    /**
     * 显示head
     */
    private void showHead() {
        int topBlank;
        if (null == builder) {
            topBlank = 0;
        } else {
            topBlank = builder.topBlank;
        }

        int distance = headView.getTop() - topBlank;
        if (null != headView && distance < 0) {
            Message message = new Message();
            message.arg1 = HEAD_SHOW;
            message.what = distance;
            svHandler.sendMessage(message);
        }
    }

    /**
     * 初始化视图
     *
     * @param headId                   head的Id
     * @param bodyId                   body的Id
     * @param onScrollViewMoveListener 视图滚动监听
     */
    public void initView(@LayoutRes int headId, @LayoutRes final int bodyId, OnScrollViewMoveListener onScrollViewMoveListener) {
        this.onScrollViewMoveListener = onScrollViewMoveListener;

        final LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setFocusable(true);
        linearLayout.setFocusableInTouchMode(true);
        linearLayout.requestFocus();
        linearLayout.setBackgroundResource(android.R.color.white);

        addView(linearLayout);

        headView = LayoutInflater.from(mContext).inflate(headId, null);
        bodyView = LayoutInflater.from(mContext).inflate(bodyId, null);

        linearLayout.addView(headView);
        linearLayout.addView(bodyView);

        headView.post(new Runnable() {
            @Override
            public void run() {
                int headHeight = builder.headHeight;
                if (headHeight != 0) {
                    bodyView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, headHeight));
                }
            }
        });

        bodyView.post(new Runnable() {
            @Override
            public void run() {
                bodyView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.getScreenHeight(mContext)));
            }
        });

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT
                , FrameLayout.LayoutParams.MATCH_PARENT
        );
        linearLayout.setLayoutParams(lp);

        invalidate();
    }

    public Builder getBuilder() {
        builder = new Builder();
        return builder;
    }

    /**
     * 获取子视图
     *
     * @param idRes 子视图Id
     * @param <T>   子视图泛型
     * @return 子视图
     */
    public <T extends View> T findChildById(@IdRes int idRes) {
        return findViewById(idRes);
    }

    /**
     * 构造器
     */
    class Builder {
        /**
         * 顶部空白，一般用于title
         */
        private int topBlank = 0;
        /**
         * 头的高度，TextView的设置的高度可能会被替换为wrap_content，因此需要设置时通过外部设置
         */
        private int headHeight = 0;
        /**
         * body的资源Id
         */
        private int bodyId;
        /**
         * head的资源Id
         */
        private int headId;

        /**
         * 设置滚动布局上方列表（如title高度等）
         *
         * @param topBlank {@link AutoHideHeadScrollView.Builder#topBlank}
         */
        public Builder setTopBlank(int topBlank) {
            this.topBlank = topBlank;
            return this;
        }

        /**
         * 设置head的高
         *
         * @param headHeight {@link AutoHideHeadScrollView.Builder#headHeight}
         */
        public Builder setHeadHeight(int headHeight) {
            setHeadHeight(headHeight, HEIGHT_DP);
            return this;
        }

        /**
         * 设置head的高
         *
         * @param headHeight {@link AutoHideHeadScrollView.Builder#headHeight}
         */
        public Builder setHeadHeight(int headHeight, @HeightUnit int heightUnit) {
            switch (headHeight) {
                // 若单位为dp则需要转换
                case HEIGHT_DP:
                    headHeight = ScreenUtil.dp2px(mContext, headHeight);
                case HEIGHT_PX:
                    this.headHeight = headHeight;
                    break;
            }
            return this;
        }

        public Builder setView(@LayoutRes int headId, @LayoutRes int bodyId) {
            this.bodyId = bodyId;
            this.headId = headId;
            return this;
        }

        public void build(OnScrollViewMoveListener onScrollViewMoveListener) {
            initView(headId, bodyId, onScrollViewMoveListener);
        }
    }

    /**
     * 滑动状态监听
     */
    public interface OnScrollViewMoveListener {
        /**
         * 向上滑动
         */
        void scrollUp();

        /**
         * 向下滑动
         */
        void scrollDown();

        /**
         * 已经滑动到顶
         */
        void scrollToTop();

        /**
         * 已经滑动到底
         */
        void scrollToBottom();

        /**
         * 已经滑动到底
         */
        void headHiddenChanged(boolean isHidden);
    }

    private static class SVHandler extends Handler {
        private WeakReference<AutoHideHeadScrollView> reference;

        private SVHandler(AutoHideHeadScrollView scrollView) {
            reference = new WeakReference<>(scrollView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            AutoHideHeadScrollView scrollView = reference.get();
            switch (msg.arg1) {
                case HEAD_HIDE:
                case HEAD_SHOW:
//                    setLayout(scrollView, msg.what / 200.0);
                    break;

                default:
//                    setLayout(scrollView, msg.what);
                    break;
            }
        }

//        private void setLayout(final AutoHideHeadScrollView scrollView, final double distance) {
//            final View headView = scrollView.headView;
//            thread = new Thread(new Runnable() {
//                private double hasMoved = 0;
//                private double top = 0;
//                private double height = 0;
//
//                @Override
//                public void run() {
//                    int headTop = headView.getTop();
//                    int headBottom = headView.getBottom();
//
//                    headTop -= distance;
//                    headBottom -= distance;
//
//                    if (distance < 0 && headTop > topBlank) {               // 显示
//                        headBottom -= headTop;
//                        headTop = topBlank;
//                        if (null != scrollView.onScrollViewMoveListener) {
//                            scrollView.onScrollViewMoveListener.headHiddenChanged(false);
//                        }
//                    } else if (distance > 0 && headTop < topBlank) {        // 隐藏
//                        headTop -= headBottom;
//                        headBottom = topBlank;
//                        if (null != scrollView.onScrollViewMoveListener) {
//                            scrollView.onScrollViewMoveListener.headHiddenChanged(true);
//                        }
//                    } else {
//                        scrollView.mContext.
//                    }
//                    headView.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            headView.layout();
//                        }
//                    });
//                }
//            });
//            thread.start();
//        }
    }

    class ScrollBean {
        private int headChangeType;
        private int top;
        private int bottom;
        private int height;
        private double pieceDistance;
        private double moveDistance = 0;

        public ScrollBean(int headChangeType, int top, int bottom, int height, double pieceDistance) {
            this.headChangeType = headChangeType;
            this.top = top;
            this.bottom = bottom;
            this.height = height;
            this.pieceDistance = pieceDistance;
        }

        /**
         * 当前类被发送
         * 便于Message的what.obj传递
         *
         * @return 当前Bean
         */
        private ScrollBean send() {
            switch (headChangeType) {
                case HEAD_HIDE:
                    moveDistance += pieceDistance;
                    break;

                case HEAD_SHOW:

                    break;
            }

            return this;
        }

        /**
         *
         */
        private void judgeEnd() {

        }
    }
}

package com.bsw.mydemo.widget.GestureLock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * https://blog.csdn.net/lmj623565791/article/details/36236113  鸿洋
 * <p>
 * 添加正确色值更改，路径隐藏，以及错误后重置时间设置。并将九宫格内按钮长宽、间距
 * 长宽连线宽度、连线透明度做活，可以根据需求自行配置。
 *
 * @author 半寿翁（改）
 * @date 2018/3/21.
 */

public class GestureLockViewGroup extends RelativeLayout {
    private static final String TAG = "GestureLockViewGroup";
    /**
     * 保存所有的GestureLockView
     */
    private GestureLockView[] mGestureLockViews;
    /**
     * 每个边上的GestureLockView的个数
     */
    private int mCount = 3;
    /**
     * 存储答案
     */
    private int[] mAnswer = {0, 1, 2, 5, 8};
    /**
     * 保存用户选中的GestureLockView的id
     */
    private List<Integer> mChoose = new ArrayList<>();

    private Paint mPaint;
    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mGestureLockViewWidth;
    /**
     * 连线的宽度（GestureView宽度的百分之多少）
     */
    private float mLineWidthPercent = 1;
    /**
     * 连线的透明度
     */
    private int mLineAlpha = 100;
    /**
     * 输入错误后，重置的时间
     */
    private int resetTime = 1000;
    /**
     * GestureLockView宽度（百分比）
     */
    private float mGestureLockViewWidthPercent = 80;
    /**
     * 每个GestureLockView中间的间距（百分比）
     */
    private float mMarginBetweenLockViewPercent = 50;
    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0xFF939090;
    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFE0DBDB;
    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xFF378FC9;
    /**
     * GestureLockView手指抬起的状态下验证正确时内圆和外圆的颜色
     */
    private int mFingerUpTrueColor = 0xFF00FF00;
    /**
     * GestureLockView手指抬起的状态下验证错误时内圆和外圆的颜色
     */
    private int mFingerUpFalseColor = 0xFFFF0000;
    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;

    private Path mPath;
    /**
     * 指引线的开始位置x
     */
    private int mLastPathX;
    /**
     * 指引线的开始位置y
     */
    private int mLastPathY;
    /**
     * 指引下的结束位置
     */
    private Point mTmpTarget = new Point();
    /**
     * 是否显示绘制路径
     */
    private boolean showPath = true;
    /**
     * 最大尝试次数
     */
    private int mTryTimes = 4;
    /**
     * 回调接口
     */
    private OnGestureLockViewListener mOnGestureLockViewListener;

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        /*
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.GestureLockViewGroup, defStyle, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr,
                            mNoFingerInnerCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColor = a.getColor(attr,
                            mNoFingerOuterCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor = a.getColor(attr, mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpTrueColor = a.getColor(attr, mFingerUpTrueColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount = a.getInt(attr, 3);
                    break;
                case R.styleable.GestureLockViewGroup_try_times:
                    mTryTimes = a.getInt(attr, 5);
                    break;
                case R.styleable.GestureLockViewGroup_line_width_percent:
                    mLineWidthPercent = a.getInt(attr, 5) / 100.0f;
                    break;
                case R.styleable.GestureLockViewGroup_lock_width_percent:
                    mGestureLockViewWidthPercent = a.getInt(attr, 80) / 100.0f;
                    break;
                case R.styleable.GestureLockViewGroup_margin_between_lock_view_percent:
                    mMarginBetweenLockViewPercent = a.getInt(attr, 50) / 100.0f;
                    break;
                case R.styleable.GestureLockViewGroup_line_alpha:
                    mLineAlpha = a.getInt(attr, 100);
                    break;
                case R.styleable.GestureLockViewGroup_reset_time:
                    resetTime = a.getInt(attr, 1000);
                    break;
                default:
                    break;
            }
        }

        a.recycle();

        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        // mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        // mPaint.setColor(Color.parseColor("#aaffffff"));
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Log.e(TAG, mWidth + "");
        // Log.e(TAG, mHeight + "");

        mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

        // setMeasuredDimension(mWidth, mHeight);

        // 初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mCount * mCount];
            // 计算每个GestureLockView的宽度
//            mGestureLockViewWidth = (int) (4 * mWidth * 1.0f / (5 * mCount + 1));
            mGestureLockViewWidth = (int) (mWidth / mCount * mGestureLockViewWidthPercent);
            //计算每个GestureLockView的间距
            /*
      每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
     */
            int mMarginBetweenLockView = (int) ((mWidth - mGestureLockViewWidth * mCount) / (mCount + 1) * mMarginBetweenLockViewPercent);
            // 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
            mPaint.setStrokeWidth(mGestureLockViewWidth * mLineWidthPercent);

            for (int i = 0; i < mGestureLockViews.length; i++) {
                //初始化每个GestureLockView
                mGestureLockViews[i] = new GestureLockView(getContext(),
                        mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                        mFingerOnColor, mFingerUpTrueColor, mFingerUpFalseColor);
                mGestureLockViews[i].setId(i + 1);
                //设置参数，主要是定位GestureLockView间的位置
                RelativeLayout.LayoutParams lockerParams = new RelativeLayout.LayoutParams(
                        mGestureLockViewWidth, mGestureLockViewWidth);

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,
                            mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW,
                            mGestureLockViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMagin = 0;
                int topMargin = 0;
                /*
                 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                 */
                if (i >= 0 && i < mCount)// 第一行
                {
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0)// 第一列
                {
                    leftMagin = mMarginBetweenLockView;
                }

                lockerParams.setMargins(leftMagin, topMargin, rightMargin,
                        bottomMargin);
                mGestureLockViews[i].setMode(GestureLockView.Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i], lockerParams);
            }

            Logger.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
                    + mGestureLockViewWidth + " , mMarginBetweenLockView = "
                    + mMarginBetweenLockView);

        }
    }

    /**
     * 是否展示绘制路径
     *
     * @param showPath 是否显示绘制路径
     */
    public void setShowPath(boolean showPath) {
        this.showPath = showPath;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 重置
                reset();
                break;

            case MotionEvent.ACTION_MOVE:
                GestureLockView child = getChildIdByPos(x, y);
                mPaint.setColor(mFingerOnColor);
                mPaint.setAlpha(showPath ? mLineAlpha : 0);
                if (child != null) {
                    int cId = child.getId();
                    if (! mChoose.contains(cId)) {
                        mChoose.add(cId);
                        if (showPath) {
                            child.setMode(GestureLockView.Mode.STATUS_FINGER_ON);
                        }
                        // 设置指引线的起点
                        mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                        mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

                        if (mChoose.size() == 1)// 当前添加为第一个
                        {
                            mPath.moveTo(mLastPathX, mLastPathY);
                        } else
                        // 非第一个，将两者使用线连上
                        {
                            mPath.lineTo(mLastPathX, mLastPathY);
                        }
                        if (mOnGestureLockViewListener != null)
                            mOnGestureLockViewListener.onBlockSelected(cId);

                    }
                }
                // 指引线的终点
                mTmpTarget.x = x;
                mTmpTarget.y = y;
                break;

            case MotionEvent.ACTION_UP:
                boolean isRight = checkAnswer();

                mPaint.setColor(isRight ? mFingerUpTrueColor : mFingerUpFalseColor);
                mPaint.setAlpha(mLineAlpha);
                this.mTryTimes--;

                // 回调是否成功
                if (mOnGestureLockViewListener != null && mChoose.size() > 0) {
                    mOnGestureLockViewListener.onGestureEvent(isRight);
                    if (this.mTryTimes == 0) {
                        mOnGestureLockViewListener.onUnmatchedExceedBoundary();
                    }
                }

                Log.e(TAG, "mUnMatchExceedBoundary = " + mTryTimes);
                Log.e(TAG, "mChoose = " + mChoose);
                // 将终点设置位置为起点，即取消指引线
                mTmpTarget.x = mLastPathX;
                mTmpTarget.y = mLastPathY;

                // 改变子元素的状态为UP
                changeItemMode(isRight);

                // 计算每个元素中箭头需要旋转的角度
                for (int i = 0; i + 1 < mChoose.size(); i++) {
                    int childId = mChoose.get(i);
                    int nextChildId = mChoose.get(i + 1);

                    GestureLockView startChild = findViewById(childId);
                    GestureLockView nextChild = findViewById(nextChildId);

                    int dx = nextChild.getLeft() - startChild.getLeft();
                    int dy = nextChild.getTop() - startChild.getTop();
                    // 计算角度
                    int angle = (int) Math.toDegrees(Math.atan2(dy, dx)) + 90;
                    startChild.setArrowDegree(angle);
                }
                if (! isRight) {
                    CountDownTimer timer = new CountDownTimer(resetTime, resetTime) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            reset();
                            invalidate();
                        }
                    }.start();
                }
                break;
        }
        invalidate();
        return true;
    }

    private void changeItemMode(boolean isRight) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (mChoose.contains(gestureLockView.getId())) {
                gestureLockView.setMode(isRight ? GestureLockView.Mode.STATUS_FINGER_UP_TRUE : GestureLockView.Mode.STATUS_FINGER_UP_FALSE);
            }
        }
    }

    /**
     * 做一些必要的重置
     */
    private void reset() {
        mChoose.clear();
        mPath.reset();
        mPaint.setAlpha(0);
        for (GestureLockView gestureLockView : mGestureLockViews) {
            gestureLockView.setMode(GestureLockView.Mode.STATUS_NO_FINGER);
            gestureLockView.setArrowDegree(- 1);
        }
    }

    /**
     * 检查用户绘制的手势是否正确
     *
     * @return
     */
    private boolean checkAnswer() {
        if (mAnswer.length != mChoose.size())
            return false;

        for (int i = 0; i < mAnswer.length; i++) {
            if (mAnswer[i] != mChoose.get(i))
                return false;
        }

        return true;
    }

    /**
     * 检查当前左边是否在child中
     *
     * @param child
     * @param x
     * @param y
     * @return
     */
    private boolean checkPositionInChild(View child, int x, int y) {

        //设置了内边距，即x,y必须落入下GestureLockView的内部中间的小区域中，可以通过调整padding使得x,y落入范围不变大，或者不设置padding
        int padding = (int) (mGestureLockViewWidth * 0.15);

        return x >= child.getLeft() + padding && x <= child.getRight() - padding
                && y >= child.getTop() + padding
                && y <= child.getBottom() - padding;
    }

    /**
     * 通过x,y获得落入的GestureLockView
     *
     * @param x
     * @param y
     * @return
     */
    private GestureLockView getChildIdByPos(int x, int y) {
        for (GestureLockView gestureLockView : mGestureLockViews) {
            if (checkPositionInChild(gestureLockView, x, y)) {
                return gestureLockView;
            }
        }

        return null;

    }

    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.mOnGestureLockViewListener = listener;
    }

    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     */
    public void setAnswer(int[] answer) {
        this.mAnswer = answer;
    }

    /**
     * 设置最大实验次数
     *
     * @param boundary
     */
    public void setUnMatchExceedBoundary(int boundary) {
        this.mTryTimes = boundary;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        //绘制GestureLockView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
        //绘制指引线
        if (mChoose.size() > 0) {
            if (mLastPathX != 0 && mLastPathY != 0)
                canvas.drawLine(mLastPathX, mLastPathY, mTmpTarget.x,
                        mTmpTarget.y, mPaint);
        }

    }

    public interface OnGestureLockViewListener {
        /**
         * 单独选中元素的Id
         *
         * @param cId
         */
        void onBlockSelected(int cId);

        /**
         * 是否匹配
         *
         * @param matched
         */
        void onGestureEvent(boolean matched);

        /**
         * 超过尝试次数
         */
        void onUnmatchedExceedBoundary();
    }
}

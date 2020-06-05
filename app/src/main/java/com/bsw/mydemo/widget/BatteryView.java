package com.bsw.mydemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * 电池控件
 *
 * @author leiming
 * @date 2020-5-25
 */
public class BatteryView extends View {
    /**
     * 当前电量
     */
    private int currentPower = 70;
    /**
     * 满电量
     */
    private float fullCharge = 100f;
    /**
     * 宽高比
     */
    private float aspectW2HRatio = 2.4f;
    /**
     * 边框宽度
     */
    private int frameWidth = 2;
    /**
     * 电池head占高度比例
     */
    private float headHeightRatio = 0.25f;
    /**
     * 电池body占宽度比例
     */
    private float bodyWidthRatio = 0.9f;

    private Paint paint = new Paint();
    private Rect rect = new Rect();

    /**
     * 上下文
     */
    private Context mContext;
    private int left;
    private int top;
    private int drawWidth;
    private int drawHeight;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs);
    }

    /**
     * 初始化参数
     *
     * @param attrs 属性
     */
    private void init(AttributeSet attrs) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        /*
//         * 高度测量模式
//         * {@link MeasureSpec#EXACTLY 精确}
//         * {@link MeasureSpec#UNSPECIFIED 不确定}
//         * {@link MeasureSpec#AT_MOST 最大值}
//         */
//        // 宽度是否精确
//        boolean isWidthExactly = MeasureSpec.EXACTLY == MeasureSpec.getMode(heightMeasureSpec);
//        // 高度是否精确
//        boolean isHeightExactly = MeasureSpec.EXACTLY == MeasureSpec.getMode(heightMeasureSpec);
//
//        // 控件宽度
//        int width;
//        // 控件高度
//        int height;
//        if (!isWidthExactly) {
//            width = MeasureSpec.getSize(widthMeasureSpec);
//        }
//
//        if (!isHeightExactly) {
//            height = MeasureSpec.getSize(heightMeasureSpec);
//        }
//
//        setMeasuredDimension(width, height);
//
//        int right;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            left = getPaddingStart();
//            right = getWidth() - getPaddingStart() - getPaddingEnd();
//        } else {
//            left = getPaddingLeft();
//            right = getWidth() - getPaddingRight() - getPaddingLeft();
//        }
//        top = getPaddingTop();
//        int bottom = getHeight() - getPaddingTop() - getPaddingBottom();
//
//        width = right - left;
//        height = bottom - top;
//
//        if (isWidthExactly && isHeightExactly) {
//            // 当宽高都是精确值时，使用精确的比例
//            drawWidth = height;
//            drawHeight = width;
//        } else {
//            // 当宽高不是精确值时，为了电池好看，依据宽高比调整绘制的宽高
//            drawWidth = (int) Math.min(width, height * aspectW2HRatio);
//            drawHeight = (int) Math.min(height, width / aspectW2HRatio);
//        }
    }

    /**
     * 获取当前电量
     *
     * @param currentPower 当前电量
     */
    public void setCurrentPower(@IntRange(from = 0, to = 100) int currentPower) {
        this.currentPower = currentPower;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // 电池身宽度
//        int bodyWidth = (int) (width * bodyWidthRatio);
//        int bodyHeight = (int) (bodyWidth / aspectW2HRatio);
//        // 电池冒宽度
//        int headWidth = (int) (width * (1 - bodyWidthRatio));
//
//        // 电池右侧边界
//        int powerRight = left + (int) ((width * currentPower) / fullCharge);
//
//        // 绘制电池背景
//        rect.set(left, top, bodyWidth, top + bodyHeight);
//        paint.setColor(Color.GREEN);
//        canvas.drawRect(rect, paint);
//
//        int headHeight = (int) (headHeightRatio * height);
//        int headHeightMargin = (1 - headHeight) / 2;
//        rect.set(bodyWidth + headWidth / 2, top + headHeightMargin, bodyWidth, top + headHeightMargin + headHeight);
//
//        rect.set(powerRight, top, bodyWidth - frameWidth, getBottom());
//        paint.setColor(Color.WHITE);
//        canvas.drawRect(rect, paint);
    }
}

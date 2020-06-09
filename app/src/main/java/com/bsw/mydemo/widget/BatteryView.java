package com.bsw.mydemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
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

import com.bsw.mydemo.R;

/**
 * 电池控件
 *
 * @author leiming
 * @date 2020-5-25
 */
@SuppressWarnings("FieldCanBeLocal")
public class BatteryView extends View {
    /**
     * 当前电量
     */
    private float currentPower = 0;
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
    private float frameWidth = -1;
    /**
     * 边框内边距
     */
    private float framePadding = 0;
    /**
     * 电池head占高度比例
     */
    private float headHeightRatio = 0.3f;
    /**
     * 电池body占宽度比例
     */
    private float bodyWidthRatio = 0.9f;
    /**
     * 电量为空时的色值
     */
    private int powerEmptyColor = -1;
    /**
     * 电量颜色
     */
    private int powerColor = -1;
    /**
     * 边框颜色
     */
    private int frameColor = -1;

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
        init(attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init(attrs, defStyleAttr);
    }

    /**
     * 初始化参数
     *
     * @param attrs 属性
     */
    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs,
                R.styleable.BatteryView, defStyleAttr, 0);
        currentPower = a.getFloat(R.styleable.BatteryView_currentPower, currentPower);
        fullCharge = a.getFloat(R.styleable.BatteryView_fullCharge, fullCharge);
        aspectW2HRatio = a.getFloat(R.styleable.BatteryView_aspectW2HRatio, aspectW2HRatio);
        frameWidth = a.getDimension(R.styleable.BatteryView_frameWidth, frameWidth);
        framePadding = a.getDimension(R.styleable.BatteryView_framePadding, framePadding);
        headHeightRatio = a.getFloat(R.styleable.BatteryView_headHeightRatio, headHeightRatio);
        bodyWidthRatio = a.getFloat(R.styleable.BatteryView_bodyWidthRatio, bodyWidthRatio);
        powerEmptyColor = a.getColor(R.styleable.BatteryView_powerEmptyColor, powerEmptyColor);
        powerColor = a.getColor(R.styleable.BatteryView_powerColor, powerColor);
        frameColor = a.getColor(R.styleable.BatteryView_frameColor, frameColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 控件宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 控件高度
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);

        width = width - (left = getPaddingLeft()) - getPaddingEnd();
        height = height - getPaddingBottom() - (top = getPaddingTop());

        // 当宽高不是精确值时，为了电池好看，依据宽高比调整绘制的宽高
        drawWidth = (int) Math.min(width, height * aspectW2HRatio);
        drawHeight = (int) Math.min(height, width / aspectW2HRatio);
    }

    /**
     * 获取当前电量
     *
     * @param currentPower 当前电量
     */
    public void setCurrentPower(@IntRange(from = 0, to = 100) int currentPower) {
        this.currentPower = currentPower;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 电池身宽度
        float bodyWidth = drawWidth * bodyWidthRatio;
        float bodyHeight = bodyWidth / aspectW2HRatio;
        // 电池帽宽度
        float headWidth = drawWidth * (1 - bodyWidthRatio);

        // 绘制电池边框

        // 如果没有设置边框宽度，则设置为电池高度的 1/20
        if (frameWidth < 0) {
            frameWidth = bodyHeight / 20;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(frameWidth);
        canvas.drawRect(left, top, left + bodyWidth, top + bodyHeight, paint);

        paint.setStyle(Paint.Style.FILL);
        // 绘制电池帽
        float headHeight = headHeightRatio * bodyHeight;
        float headHeightMargin = (bodyHeight - headHeight) / 2;
        rect.set((int) (left + bodyWidth + headWidth / 2)
                , (int) (top + headHeightMargin)
                , left + drawWidth
                , (int) (top + drawHeight - headHeightMargin));
        canvas.drawRect(rect, paint);

        // 绘制电池剩余电量
        // 向内缩进电池边框的距离，以及边框的到电量显示部分的内边距
        // 电池消耗电量绘制的右侧边界(需要用绘制的总宽度减去左右边框的宽度、左右内边距的宽度)
        int powerRight = (int) (((drawWidth - framePadding * 2 - frameWidth * 2) * currentPower) / fullCharge);

        rect.set((int) (left + frameWidth + framePadding)
                , (int) (top + frameWidth + framePadding)
                , (int) (left + frameWidth + framePadding + powerRight)
                , (int) (top + bodyHeight - frameWidth - framePadding));
        paint.setColor(Color.GREEN);
        canvas.drawRect(rect, paint);

        // 如果有设置绘制电量消耗掉的部分的色值，则绘制消耗掉的部分
        if (powerEmptyColor != 0) {
            // 绘制
            rect.set((int) (left + frameWidth + framePadding + powerRight)
                    , (int) (top + frameWidth + framePadding)
                    , (int) (left + bodyWidth - frameWidth - framePadding)
                    , (int) (top + bodyHeight - frameWidth - framePadding));
            paint.setColor(powerEmptyColor);
            canvas.drawRect(rect, paint);
        }
    }
}

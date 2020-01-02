package com.bsw.mydemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.bsw.mydemo.R;

import java.util.ArrayList;
import java.util.List;

public class SeismicWaveView extends View {
    private final float WAVE_SPEED_COEFFICIENT = 0.75f;

    private int waveColor = 0xFFFFFFFF;
    private int waveNum = 5;
    private float circlePadding = 100;

    private final double waveSpeed;

    /**
     * 1~5
     */
    private int waveSpeedLevel = -2;

    private Paint paint;

    private int maxWidth;
    private boolean isStarting = false;
    private List<Double> alphaList = new ArrayList<>();
    private List<Double> startWidthList = new ArrayList<>();

    public SeismicWaveView(Context context) {
        this(context, null);
    }

    public SeismicWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeismicWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (null != attrs) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                    R.styleable.waveStyle, defStyleAttr, 0);
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                switch (attr) {
                    case R.styleable.waveStyle_wave_color:
                        waveColor = a.getColor(attr, waveColor);
                        break;

                    case R.styleable.waveStyle_wave_num:
                        waveNum = a.getColor(attr, waveNum);
                        break;

                    case R.styleable.waveStyle_circle_padding:
                        circlePadding = a.getDimension(attr, circlePadding);
                        break;

                    case R.styleable.waveStyle_wave_speed:
                        waveSpeedLevel = a.getInteger(attr, waveSpeedLevel);
                        break;
                }
            }
        }
        // 默认waveSpeed为0.75的-2次幂
        waveSpeed = Math.pow(WAVE_SPEED_COEFFICIENT, waveSpeedLevel);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(waveColor);//此处颜色可以改为自己喜欢的
        alphaList.add(255d);//圆心的不透明度
        startWidthList.add(0d);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundColor(Color.TRANSPARENT);//颜色：完全透明

        int viewWidth = getWidth();
        int viewHeight = getHeight();
        maxWidth = getMin(viewWidth, viewHeight) / 2;

        /*
         * 每减少1透明度，移动的距离
         * 由于透明度与宽度每次循环都需要设置，避免maxWidth过宽时，alpha先减到0，导致无法显示
         */
        double alphaWidth = 255.0f / maxWidth * waveSpeed;

        //依次绘制 同心圆
        for (int i = 0; i < alphaList.size(); i++) {
            double alpha = alphaList.get(i);
            double startWidth = startWidthList.get(i);
            paint.setAlpha((int) Math.ceil(alpha < 0 ? 0 : alpha));
            canvas.drawCircle(viewWidth / 2.0f, viewHeight / 2.0f, (int) Math.ceil(startWidth), paint);
            //同心圆扩散
            if (isStarting && alpha > 0 && startWidth < maxWidth) {
                alphaList.set(i, (alpha - alphaWidth));
                startWidthList.set(i, (startWidth + waveSpeed));
            }
        }

        //同心圆数量达到waveNum个，删除最外层圆
        if (isStarting && startWidthList.size() > waveNum) {
            startWidthList.remove(0);
            alphaList.remove(0);
        }

        if (isStarting && startWidthList.get(startWidthList.size() - 1) > maxWidth / waveNum) {
            alphaList.add(255d);
            startWidthList.add(0d);
        }
        //刷新界面
        invalidate();
    }

    //地震波开始/继续进行
    public void start() {
        isStarting = true;
    }

    //地震波暂停
    public void stop() {
        isStarting = false;
    }

    public boolean isStarting() {
        return isStarting;
    }

    private int getMin(int viewWidth, int viewHeight) {
        return (int) (viewWidth < viewHeight ? (viewWidth - circlePadding) : (viewHeight - circlePadding));
    }
}

package com.bsw.mydemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.bsw.mydemo.R;

/**
 * @author 半寿翁
 * @date 2019-12-31
 */
public class BswToolbar extends ViewGroup {
    /**
     *
     */
    private int titleGravity;
    /**
     * 标题字体文本
     */
    private String title;
    /**
     * 标题字体大小
     */
    private float titleSize = 32;
    /**
     * 标题字体颜色
     */
    private int titleColor = Color.parseColor("#8a000000");
    /**
     * 子标题字体大小
     */
    private String subtitle;
    /**
     * 子标题字体大小
     */
    private float subtitleSize = 26;
    /**
     * 子标题字体颜色
     */
    private int subtitleColor = Color.parseColor("#8a000000");
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 尺寸获取
     */
    private Rect rect;

    public BswToolbar(Context context) {
        this(context, null);
    }

    public BswToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BswToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        /*
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.BswToolbar, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.BswToolbar_title:
                    title = a.getString(attr);
                    break;

                case R.styleable.BswToolbar_title_size:
                    titleSize = a.getDimension(attr, titleSize);
                    break;

                case R.styleable.BswToolbar_title_color:
                    titleColor = a.getColor(attr, titleColor);
                    break;

                case R.styleable.BswToolbar_subtitle:
                    subtitle = a.getString(attr);
                    break;

                case R.styleable.BswToolbar_subtitle_size:
                    subtitleSize = a.getDimension(attr, subtitleSize);
                    break;

                case R.styleable.BswToolbar_subtitle_color:
                    subtitleColor = a.getColor(attr, subtitleColor);
                    break;

                case R.styleable.BswToolbar_title_gravity:
                    subtitleColor = a.getColor(attr, subtitleColor);
                    break;

                default:
                    break;
            }
        }
        init();
    }

    private void init() {
        setWillNotDraw(false);
        paint = new Paint();
        rect = new Rect();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置字体大小
        paint.setTextSize(titleSize);
        //	写文本
        paint.getTextBounds(title, 0, title.length(), rect);
        //canvas.drawText(text, 0 - rect.left, 0 - rect.top, paint);//	完整显示文本内容
        //居中显示文本
        canvas.drawText(title,
                0 - rect.left + (getWidth() - rect.width()) / 2.0f,
                0 - rect.top + (getHeight() - rect.height()) / 2.0f,
                paint);
    }
}

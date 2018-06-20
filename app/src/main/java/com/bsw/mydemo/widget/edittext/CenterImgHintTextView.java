package com.bsw.mydemo.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Logger;

/**
 * http://www.jb51.net/article/118161.htm
 *
 * @author leiming
 * @date 2018/4/21.
 */

public class CenterImgHintTextView extends AppCompatEditText {
    private float searchSize = 0;
    private float textSize = 0;
    private int textColor = 0xFF000000;
    private Drawable mDrawable;
    private Paint paint;

    public CenterImgHintTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitResource(context, attrs);
        InitPaint();
    }

    private void InitResource(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CenterImgHintTextView);
        float density = context.getResources().getDisplayMetrics().density;
        searchSize = mTypedArray.getDimension(R.styleable.CenterImgHintTextView_image_width, 18 * density + 0.5F);
        textColor = mTypedArray.getColor(R.styleable.CenterImgHintTextView_textColor, 0xFF848484);
        textSize = mTypedArray.getDimension(R.styleable.CenterImgHintTextView_textSize, 14 * density + 0.5F);
        mTypedArray.recycle();
    }

    private void InitPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawSearchIcon(canvas);
    }

    private void DrawSearchIcon(Canvas canvas) {
        if (this.getText().toString().length() == 0) {
            float textWidth = paint.measureText("搜索");
            float textHeight = getFontLeading(paint);

            float dx = (getWidth() - searchSize - textWidth - 8) / 2;
            float dy = (getHeight() - searchSize) / 2;

            canvas.save();
            canvas.translate(getScrollX() + dx, getScrollY() + dy);
            if (mDrawable != null) {
                mDrawable.draw(canvas);
            }
            canvas.drawText("搜索", getScrollX() + searchSize + 8, getScrollY() + (getHeight() - (getHeight() - textHeight) / 2) - paint.getFontMetrics().bottom - dy, paint);
            canvas.restore();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mDrawable == null) {
            try {
                mDrawable = getContext().getResources().getDrawable(R.mipmap.search);
                mDrawable.setBounds(0, 0, (int) searchSize, (int) searchSize);
            } catch (Exception e) {
                Logger.i(getClass().getSimpleName(), e);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mDrawable != null) {
            mDrawable.setCallback(null);
            mDrawable = null;
        }
        super.onDetachedFromWindow();
    }

    public float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.bottom - fm.top;
    }
}

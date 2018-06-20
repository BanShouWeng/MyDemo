package com.bsw.mydemo.widget.MarkerImg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.GlideUtils;
import com.bsw.mydemo.Utils.Logger;

/**
 * @author leiming
 * @date 2018/6/20.
 */
public class MarkerImgLayout extends FrameLayout {
    private Context mContext;
    private ImageView imageView;
    private ImageView bgImage;

    public MarkerImgLayout(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public MarkerImgLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public MarkerImgLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setImageBg(@DrawableRes int imgResId) {
        bgImage = new ImageView(mContext);
        FrameLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        bgImage.setLayoutParams(layoutParams);
        bgImage.setScaleType(ImageView.ScaleType.CENTER);
        bgImage.setAdjustViewBounds(true);
        bgImage.setImageResource(imgResId);
        bgImage.setOnTouchListener(onTouchListener);
        addView(bgImage);
    }

    private OnTouchListener onTouchListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (Const.notEmpty(imageView)) {
                removeView(imageView);
            }
            Logger.i("onTouchEvent", "getAction" + event.getAction() + "  getX = " + event.getX() + "  getY = " + event.getY());
            Rect rect = new Rect();
            bgImage.getGlobalVisibleRect(rect);
            if (event.getAction() == MotionEvent.ACTION_DOWN && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                imageView = new ImageView(mContext);
                FrameLayout.LayoutParams layoutParams = new LayoutParams(150, 150);
                imageView.setImageResource(R.drawable.prod_point_img);
                GlideUtils.loadImageViewAsBitmap(mContext, R.drawable.prod_point_img, imageView, true);

                imageView.measure(View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));

                layoutParams.leftMargin = (int) (event.getX() - 75);
                layoutParams.topMargin = (int) (event.getY() - 75);

                imageView.setLayoutParams(layoutParams);
                addView(imageView);
            }
            return false;
        }
    };
}
package com.bsw.mydemo.widget.cropImange;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.bsw.mydemo.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 */
public class ClipLayout extends RelativeLayout {

    private ClipZoomView mZoomImageView;
    private ClipBorderView mClipImageView;
    @SuppressWarnings("FieldCanBeLocal")
    private int measureWidth;
    @SuppressWarnings("FieldCanBeLocal")
    private int measureHeight;
    private int width;

    /**
     * mHorizontalPadding           截图宽度
     * mVerticalPadding             截图高度
     * mHorizontalPaddingPercent    截图宽度占据屏幕宽度百分比
     * mVerticalPaddingPercent      截图高度占据屏幕宽度百分比
     */
    private int mHorizontalCrop = 0;
    private int mVerticalCrop = 0;
    private float mHorizontalCropPercent = 0;
    private float mVerticalCropPercent = 0;
    private Context context;

    public ClipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mZoomImageView = new ClipZoomView(context);
        mClipImageView = new ClipBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CropImage, defStyle, 0);
        float density = context.getResources().getDisplayMetrics().density;
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            switch (ta.getIndex(i)) {
                case R.styleable.CropImage_horizontal_crop:
                    mHorizontalCrop = (int) ta.getDimension(attr, 0);
                    break;

                case R.styleable.CropImage_vertical_crop:
                    mVerticalCrop = (int) ta.getDimension(attr, 0);
                    break;

                case R.styleable.CropImage_horizontal_crop_percent:
                    mHorizontalCropPercent = ta.getFloat(attr, 0);
                    break;

                case R.styleable.CropImage_vertical_crop_percent:
                    mVerticalCropPercent = ta.getFloat(attr, 0);
                    break;
            }
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int mHorizontalPadding = 0;
        if (mHorizontalCrop != 0 && measureWidth > mHorizontalCrop) {
            mHorizontalPadding = measureWidth - mHorizontalCrop;
        } else if (mHorizontalCropPercent != 0 && mHorizontalCropPercent < 1) {
            mHorizontalPadding = (int) (measureWidth * (1 - mHorizontalCropPercent));
        }

        mZoomImageView.setHorizontalPadding(mHorizontalPadding/2);
        mClipImageView.setHorizontalPadding(mHorizontalPadding/2);

//        int mVerticalPadding = 0;
        int mVerticalPadding = measureHeight - (measureWidth - mHorizontalPadding);

        if (mVerticalCrop != 0 && measureHeight > mVerticalCrop) {
            mVerticalPadding = measureHeight - mVerticalCrop;
        } else if (mVerticalCropPercent != 0 && mVerticalCropPercent < 1) {
            mVerticalPadding = (int) (measureHeight * (1 - mVerticalCropPercent));
        }

        mZoomImageView.setVerticalPadding(mVerticalPadding / 2);
        mClipImageView.setVerticalPadding(mVerticalPadding / 2);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
    }

    /**
     * 裁切图片
     *
     * @return 裁切的图片
     */
    public String clip() {
        Bitmap bmp = mZoomImageView.clip();
        return mZoomImageView.savaBitmap(bmp);
    }

}

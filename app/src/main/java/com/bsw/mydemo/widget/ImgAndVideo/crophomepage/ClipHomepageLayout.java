package com.bsw.mydemo.widget.ImgAndVideo.crophomepage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 */
public class ClipHomepageLayout extends RelativeLayout {

    private ClipZoomHomepageView mZoomImageView;
    private ClipHomepageBorderView mClipImageView;
    private int measureWidth;
    private int measureHeigth;
    private int width;
    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 0;
    private int mVerticalPadding = 0;
    private Context context;

    public ClipHomepageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomHomepageView(context);
        mClipImageView = new ClipHomepageBorderView(context);

        android.view.ViewGroup.LayoutParams lp = new LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);

        this.addView(mZoomImageView, lp);
        this.addView(mClipImageView, lp);

//		// 计算padding的px
        width = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
        /*TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.cropimage);
        mHorizontalPadding = ta.getInteger(R.styleable.cropimage_crop_width, width);
        mVerticalPadding = ta.getInteger(R.styleable.cropimage_crop_height, 0);
        ta.recycle();*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        if (width != mHorizontalPadding) {
            mHorizontalPadding = (measureWidth - mHorizontalPadding) / 2;
        }

        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);

//        mVerticalPadding = measureHeigth - measureWidth * 276 / 670;.
        mVerticalPadding = measureHeigth - measureWidth;
        mZoomImageView.setVerticalPadding(mVerticalPadding / 2);
        mClipImageView.setVerticalPadding(mVerticalPadding / 2);
    }

    public void setImageBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mVerticalPadding
     */
    public void setVerticalPadding(int mVerticalPadding) {
        this.mVerticalPadding = mVerticalPadding;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public String clip() {
        Bitmap bmp = mZoomImageView.clip();
        return mZoomImageView.savaBitmap(bmp);
    }

}

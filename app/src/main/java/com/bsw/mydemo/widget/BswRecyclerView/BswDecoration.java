package com.bsw.mydemo.widget.BswRecyclerView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bsw.mydemo.R;

/**
 * @author 半寿翁
 * @date 2018/4/23.
 */

public class BswDecoration extends RecyclerView.ItemDecoration {

    /**
     * 分割线的宽度
     */
    private int myDividerWidth;
    /**
     * 分割线画笔
     */
    private Paint dividerPaint;
    /**
     * 分割线类型
     * {@link LimitAnnotation#BOTTOM_DECORATION 底部横线}
     * {@link LimitAnnotation#ROUND_DECORATION 描边}
     */
    private int type;

    public BswDecoration(Context context, @LimitAnnotation.DecorationType int type) {
        this.type = type;
        dividerPaint = new Paint();
        //设置分割线颜色
        dividerPaint.setColor(context.getResources().getColor(R.color.divider_line_color));
        //设置分割线宽度
        myDividerWidth = context.getResources().getDimensionPixelSize(R.dimen.line_height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = myDividerWidth;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        switch (type) {
            case LimitAnnotation.BOTTOM_DECORATION:
                int left = parent.getPaddingLeft();
                int right = parent.getWidth() - parent.getPaddingRight();
                for (int i = 0; i < childCount - 1; i++) {
                    View view = parent.getChildAt(i);
                    float bTop = view.getBottom();
                    float bBottom = view.getBottom() + myDividerWidth;
                    c.drawRect(left, bTop, right, bBottom, dividerPaint);
                }
                break;

            case LimitAnnotation.ROUND_DECORATION:
                for (int i = 0; i < childCount; i++) {
                    View view = parent.getChildAt(i);
                    float leftV = view.getLeft();
                    float rightV = view.getRight();
                    float topV = view.getTop();
                    float bottomV = view.getBottom();

                    // 顶部横线绘制
                    // noinspection UnnecessaryLocalVariable
                    float tTop = topV;
                    float tBottom = view.getTop() + myDividerWidth;
                    c.drawRect(leftV, tTop, rightV, tBottom, dividerPaint);

                    // 底部横线绘制
                    // noinspection UnnecessaryLocalVariable
                    float bTop = bottomV;
                    float bBottom = view.getBottom() + myDividerWidth;
                    c.drawRect(leftV, bTop, rightV, bBottom, dividerPaint);

                    // 左侧横线绘制
                    // noinspection UnnecessaryLocalVariable
                    float lLeft = leftV;
                    float lRight = view.getLeft() + myDividerWidth;
                    c.drawRect(lLeft, topV, lRight, bottomV, dividerPaint);

                    // 右侧横线绘制
                    // noinspection UnnecessaryLocalVariable
                    float rLeft = rightV;
                    float rRight = view.getRight() + myDividerWidth;
                    c.drawRect(rLeft, topV, rRight, bottomV, dividerPaint);
                }
                break;
        }
    }
}

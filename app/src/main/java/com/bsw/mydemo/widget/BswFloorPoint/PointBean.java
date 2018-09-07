package com.bsw.mydemo.widget.BswFloorPoint;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.Size;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.Utils.GlideUtils;
import com.bsw.mydemo.Utils.MeasureUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 半寿翁
 * @date 2018/8/28.
 */

public class PointBean {
    public static final int POSITION_DOWN = 21;
    public static final int POSITION_TOP = 22;
    public static final int POSITION_CENTER = 23;
    public static final int POSITION_LEFT = 24;
    public static final int POSITION_RIGHT = 25;

    // 不添加到.class文件中
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_DOWN, POSITION_TOP, POSITION_CENTER, POSITION_LEFT, POSITION_RIGHT})
    public @interface pointPositionLimit {
    }

    private double x;
    private double y;
    private String path;
    private int imgRes;
    private ImageView pointView;
    private int positionLimit;

    private int halfHeight = 0;
    private int halfWidth = 0;

    /**
     * @param x    横坐标
     * @param y    纵坐标
     * @param path 显示文件的路径
     */
    public PointBean(@FloatRange(from = 0, to = 1) double x, @FloatRange(from = 0, to = 1) double y, String path, @pointPositionLimit int positionLimit) {
        this.x = x;
        this.y = y;
        this.path = path;
        this.positionLimit = positionLimit;
    }

    /**
     * @param x      横坐标
     * @param y      纵坐标
     * @param imgRes 显示文件的路径
     */
    public PointBean(@FloatRange(from = 0, to = 1) double x, @FloatRange(from = 0, to = 1) double y, @DrawableRes int imgRes, @pointPositionLimit int positionLimit) {
        this.x = x;
        this.y = y;
        this.imgRes = imgRes;
        this.positionLimit = positionLimit;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getPath() {
        return path;
    }

    public int getImgRes() {
        return imgRes;
    }

    ImageView getPointView() {
        return pointView;
    }

    void setPointView(ImageView pointView) {
        this.pointView = pointView;
    }

    int getHalfWidth() {
        if (halfWidth == 0) {
            halfWidth = MeasureUtil.getWidth(pointView) / 2;
        }
        return halfWidth;
    }

    int getHalfHeight() {
        if (halfHeight == 0) {
            halfHeight = MeasureUtil.getHeight(pointView) / 2;
        }
        return halfHeight;
    }

    public int getPositionLimit() {
        return positionLimit;
    }

}

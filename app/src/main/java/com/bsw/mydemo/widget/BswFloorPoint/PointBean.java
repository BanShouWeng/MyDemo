package com.bsw.mydemo.widget.BswFloorPoint;

import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.widget.ImageView;

import com.bsw.mydemo.utils.MeasureUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author 半寿翁
 * @date 2018/8/28.
 */

public class PointBean {
    public static final int TYPE_PROPORTION = 1;
    public static final int TYPE_REAL = 2;

    /**
     * 点位类型
     * {@link PointBean#TYPE_PROPORTION 比例尺寸}
     * {@link PointBean#TYPE_REAL 真实尺寸}
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_PROPORTION, TYPE_REAL})
    public @interface PointType {
    }

    public static final int POSITION_BELOW = 21;
    public static final int POSITION_ABOVE = 22;
    public static final int POSITION_CENTER = 23;
    public static final int POSITION_LEFT = 24;
    public static final int POSITION_RIGHT = 25;

    /**
     * 横纵坐标在icon的相对位置
     * {@link PointBean#POSITION_BELOW 在横纵坐标点下方}
     * {@link PointBean#POSITION_ABOVE 在横纵坐标点上方}
     * {@link PointBean#POSITION_BELOW 横纵坐标在icon中间}
     * {@link PointBean#POSITION_LEFT 在横纵坐标点左侧}
     * {@link PointBean#POSITION_BELOW 在横纵坐标点右侧}
     */
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({POSITION_BELOW, POSITION_ABOVE, POSITION_CENTER, POSITION_LEFT, POSITION_RIGHT})
    public @interface PointPositionLimit {
    }

    private int type;

    private double x;
    private double y;
    private String path;
    private int imgRes;
    private ImageView pointView;
    private int positionLimit;

    private int halfHeight = 0;
    private int halfWidth = 0;

    /**
     * @param type 是比例（0~1），还是实际在图片上的坐标
     * @param x    横坐标
     * @param y    纵坐标
     * @param path 显示文件的路径
     */
    public PointBean(@PointType int type, double x, double y, String path, @PointPositionLimit int positionLimit) {
        this.type = type;
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
    public PointBean(@PointType int type, double x, double y, @DrawableRes int imgRes, @PointPositionLimit int positionLimit) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.imgRes = imgRes;
        this.positionLimit = positionLimit;
    }

    public int getType() {
        return type;
    }

    public double getX(double bgWidth) {
        return type == TYPE_PROPORTION ? x : (x / bgWidth);
    }

    public double getY(double bgHeight) {
        return type == TYPE_PROPORTION ? y : (y / bgHeight);
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

    public PointBean setHalfHeight(int halfHeight) {
        this.halfHeight = halfHeight;
        return this;
    }

    public void setHalfWidth(int halfWidth) {
        this.halfWidth = halfWidth;
    }

    public void resetXY(@FloatRange(from = 0, to = 1) double x, @FloatRange(from = 0, to = 1) double y) {
        this.x = x;
        this.y = y;
    }

    int getHalfWidth() {
        return halfWidth;
    }

    int getHalfHeight() {
        return halfHeight;
    }

    public int getPositionLimit() {
        return positionLimit;
    }

}

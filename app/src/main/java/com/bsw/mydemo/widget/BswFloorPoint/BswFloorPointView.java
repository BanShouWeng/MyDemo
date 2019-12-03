package com.bsw.mydemo.widget.BswFloorPoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.GlideUtils;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.MeasureUtil;
import com.bsw.mydemo.utils.ScreenUtil;
import com.bsw.mydemo.widget.photoview.OnMatrixChangedListener;
import com.bsw.mydemo.widget.photoview.OnPhotoTapListener;
import com.bsw.mydemo.widget.photoview.PhotoView;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * 在可缩放的背景图上标注点位
 * 此控件是根据点x/y位于地图百分比标注的，这也是现阶段比较准确的一种使用方式；
 * 若使用坐标的需注意，这里使用的底图承载控件是PhotoView，会根据屏幕大小约束图片，因此需要添加一个
 *
 * @author 半寿翁
 * @date 2018/8/28.
 */

public class BswFloorPointView extends RelativeLayout {
    /**
     * 当底图拉伸时，点位icon变化情况
     * {@link BswFloorPointView#KEEP_SIZE 保持icon原本大小，不随底图拉伸而变化}
     * {@link BswFloorPointView#CHANGE_SIZE 随底图拉伸而同步放大/缩小icon}
     * <p>
     * {@link BswFloorPointView#size 被配置的变化效果}
     */
    public static final int KEEP_SIZE = 56;
    public static final int CHANGE_SIZE = 57;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({KEEP_SIZE, CHANGE_SIZE})
    @interface SizeLimit {

    }

    private int size = KEEP_SIZE;

    /**
     * 由于存在有需要获取地图实际宽高的情况因此专门记录
     * {@link BswFloorPointView#cw 在当前PhotoView中的显示宽度}
     * {@link BswFloorPointView#ch 在当前PhotoView中的显示高度}
     * {@link BswFloorPointView#rw 原图的实际宽度}
     * {@link BswFloorPointView#rh 原图的实际高度}
     */
    private double cw;
    private double ch;
    private double rw;
    private double rh;

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 底图是否加载完成
     */
    private boolean isPrepare = false;

    /**
     * 底图左侧坐标
     */
    private int startL = 0;
    /**
     * 地图右侧坐标
     */
    private int startR = 0;

    /**
     * 底图载体控件（可拉伸）
     * https://github.com/chrisbanes/PhotoView
     */
    private PhotoView photoZoom;

    /**
     * 点位列表
     */
    private List<PointBean> pointList;

    /**
     * 是否允许点击标注点位
     */
    private boolean canMarker = false;

    @PointBean.PointPositionLimit
    private int positionLimit = PointBean.POSITION_CENTER;

    /**
     * 标注icon资源Id
     */
    private int imgResId = -1;
    /**
     * 背景地址
     */
    private String bgPath;

    /**
     * 充满全屏
     */
    private ViewGroup.LayoutParams lpM = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    /**
     * 自适应父布局
     */
    private ViewGroup.LayoutParams lpW = new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    /**
     * 30dp
     */
    private ViewGroup.LayoutParams lp30;
    /**
     * 当前底图坐标缓存，用于当点击标点时，计算被点击位置
     */
    private RectF rectTemp;

    public BswFloorPointView(Context context) {
        this(context, null);
    }

    public BswFloorPointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BswFloorPointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        photoZoom = new PhotoView(context);
        photoZoom.setOnMatrixChangeListener(new OnMatrixChangedListener() {

            @Override
            public void onMatrixChanged(RectF rect) {
                rectTemp = rect;
                if (isPrepare) {
                    movePoint(rect);
                } else {
                    startL = (int) rect.left;
                    startR = (int) rect.right;
                    if (startR != 0) {
                        isPrepare = true;
                        setPointLayout(1, 0, 0);
                    }
                }
            }
        });

        lp30 = new LayoutParams(
                ScreenUtil.dp2px(mContext, 30),
                ScreenUtil.dp2px(mContext, 30));
    }

    /**
     * 底图移动时，点位位置变化联动
     *
     * @param rect 移动后的底图位置信息
     */
    private void movePoint(RectF rect) {
        int moveL = (int) rect.left;
        int moveT = (int) rect.top;
        int moveR = (int) rect.right;

        double startLength = startR - startL;
        double moveLength = moveR - moveL;

        double multiple;
        if (moveLength == 0 || startLength == 0) {
            multiple = 1;
        } else {
            multiple = moveLength / startLength;
        }
        setPointLayout(multiple, moveL, moveT);
    }

    /**
     * 是否可以标注点位设置
     *
     * @param canMarker 是否可以标注点位
     * @return 当前点位布局组件
     */
    public BswFloorPointView setCanMarker(boolean canMarker) {
        this.canMarker = canMarker;
        return this;
    }

    /**
     * 设置显示的点位Icon
     *
     * @param imgResId 显示的点位Icon资源Id
     * @return 当前点位布局组件
     */
    public BswFloorPointView setImgResId(@DrawableRes int imgResId) {
        this.imgResId = imgResId;
        return this;
    }

    /**
     * 设置位置约束条件
     *
     * @param positionLimit 约束条件
     * @return 当前点位布局组件
     */
    public BswFloorPointView setPositionLimit(@PointBean.PointPositionLimit int positionLimit) {
        this.positionLimit = positionLimit;
        return this;
    }

    /**
     * 设置点位布局
     *
     * @param multiple 放大倍数
     * @param moveL    距底图左侧距离
     * @param moveT    距底图顶距离
     */
    private void setPointLayout(double multiple, double moveL, double moveT) {
        if (Const.judgeListNull(pointList) == 0) {
            return;
        }
        for (PointBean pointBean : pointList) {
            double halfHeight;
            double halfWidth;
            switch (size) {
                case CHANGE_SIZE:
                    halfHeight = pointBean.getHalfHeight() * multiple;
                    halfWidth = pointBean.getHalfWidth() * multiple;
                    break;

                case KEEP_SIZE:
                    halfHeight = pointBean.getHalfHeight();
                    halfWidth = pointBean.getHalfWidth();
                    break;

                default:
                    halfHeight = pointBean.getHalfHeight();
                    halfWidth = pointBean.getHalfWidth();
                    break;
            }
            if (halfHeight == 0 || halfWidth == 0 || cw == 0 || ch == 0) {
                return;
            }
            double xTemp = (pointBean.getX(rw) * cw * multiple + moveL);
            double yTemp = (pointBean.getY(rh) * ch * multiple + moveT);
            switch (pointBean.getPositionLimit()) {
                case PointBean.POSITION_CENTER:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight), (int) (xTemp + halfWidth), (int) (yTemp + halfHeight));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;

                case PointBean.POSITION_ABOVE:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight * 2), (int) (xTemp + halfWidth), (int) (yTemp));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;

                case PointBean.POSITION_BELOW:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) yTemp, (int) (xTemp + halfWidth), (int) (yTemp + halfHeight * 2));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;

                case PointBean.POSITION_LEFT:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth * 2), (int) (yTemp - halfHeight), (int) xTemp, (int) (yTemp + halfHeight));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;

                case PointBean.POSITION_RIGHT:
                    pointBean.getPointView().layout((int) xTemp, (int) (yTemp - halfHeight), (int) (xTemp + halfWidth * 2), (int) (yTemp + halfHeight));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;

                default:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight), (int) (xTemp + halfWidth), (int) (yTemp + halfHeight));
                    pointBean.getPointView().setVisibility(VISIBLE);
                    break;
            }
        }
    }

    /**
     * 设置需展示的点位列表
     *
     * @param pointList 点位列表
     * @return 当前点位布局组件
     */
    public BswFloorPointView setPointList(List<PointBean> pointList) {
        removeAllViews();
        this.pointList = new ArrayList<>();
        for (PointBean pointBean : pointList) {
            pointBean.setPointView(new ImageView(mContext));
            this.pointList.add(pointBean);
        }
        this.pointList = pointList;
        return this;
    }

    /**
     * 设置背景图片
     *
     * @param bgPath 背景图片地址
     * @return 点位布局组件
     */
    public BswFloorPointView setFloorBackground(String bgPath) {
        this.bgPath = bgPath;
        return this;
    }

    /**
     * 设置图片尺寸随背景图变化、或是固定尺寸
     *
     * @param size 尺寸情况
     */
    public BswFloorPointView setSize(@SizeLimit int size) {
        this.size = size;
        return this;
    }

    /**
     * 在平面图上绘制点位
     */
    public void paint() {
        photoZoom.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                cw = MeasureUtil.getWidth(photoZoom);
                updateList();
            }
        });
        addView(photoZoom, lpM);
        SimpleTarget simpleTarget = new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                photoZoom.setImageBitmap(resource);
                Logger.i(getName(), "bg height : " + resource.getHeight() + "bg width : " + resource.getWidth());
                rw = resource.getWidth();
                rh = resource.getHeight();
                updateList();
            }
        };
        GlideUtils.loadImageView(mContext, bgPath, simpleTarget);

        if (Const.judgeListNull(pointList) > 0) {
            for (int i = 0; i < pointList.size(); i++) {
                final PointBean pointBean = pointList.get(i);
                final ImageView imageView = pointBean.getPointView();
                removeView(imageView);
                addView(imageView, lp30);
                int imgRes = pointBean.getImgRes();
                final int finalI = i;
                SimpleTarget target = new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
//                        pointBean.setHalfWidth(resource.setWidth() / 2);
//                        pointBean.setHalfHeight(resource.getHeight() / 2);
//                        pointList.add(finalI, pointBean);
                        Logger.i(getName(), "icon height : " + resource.getHeight() + "icon width : " + resource.getWidth());
                        pointList.get(finalI)
                                .setHalfHeight(resource.getHeight() / 2)
                                .setHalfWidth(resource.getWidth() / 2);
                    }
                };
                if (imgRes == 0) {
                    GlideUtils.loadImageView(mContext, pointBean.getPath(), target);
                } else {
                    GlideUtils.loadImageView(mContext, imgRes, target);
                }
            }
        }

        if (canMarker) {
            photoZoom.setOnPhotoTapListener(onPhotoTapListener);
        }
    }

    /**
     * 更新点位列表
     */
    private void updateList() {
        // 防止获取ch时除数为零
        if (cw * rw == 0) {
            return;
        }

        // 计算ch
        if (cw == 0 || ch == 0) {
            ch = rh / rw * cw;
        }

        // 只有当cw、ch都有值时刷新
        if (cw * ch == 0) {
            return;
        }

        Const.threadPoolExecute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            photoZoom.update();
                        }
                    });
                }
            }
        });
    }

    /**
     * 当背景图片被点击时的回调
     */
    private OnPhotoTapListener onPhotoTapListener = new OnPhotoTapListener() {
        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            if (Const.judgeListNull(pointList) == 0) {
                pointList = new ArrayList<>();
                final ImageView imageView = new ImageView(mContext);
                imageView.setVisibility(INVISIBLE);
                addView(imageView, lp30);
                final PointBean pointBean = new PointBean(PointBean.TYPE_PROPORTION, x, y, imgResId, positionLimit);
                SimpleTarget target = new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        pointBean.setHalfWidth(resource.getWidth() / 2);
                        pointBean.setHalfHeight(resource.getHeight() / 2);
                        pointBean.setPointView(imageView);
                        pointList.add(pointBean);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } finally {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            movePoint(rectTemp);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                };
                GlideUtils.loadImageView(mContext, imgResId, target);

            } else {
                pointList.get(0).resetXY(x, y);
                movePoint(rectTemp);
            }
        }
    };

    /**
     * 获取标注点位列表
     *
     * @return 标注点位列表
     */
    public List<Size> getSizeList() {
        List<Size> sizeList = new ArrayList<>();
        if (Const.judgeListNull(pointList) > 0) {
            for (PointBean pointBean : pointList) {
                sizeList.add(new Size(rw * pointBean.getX(rw), rh * pointBean.getY(rh)));
            }
        }
        return sizeList;
    }

    /**
     * 是否已添加点位
     *
     * @return 是否添加点位
     */
    public boolean isMarked() {
        return Const.judgeListNull(pointList) > 0;
    }

    /**
     * 获取标注点位（若为列表，则获取第一个点位）
     *
     * @return 标注列表中的第一个点位
     */
    public Size getSize() {
        PointBean pointBean;
        if (Const.judgeListNull(pointList) > 0) {
            pointBean = pointList.get(0);
            return new Size(rw * pointBean.getX(rw), rh * pointBean.getY(rh));
        } else {
            return null;
        }
    }

    /**
     * 更新点位信息
     */
    public void update() {
        photoZoom.update();
    }

    /**
     * 尺寸工具，用于获取
     */
    public class Size {
        private double width;
        private double height;

        Size(double width, double height) {
            this.width = width;
            this.height = height;
        }

        public double getHeight() {
            return height;
        }

        public double getWidth() {
            return width;
        }
    }

    private String getName() {
        return getClass().getSimpleName();
    }
}

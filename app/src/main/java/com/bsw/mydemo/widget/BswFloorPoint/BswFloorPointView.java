package com.bsw.mydemo.widget.BswFloorPoint;

import android.app.Activity;
import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bsw.mydemo.Utils.Const;
import com.bsw.mydemo.Utils.GlideUtils;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.TimerUtils;
import com.bsw.mydemo.widget.photoview.OnMatrixChangedListener;
import com.bsw.mydemo.widget.photoview.OnPhotoTapListener;
import com.bsw.mydemo.widget.photoview.PhotoView;

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
    public static final int KEEP_SIZE = 56;
    public static final int CHANGE_SIZE = 57;
    /**
     * 当前显示图片的宽高，用于获取点位相对位置
     */
    private double cw;
    private double ch;
    /**
     * 图片实际宽高，用于获取标记点位
     */
    private double rw;
    private double rh;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({KEEP_SIZE, CHANGE_SIZE})
    @interface SizeLimit {

    }

    private final String imgPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530278301610&di=f09a4c1eb4436d128f3e49220f4244d0&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161115%2F6163765431c44d538b37d6efb32ee885_th.jpg";

    private final Context mContext;
    private boolean isPrepare = false;
    private int startL = 0;
    private int startR = 0;

    private PhotoView photoZoom;

    private List<PointBean> pointList;

    private int maxCount = - 1;

    private boolean canMarker = false;

    private String bgPath;

    private ViewGroup.LayoutParams lpM = new LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);

    private ViewGroup.LayoutParams lpW = new LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    private ViewGroup.LayoutParams lp100 = new LayoutParams(
            100,
            100);
    private RectF rectTemp;

    private int size = KEEP_SIZE;

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
        photoZoom.setOnMatrixChangeListener(onMatrixChangedListener);
    }

    private void moveGif(RectF rect) {
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
        setGifLayout(multiple, moveL, moveT);
    }

    public BswFloorPointView setCanMarker(boolean canMarker) {
        this.canMarker = canMarker;
        return this;
    }

    public BswFloorPointView setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        return this;
    }

    private void setGifLayout(double multiple, double moveL, double moveT) {
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
            if (halfHeight == 0 || halfWidth == 0) {
                return;
            }
            double xTemp = (pointBean.getX() * cw * multiple + moveL);
            double yTemp = (pointBean.getY() * ch * multiple + moveT);
            Logger.i(getName(), "xyxyxyxyx  xTemp = " + xTemp + " &&&&&&& yTemp = " + yTemp + " &&&&&&& halfWidth = " + halfWidth + " &&&&&&& halfHeight = " + halfHeight);
            switch (pointBean.getPositionLimit()) {
                case PointBean.POSITION_CENTER:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight), (int) (xTemp + halfWidth), (int) (yTemp + halfHeight));
                    break;

                case PointBean.POSITION_TOP:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight * 2), (int) (xTemp + halfWidth), (int) (yTemp));
                    break;

                case PointBean.POSITION_DOWN:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) yTemp, (int) (xTemp + halfWidth), (int) (yTemp + halfHeight * 2));
                    break;

                case PointBean.POSITION_LEFT:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth * 2), (int) (yTemp - halfHeight), (int) xTemp, (int) (yTemp + halfHeight));
                    break;

                case PointBean.POSITION_RIGHT:
                    pointBean.getPointView().layout((int) xTemp, (int) (yTemp - halfHeight), (int) (xTemp + halfWidth * 2), (int) (yTemp + halfHeight));
                    break;

                default:
                    pointBean.getPointView().layout((int) (xTemp - halfWidth), (int) (yTemp - halfHeight), (int) (xTemp + halfWidth), (int) (yTemp + halfHeight));
                    break;
            }
        }
    }

    private String getName() {
        return getClass().getSimpleName();
    }

    public BswFloorPointView setPointList(List<PointBean> pointList) throws NullPointerException {
        this.pointList = new ArrayList<>();
        for (PointBean pointBean : pointList) {
            pointBean.setPointView(new ImageView(mContext));
            this.pointList.add(pointBean);
        }
        this.pointList = pointList;
        return this;
    }

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

    public void paint() {
        addView(photoZoom, lpM);
        GlideUtils.loadImageView(mContext, bgPath, photoZoom, new GlideUtils.ImgSizeCallBack() {

            @Override
            public void getImgSize(int width, int height) {
                rw = width;
                rh = height;
            }
        });
        for (PointBean pointBean : pointList) {
            ImageView imageView = pointBean.getPointView();
            addView(imageView, lp100);
            int imgRes = pointBean.getImgRes();
            if (imgRes == 0) {
                GlideUtils.loadImageView(mContext, pointBean.getPath(), imageView);
            } else {
                GlideUtils.loadImageView(mContext, imgRes, imageView);
            }
        }
        new TimerUtils(2000, 50, new TimerUtils.OnBaseTimerCallBack() {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    cw = photoZoom.getDrawable().getBounds().width();
                    ch = photoZoom.getDrawable().getBounds().height();
                    photoZoom.update();
                    Logger.i(getName(), "real ***** ch = " + ch + " ***** cw = " + cw);
                } catch (NullPointerException e) {
                    Logger.i(getName(), "稍等");
                }
            }

            @Override
            public void onFinish() {
            }
        }).start();
        if (canMarker) {
            photoZoom.setOnPhotoTapListener(onPhotoTapListener);
        }
    }

    public List<Size> getSizeList() {
        List<Size> sizeList = new ArrayList<>();
        if (Const.judgeListNull(pointList) > 0) {
            for (PointBean pointBean : pointList) {
                sizeList.add(new Size(rw * pointBean.getX(), rh * pointBean.getY()));
            }
        }
        return sizeList;
    }

    private OnPhotoTapListener onPhotoTapListener = new OnPhotoTapListener() {
        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            ImageView imageView = new ImageView(mContext);
            addView(imageView, lp100);
            GlideUtils.loadImageView(mContext, imgPath, imageView);
            PointBean pointBean = new PointBean(x, y, imgPath, PointBean.POSITION_CENTER);
            pointBean.setPointView(imageView);
            int overSize = Const.judgeListNull(pointList) - maxCount;
            pointList.add(pointBean);
            if (overSize > 0) {
                if (overSize == 1) {
                    pointList.remove(0);
                }
                else {
                    for (int i = overSize - 1; i >= 0; i--)
                        pointList.remove(i);
                }
            }
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
                                moveGif(rectTemp);
                            }
                        });
                    }
                }
            }).start();
        }
    };

    private OnMatrixChangedListener onMatrixChangedListener = new OnMatrixChangedListener() {

        private int startT;

        @Override
        public void onMatrixChanged(RectF rect) {
            rectTemp = rect;
            Logger.i(getName(), "isPrepare = " + isPrepare);
            if (isPrepare) {
                moveGif(rect);
            } else {
                startL = (int) rect.left;
                startR = (int) rect.right;
                startT = (int) rect.top;
                if (startR != 0) {
                    isPrepare = true;
                    setGifLayout(1, 0, 0);
                }
            }
        }
    };

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
}
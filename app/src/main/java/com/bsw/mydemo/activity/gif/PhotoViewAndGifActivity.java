package com.bsw.mydemo.activity.gif;

import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.GlideUtils;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.Utils.MeasureUtil;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.photoview.OnMatrixChangedListener;
import com.bsw.mydemo.widget.photoview.PhotoView;
import com.bumptech.glide.Glide;

public class PhotoViewAndGifActivity extends BaseActivity {

    private String imgPath = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1530278301610&di=f09a4c1eb4436d128f3e49220f4244d0&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20161115%2F6163765431c44d538b37d6efb32ee885_th.jpg";
    private final int x = 100;
    private final int y = 200;

    private int startL = 0;
    private int startR = 0;
    private int startT = 0;
    private int startB = 0;

    private boolean firstIn = true;

    private PhotoView photoZoom;
    private ImageView gitIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_view_and_gif;
    }

    @Override
    protected void findViews() {
        photoZoom = getView(R.id.photo_zoom);
        gitIcon = getView(R.id.git_icon);
    }

    @Override
    protected void formatViews() {
        photoZoom.setOnMatrixChangeListener(onMatrixChangedListener);
        GlideUtils.loadImageView(context, imgPath, gitIcon);
    }

    @Override
    protected void formatData() {
        photoZoom.setImageResource(R.drawable.wallpaper);
    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {

    }

    private void moveGif(RectF rect) {
        int moveL = (int) rect.left;
        int moveT = (int) rect.top;
        int moveR = (int) rect.right;
        int moveB = (int) rect.bottom;

        int xTemp = x / (startR - startL == 0 ? 1 : (startR - startL)) * (moveR - moveL) + moveL;
        int yTemp = y / (startB - startT == 0 ? 1 : (startB - startT)) * (moveB - moveT) + moveT;

        Logger.i(getName(), "start **** startL = " + startL + " **** moveT = " + moveT + " **** moveR = " + moveR + " **** moveB = " + moveB + " **** xTemp = " + xTemp + " **** yTemp = " + yTemp);
        Logger.i(getName(), "move **** moveL = " + moveL + " **** moveT = " + moveT + " **** moveR = " + moveR + " **** moveB = " + moveB + " **** xTemp = " + xTemp + " **** yTemp = " + yTemp);
        setGifLayout(xTemp, yTemp);
    }

    private void setGifLayout(int x, int y) {
        Logger.i(getName(), "xyxyxyxyx  x = " + x + " &&&&&&& y = " + y);
        int halfWidth = MeasureUtil.getWidth(gitIcon) / 2;
        int halfHeight = MeasureUtil.getHeight(gitIcon) / 2;
        gitIcon.layout(x - halfWidth, y - halfHeight, x + halfWidth, y + halfHeight);
    }

    private OnMatrixChangedListener onMatrixChangedListener = new OnMatrixChangedListener() {
        @Override
        public void onMatrixChanged(RectF rect) {
            if (firstIn) {
                firstIn = false;
                startL = (int) rect.left;
                startB = (int) rect.bottom;
                startR = (int) rect.right;
                startT = (int) rect.top;
                setGifLayout(x + startL, y + startT);
            } else {
                moveGif(rect);
            }
        }
    };
}

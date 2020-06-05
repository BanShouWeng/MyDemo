package com.bsw.mydemo.widget.PopupWindows;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.text.Spanned;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.MeasureUtil;
import com.bsw.mydemo.utils.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class CustomPopupWindows {
    private PopupParam location;

    private PopupWindow popu;
    private View mPopupView;
    private View clickView;
    private WeakReference<Context> weakReference;

    public static CustomPopupWindows getInstance(Context context, View view, @LayoutRes int layoutId, PopupParam location) {
        return new CustomPopupWindows(context, view, layoutId, location);
    }

    public static CustomPopupWindows getInstance(Context context, View view, @LayoutRes int layoutId) {
        return new CustomPopupWindows(context, view, layoutId, null);
    }

    private CustomPopupWindows(Context context, View view, int layoutId, PopupParam location) {
        if (null == context) {
            return;
        }
        mPopupView = View.inflate(context, layoutId, null);
        weakReference = new WeakReference<>(context);
        clickView = view;
        this.location = location;
    }

    private void backgroundAlpha(float bgAlpha) {
        Context context = weakReference.get();
        if (!(context instanceof Activity)) {
            return;
        }
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    /**
     * 展示弹窗
     *
     * @param popupListener
     */
    public void showPopu(InitCustomPopupListener popupListener) {
        showPopu(popupListener, null);
    }

    /**
     * 展示弹窗
     *
     * @param popupListener 监听回调
     * @param tag           复用标识
     */
    public CustomPopupWindows showPopu(InitCustomPopupListener popupListener, Object tag) {
        Context context = weakReference.get();
        if (popu != null && popu.isShowing()) {
            popu.dismiss();
            return this;
        }
        if (null == context) {
            return null;
        }

        if (null != popupListener) {
            popupListener.initPopup(new PopupHolder(context, mPopupView), tag);
        }

        popu = new PopupWindow(context);
        popu.setContentView(mPopupView);
        popu.setFocusable(true);
        popu.setOutsideTouchable(true);
        popu.setBackgroundDrawable(new BitmapDrawable());
        popu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });

        /*
         * 若没有位置信息，则默认在控件左下角弹出
         */
        if (null == location) {
            popu.setWidth(clickView.getWidth());
            int[] location = new int[2];
            clickView.getLocationOnScreen(location);

            popu.showAtLocation(clickView, Gravity.NO_GRAVITY, location[0], clickView.getHeight() + location[1]);
        } else {
            if (location.getRelativeReference() == PopupParam.RELATIVE_VIEW) {
                int[] clickViewLocation = new int[2];
                clickView.getLocationOnScreen(clickViewLocation);

                if (location.isSetWindowWidth()) {
                    popu.setWidth(location.getPopWindowWidth() == PopupParam.DEFAULT_NUM ? clickView.getWidth() : location.getPopWindowWidth());
                }
                if (location.isSetWindowHeight()) {
                    popu.setHeight(location.getPopWindowHeight() == PopupParam.DEFAULT_NUM ? clickView.getHeight() : location.getPopWindowHeight());
                }

                popu.showAtLocation(clickView
                        , location.getShowPosition()
                        , location.getMarginLeft() == PopupParam.DEFAULT_NUM ? clickViewLocation[0] : location.getMarginLeft()
                        , location.getMarginTop() == PopupParam.DEFAULT_NUM ? (clickView.getHeight() + clickViewLocation[1]) : location.getMarginTop());
            } else {
                int screenWidth = ScreenUtil.getScreenWidth(context);
                int screenHeight = ScreenUtil.getScreenHeight(context);

                if (location.isSetWindowWidth()) {
                    popu.setWidth(location.getPopWindowWidth() == PopupParam.DEFAULT_NUM ? screenWidth : location.getPopWindowWidth());
                }
                if (location.isSetWindowHeight()) {
                    popu.setHeight(location.getPopWindowHeight() == PopupParam.DEFAULT_NUM ? screenHeight : location.getPopWindowHeight());
                }

                popu.showAtLocation(clickView
                        , location.getShowPosition()
                        , location.getMarginLeft() == PopupParam.DEFAULT_NUM ? 0 : location.getMarginLeft()
                        , location.getMarginTop() == PopupParam.DEFAULT_NUM ? 0 : location.getMarginTop());
            }
        }
        backgroundAlpha(null == location ? 1f : location.getAlpha());
        return this;
    }

    /**
     * 是否处于展示状态
     *
     * @return 是否处于展示状态
     */
    public boolean isShowing() {
        return Const.notEmpty(popu) && popu.isShowing();
    }

    /**
     * 是否处于展示状态
     */
    public void dismiss() {
        try {
            popu.dismiss();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static class PopupHolder {
        private View mPopupView;

        private SparseArray<View> mViews = new SparseArray<>();//集合类，layout里包含的View,以view的id作为key，value是view对象
        @SuppressWarnings("FieldCanBeLocal")
        private Context mContext;//上下文对象

        @Retention(RetentionPolicy.SOURCE)
        @IntDef({View.VISIBLE, View.GONE, View.INVISIBLE})
        @interface Visibility {
        }

        private PopupHolder(Context mContext, View mPopupView) {
            this.mPopupView = mPopupView;
            this.mContext = mContext;
        }

        @SuppressWarnings("unchecked")
        public <T extends View> T getView(@IdRes int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mPopupView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T) view;
        }

        public ImageView getImageView(@IdRes int viewId) {
            return getView(viewId);
        }

        public PopupHolder setText(@IdRes int viewId, String value) {
            TextView view = getView(viewId);
            view.setText(value);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setTextWithDrawables(@IdRes int viewId, @StringRes int stringRes, @DrawableRes int left,
                                                @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
            TextView view = getView(viewId);
            view.setText(stringRes);
            view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setTextWithDrawables(@IdRes int viewId, String string, @DrawableRes int left,
                                                @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
            TextView view = getView(viewId);
            view.setText(string);
            view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setTextDrawables(@IdRes int viewId, @DrawableRes int left,
                                            @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
            TextView view = getView(viewId);
            view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setText(@IdRes int viewId, Spanned value) {
            TextView view = getView(viewId);
            view.setText(value);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setText(@IdRes int viewId, @StringRes int valueId) {
            TextView view = getView(viewId);
            view.setText(valueId);
            if (view instanceof EditText) {
                ((EditText) view).setSelection(view.getText().length());
            }
            return this;
        }

        public PopupHolder setVisibility(@IdRes int viewId, @PopupHolder.Visibility int visibility) {
            View view = getView(viewId);
            view.setVisibility(visibility);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public int getVisibility(@IdRes int viewId) {
            return getView(viewId).getVisibility();
        }

        /**
         * 设置文本以及色值
         *
         * @param viewId 设置文本的View的Id
         * @param color  设置的颜色
         * @param value  设置的文字
         * @return holder
         */
        public PopupHolder setText(@IdRes int viewId, int color, String value) {
            TextView view = getView(viewId);
            view.setText(value);
            view.setTextColor(color);
            return this;
        }

        public PopupHolder setImageRes(@IdRes int viewId, @DrawableRes int resId) {
            ((ImageView) getView(viewId)).setImageResource(resId);
            return this;
        }

        public PopupHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
            ((ImageView) getView(viewId)).setImageBitmap(bitmap);
            return this;
        }

        public PopupHolder setBackground(@IdRes int viewId, @DrawableRes int resId) {
            getView(viewId).setBackgroundResource(resId);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public PopupHolder setClickListener(@IdRes int viewId, View.OnClickListener listener) {
            getView(viewId).setOnClickListener(listener);
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public PopupHolder setTouchListener(@IdRes int viewId, View.OnTouchListener listener) {
            getView(viewId).setOnTouchListener(listener);
            return this;
        }

        public PopupHolder setClickListener(View.OnClickListener listener) {
            mPopupView.setOnClickListener(listener);
            return this;
        }
    }

    /**
     * Pupop初始化监听
     */
    public interface InitCustomPopupListener {
        /**
         * Pupop初始化监听
         *
         * @param holder 布局持有者
         * @param tag    标识
         */
        void initPopup(PopupHolder holder, Object tag);
    }


}

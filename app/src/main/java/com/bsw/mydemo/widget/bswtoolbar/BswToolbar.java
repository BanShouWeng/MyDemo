package com.bsw.mydemo.widget.bswtoolbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义Toolbar
 *
 * @author 半寿翁
 * @date 2019-12-31
 */
public class BswToolbar extends FrameLayout {
    /**
     * 通用没有时显示的数字
     */
    private final int NUMBER_COMMON_NULL = 0;
    /**
     * 通用减半的数字
     */
    private final float NUMBER_COMMON_HALF = 2f;

    /**
     * 默认title字体大小
     */
    private final float DEFAULT_TEXT_SIZE_TITLE = 32;
    /**
     * 默认子标题字体大小
     */
    private final float DEFAULT_TEXT_SIZE_SUBTITLE = 26;
    /**
     * 默认返回文字字体大小
     */
    private final float DEFAULT_TEXT_SIZE_BACK = 26;

    /**
     * 默认title文本右对齐时，与边缘的距离
     */
    private final float DEFAULT_PADDING_TITLE_START = 30;
    /**
     * 标题行间距
     */
    private final float DEFAULT_PADDING_TITLE_SPACING = 15;
    /**
     * 返回文字与icon之间的默认间距
     */
    private final float DEFAULT_PADDING_BTN = 15;
    /*--------------------------------------RectType-----------------------------------------*/
    /**
     * 通用RECT
     */
    private final int RECT_TYPE_COMMON = 0x110;
    /**
     * title的rect
     */
    private final int RECT_TYPE_TITLE = 0x110;
    /**
     * subtitle的rect
     */
    private final int RECT_TYPE_SUBTITLE = 0x111;
    /**
     * back的icon的rect
     */
    private final int RECT_TYPE_BACK_ICON = 0x112;
    /**
     * back的文字的rect
     */
    private final int RECT_TYPE_BACK_TEXT = 0x113;
    /**
     * 用于批量关闭的rect
     */
    private final int RECT_TYPE_CLOSE_ICON = 0x114;
    /**
     * 返回点击判定区域
     */
    private final int RECT_TYPE_BACK_CLICK = 0x115;
    /**
     * 批量关闭判定区域
     */
    private final int RECT_TYPE_CLOSE_CLICK = 0x116;
    /*---------------------------------------基本参数------------------------------------------*/
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 画笔缓存
     */
    private final ConcurrentHashMap<Integer, Paint> paintMap = new ConcurrentHashMap<>();
    /**
     * 尺寸缓存
     */
    private final ConcurrentHashMap<Integer, Rect> rectMap = new ConcurrentHashMap<>();
    /**
     * 通用画笔，无特殊情况，为节省资源，均使用该画笔
     */
    private Paint paint;
    /**
     * 子控件数量
     */
    private int childCount;
    /**
     * 高度测量模式
     * {@link MeasureSpec#EXACTLY 精确}
     * {@link MeasureSpec#UNSPECIFIED 不确定}
     * {@link MeasureSpec#AT_MOST 最大值}
     */
    private int heightMeasureMode;

    private OnToolbarBtnClickListener btnClickListener;

    /*---------------------------------------控件属性------------------------------------------*/
    /**
     * title文字位置：左侧（默认，为左侧功能按钮预留位置）
     */
    public static final int TITLE_GRAVITY_RIGHT = 0;
    /**
     * title文字位置：居中
     */
    public static final int TITLE_GRAVITY_CENTER = 1;
    /**
     * 点击事件X坐标缓存，用于抬手时取消计算
     */
    private int clickDownX;
    /**
     * 点击事件Y坐标缓存，用于抬手时取消计算
     */
    private int clickDownY;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TITLE_GRAVITY_RIGHT, TITLE_GRAVITY_CENTER})
    @interface TitleGravity {

    }

    /**
     * title文字位置
     * {@link BswToolbar#TITLE_GRAVITY_RIGHT 左侧，默认}
     * {@link BswToolbar#TITLE_GRAVITY_CENTER 居中}
     */
    private int titleGravity;
    /**
     * title文本右对齐时，与边缘的距离
     */
    private float titlePadding = DEFAULT_PADDING_TITLE_START;
    /**
     * 标题字体文本
     */
    private String title;
    /**
     * 标题字体大小
     */
    private float titleSize = DEFAULT_TEXT_SIZE_TITLE;
    /**
     * 标题字体颜色
     */
    private int titleColor = Color.parseColor("#8A000000");
    /**
     * 标题行间距
     */
    private float titleRowSpacing = DEFAULT_PADDING_TITLE_SPACING;
    /**
     * 子标题字体大小
     */
    private String subtitle;
    /**
     * 子标题字体大小
     */
    private float subtitleSize = DEFAULT_TEXT_SIZE_SUBTITLE;
    /**
     * 子标题字体颜色
     */
    private int subtitleColor = Color.parseColor("#8A000000");
    /**
     * 返回按钮Icon
     */
    private Drawable backDrawable;
    /**
     * 返回按钮文本
     */
    private String backText;
    /**
     * 返回按钮文本字体大小
     */
    private float backTextSize = DEFAULT_TEXT_SIZE_BACK;
    /**
     * 返回按钮文本字体大小
     */
    private int backColor = NUMBER_COMMON_NULL;
    /**
     * 返回按钮文本字体大小
     */
    private int backTextColor = NUMBER_COMMON_NULL;
    /**
     * 返回按钮文本字体大小
     */
    private int backIconColor = NUMBER_COMMON_NULL;
    /**
     * 返回Icon与Text之间的间距
     */
    private float backPadding = DEFAULT_PADDING_BTN;
    /**
     * 返回键显示类型：文本
     */
    public static final int BACK_TYPE_TEXT = 10;
    /**
     * 返回键显示类型：图标
     */
    public static final int BACK_TYPE_ICON = 11;
    /**
     * 返回键显示类型：图标 + 文本（如返回图标 + “返回”二字）
     */
    public static final int BACK_TYPE_BOTH = 12;

    /**
     * 返回键显示类型
     * {@link BswToolbar#BACK_TYPE_TEXT 文本}
     * {@link BswToolbar#BACK_TYPE_ICON 图标}
     * {@link BswToolbar#BACK_TYPE_BOTH 图标 + 文字}
     */
    private int backType;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            BACK_TYPE_BOTH
            , BACK_TYPE_ICON
            , BACK_TYPE_TEXT
    })
    @interface BackType {

    }

    /**
     * 是否显示返回按钮
     */
    private boolean backDisplay = false;
    /**
     * 点击title文本时，是否与返回按钮有相同的联动
     * 若title与返回按钮有相同联动时，不会显示{@link BswToolbar#closeDrawable}
     */
    private boolean canTitleBackLinkage = false;
    /**
     * 批量关闭Icon
     */
    private Drawable closeDrawable;
    /**
     * 批量关闭Icon返回之间的间距
     */
    private float closePadding = 5;
    /**
     * 批量关闭Icon的颜色
     * 若没设置默认显示{@link BswToolbar#backColor}
     */
    private int closeColor = NUMBER_COMMON_NULL;
    /**
     * 是否显示批量关闭按钮
     */
    private boolean closeDisplay = false;
    /*-----------------------------------控件属性变更标识--------------------------------------*/
    /**
     * 标题是否更新
     */
    private boolean isTitleUpdate;
    /**
     * 子标题是否更新
     */
    private boolean isSubtitleUpdate;
    /**
     * 返回按钮文字是否更新
     */
    private boolean isBackTextUpdate;
    /**
     * 返回按钮icon是否更新
     */
    private boolean isBackIconUpdate;
    /**
     * 返回按钮icon是否更新
     */
    private boolean isCloseIconUpdate;
    /**
     * title点击与返回联动的设定更新
     */
    private boolean isTitleClickLinkageUpdate;

    public BswToolbar(Context context) {
        this(context, null);
    }

    public BswToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BswToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        /*
         * 获得所有自定义的参数的值
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.BswToolbar, defStyleAttr, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.BswToolbar_title_gravity:
                    titleGravity = a.getInteger(attr, TITLE_GRAVITY_RIGHT);
                    break;

                case R.styleable.BswToolbar_title_padding:
                    titlePadding = a.getDimension(attr, titlePadding);
                    break;

                case R.styleable.BswToolbar_title:
                    title = a.getString(attr);
                    break;

                case R.styleable.BswToolbar_title_size:
                    titleSize = a.getDimension(attr, titleSize);
                    break;

                case R.styleable.BswToolbar_title_color:
                    titleColor = a.getColor(attr, titleColor);
                    break;

                case R.styleable.BswToolbar_subtitle:
                    subtitle = a.getString(attr);
                    break;

                case R.styleable.BswToolbar_subtitle_size:
                    subtitleSize = a.getDimension(attr, subtitleSize);
                    break;

                case R.styleable.BswToolbar_subtitle_color:
                    subtitleColor = a.getColor(attr, subtitleColor);
                    break;

                case R.styleable.BswToolbar_title_row_spacing:
                    titleRowSpacing = a.getDimension(attr, titleRowSpacing);
                    break;

                case R.styleable.BswToolbar_back_src:
                    backDrawable = a.getDrawable(attr);
                    backDisplay = true;
                    break;

                case R.styleable.BswToolbar_can_title_back_linkage:
                    canTitleBackLinkage = a.getBoolean(attr, false);
                    if (canTitleBackLinkage && closeDisplay) {
                        closeDisplay = false;
                    }
                    break;

                case R.styleable.BswToolbar_back_type:
                    backType = a.getInteger(attr, BACK_TYPE_BOTH);
                    break;

                case R.styleable.BswToolbar_back_text:
                    backText = a.getString(attr);
                    backDisplay = true;
                    break;

                case R.styleable.BswToolbar_back_padding:
                    backPadding = a.getDimension(attr, backPadding);
                    break;

                case R.styleable.BswToolbar_back_text_size:
                    backTextSize = a.getDimension(attr, backTextSize);
                    break;

                case R.styleable.BswToolbar_back_color:
                    backColor = a.getColor(attr, backColor);
                    break;

                case R.styleable.BswToolbar_back_text_color:
                    backTextColor = a.getColor(attr, backTextColor);
                    break;

                case R.styleable.BswToolbar_back_icon_color:
                    backIconColor = a.getColor(attr, backIconColor);
                    break;

                case R.styleable.BswToolbar_close_src:
                    closeDrawable = a.getDrawable(attr);
                    // 若title与返回按钮联动，则不显示关闭按钮
                    closeDisplay = !canTitleBackLinkage;
                    break;

                case R.styleable.BswToolbar_close_color:
                    closeColor = a.getColor(attr, closeColor);
                    break;

                case R.styleable.BswToolbar_close_padding:
                    closePadding = a.getDimension(attr, closePadding);
                    break;

                default:
                    break;
            }
        }
        init();
    }

    private void init() {
        // ViewGroup默认不会开启绘制，通过setWillNotDraw设置为自行绘制
        setWillNotDraw(false);
        // 通用画笔
        paint = new Paint();
    }

    /**
     * 测量布局尺寸，由于为title，默认宽度均为match_parent，因此只测量高度
     *
     * @param widthMeasureSpec  宽度测量属性
     * @param heightMeasureSpec 高度测量属性
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 没有子控件的时候，以绘制的内容计算控件高度
        // 获取测量模式
        heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);

        // 用于判断是否需要绘制
        childCount = getChildCount();
        if (MeasureSpec.EXACTLY != heightMeasureMode) {
            // 控件高度，防止wrap_content失效
            int height;
            if (childCount > NUMBER_COMMON_NULL) {
                // 有子控件的时候，获取子控件的高度
                if (childCount == 1) {
                    View child = this.getChildAt(0);
                    FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                    int childSize = child.getMeasuredHeight();
                    int parentSpace = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - lp.topMargin - lp.bottomMargin;
                    if (childSize < parentSpace) {
                        int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin, lp.width);
                        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(parentSpace, heightMeasureSpec);
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                    }
                    height = child.getHeight() + getPaddingTop() + getPaddingBottom();
                } else {
                    throw new IllegalStateException("BswToolbar can host only one direct child");
                }
            } else {
                Rect titleRect = calculateText(RECT_TYPE_TITLE);
                Rect subtitleRect = null;
                if (!TextUtils.isEmpty(subtitle)) {
                    subtitleRect = calculateText(RECT_TYPE_SUBTITLE);
                }

                height = (int) (titleRect.height()
                        + (TextUtils.isEmpty(subtitle) || null == subtitleRect ? 0 : (titleRowSpacing + +subtitleRect.height()))
                        + getPaddingTop() + getPaddingBottom());
            }
            if (null != backDrawable) {
                height = Math.max(height, backDrawable.getIntrinsicHeight());
            }
            setMeasuredDimension(widthMeasureSpec, height);
        }
    }

    @Override
    public void addView(View child) {
        if (this.getChildCount() > NUMBER_COMMON_NULL) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int index) {
        if (this.getChildCount() > NUMBER_COMMON_NULL) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        if (this.getChildCount() > NUMBER_COMMON_NULL) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        if (this.getChildCount() > NUMBER_COMMON_NULL) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        } else {
            super.addView(child, index, params);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * 绘制，由左至右，由上至下
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 若有子控件则不展示自定义布局内容，避免布局冲突
        if (childCount > NUMBER_COMMON_NULL) {
            return;
        }

        getRect(RECT_TYPE_BACK_CLICK).set(NUMBER_COMMON_NULL, getTop(), NUMBER_COMMON_NULL, getBottom());

        int paddingStart = getTbPaddingStart();

        // 获取返回按钮尺寸，用于后续绘制
        Rect backIconRect = getRect(RECT_TYPE_BACK_ICON);
        // 获取返回文字尺寸，用于后续绘制
        Rect backTextRect = getRect(RECT_TYPE_BACK_TEXT);
        // 返回按钮是否显示
        if (backDisplay) {
            // 绘制返回图片
            if (null != backDrawable) {
                drawBackDrawable(canvas);
            }

            // 绘制返回文字
            if (!TextUtils.isEmpty(backText)) {
                drawBackText(canvas, paddingStart + backIconRect.width());
            }
            // 若返回按钮显示，则在返回按钮右侧添加backPadding，增加点击范围
            Rect rectBackClick = getRect(RECT_TYPE_BACK_CLICK);
            rectBackClick.right += backPadding;
        } else {
            // 不显示时清空缓存的尺寸设定，避免绘制时出错
            backIconRect.set(0, 0, 0, 0);
            backTextRect.set(0, 0, 0, 0);
        }

        // 若展示返回键，则在返回键后添加backPadding，扩大点击范围
        float closeStartPadding = (backDisplay ? backPadding : NUMBER_COMMON_NULL);
        // 获取批量关闭按钮尺寸，用于后续绘制
        Rect closeRect = getRect(RECT_TYPE_CLOSE_ICON);
        // 批量关闭按钮是否显示
        if (closeDisplay) {
            // 绘制批量关闭按钮，若title与返回按钮点击范围联动，则不绘制
            if (!canTitleBackLinkage && null != closeDrawable) {
                closeStartPadding += paddingStart + backIconRect.width() + backTextRect.width() + closePadding;
                // 若返回图片与文字同时存在，则需添加返回图片与文字之间的间距
                if (NUMBER_COMMON_NULL != backIconRect.width() && NUMBER_COMMON_NULL != backTextRect.width()) {
                    closeStartPadding += backPadding;
                }
                if (NUMBER_COMMON_NULL != backIconRect.width() || NUMBER_COMMON_NULL != backTextRect.width()) {
                    closeStartPadding += backPadding;
                }
                drawCloseDrawable(canvas, closeStartPadding);
            }
        } else {
            // 不显示时清空缓存的尺寸设定，避免绘制时出错
            closeRect.set(0, 0, 0, 0);
        }

        // 编辑title左侧绘制边界
        float titleStartPadding = closeStartPadding;
        if (!closeDisplay) {
            titleStartPadding = backIconRect.width() + backTextRect.width();
            // 若返回按钮存在，则需要添加图片与文字间用于扩大返回键点击范围的间距
            if (NUMBER_COMMON_NULL != backIconRect.width() || NUMBER_COMMON_NULL != backTextRect.width()) {
                titleStartPadding += backPadding;
            }
            // 若返回图片与文字同时存在，则需添加返回图片与文字之间的间距
            if (NUMBER_COMMON_NULL != backIconRect.width() && NUMBER_COMMON_NULL != backTextRect.width()) {
                titleStartPadding += backPadding;
            }
        } else {
            // 关闭按钮左右各设置一个边距，用于点击事件计算
            titleStartPadding += closeRect.width() + closePadding;
        }

        // 判断绘制title是否有子标题
        if (TextUtils.isEmpty(subtitle)) {
            drawSingleTitleText(canvas, titleStartPadding);
        } else {
            drawTitleWithSubTitleText(canvas, titleStartPadding);
        }
    }

    /**
     * 获取左内边界
     *
     * @return 左内边界
     */
    private int getTbPaddingStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getPaddingStart();
        } else {
            return getPaddingLeft();
        }
    }

    /**
     * 计算title尺寸
     */
    private Rect calculateText(int rectType) {
        Rect rect = getRect(rectType);
        UpdateTag updateTag = getUpdateTag(rectType);
        if (rect.width() == NUMBER_COMMON_NULL || rect.height() == NUMBER_COMMON_NULL) {
            updateTag.setUpdate(true);
        }
        if (updateTag.isUpdate()) {
            setUpdateTagState(rectType, false);
            Paint paint = getPaint(rectType);
            // 设置字体大小
            paint.setTextSize(updateTag.getTextSize());
            // 设置字体颜色
            paint.setColor(updateTag.getColor());
            // 画笔获取尺寸
            String text = updateTag.getText();
            paint.getTextBounds(text, NUMBER_COMMON_NULL, text.length(), rect);
        }
        return rect;
    }

    /**
     * 获取更新标识
     *
     * @param rectType 获取依据
     * @return 更新标识
     */
    private UpdateTag getUpdateTag(int rectType) {
        UpdateTag updateTag;
        switch (rectType) {
            case RECT_TYPE_BACK_ICON:
                updateTag = new UpdateTag(isBackIconUpdate, backIconColor == NUMBER_COMMON_NULL ? backColor : backIconColor, 0, null);
                break;

            case RECT_TYPE_BACK_TEXT:
                updateTag = new UpdateTag(isBackTextUpdate, backTextColor == NUMBER_COMMON_NULL ? backColor : backTextColor, backTextSize, backText);
                break;

            case RECT_TYPE_TITLE:
                updateTag = new UpdateTag(isTitleUpdate, titleColor, titleSize, title);
                break;

            case RECT_TYPE_SUBTITLE:
                updateTag = new UpdateTag(isSubtitleUpdate, subtitleColor, subtitleSize, subtitle);
                break;

            case RECT_TYPE_CLOSE_ICON:
                updateTag = new UpdateTag(isCloseIconUpdate, closeColor == NUMBER_COMMON_NULL ? backColor : closeColor, 0, null);
                break;

            default:
                updateTag = new UpdateTag(false, NUMBER_COMMON_NULL, 0, null);
                break;
        }
        return updateTag;
    }

    /**
     * 设置更新状态
     *
     * @param rectType       类型
     * @param updateTagState 更新的状态
     */
    private void setUpdateTagState(int rectType, boolean updateTagState) {
        switch (rectType) {
            case RECT_TYPE_BACK_ICON:
                isBackIconUpdate = updateTagState;
                break;

            case RECT_TYPE_BACK_TEXT:
                isBackTextUpdate = updateTagState;
                break;

            case RECT_TYPE_TITLE:
                isTitleUpdate = updateTagState;
                break;

            case RECT_TYPE_SUBTITLE:
                isSubtitleUpdate = updateTagState;
                break;

            case RECT_TYPE_CLOSE_ICON:
                isCloseIconUpdate = updateTagState;
                break;

            default:
                break;
        }
    }

    /**
     * 计算左侧按钮图标尺寸（通常为返回）
     */
    private void calculateBackIcon() {
        Rect backRect = getRect(RECT_TYPE_BACK_ICON);
        // 未测量时，或子标题文字有变化时重新测量
        if (backRect.width() == NUMBER_COMMON_NULL || isBackIconUpdate) {
            if (null != backDrawable) {
                int paddingStart = getTbPaddingStart();

                int drWidth = backDrawable.getIntrinsicWidth();
                int drHeight = backDrawable.getIntrinsicHeight();
                if (NUMBER_COMMON_NULL == drWidth || NUMBER_COMMON_NULL == drHeight) {
                    return;
                }
                int showWidth;
                int showHeight;
                if (MeasureSpec.EXACTLY != heightMeasureMode) {
                    int viewWidth = getWidth();
                    showWidth = Math.min(drWidth, viewWidth);
                    showHeight = showWidth * drHeight / drWidth;
                } else {
                    int viewHeight = getHeight();
                    showHeight = Math.min(drWidth, viewHeight);
                    showWidth = showHeight * drWidth / drHeight;
                }

                int paddingTop = getPaddingTop();
                int drTop = (getHeight() - getPaddingBottom() - paddingTop - showHeight) / 2 + paddingTop;
                backRect.set(paddingStart, drTop, paddingStart + showWidth, drTop + showHeight);
                backDrawable.setBounds(backRect);
            }
        }
        // 重新测量返回按钮点击范围
        Rect backClick = getRect(RECT_TYPE_BACK_CLICK);
        backClick.right = Math.max(backClick.right, backRect.right);
    }

    /**
     * 计算左侧批量关闭图标尺寸（通常为返回）
     *
     * @param closeStartPadding 批量关闭按钮左侧控件占据宽度
     */
    private void calculateCloseIcon(float closeStartPadding) {
        Rect closeRect = getRect(RECT_TYPE_CLOSE_ICON);
        // 未测量时，或子标题文字有变化时重新测量
        if (closeRect.width() == NUMBER_COMMON_NULL || isCloseIconUpdate || null != closeDrawable) {
            int drWidth = closeDrawable.getIntrinsicWidth();
            int drHeight = closeDrawable.getIntrinsicHeight();
            if (NUMBER_COMMON_NULL == drWidth || NUMBER_COMMON_NULL == drHeight) {
                return;
            }
            int showWidth;
            int showHeight;
            if (MeasureSpec.EXACTLY != heightMeasureMode) {
                // 若非精确，以图片宽度为基准，高度适配
                int viewWidth = getWidth();
                showWidth = Math.min(drWidth, viewWidth);
                showHeight = showWidth * drHeight / drWidth;
            } else {
                // 若高度精确，以图片宽度为基准，高度适配
                int viewHeight = getHeight();
                showHeight = Math.min(drWidth, viewHeight);
                showWidth = showHeight * drWidth / drHeight;
            }

            int paddingTop = getPaddingTop();
            int drTop = (getHeight() - getPaddingBottom() - paddingTop - showHeight) / 2 + paddingTop;
            closeRect.set((int) closeStartPadding, drTop, (int) closeStartPadding + showWidth, drTop + showHeight);
            closeDrawable.setBounds(closeRect);
        }
        // 如果title添加返回的点击事件联动，则批量关闭按钮失效
        if (!canTitleBackLinkage) {
            /*
             * 重新测量批量关闭按钮点击范围
             * 关闭的边距在关闭按钮左右都添加，避免图标过小，无法点击
             */
            getRect(RECT_TYPE_CLOSE_CLICK).set(closeRect.left - (int) closePadding, getTop(), closeRect.right + (int) closePadding, getBottom());
        }
    }

    /**
     * 设置图片颜色
     *
     * @param drawable 被设置drawable
     * @param color    设置色值
     */
    public void tintDrawable(Drawable drawable, int color) {
        if (NUMBER_COMMON_NULL == color) {
            return;
        }
        DrawableCompat.setTintList(drawable, ColorStateList.valueOf(color));
    }

    /**
     * 绘制左侧按钮（主要是返回）
     *
     * @param canvas 画布
     */
    private void drawBackDrawable(Canvas canvas) {
        if (null == backDrawable) {
            return;
        }
        calculateBackIcon();
        tintDrawable(backDrawable, backIconColor == NUMBER_COMMON_NULL ? backColor : backIconColor);
        backDrawable.draw(canvas);
    }

    /**
     * 绘制批量关闭按钮
     *
     * @param canvas            画布
     * @param closeStartPadding 批量关闭的左侧
     */
    private void drawCloseDrawable(Canvas canvas, float closeStartPadding) {
        if (null == closeDrawable) {
            return;
        }
        calculateCloseIcon(closeStartPadding);
        // 渲染颜色，若未设置关闭按钮颜色，则使用返回通用颜色
        tintDrawable(closeDrawable, closeColor == NUMBER_COMMON_NULL ? backColor : closeColor);
        // 绘制图片
        closeDrawable.draw(canvas);
    }

    /**
     * 绘制返回的文本
     *
     * @param canvas        画布
     * @param backTextWidth backText左侧绘制边距
     */
    private void drawBackText(Canvas canvas, float backTextWidth) {
        Rect rect = calculateText(RECT_TYPE_BACK_TEXT);
        paint = getPaint(RECT_TYPE_BACK_TEXT);
        // 返回键右侧文本
        canvas.drawText(backText
                , backTextWidth + backPadding - rect.left
                , NUMBER_COMMON_NULL - rect.top + (getHeight() - getPaddingTop() - getPaddingBottom() - rect.height()) / NUMBER_COMMON_HALF + getPaddingTop()
                , paint);
        Rect backClick = getRect(RECT_TYPE_BACK_CLICK);
        // 重新测量返回按钮点击范围
        backClick.right = (int) Math.max(backClick.right, backTextWidth + backPadding - rect.left + rect.width() + backPadding);
    }

    /**
     * 绘制单title文本
     *
     * @param canvas            画布
     * @param titleStartPadding back图标与文字的宽度，用于title左侧对齐时距离测量
     */
    private void drawSingleTitleText(Canvas canvas, float titleStartPadding) {
        Rect rect = calculateText(RECT_TYPE_TITLE);
        paint = getPaint(RECT_TYPE_TITLE);
        float backRight;
        if (TITLE_GRAVITY_RIGHT == titleGravity) {
            backRight = titlePadding - rect.left + titleStartPadding;
            // 左侧显示文本
            canvas.drawText(title
                    , backRight
                    , NUMBER_COMMON_NULL - rect.top + (getHeight() - getPaddingTop() - getPaddingBottom() - rect.height()) / NUMBER_COMMON_HALF + getPaddingTop()
                    , paint);
        } else {
            backRight = NUMBER_COMMON_NULL - rect.left + (getWidth() - rect.width()) / NUMBER_COMMON_HALF;
            // 居中显示文本
            canvas.drawText(title
                    , backRight
                    , NUMBER_COMMON_NULL - rect.top + (getHeight() - getPaddingTop() - getPaddingBottom() - rect.height()) / NUMBER_COMMON_HALF + getPaddingTop()
                    , paint);
        }
        if (canTitleBackLinkage) {
            backRight += rect.width() + titlePadding;
            Rect backClick = getRect(RECT_TYPE_BACK_CLICK);
            backClick.right = (int) Math.max(backClick.right, backRight);
        }
    }

    /**
     * 绘制title与subtitle（纵向排列）
     *
     * @param canvas            画布
     * @param titleStartPadding backDrawable的宽度，用于title左侧对齐时距离测量
     */
    private void drawTitleWithSubTitleText(Canvas canvas, float titleStartPadding) {
        Rect rect = calculateText(RECT_TYPE_TITLE);
        Rect subtitleRect = calculateText(RECT_TYPE_SUBTITLE);

        int titleTop;
        if (MeasureSpec.EXACTLY != heightMeasureMode) {
            titleTop = (int) ((getHeight() - getPaddingTop() - getPaddingBottom() - rect.height() - subtitleRect.height() - titleRowSpacing) / NUMBER_COMMON_HALF + getPaddingTop());
        } else {
            titleTop = +getPaddingTop();
        }

        int backRight;

        if (TITLE_GRAVITY_RIGHT == titleGravity) {
            // 左侧title
            paint = getPaint(RECT_TYPE_TITLE);
            canvas.drawText(title
                    , titlePadding - rect.left + titleStartPadding
                    , titleTop - rect.top
                    , paint);
            // 左侧subtitle
            paint = getPaint(RECT_TYPE_SUBTITLE);
            canvas.drawText(subtitle
                    , titlePadding - subtitleRect.left + titleStartPadding
                    , titleTop + rect.height() + titleRowSpacing - subtitleRect.top
                    , paint);
            boolean titleLongerThanSubtitle = rect.width() > subtitleRect.width();
            backRight = (int) (titlePadding * 2 + titleStartPadding + (titleLongerThanSubtitle ? rect.width() - rect.left : subtitleRect.width() - subtitleRect.left));
        } else {
            // 居中title
            float titleLeft = (getWidth() - rect.width()) / NUMBER_COMMON_HALF;
            paint = getPaint(RECT_TYPE_TITLE);
            canvas.drawText(title
                    , titleLeft
                    , titleTop - rect.top
                    , paint);
            // 居中subtitle，Y坐标是基于title的位置计算的
            float subtitleLeft = NUMBER_COMMON_NULL - subtitleRect.left + (getWidth() - subtitleRect.width()) / NUMBER_COMMON_HALF;
            paint = getPaint(RECT_TYPE_SUBTITLE);
            canvas.drawText(subtitle
                    , subtitleLeft
                    , titleTop + rect.height() + titleRowSpacing - subtitleRect.top
                    , paint);

            boolean titleLongerThanSubtitle = rect.width() > subtitleRect.width();
            backRight = (int) (titlePadding * 2 + titleStartPadding + (titleLongerThanSubtitle
                    ? rect.width() - rect.left + titleLeft
                    : subtitleRect.width() - subtitleRect.left + subtitleLeft));
        }
        if (canTitleBackLinkage) {
            Rect backClick = getRect(RECT_TYPE_BACK_CLICK);
            backClick.right = Math.max(backClick.right, backRight);
        }
    }

    /**
     * 设置title
     *
     * @param title title
     * @return 当前BswToolbar
     */
    public BswToolbar setTitle(String title) {
        this.title = title;
        isTitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置title
     *
     * @param titleRes title资源Id
     * @return 当前BswToolbar
     */
    public BswToolbar setTitle(@StringRes int titleRes) {
        return setTitle(mContext.getResources().getString(titleRes));
    }


    /**
     * 设置TItle位置
     *
     * @param titleGravity title位置
     * @return 当前BswToolbar
     */
    public BswToolbar setTitleGravity(@TitleGravity int titleGravity) {
        this.titleGravity = titleGravity;
        invalidate();
        return this;
    }

    /**
     * 设置title与子标题之间的间距
     *
     * @param titlePadding 间距
     * @return 当前BswToolbar
     */
    public BswToolbar setTitlePadding(float titlePadding) {
        this.titlePadding = titlePadding;
        invalidate();
        return this;
    }

    /**
     * 设置title的字体大小
     *
     * @param titleSize 字体大小
     * @return 当前BswToolbar
     */
    public BswToolbar setTitleSize(float titleSize) {
        this.titleSize = titleSize;
        isTitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置title的字体颜色
     *
     * @param titleColor 字体颜色
     * @return 当前BswToolbar
     */
    public BswToolbar setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        isTitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置title与subtitle之间间距
     *
     * @param titleRowSpacing title行间距
     * @return 当前BswToolbar
     */
    public BswToolbar setTitleRowSpacing(float titleRowSpacing) {
        this.titleRowSpacing = titleRowSpacing;
        isTitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置subtitle
     *
     * @param subtitleRes subtitle资源ID
     * @return 当前BswToolbar
     */
    public BswToolbar setSubtitle(@StringRes int subtitleRes) {
        return setSubtitle(mContext.getResources().getString(subtitleRes));
    }

    /**
     * 设置subtitle
     *
     * @param subtitle subtitle
     * @return 当前BswToolbar
     */
    public BswToolbar setSubtitle(String subtitle) {
        isSubtitleUpdate = true;
        this.subtitle = subtitle;
        invalidate();
        return this;
    }

    /**
     * 设置子标题字体大小
     *
     * @param subtitleSize 字体大小
     * @return 当前BswToolbar
     */
    public BswToolbar setSubtitleSize(float subtitleSize) {
        this.subtitleSize = subtitleSize;
        isSubtitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置子标题字体颜色
     *
     * @param subtitleColor 字体颜色
     * @return 当前BswToolbar
     */
    public BswToolbar setSubtitleColor(int subtitleColor) {
        this.subtitleColor = subtitleColor;
        isSubtitleUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回图标
     *
     * @param backIconRes 返回图标资源文件
     * @return 当前BswToolbar
     */
    public BswToolbar setBackIcon(@DrawableRes int backIconRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setBackIconDrawable(mContext.getResources().getDrawable(backIconRes, mContext.getTheme()));
        } else {
            return setBackIconDrawable(mContext.getResources().getDrawable(backIconRes));
        }
    }

    /**
     * 设置返回图标
     *
     * @param backIconRes 返回图标资源文件
     * @param backType    返回显示样式
     * @return 当前BswToolbar
     */
    public BswToolbar setBackIcon(@DrawableRes int backIconRes, @BackType int backType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setBackIconDrawable(mContext.getResources().getDrawable(backIconRes, mContext.getTheme()), backType);
        } else {
            return setBackIconDrawable(mContext.getResources().getDrawable(backIconRes), backType);
        }
    }

    /**
     * 设置返回Drawable
     *
     * @param backDrawable drawable
     * @return 当前BswToolbar
     */
    public BswToolbar setBackIconDrawable(Drawable backDrawable) {
        return setBackIconDrawable(backDrawable, backType);
    }

    /**
     * 设置返回Drawable
     *
     * @param backDrawable drawable
     * @return 当前BswToolbar
     */
    public BswToolbar setBackIconDrawable(Drawable backDrawable, @BackType int backType) {
        this.backType = backType;
        this.backDrawable = backDrawable;
        isBackIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回Drawable
     *
     * @param bm 返回图片的bitmap
     */
    public BswToolbar setBackIconBitmap(Bitmap bm) {
        // Hacky fix to force setImageDrawable to do a full setImageDrawable
        // instead of doing an object reference comparison
        backDrawable = null;
        return setBackIconDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }

    /**
     * 设置返回按钮处的文字
     *
     * @param backTextRes 当返回键包含文字时
     * @return 当前BswToolbar
     */
    public BswToolbar setBackText(@StringRes int backTextRes) {
        return setBackText(backTextRes, backType);
    }

    /**
     * 设置返回按钮处的文字
     *
     * @param backTextRes 当返回键包含文字时
     * @param backType    返回显示样式
     * @return 当前BswToolbar
     */
    public BswToolbar setBackText(@StringRes int backTextRes, @BackType int backType) {
        return setBackText(mContext.getResources().getString(backTextRes), backType);
    }

    /**
     * 当返回键包含文字时
     *
     * @param backText 有左侧使用文字的情况
     * @return 当前BswToolbar
     */
    public BswToolbar setBackText(String backText) {
        return setBackText(backText, backType);
    }

    /**
     * 当返回键包含文字时
     *
     * @param backText 有左侧使用文字的情况
     * @return 当前BswToolbar
     */
    public BswToolbar setBackText(String backText, @BackType int backType) {
        this.backType = backType;
        this.backText = backText;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回文字的字体大小
     *
     * @param backTextSize 字体大小
     * @return 当前BswToolbar
     */
    public BswToolbar setBackTextSize(float backTextSize) {
        this.backTextSize = backTextSize;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回按钮颜色
     * 影响范围{@link BswToolbar#backIconColor 返回图片颜色}
     * 影响范围{@link BswToolbar#backTextColor 返回文字颜色}
     * 影响范围{@link BswToolbar#closeColor 批量关闭颜色}
     *
     * @param backColor 色值
     * @return 当前BswToolbar
     */
    public BswToolbar setBackColor(int backColor) {
        return setBackColor(backColor, true);
    }

    /**
     * 设置返回按钮颜色
     * 影响范围{@link BswToolbar#backIconColor 返回图片颜色}
     * 影响范围{@link BswToolbar#backTextColor 返回文字颜色}
     * 影响范围{@link BswToolbar#closeColor 批量关闭颜色}
     * <p>
     * 设置{@link BswToolbar#isBackTextUpdate}是因为文字画笔的字体颜色是在尺寸计算处设置
     *
     * @param backColor        色值
     * @param impactCloseColor 是否影响关闭图标按钮
     * @return 当前BswToolbar
     */
    public BswToolbar setBackColor(int backColor, boolean impactCloseColor) {
        this.backColor = backColor;
        this.backIconColor = backColor;
        this.backTextColor = backColor;
        isBackTextUpdate = true;
        if (impactCloseColor) {
            this.closeColor = backColor;
        }
        invalidate();
        return this;
    }

    /**
     * 设置返回文字颜色值
     *
     * @param backTextColor 返回文字颜色
     * @return 当前BswToolbar
     */
    public BswToolbar setBackTextColor(int backTextColor) {
        this.backTextColor = backTextColor;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回图标颜色
     *
     * @param backIconColor 返回图标颜色
     * @return 当前BswToolbar
     */
    public BswToolbar setBackIconColor(int backIconColor) {
        this.backIconColor = backIconColor;
        isBackIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置返回图标与文字之间的间距，只有在两者均显示时使用
     *
     * @param backPadding 返回图标与文字之间的间距
     * @return 当前BswToolbar
     */
    public BswToolbar setBackPadding(float backPadding) {
        this.backPadding = backPadding;
        isBackIconUpdate = true;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 返回显示类型：
     * {@link BswToolbar#BACK_TYPE_ICON 只显示图标}
     * {@link BswToolbar#BACK_TYPE_TEXT 只显示文字}
     * {@link BswToolbar#BACK_TYPE_BOTH 图标 + 文字}
     * 默认为{@link BswToolbar#BACK_TYPE_BOTH }
     *
     * @param backType 返回显示样式
     * @return 当前BswToolbar
     */
    public BswToolbar setBackType(@BackType int backType) {
        this.backType = backType;
        isBackIconUpdate = true;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 是否显示返回那妞
     *
     * @param backDisplay 是否显示返回按钮
     * @return 当前BswToolbar
     */
    public BswToolbar setBackDisplay(boolean backDisplay) {
        this.backDisplay = backDisplay;
        isBackIconUpdate = true;
        isBackTextUpdate = true;
        invalidate();
        return this;
    }

    /**
     * title是否与返回按钮联动
     *
     * @param canTitleBackLinkage title是否与返回按钮联动
     * @return 当前BswToolbar
     */
    public BswToolbar setCanTitleBackLinkage(boolean canTitleBackLinkage) {
        this.canTitleBackLinkage = canTitleBackLinkage;
        isTitleClickLinkageUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置批量关闭drawable
     *
     * @param closeDrawable 批量关闭drawable
     * @return 当前BswToolbar
     */
    public BswToolbar setCloseDrawable(Drawable closeDrawable) {
        this.closeDrawable = closeDrawable;
        isCloseIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置批量关闭Drawable
     *
     * @param bm 批量关闭图片的bitmap
     */
    public BswToolbar setCloseBitmap(Bitmap bm) {
        // Hacky fix to force setImageDrawable to do a full setImageDrawable
        // instead of doing an object reference comparison
        closeDrawable = null;
        return setCloseDrawable(new BitmapDrawable(mContext.getResources(), bm));
    }

    /**
     * 设置批量关闭Drawable
     *
     * @param closeDrawableResId drawable资源Id
     * @return 当前BswToolbar
     */
    public BswToolbar setCloseIcon(@DrawableRes int closeDrawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return setCloseDrawable(mContext.getResources().getDrawable(closeDrawableResId, mContext.getTheme()));
        } else {
            return setCloseDrawable(mContext.getResources().getDrawable(closeDrawableResId));
        }
    }

    /**
     * 设置批量关闭按钮与返回按钮之间的间距
     * 只有在两者均存在的时候使用
     *
     * @param closePadding 间距
     * @return 当前BswToolbar
     */
    public BswToolbar setClosePadding(float closePadding) {
        this.closePadding = closePadding;
        isCloseIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置批量关闭按钮的颜色
     * 若未设置使用{@link BswToolbar#backColor}
     *
     * @param closeColor 关闭按钮按钮
     * @return 当前BswToolbar
     */
    public BswToolbar setCloseColor(int closeColor) {
        this.closeColor = closeColor;
        isCloseIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置批量关闭按钮是否显示
     *
     * @param closeDisplay 批量关闭按钮是否显示
     * @return 当前BswToolbar
     */
    public BswToolbar setCloseDisplay(boolean closeDisplay) {
        this.closeDisplay = closeDisplay;
        isCloseIconUpdate = true;
        invalidate();
        return this;
    }

    /**
     * 设置点击事件
     *
     * @param btnClickListener 点击事件监听
     * @return 当前BswToolbar
     */
    public BswToolbar setOnToolbarBtnClickListener(OnToolbarBtnClickListener btnClickListener) {
        this.btnClickListener = btnClickListener;
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == btnClickListener) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (backDisplay) {
                    clickDownX = (int) event.getX();
                    clickDownY = (int) event.getY();
                }
                //
                return true;

            case MotionEvent.ACTION_UP:
                int clickUpX = (int) event.getX();
                int clickUpY = (int) event.getY();

                Logger.i(getName(), "clickDownX = " + clickDownX + " clickDownY = " + clickDownY + " clickUpX = " + clickUpX + " clickUpY = " + clickUpY);

                if (Math.abs(clickUpX - clickDownX) > 20 || Math.abs(clickUpY - clickDownY) > 20) {
                    return super.onTouchEvent(event);
                }

                // 返回点击判断
                Rect judgeRect = getRect(RECT_TYPE_BACK_CLICK);
                Logger.i(getName(), "返回：点击在范围" + (judgeRect.contains(clickDownX, clickDownY) ? "内" : "外") + ", rectBack = " + judgeRect.toString());
                if (judgeRect.contains(clickDownX, clickDownY)) {
                    btnClickListener.onBackClick();
                    return true;
                }

                // 批量关闭点击判断
                judgeRect = getRect(RECT_TYPE_CLOSE_CLICK);
                Logger.i(getName(), "批量关闭：点击在范围" + (judgeRect.contains(clickDownX, clickDownY) ? "内" : "外") + ", rectClose= " + judgeRect.toString());
                if (judgeRect.contains(clickDownX, clickDownY)) {
                    btnClickListener.onCloseClick();
                    return true;
                }

                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 获取尺寸
     *
     * @param rectType 尺寸类型，与Rect通用
     * @return 对应的画笔
     */
    private Rect getRect(Integer rectType) {
        Rect rect = rectMap.get(rectType);
        if (null == rect) {
            rect = new Rect();
            rectMap.put(rectType, rect);
        }
        return rect;
    }

    /**
     * 获取画笔
     *
     * @param paintType 画笔类型，与Rect通用
     * @return 对应的画笔
     */
    private Paint getPaint(Integer paintType) {
        Paint paint = paintMap.get(paintType);
        if (null == paint) {
            paint = new Paint();
            paintMap.put(paintType, paint);
        }
        return paint;
    }

    private String getName() {
        return getClass().getSimpleName();
    }
}
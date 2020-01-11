package com.bsw.mydemo.widget.bswtoolbar;

/**
 * 文字更新标签
 *
 * @author 半寿翁
 * @date 2020-1-7
 */
class UpdateTag {
    /**
     * 是否刷新
     */
    private boolean isUpdate;
    /**
     * 若为文本的时候，需展示文字
     */
    private String text;
    /**
     * 颜色
     */
    private int color;
    /**
     * 如有文字时字体大小
     */
    private float textSize;

    public UpdateTag(boolean isUpdate, int color, float textSize, String text) {
        this.isUpdate = isUpdate;
        this.color = color;
        this.textSize = textSize;
        this.text = text;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public int getColor() {
        return color;
    }

    public float getTextSize() {
        return textSize;
    }

    public String getText() {
        return text;
    }
}

package com.bsw.mydemo.bean;

/**
 * @author leiming
 * @date 2018/11/23.
 */
public class JumpBean {
    private int btnTextId;
    private Class jumpToClass;

    public JumpBean(int btnTextId, Class jumpToClass) {
        this.btnTextId = btnTextId;
        this.jumpToClass = jumpToClass;
    }

    public int getBtnTextId() {
        return btnTextId;
    }

    public Class getJumpToClass() {
        return jumpToClass;
    }
}

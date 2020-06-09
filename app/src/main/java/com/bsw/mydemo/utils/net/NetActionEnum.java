package com.bsw.mydemo.utils.net;

/**
 * 网络请求尾址枚举
 *
 * @author leiming
 * @date 2019/3/20.
 */
public enum NetActionEnum {
    // 获取添加设备的待设备列表
    POST__UPLOAD_AAC("fileUpload/1.0/upload/aac"),
    ;

    private String actionString;

    NetActionEnum(String actionString) {
        this.actionString = actionString;
    }

    public String getActionString() {
        return actionString;
    }
}



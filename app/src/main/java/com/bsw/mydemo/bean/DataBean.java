package com.bsw.mydemo.bean;

import com.bsw.mydemo.base.BaseBean;

/**
 * @author 半寿翁
 * @date 2018/4/28.
 */

public class DataBean  extends BaseBean{

    /**
     * code : 200
     * msg : OK
     * data : {"rtmp":"rtmp://121.40.50.44:10085/live/ce349dc5-2bf4-45be-bfd8-628b5eee65f5"}
     */

    private int code;
    private String msg;
    private RtmpBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RtmpBean getData() {
        return data;
    }

    public void setData(RtmpBean data) {
        this.data = data;
    }
}

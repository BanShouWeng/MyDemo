package com.bsw.mydemo.bean;

/**
 * 记住用户历史账号
 *
 * @author 半寿翁
 * @date 2018/4/2.
 */
public class UserAccountBean {
    private String account;
    private String password;
    private boolean rememberPW;

    public UserAccountBean(String account, String password, boolean rememberPW) {
        this.account = account;
        this.password = password;
        this.rememberPW = rememberPW;
    }

    public UserAccountBean() {

    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberPW() {
        return rememberPW;
    }

    public void setRememberPW(boolean rememberPW) {
        this.rememberPW = rememberPW;
    }
}

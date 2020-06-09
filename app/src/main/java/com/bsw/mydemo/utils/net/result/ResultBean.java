package com.bsw.mydemo.utils.net.result;

/**
 * 泛型回显Bean
 *
 * @author leiming
 * @date 2019/3/14.
 */
public class ResultBean {
    private String code;
    private String message;
    private int totalPages;
    private int totalRecords;

    public boolean isSuccessful() {
        return code.equals("000000");
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public String getCode() {
        return code;
    }
}

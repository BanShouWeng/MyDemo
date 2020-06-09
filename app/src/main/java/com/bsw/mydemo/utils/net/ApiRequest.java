package com.bsw.mydemo.utils.net;


import android.support.annotation.IntDef;

import com.bsw.mydemo.utils.Const;
import com.bsw.mydemo.utils.net.result.ResultBean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author leiming
 * @date 2018/12/20.
 */
public class ApiRequest<ResultData extends ResultBean> {
    /**
     * 网络请求结果回调
     */
    private NetUtils.NetRequestCallBack netRequestCallBack;
    /**
     * 通用业务
     */
    public static final int API_TYPE_NORMAL = 0x096;
    /**
     * 文件操作
     */
    public static final int API_TYPE_FILE_OPERATION = 0x095;
    /**
     * 统计
     */
    public static final int API_TYPE_STATISTICS = 0x094;
    /**
     * 登录设置
     */
    public static final int API_TYPE_LOGIN_SETTING = 0x093;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({API_TYPE_NORMAL, API_TYPE_FILE_OPERATION, API_TYPE_STATISTICS, API_TYPE_LOGIN_SETTING})
    public @interface NetApiType {
    }

    public static final int REQUEST_TYPE_GET = 0x079;
    public static final int REQUEST_TYPE_POST = 0x078;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({REQUEST_TYPE_GET, REQUEST_TYPE_POST})
    public @interface NetRequestType {
    }

    public static final int SPECIAL_GET_BITMAP = 0x069;
    public static final int SPECIAL_FILE_UPLOAD = 0x068;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SPECIAL_GET_BITMAP, SPECIAL_FILE_UPLOAD})
    public @interface NetSpecialRequestType {
    }

    /**
     * 网络请求尾址
     */
    private NetActionEnum action;

    /**
     * 网络请求API类型
     * API_TYPE_NORMAL              业务请求
     * API_TYPE_FILE_OPERATION      文件操作
     * API_TYPE_STATISTICS          统计
     * API_TYPE_LOGIN_SETTING       登录设置
     */
    private int apiType = API_TYPE_NORMAL;

    /**
     * 网络请求类型
     * GET
     * POST
     */
    private int requestType = 0;

    /**
     * 需要特殊处理的类型
     * SPECIAL_GET_BITMAP       返回结果获取bitmap
     * SPECIAL_FILE_UPLOAD      需要上传文件
     */
    private int specialTreatment = 0;

    /**
     * 标签，用于同一个接口多环境调用时区分，或特殊值需在返回时使用时传参
     */
    private Object tag;

    /**
     * 键值对请求参数
     */
    private Map<String, Object> requestParams;

    /**
     * post请求体
     */
    private Object requestBody;

    /**
     * 是否支持加载缺省页联动，默认为true
     */
    private boolean isLoadingLayoutLinkage = true;
    /**
     * 结果类
     */
    private Class<ResultData> clz;

    /**
     * 构造方法
     *
     * @param action {@link ApiRequest#action}
     */
    public ApiRequest(NetActionEnum action, Class<ResultData> clz) {
        this.action = action;
        this.clz = clz;
    }

    /**
     * 构造方法
     *
     * @param action     {@link ApiRequest#action}
     * @param resultType {@link ApiRequest#specialTreatment}
     */
    public ApiRequest(NetActionEnum action, @NetSpecialRequestType int resultType) {
        this.action = action;
        specialTreatment = resultType;
    }

    /**
     * 设置请求的API类型，将影响调用的路径
     *
     * @param apiType {@link ApiRequest#apiType}
     * @return 当前类
     */
    public ApiRequest<ResultData> setApiType(@NetApiType int apiType) {
        this.apiType = apiType;
        return this;
    }

    /**
     * @param requestType {@link ApiRequest#requestType}
     * @return 当前类
     */
    public ApiRequest<ResultData> setRequestType(@NetRequestType int requestType) {
        if (this.requestType != 0) {
            throw new IllegalStateException("Request type can't repeat settings");
        }
        this.requestType = requestType;
        return this;
    }


    /**
     * 设置请求参数
     *
     * @param key   请求键
     * @param value 请求值
     * @return 当前类
     */
    public ApiRequest<ResultData> setRequestParams(String key, Object value) {
        if (Const.isEmpty(requestParams)) {
            requestParams = new HashMap<>();
        }
        requestParams.put(key, value);
        return this;
    }

    /**
     * 移除请求参数
     *
     * @param key 请求键
     * @return 当前类
     */
    public ApiRequest<ResultData> removeRequestParams(String key) {
        if (Const.isEmpty(requestParams)) {
            requestParams = new HashMap<>();
        }
        requestParams.remove(key);
        return this;
    }

    /**
     * 设置请求参数
     *
     * @param requestParams 键值对请求参数
     * @return 当前类
     */
    public ApiRequest<ResultData> setRequestParams(Map<String, Object> requestParams) {
        if (Const.isEmpty(this.requestParams)) {
            this.requestParams = new HashMap<>();
        }
        this.requestParams.putAll(requestParams);
        return this;
    }

    /**
     * 防止多次添加body导致异常
     *
     * @param body post信息体
     */
    public ApiRequest<ResultData> setRequestBody(Object body) {
        if (requestType == REQUEST_TYPE_GET) {
            throw new IllegalStateException("GET can't support body, must be POST");
        }
        requestBody = body;
        return this;
    }

    /**
     * 特殊请求类型：文件上传
     * {@link ApiRequest#specialTreatment}
     *
     * @return 当前类
     */
    public ApiRequest<ResultData> uploadFile() {
        specialTreatment = SPECIAL_FILE_UPLOAD;
        return this;
    }

    /**
     * 上传网络请求标签
     *
     * @param tag {@link ApiRequest#tag}
     * @return 当前类
     */
    public ApiRequest<ResultData> setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 获取尾址
     *
     * @return {@link ApiRequest#action}
     */
    public NetActionEnum getAction() {
        return action;
    }

    /**
     * 获取尾址
     *
     * @return {@link ApiRequest#action}
     */
    public int getApiType() {
        return apiType == 0 ? API_TYPE_NORMAL : apiType;
    }

    /**
     * 获取尾址
     *
     * @return {@link ApiRequest#action}
     */
    public int getRequestType() {
        return requestType == 0 ? REQUEST_TYPE_GET : requestType;
    }

    /**
     * 获取标签
     *
     * @return {@link ApiRequest#tag}
     */
    public Object getTag() {
        return tag;
    }

    /**
     * 获取请求键值对
     *
     * @return {@link ApiRequest#requestParams}
     */
    public Map<String, Object> getRequestParams() {
        return requestParams;
    }

    /**
     * 获取根据网络请求键获取值
     *
     * @return {@link ApiRequest#requestParams}
     */
    public Object getRequestParams(String key) {
        return requestParams.get(key);
    }

    /**
     * 获取POST请求体
     *
     * @return {@link ApiRequest#requestBody}
     */
    public Object getRequestBody() {
        return requestBody;
    }

    /**
     * 获取特殊请求体
     *
     * @return {@link ApiRequest#specialTreatment}
     */
    public int getSpecialTreatment() {
        return specialTreatment;
    }

    /**
     * 获取是否缺省页联动
     *
     * @return 是否联动
     */
    public boolean isLoadingLayoutLinkage() {
        return isLoadingLayoutLinkage;
    }

    /**
     * 获取是否为分页
     *
     * @return 是否为分页
     */
    public boolean isPaging() {
        return getPageIndex() > 0;
    }

    public Class<ResultData> getClz() {
        return clz;
    }

    /**
     * 设置是否缺省页联动
     *
     * @param loadingLayoutLinkage 缺省页联动状态
     */
    public ApiRequest<ResultData> setLoadingLayoutLinkage(boolean loadingLayoutLinkage) {
        isLoadingLayoutLinkage = loadingLayoutLinkage;
        return this;
    }

    /**
     * 在netUtil中初始化，因此不添加public
     *
     * @param netRequestCallBack 网络请求结果回调
     */
    void setNetRequestCallBack(NetUtils.NetRequestCallBack netRequestCallBack) {
        this.netRequestCallBack = netRequestCallBack;
    }

    /*----------------------------------分页----------------------------------*/
    /**
     * 是否做分页变更
     */
    private boolean isChanged = false;
    /**
     * 没有页码的标示位
     */
    public static final int NULL_PAGE_INDEX = -99;
    /**
     * 页码
     */
    private final String PAGE_INDEX = "pageIndex";
    /**
     * 单页显示最大条目数
     */
    private final String PAGE_SIZE = "pageSize";

    /**
     * 设置分页
     *
     * @param pageIndex 页码
     * @param pageSize  单页显示最大列数
     * @return 当前类
     */
    public ApiRequest<ResultData> setPaging(int pageIndex, int pageSize) {
        setRequestParams(PAGE_INDEX, pageIndex)
                .setRequestParams(PAGE_SIZE, pageSize);
        return this;
    }

    /**
     * 加载
     */
    public ApiRequest<ResultData> loadMore() {
        if (!isChanged) {
            isChanged = true;
            int currentPageIndex = getPageIndex();
            if (NULL_PAGE_INDEX == currentPageIndex) {
                // 如果当前没有页码
                return this;
            }
            setRequestParams(PAGE_INDEX, ++currentPageIndex);
        }
        return this;
    }

    /**
     * 刷新
     */
    public ApiRequest<ResultData> refresh() {
        if (!isChanged) {
            int currentPageIndex = getPageIndex();
            if (NULL_PAGE_INDEX == currentPageIndex) {
                // 如果当前没有页码
                return this;
            }
            setRequestParams(PAGE_INDEX, 1);
        }
        return this;
    }

    /**
     * 获取当前的页码
     *
     * @return 页码
     */
    public int getPageIndex() {
        if (null == requestParams) {
            return NULL_PAGE_INDEX;
        }
        Object value = requestParams.get(PAGE_INDEX);
        if (null == value) {
            return NULL_PAGE_INDEX;
        }
        return Integer.parseInt(value.toString());
    }

    /**
     * 获取当前的页码
     *
     * @return 页码
     */
    public int getPageSize() {
        if (null == requestParams) {
            return NULL_PAGE_INDEX;
        }
        Object value = requestParams.get(PAGE_SIZE);
        if (null == value) {
            return NULL_PAGE_INDEX;
        }
        return Integer.parseInt(value.toString());
    }

    /*----------------------------------重新加载----------------------------------*/

    /**
     * 重新加载
     */
    public void reload() {
        isChanged = false;

        NetUtils netUtils = NetUtils.getNewInstance(null);
        if (null == netUtils) {
            // 网络请求工具为空时，不可重新加载请求
            return;
        }
        netUtils.request(netRequestCallBack, false, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApiRequest)) {
            return false;
        }
        ApiRequest<?> that = (ApiRequest<?>) o;
        return getApiType() == that.getApiType() &&
                getRequestType() == that.getRequestType() &&
                getSpecialTreatment() == that.getSpecialTreatment() &&
                isLoadingLayoutLinkage() == that.isLoadingLayoutLinkage() &&
                isChanged == that.isChanged &&
                getAction() == that.getAction() &&
                Objects.equals(getTag(), that.getTag()) &&
                Objects.equals(getRequestParams(), that.getRequestParams()) &&
                Objects.equals(getRequestBody(), that.getRequestBody()) &&
                getClz().equals(that.getClz()) &&
                Objects.equals(PAGE_INDEX, that.PAGE_INDEX) &&
                Objects.equals(PAGE_SIZE, that.PAGE_SIZE);
    }

    NetUtils.NetRequestCallBack getNetRequestCallBack() {
        return netRequestCallBack;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAction(), getApiType(), getRequestType(), getSpecialTreatment(), getTag(), getRequestParams(), getRequestBody(), isLoadingLayoutLinkage(), getClz(), isChanged, PAGE_INDEX, PAGE_SIZE);
    }
}
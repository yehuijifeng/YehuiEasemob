package com.yehui.utils.http.action;


import com.yehui.utils.http.SignUtil;

import java.util.Map;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求的参数
 */
public class RequestParameter {

    /**
     * 发送请求的路径
     */
    private String requestUrl;

    /**
     * 服务器返回的数据类型
     */
    private Class<?> cls;

    /**
     * 请求参数
     */
    private Map<String, Object> parameters = SignUtil.getParameters();

    /**
     * 只传入请求地址
     */
    public RequestParameter(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**传入请求地址和返回的解析数据格式
     * @param requestUrl
     * @param cls
     */
    public RequestParameter(String requestUrl, Class<?> cls) {
        this.requestUrl = requestUrl;
        this.cls = cls;
    }

    /**
     * 获得请求的url
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * 传入请求url
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * 获得解析格式的class
     */
    public Class<?> getCls() {
        return cls;
    }

    /**
     * 传入解析格式的class
     */
    public void setCls(Class<?> cls) {
        this.cls = cls;
    }

    /**
     * 获得参数map集合
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * 传入参数集合
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

}

package com.yehui.utils.http.bean;

import com.squareup.okhttp.Request;
import com.yehui.utils.http.action.RequestAction;

/**
 * Created by yehuijifeng
 * on 2016/1/11.
 * 线程阻塞的网络请求方式的实体类
 */
public class RequestInstanceBean {
    private int instanceType;//0,get;1,post
    private Request request;//请求
    private RequestAction action;//请求响应

    public int getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(int instanceType) {
        this.instanceType = instanceType;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RequestAction getAction() {
        return action;
    }

    public void setAction(RequestAction action) {
        this.action = action;
    }
}

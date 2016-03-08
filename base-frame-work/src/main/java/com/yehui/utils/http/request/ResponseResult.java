package com.yehui.utils.http.request;

import com.yehui.utils.http.action.RequestAction;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求后各个参数存放的抽象类
 */
public abstract class ResponseResult {

    protected int statusCode;
    protected String message;
    protected Object resultContent;
    protected RequestAction requestAction;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResultContent() {
        return resultContent;
    }

    public void setResultContent(Object resultContent) {
        this.resultContent = resultContent;
    }

    public RequestAction getRequestAction() {
        return requestAction;
    }

    public void setRequestAction(RequestAction requestAction) {
        this.requestAction = requestAction;
    }
}

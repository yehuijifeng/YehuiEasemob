package com.yehui.utils.http.interfaces;

import com.yehui.utils.http.action.RequestAction;

import java.io.File;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求的所有方法接口
 */
public interface RequestInterface {

    //状态码
    String TAG_STATUS_CODE = "error_code";
    //信息
    String TAG_MESSAGE = "reason";
    //数据1
    String TAG_RESULT = "result";
    //数据2
    String TAG_DATA = "data";
    //数据3
    String TAG_FILES = "files";
    //数据集合
    String TAG_OBJECTS = "objects";

    int VALUE_DEFAULT_TIME_OUT = 20 * 1000;

    /**
     * get请求
     * 获取单个json对象的请求（不传文件）
     *
     * @param action 请求的哪个接口,异步请求
     */
    void sendGetRequest(RequestAction action);

    /**
     * get请求
     * 获取单个json对象的请求（不传文件）
     *
     * @param action 请求的哪个接口,单线程阻塞请求
     */
    void sendGetInstanceRequest(RequestAction action);

    /**
     * post请求
     * 获取单个json对象的请求（不传文件）
     *
     * @param action 请求的哪个接口
     */
    void sendPostRequest(RequestAction action);

    /**
     * get请求
     * 获取单个json对象的请求（不传文件）
     *
     * @param action 请求的哪个接口,单线程阻塞请求
     */
    void sendPostInstanceRequest(RequestAction action);

    /**
     * 获取单个json对象的请求（传文件）
     *
     * @param files
     * @param action
     */
    void sendPostAddFileRequest(File[] files, RequestAction action);

    /**
     * 取消某个网络请求
     *
     * @param action
     */
    void cancelByActionRequests(RequestAction action);


    /**
     * 取消所有网络请求
     *
     * @param isCancelAllRequests
     */
    void cancelAllRequests(boolean isCancelAllRequests);

    /**
     * 设置网络请求超时时间
     *
     * @param value
     */
    void setTimeOut(int value);

    /**
     * 大文件下载
     * @param url
     */
    void downloadFile(String url);
    void downloadFile(RequestAction action);
}

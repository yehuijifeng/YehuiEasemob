package com.yehui.utils.http;

/**
 * Created by yehuijifeng
 * on 2015/9/9 11:56.
 * 网络请求状态
 */
public class StatusCode {
    /**
     * 状态码:请求成功
     */
    public static final int REQUEST_SUCCESS = 0;

    /**
     * 状态码：没有更多数据
     */
    public static final int NOT_MORE_DATA = 201;

    /**
     * 状态码：服务器繁忙
     */
    public static final int SERVER_BUSY = 101;

    /**
     * 状态码：错误请求
     */
    public static final int SERVER_ERROR = 500;

    /**
     * 状态码：无网络
     */
    public static final int SERVER_NO_NETWORK = -1;
}

package com.yehui.utils.http.action;

import com.yehui.utils.http.request.RequestUrls;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 所有网络请求的枚举类型
 */
public enum RequestAction {

    /**
     * 天气预报
     */
    //GET_WEATHER(new RequestParameter(RequestUrls.GET_WEATHER_URL, String.class)),

    /**
     * 天气预报
     */
    POST_WEATHER(new RequestParameter(RequestUrls.POST_WEATHER_URL, Integer.class)),

    /**
     * 天气预报
     */
    POST_UP_FILE(new RequestParameter(RequestUrls.POST_UP_FILE_URL, String.class)),


    ;
    public RequestParameter parameter;

    RequestAction(RequestParameter parameter) {
        this.parameter = parameter;
    }


}

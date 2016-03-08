package com.yehui.utils.http.request;

import com.yehui.utils.contacts.FileContact;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求的所有url
 */
public class RequestUrls {

    public static final String IP = "http://op.juhe.cn/";
    public static final String ROOT_URL = IP + "onebox/weather/";

    /**
     * 天气预报
     */
    //public static final String GET_WEATHER_URL = ROOT_URL + "query?cityname=上海&key="+ WeatherKey.WEATHER_KEY;

    /**
     * 天气预报
     */
    public static final String POST_WEATHER_URL = ROOT_URL + "query";

    /**
     * 下载文件,大文件，566MB
     */
    public static final String POST_DOWN_FILE= FileContact.downFileUrl;

    public static final String POST_UP_FILE_URL="http://115.29.224.175:9999/alsfoxShop/site/shopcomment/insertShopComment.action?&shopComment.commentCon=Fggggg&shopComment.commentLv=5.0&shopComment.orderDetailId=672&shopComment.orderId=519&shopComment.shopId=99&shopComment.userId=27&timestamp=1452690374918&sign=499A25444D903BA751D3E0E470F9B949";

    /**
     * 下载文件，小文件，96KB
     */
    public static final String POST_DOWN_FILE_SMALL= FileContact.downFileSmallUrl;

}

package com.yehui.utils.http.request;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yehui.utils.R;
import com.yehui.utils.activity.base.BaseActivity;
import com.yehui.utils.http.StatusCode;
import com.yehui.utils.http.action.RequestAction;
import com.yehui.utils.http.bean.DownloadFileBean;
import com.yehui.utils.http.bean.RequestInstanceBean;
import com.yehui.utils.http.bean.UploadFileBean;
import com.yehui.utils.http.downfileprogress.helper.ProgressHelper;
import com.yehui.utils.http.downfileprogress.listener.ProgressListener;
import com.yehui.utils.http.interfaces.RequestInterface;
import com.yehui.utils.utils.DateUtil;
import com.yehui.utils.utils.EmptyUtil;
import com.yehui.utils.utils.GsonUtil;
import com.yehui.utils.utils.LogUtil;
import com.yehui.utils.utils.NetWorkUtil;
import com.yehui.utils.utils.files.FileContact;
import com.yehui.utils.utils.files.FileFoundUtil;
import com.yehui.utils.utils.files.FileOperationUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/**
 * Created by yehuijifeng
 * on 2016/1/10.
 * 网络请求的操作类
 * okhttp建议使用单例来操作其实例
 */
public class RequestHandle implements RequestInterface {

    /**
     * 单例请求网络,只能传入baseactivity
     */
    private static volatile RequestInterface requestHandle = null;

    private static BaseActivity mContext;

    private EventBus eventBus;

    private GsonUtil gsonUtil;

    private OkHttpClient mOkHttpClient;

    //创建队列，用于异步调用
    private Request request;

    //线程阻塞方式调用
    private Response response;
    //访问
    private Call call;

    //线程阻塞方式请求的get，post状态码
    private final static int GET_INSTANCE = 0;
    private final static int POST_INSTANCE = 1;
    /**
     * 线程阻塞方式请求的实体类
     */
    private RequestInstanceBean requestInstanceBean;

    /**
     * 文件上传的进度实体类
     */
    private UploadFileBean uploadFileBean;

    /**
     * 文件下载的进度实体类
     */
    private DownloadFileBean downloadFileBean;

    public synchronized static RequestInterface getRequestInterface(BaseActivity context) {
        mContext = context;
        if (requestHandle == null) {
            synchronized (RequestHandle.class) {
                if (requestHandle == null) {
                    requestHandle = new RequestHandle();
                }
            }
        }
        return requestHandle;
    }

    public synchronized static RequestInterface getRequestInterface() {
        if (requestHandle == null) {
            synchronized (RequestHandle.class) {
                if (requestHandle == null) {
                    requestHandle = new RequestHandle();
                }
            }
        }
        return requestHandle;
    }

    private RequestHandle() {
        //创建okHttpClient对象
        mOkHttpClient = new OkHttpClient();
        requestInstanceBean = new RequestInstanceBean();
        mOkHttpClient.setConnectTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);//连接超时
        mOkHttpClient.setReadTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);//读取超时
        mOkHttpClient.setWriteTimeout(VALUE_DEFAULT_TIME_OUT, TimeUnit.MILLISECONDS);//写入超时
        eventBus = EventBus.getDefault();
        eventBus.register(this);//注册eventbus事件
    }


    //网络请求成功
    private ResponseResult createResponseSuccess(JSONObject response, RequestAction requestAction) {
        try {
            ResponseSuccess responseSuccess = new ResponseSuccess();
            responseSuccess.setStatusCode(response.getInt(TAG_STATUS_CODE));
            responseSuccess.setMessage(response.getString(TAG_MESSAGE));
            responseSuccess.setRequestAction(requestAction);
            if (requestAction.parameter.getCls() == null) return responseSuccess;
            //单个数据
            if (response.has(TAG_RESULT)) {
                JSONObject result = gsonUtil.toJSONObject(response.getString(TAG_RESULT));
                responseSuccess.setResultContent(gsonUtil.fromJsonObject(result.getString(TAG_DATA), requestAction.parameter.getCls()));
            }
// 数据集合
//                JSONArray array = response.getJSONArray(TAG_OBJECT);
//                int length = array.length();
//                List<Object> data = new ArrayList<>();
//                for (int i = 0; i < length; i++) {
//                    data.add(gson.fromJson(array.getString(i), cls));
//                }
//                responseSuccess.setResultContent(data);
            else {
                try {
                    responseSuccess.setResultContent(response.toString());
                } catch (Exception e) {
                    responseSuccess.setResultContent(mContext.getResourceString(R.string.json_error));
                }
            }
            return responseSuccess;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    //网络请求失败
    private ResponseResult createResponseFailure(int statusCode, String responseString, RequestAction requestAction) {
        ResponseResult responseResult = new ResponseFailure();
        responseResult.setMessage(responseString);
        responseResult.setStatusCode(statusCode);
        responseResult.setRequestAction(requestAction);
        return responseResult;
    }

    //网络请求不管成功还是失败都会调用
    private ResponseResult createResponseComplete(int statusCode, String responseString, RequestAction requestAction) {
        ResponseResult responseResult = new ResponseComplete();
        responseResult.setMessage(responseString);
        responseResult.setStatusCode(statusCode);
        responseResult.setRequestAction(requestAction);
        return responseResult;
    }

    /**
     * 判断是否有网络的请求
     */
    private void judgeNetWork(RequestAction action) {
        if (!NetWorkUtil.isConnected(mContext)) {
            ResponseResult result = createResponseFailure(StatusCode.SERVER_NO_NETWORK, mContext.getResourceString(R.string.network_error), action);
            eventBus.post(result);
            return;
        }
    }

    /**
     * 网络请求成功回调后的处理方法
     *
     * @param response 响应实例
     */
    private void setResponse(Response response, final RequestAction action) {
        try {
            String jsonStr = new String(response.body().bytes(), "UTF-8");//二进制字节数组
            //String jsonStr=response.body().string();//字符串
            //InputStream inputStream=response.body().byteStream();//输入流
            if (EmptyUtil.isStringEmpty(jsonStr)) {
                eventBus.post(createResponseFailure(StatusCode.NOT_MORE_DATA, mContext.getResourceString(R.string.lading_empty), action));
                eventBus.post(createResponseComplete(StatusCode.NOT_MORE_DATA, mContext.getResourceString(R.string.lading_empty), action));
                return;
            }
            JSONObject jsonObject = new JSONObject(jsonStr);
            final int statusCode = jsonObject.getInt(TAG_STATUS_CODE);
            if (statusCode == StatusCode.REQUEST_SUCCESS) {
                eventBus.post(createResponseSuccess(jsonObject, action));
            } else {
                eventBus.post(createResponseFailure(statusCode, jsonObject.getString(TAG_MESSAGE), action));
            }
            eventBus.post(createResponseComplete(statusCode, jsonObject.getString(TAG_MESSAGE), action));
        } catch (Exception e) {
            e.printStackTrace();
            eventBus.post(createResponseFailure(StatusCode.SERVER_ERROR, mContext.getResourceString(R.string.json_error), action));
        }
    }

    /**
     * okhttp线程阻塞的
     */
    private void callBackByExecute(Request request, final RequestAction action) {
        try {
            //new call
            call = mOkHttpClient.newCall(request);
            response = call.execute();
            setResponse(response, action);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * okhttp线程队列发送，异步的
     */
    private void callBackByEnqueue(Request request, final RequestAction action) {
        //new call
        call = mOkHttpClient.newCall(request);
        //请求加入调度，请求异步进行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                String message;
                int statusCode;
                if (!NetWorkUtil.isConnected(mContext)) {
                    message = mContext.getResourceString(R.string.network_error);
                    statusCode = StatusCode.SERVER_NO_NETWORK;
                } else {
                    message = e.getLocalizedMessage();
                    statusCode = StatusCode.SERVER_BUSY;
                }
                eventBus.post(createResponseFailure(statusCode, message, action));
                eventBus.post(createResponseComplete(statusCode, message, action));
            }

            @Override
            public void onResponse(Response response) {
                setResponse(response, action);
            }
        });
    }


    /**
     * 线程阻塞方式的请求需要在ui线程中进行
     */
    public void onEventBackgroundThread(RequestInstanceBean requestInstanceBean) {
        callBackByExecute(requestInstanceBean.getRequest(), requestInstanceBean.getAction());
    }

    /**
     * 异步get请求
     *
     * @param action 请求的哪个接口,异步请求
     */
    @Override
    public void sendGetRequest(RequestAction action) {
        judgeNetWork(action);
        //创建一个Request
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())
                .build();
        callBackByEnqueue(request, action);
    }

    /**
     * 线程阻塞的单例发送get请求
     */
    @Override
    public void sendGetInstanceRequest(RequestAction action) {
        judgeNetWork(action);
        //创建一个Request
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())
                .build();
        requestInstanceBean.setInstanceType(GET_INSTANCE);
        requestInstanceBean.setRequest(request);
        requestInstanceBean.setAction(action);
        eventBus.post(requestInstanceBean);
    }

    /**
     * 异步的post请求
     *
     * @param action 请求的哪个接口
     */
    @Override
    public void sendPostRequest(RequestAction action) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Map<String, Object> parameters = action.parameter.getParameters();
        //遍历
        for (String key : parameters.keySet()) {
            builder.add(key, (String) parameters.get(key));
        }
        //如果网络请求需要签名，则加上这行代码
        //builder.add(SignUtil.KEY_SIGN, SignUtil.createSign(parameters));
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())
                .post(builder.build())
                .build();
        callBackByEnqueue(request, action);
    }

    /**
     * 提交json数据
     * public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
     * String json；
     * RequestBody body = RequestBody.create(JSON, json);
     * post(body);
     *
     */

    /**
     * 线程阻塞的post请求
     *
     * @param action 请求的哪个接口,单线程阻塞请求
     */
    @Override
    public void sendPostInstanceRequest(RequestAction action) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        Map<String, Object> parameters = action.parameter.getParameters();
        //遍历map中所有参数到builder
        for (String key : parameters.keySet()) {
            builder.add(key, (String) parameters.get(key));
        }
        //如果网络请求需要签名，则加上这行代码
        //builder.add(SignUtil.KEY_SIGN, SignUtil.createSign(parameters));
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())
                .post(builder.build())
                .build();
        requestInstanceBean.setInstanceType(POST_INSTANCE);
        requestInstanceBean.setRequest(request);
        requestInstanceBean.setAction(action);
        eventBus.post(requestInstanceBean);
    }

    /**
     * 非ui线程回调文件上传进度
     * 这个是非ui线程回调，不可直接操作UI
     */
    final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void onProgress(long bytesWrite, long contentLength, boolean done) {
            uploadFileBean.setBytesWrite(bytesWrite);
            uploadFileBean.setContentLength(contentLength);
            uploadFileBean.setDone(done);
            String donePercent = (100 * bytesWrite) / contentLength + "%";//上传进度
            eventBus.post(uploadFileBean);
        }
    };

    /**
     * 非ui线程回调文件下载进度
     * 这个是非ui线程回调，不可直接操作UI
     */
    final ProgressListener progressResponseListener = new ProgressListener() {
        @Override
        public void onProgress(long bytesRead, long contentLength, boolean done) {
            downloadFileBean.setDone(false);//当成功回调后才返回true
            downloadFileBean.setBytesRead(bytesRead);
            downloadFileBean.setContentLength(contentLength);
            //长度未知的情况下回返回-1
            if (contentLength != -1 && (100 * bytesRead) / contentLength >= 0) {
                String donePercent = (100 * bytesRead) / contentLength + "%";//上传进度
                downloadFileBean.setDownSuccess(false);
            }
            downloadFileBean.setDownSuccess(true);
            eventBus.post(downloadFileBean);
        }
    };
    /**
     * 参数类型
     * "text", 文本
     * "image", 图片
     * "audio",音频
     * "video"，视频
     */
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final MediaType MEDIA_TYPE_AUDIO = MediaType.parse("audio/mp3");
    private static final MediaType MEDIA_TYPE_VIDEO = MediaType.parse("video/mp4");
    public static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/x-markdown; charset=utf-8");
    public static final MediaType MEDIA_TYPE_OBJECT = MediaType.parse("application/octet-stream");

    @Override
    public void sendPostAddFileRequest(File[] files, RequestAction action) {
        uploadFileBean = new UploadFileBean();
        uploadFileBean.setUrl(action.parameter.getRequestUrl());
        //多部分组成的建造器
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
//        Map<String, Object> parameters = action.parameter.getParameters();
//        //遍历map中所有参数到builder
//        for (String key : parameters.keySet()) {
//            builder.addFormDataPart(key, (String) parameters.get(key));
//        }
        if (files != null || files.length != 0) {
            //遍历file数组，得到所有file文件
            for (File file : files) {
                builder.addFormDataPart(TAG_FILES, null, RequestBody.create(MEDIA_TYPE_OBJECT, file));
            }
        }
        //构建请求
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())//地址
                .post(ProgressHelper.addProgressRequestListener(builder.build(), progressListener))
                //.post(builder.build())//添加请求体
                .build();
        //发送异步请求
        callBackByEnqueue(request, action);
    }

    @Override
    public void cancelByActionRequests(RequestAction action) {
        /**
         * 取消一个Call
         使用Call.cancel()可以立即停止掉一个正在执行的call。
         注意：如果一个线程正在写请求或者读响应，将会引发IOException。

         你可以通过tags来同时取消多个请求。
         当你构建一请求时，使用RequestBuilder.tag(tag)来分配一个标签。
         之后你就可以用OkHttpClient.cancel(tag)来取消所有带有这个tag的call。
         */
        if (call != null) {
            try {
                if (request != null && request.urlString().equals(action.parameter.getRequestUrl()))
                    call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(mContext.getResourceString(R.string.call_error));
            }
        }
    }

    @Override
    public void cancelAllRequests(boolean isCancelAllRequests) {
        if (call != null) {
            try {
                if (isCancelAllRequests)
                    call.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(mContext.getResourceString(R.string.call_error));
            }
        }
    }

    @Override
    public void setTimeOut(int value) {
        if (mOkHttpClient == null) return;
        mOkHttpClient.setConnectTimeout(value, TimeUnit.MILLISECONDS);//连接超时
        mOkHttpClient.setReadTimeout(value, TimeUnit.MILLISECONDS);//读取超时
        mOkHttpClient.setWriteTimeout(value, TimeUnit.MILLISECONDS);//写入超时
    }

    /**
     * 带请求参数验证的下载文件
     */
    private void postFileEnqueue(Request request, final RequestAction action, final String url) {
        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(mOkHttpClient, progressResponseListener)
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        String message;
                        int statusCode;
                        if (!NetWorkUtil.isConnected(mContext)) {
                            message = mContext.getResourceString(R.string.network_error);
                            statusCode = StatusCode.SERVER_NO_NETWORK;
                        } else {
                            message = e.getLocalizedMessage();
                            statusCode = StatusCode.SERVER_BUSY;
                        }
                        if (action != null) {
                            LogUtil.e(message);
                            eventBus.post(createResponseFailure(statusCode, message, action));
                            eventBus.post(createResponseComplete(statusCode, message, action));
                        }
                        LogUtil.e("文件保存失败");
                        downloadFileBean.setDone(false);
                        eventBus.post(downloadFileBean);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        String fileName, message;
                        if (action != null)
                            fileName = DateUtil.getNow("yyyyMMddHHmmss") + "_" + action.parameter.getRequestUrl().substring(action.parameter.getRequestUrl().lastIndexOf("/") + 1, action.parameter.getRequestUrl().length());
                        else if (url != null)
                            fileName = DateUtil.getNow("yyyyMMddHHmmss") + "_" + url.substring(url.lastIndexOf("/") + 1, url.length());
                        else return;
                        InputStream inputStream = response.body().byteStream();//输入流
                        if (FileFoundUtil.insertSDCardFromInput(FileContact.YEHUI_FILES_PATH, fileName, inputStream))
                            message = "下载完成";
                        else
                            message = "文件保存失败";
                        if (action != null) {
                            try {
                                eventBus.post(createResponseSuccess(new JSONObject(message), action));
                                eventBus.post(createResponseComplete(StatusCode.SERVER_ERROR, message, action));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        downloadFileBean.setDone(true);
                        downloadFileBean.setFileBean(FileOperationUtil.queryFileByDetails(new File(FileContact.YEHUI_FILES_PATH, fileName)));
                        eventBus.post(downloadFileBean);
                    }
                });
    }

    /**
     * 单url的下载文件
     */
    private void postFileEnqueue(Request request, String url) {
        postFileEnqueue(request, null, url);
    }

    @Override
    public void downloadFile(String url) {
        downloadFileBean = new DownloadFileBean();
        downloadFileBean.setUrl(url);
        request = new Request.Builder()
                .url(url)
                .build();
        postFileEnqueue(request, url);
    }

    @Override
    public void downloadFile(RequestAction action) {
        downloadFileBean = new DownloadFileBean();
        downloadFileBean.setUrl(action.parameter.getRequestUrl());
        Map<String, Object> parameters = action.parameter.getParameters();
        //多部分组成的建造器
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        //遍历map中所有参数到builder
        for (String key : parameters.keySet()) {
            builder.addFormDataPart(key, (String) parameters.get(key));
        }
        //构建请求体
        RequestBody requestBody = builder.build();
        request = new Request.Builder()
                .url(action.parameter.getRequestUrl())
                .post(requestBody)//添加请求体
                .build();
        postFileEnqueue(request, action, null);
    }
}

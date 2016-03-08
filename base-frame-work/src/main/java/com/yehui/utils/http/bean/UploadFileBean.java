package com.yehui.utils.http.bean;

/**
 * Created by yehuijifeng
 * on 2016/1/13.
 * 文件上传的进度实体类
 */
public class UploadFileBean {

    private String url;//上传的地址，作为唯一标识
    private long bytesWrite;//写入字节
    private long contentLength;//总长度
    private boolean done;//是否完成


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getBytesWrite() {
        return bytesWrite;
    }

    public void setBytesWrite(long bytesWrite) {
        this.bytesWrite = bytesWrite;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}

package com.yehui.utils.http.bean;

import com.yehui.utils.utils.files.FileBean;

/**
 * Created by yehuijifeng
 * on 2016/1/13.
 * 文件下载进度实体类
 */
public class DownloadFileBean {
    private String url;//唯一标识，下载的地址
    private long bytesRead;//读取字节
    private long contentLength;//总长度
    private boolean done;//是否完成
    private FileBean fileBean;//如果下载完成，则获取文件详细信息
    private boolean isDownSuccess;//是否正确获得下载资源

    public boolean isDownSuccess() {
        return isDownSuccess;
    }

    public void setDownSuccess(boolean downSuccess) {
        isDownSuccess = downSuccess;
    }

    public FileBean getFileBean() {
        return fileBean;
    }

    public void setFileBean(FileBean fileBean) {
        this.fileBean = fileBean;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
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

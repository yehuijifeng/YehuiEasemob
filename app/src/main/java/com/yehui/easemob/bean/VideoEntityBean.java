package com.yehui.easemob.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Luhao
 * on 2016/4/15.
 * 视频资源实体类
 */
public class VideoEntityBean implements Parcelable {


    private int id;
    private String title;//
    private String filePath;//路径
    private int duration;//时长
    private int size;//大小

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }


}

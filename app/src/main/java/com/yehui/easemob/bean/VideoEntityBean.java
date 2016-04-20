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
    private String title;//  文件名称
    private String filePath;//路径
    private int duration;//时长
    private int size;//大小
    private String imgPath;//缩略图地址

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

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


    public VideoEntityBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.filePath);
        dest.writeInt(this.duration);
        dest.writeInt(this.size);
        dest.writeString(this.imgPath);
    }

    private VideoEntityBean(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.filePath = in.readString();
        this.duration = in.readInt();
        this.size = in.readInt();
        this.imgPath = in.readString();
    }

    public static final Creator<VideoEntityBean> CREATOR = new Creator<VideoEntityBean>() {
        public VideoEntityBean createFromParcel(Parcel source) {
            return new VideoEntityBean(source);
        }

        public VideoEntityBean[] newArray(int size) {
            return new VideoEntityBean[size];
        }
    };
}

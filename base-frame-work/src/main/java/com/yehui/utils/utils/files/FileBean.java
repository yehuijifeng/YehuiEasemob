package com.yehui.utils.utils.files;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 存放文件信息的实体类
 */
public class FileBean implements Parcelable{

    private File file;//该文件
    private String fileName;//文件名
    private long fileLength;//文件长度
    private String fileSize;//文件大小
    private String filePath;//文件路径
    private boolean isDirectory;//是否是文件夹


    protected FileBean(Parcel in) {
        fileName = in.readString();
        fileLength = in.readLong();
        fileSize = in.readString();
        filePath = in.readString();
        isDirectory = in.readByte() != 0;
    }

    public static final Creator<FileBean> CREATOR = new Creator<FileBean>() {
        @Override
        public FileBean createFromParcel(Parcel in) {
            return new FileBean(in);
        }

        @Override
        public FileBean[] newArray(int size) {
            return new FileBean[size];
        }
    };

    public FileBean() {

    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean directory) {
        isDirectory = directory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeLong(fileLength);
        dest.writeString(fileSize);
        dest.writeString(filePath);
        dest.writeByte((byte) (isDirectory ? 1 : 0));
    }
}

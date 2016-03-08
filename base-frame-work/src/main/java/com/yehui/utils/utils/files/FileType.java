package com.yehui.utils.utils.files;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 文件类型
 */
public class FileType {

    private static FileType fileType = null;

    private FileType() {
    }

    public static FileType getFileType() {
        if (fileType == null) {
            synchronized (FileType.class) {
                if (fileType == null) {
                    fileType = new FileType();
                }
            }
        }
        return fileType;
    }

    //音乐类型文件
    public String[] musicFileType() {
        String[] music = {"mp3", "WAV", "MIDI", "MP3Pro", "WMA", "SACD", "QuickTime", "VQF", "lrc"};
        return music;
    }


    //视频类型文件
    public String[] videoFileType() {
        String[] video = {"avi", "mp4", "mpeg", "wmv", "WMA", "mov", "flv", "VQF", "rmvb"};
        return video;
    }

    //文档类型文件
    public String[] wordFileType() {
        String[] office = {"doc", "docx"};
        return office;
    }

    //表格类型文件
    public String[] excelFileType() {
        String[] office = {"xls", "xlsx"};
        return office;
    }

    //表格类型文件
    public String[] pptFileType() {
        String[] office = {"ppt", "pptx"};
        return office;
    }

    //记事本类型文件
    public String[] txtFileType() {
        String[] office = {"txt"};
        return office;
    }

    //app类型文件
    public String[] apkFileType() {
        String[] office = {"apk"};
        return office;
    }

    //图片类型文件
    public String[] imageFileType() {
        String[] image = {"BMP", "GIF", "JPEG", "TIFF", "PSD", "PNG", "3gp", "jpg"};
        return image;
    }

    //压缩文件类型文件
    public String[] zipFileType() {
        String[] zip = {"zip", "rar", "iso", "tar", "jar", "UUEuue", "ARJ", "KZ"};
        return zip;
    }
}

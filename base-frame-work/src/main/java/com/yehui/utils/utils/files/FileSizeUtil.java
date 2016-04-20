package com.yehui.utils.utils.files;

/**
 * Created by Luhao
 * on 2016/4/20.
 * 获得文件大小的工具类
 */
public class FileSizeUtil {

    public static String getFileSize(long size, FileSizeType fileSizeType) {
        int endSize = 0;
        if (fileSizeType == FileSizeType.FILE_KB) {
            if (size > 1024) {
                endSize = (int) (size / 1024);
            }
        } else if (fileSizeType == FileSizeType.FILE_MB) {
            if (size > 1024 * 1024) {
                endSize = (int) (size / 1024 / 1024);
            } else if (size > 1024) {
                endSize = (int) (size / 1024);
            }
        } else if (fileSizeType == FileSizeType.FILE_GB) {
            if (size > 1024 * 1024 * 1024) {
                endSize = (int) (size / 1024 / 1024 / 1024);
            } else if (size > 1024 * 1024) {
                endSize = (int) (size / 1024 / 1024);
            } else if (size > 1024) {
                endSize = (int) (size / 1024);
            }
        }
        return endSize + fileSizeType.getName();
    }

    public static String getFileSize(long size) {
        int endSize = 0;
        FileSizeType fileSizeType = FileSizeType.FILE_KB;
        if (size > 1024 * 1024 * 1024) {
            endSize = (int) (size / 1024 / 1024 / 1024);
            fileSizeType = FileSizeType.FILE_GB;
        } else if (size > 1024 * 1024) {
            endSize = (int) (size / 1024 / 1024);
            fileSizeType = FileSizeType.FILE_MB;
        } else if (size > 1024) {
            endSize = (int) (size / 1024);
        }

        return endSize + fileSizeType.getName();
    }
}

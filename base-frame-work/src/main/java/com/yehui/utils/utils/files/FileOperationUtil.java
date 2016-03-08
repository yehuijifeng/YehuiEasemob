package com.yehui.utils.utils.files;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 本地文件的操作工具类
 */
public class FileOperationUtil {

    /**
     * 防止被实例化
     */
    private FileOperationUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 是否存在该文件夹
     *
     * @param path
     * @return
     */
    public static boolean isHaveFileDirectory(String path) {

        File file = new File(path);
        if (file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否存在该文件
     *
     * @param path
     * @return
     */
    public static boolean isHaveFile(String path) {

        File file = new File(path);
        if (file.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    public static String getSDAllSize(Context con) {
        File path = FileContact.getSDFile();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(con, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDRemainingSize(Context con) {
        File path = FileContact.getSDFile();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(con, blockSize * availableBlocks);
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    public static String getRomAllSize(Context con) {
        File path = FileContact.getSDFile();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(con, blockSize * totalBlocks);
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static String getRomRemainingSize(Context con) {
        File path = FileContact.getSDFile();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(con, blockSize * availableBlocks);
    }

    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(FileContact.getSDPath())) {
            filePath = FileContact.getSDPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }

    /**
     * 文件详情
     */
    public static FileBean queryFileByDetails(File f) {
        FileBean fileBean = new FileBean();
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        fileBean.setFile(f);
        fileBean.setDirectory(f.isDirectory());
        fileBean.setFileName(f.getName());
        fileBean.setFilePath(f.getPath());
        if (!f.isDirectory()) {
            fileBean.setFileLength(f.length());
            if (((double) f.length() / 1024 / 1024) <= 1) {
                fileBean.setFileSize(decimalFormat.format(f.length() / 1024) + "KB");
            } else if (((double) f.length() / 1024 / 1024 / 1024) <= 1) {
                fileBean.setFileSize(decimalFormat.format(f.length() / 1024 / 1024) + "MB");
            } else if (((double) f.length() / 1024 / 1024 / 1024 / 1024) <= 1) {
                fileBean.setFileSize(decimalFormat.format(f.length() / 1024 / 1024 / 1024) + "GB");
            } else if (((double) f.length() / 1024 / 1024 / 1024 / 1024 / 1024) <= 1) {
                fileBean.setFileSize(decimalFormat.format(f.length() / 1024 / 1024 / 1024 / 1024) + "TB");
            }
        } else
            fileBean.setFileSize("");
        return fileBean;
    }

    private static ExecutorService threadPoolFind;//线程池
    private static List<FileBean> sdFileListFind;//文件集合


    /**
     * 外部调用，获得本机所有的文件集合
     */
    public static List<FileBean> querySDFileByAll() {
        queryFileByAll(FileContact.getSDFile());
        return sdFileListFind;
    }

    /**
     * 外部调用，获得本机指定路径的文件内容
     */
    public static FileBean querySDFileByFile(String fileName) {
        File f = new File(fileName);
        if (f.isFile()) {
            return  queryFileByDetails(f);
        }
        return null;
    }


    /**
     * 查询本机sd卡所有文件夹，内部自己调用的方法，不需要外部调用
     */
    private static void queryFileByAll(File filePath) {
        if (filePath == FileContact.getSDFile()) {
            sdFileListFind = new ArrayList<>();
            threadPoolFind = Executors.newFixedThreadPool(5);
        }
        File[] files = filePath.listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isFile()) {
                    FileBean fileBean = queryFileByDetails(f);
                    sdFileListFind.add(fileBean);
                } else {
                    threadPoolFind.execute(new Runnable() {
                        @Override
                        public void run() {
                            queryFileByDetails(f);
                        }
                    });
                }
            }
        }
    }


    private static ExecutorService threadPoolSerch;//线程池
    private static List<FileBean> sdFileListSerch;//文件集合

    /**
     * 外部调用，获得本机搜索出来的文件
     */
    public static List<FileBean> querySDFileByName(String name) {
        queryFileByName(FileContact.getSDFile(), name);
        return sdFileListSerch;
    }

    /**
     * 根据关键词查询本地文件
     */
    private static void queryFileByName(File filePath, final String name) {
        if (filePath == FileContact.getSDFile()) {
            sdFileListSerch = new ArrayList<>();
            threadPoolSerch = Executors.newFixedThreadPool(5);
        }
        File[] files = filePath.listFiles();
        if (files != null) {
            for (final File f : files) {
                if (f.isFile()) {
                    if (f.getName().contains(name)) {
                        FileBean fileBean = queryFileByDetails(f);
                        sdFileListSerch.add(fileBean);
                    }
                } else {
                    threadPoolSerch.execute(new Runnable() {
                        @Override
                        public void run() {
                            queryFileByName(f, name);
                        }
                    });
                }
            }

        }
    }

    /**
     * 查询文件类型
     * 0,文件夹；
     * 1，图片
     * 2，音乐
     * 3，视频
     * 4，文档
     * 5，表格
     * 6，ppt
     * 7，apk
     * 8，记事本
     * 9，压缩文件
     * 10，其他
     */
    public static int queryFileType(File f) {
        if (f.isDirectory()) {
            return 0;
        } else {
            String fileName = f.getName();
            String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
            String image[] = FileType.getFileType().imageFileType();
            for (int ii = 0; ii < image.length; ii++) {
                if (prefix.equalsIgnoreCase(image[ii])) {
                    return 1;
                }
            }
            String music[] = FileType.getFileType().musicFileType();
            for (int ii = 0; ii < music.length; ii++) {
                if (prefix.equalsIgnoreCase(music[ii])) {
                    return 2;
                }
            }
            String video[] = FileType.getFileType().videoFileType();
            for (int ii = 0; ii < video.length; ii++) {
                if (prefix.equalsIgnoreCase(video[ii])) {
                    return 3;
                }
            }
            String word[] = FileType.getFileType().wordFileType();
            for (int ii = 0; ii < word.length; ii++) {
                if (prefix.equalsIgnoreCase(word[ii])) {
                    return 4;
                }
            }
            String excel[] = FileType.getFileType().excelFileType();
            for (int ii = 0; ii < excel.length; ii++) {
                if (prefix.equalsIgnoreCase(excel[ii])) {
                    return 5;
                }
            }
            String ppt[] = FileType.getFileType().pptFileType();
            for (int ii = 0; ii < ppt.length; ii++) {
                if (prefix.equalsIgnoreCase(ppt[ii])) {
                    return 6;
                }
            }
            String apk[] = FileType.getFileType().apkFileType();
            for (int ii = 0; ii < apk.length; ii++) {
                if (prefix.equalsIgnoreCase(apk[ii])) {
                    return 7;
                }
            }
            String txt[] = FileType.getFileType().txtFileType();
            for (int ii = 0; ii < txt.length; ii++) {
                if (prefix.equalsIgnoreCase(txt[ii])) {
                    return 7;
                }
            }
            String zip[] = FileType.getFileType().zipFileType();
            for (int ii = 0; ii < zip.length; ii++) {
                if (prefix.equalsIgnoreCase(zip[ii])) {
                    return 9;
                }
            }
            return 10;
        }
    }
}
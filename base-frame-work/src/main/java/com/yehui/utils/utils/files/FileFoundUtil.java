package com.yehui.utils.utils.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 创建文件工具类，复制粘贴等
 */
public class FileFoundUtil {
    /**
     * 防止被实例化
     */
    private FileFoundUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 删除文件夹下全部文件
     * 递归删除
     */
    public static void deleteFileByPath(String filePath) {
        File file=new File(filePath);
        if (file.isFile())
            file.delete();
        else {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                file.delete();
                return ;
            }
            if (files != null) {
                for (final File f : files) {
                    if (f.isFile()) {
                        file.delete();
                    } else {
                        deleteFileByPath(f.getPath());
                    }
                }
            }
        }
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public static boolean insertSDCardFromInput(String path, String fileName,
                                                InputStream input) {
        File file;
        OutputStream output = null;
        try {
            if (!FileOperationUtil.isSDCardEnable()) return false;
            file = createFile(path, fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[1024];
            while (true) {
                int temp = input.read(buffer, 0, buffer.length);
                if (temp == -1) {
                    break;
                }
                output.write(buffer, 0, temp);
            }
            output.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    /**
     * 添加文件到指定路径,该文件创建出来是空的
     *
     * @param filePath
     * @param fileName
     * @return
     */
    public static File createFile(String filePath, String fileName) {
        try {
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建文件夹规则说明
     * 1，参数1，文件类型，0——文件夹，1——文件
     * 2，参数2，需要创建的文件路径
     * 3，参数3，如果该文件存在，是否替换
     * 4，返回参数：0，失败；1，成功；2，文件已存在；
     */
    public static int createFile(int fileType, String filePath, boolean bl) {
        File file = new File(filePath);
        if (fileType == 0) {
            if (!file.exists()) {
                try {
                    file.mkdirs();
                    return 1;
                } catch (Exception e) {
                    return 0;
                }
            } else {
                if (bl) {
                    deleteFileByPath(file.getPath());
                    try {
                        file.mkdirs();
                        return 1;
                    } catch (Exception e) {
                        return 0;
                    }
                } else
                    return 2;
            }
        } else {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    return 1;
                } catch (Exception e) {
                    return 0;
                }
            } else {
                if (bl) {
                    deleteFileByPath(file.getPath());
                    try {
                        file.createNewFile();
                        return 1;
                    } catch (Exception e) {
                        return 0;
                    }
                } else
                    return 2;
            }
        }
    }

    /**
     * 复制
     */
    public static boolean copyFile(File toFile, String filePath) {
        return copyFile(toFile, filePath, toFile.getName());
    }

    /**
     * 复制
     */
    public static boolean copyFile(File toFile, String filePath, String fileName) {
        try {
            if (toFile != null && toFile.exists()) {
                // 另外还需要一个输出流来将复制的内容写入到文件中去
                //File toFile = new File(filePath);

                // 复制：先读取，然后写入
                FileInputStream fis = new FileInputStream(toFile);

                // 为了效率高使用Buffered带缓冲区的输入流去包装fis
                BufferedInputStream buf = new BufferedInputStream(fis);

                File paste = new File(filePath + "/" + fileName);
                if (!paste.exists()) {
                    paste.createNewFile();
                }
                FileOutputStream fos = new FileOutputStream(paste);
                BufferedOutputStream bufO = new BufferedOutputStream(fos);

                byte[] b = new byte[1024];

                int len;
                while ((len = buf.read(b)) != -1) {
                    // 写入
                    bufO.write(b, 0, len);
                }
                bufO.flush();
                buf.close();
                bufO.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 剪切
     */
    public boolean shearFile(File toFile, String filePath) {
        if (copyFile(toFile, filePath, toFile.getName())) {
            toFile.delete();
            return true;
        }
        return false;
    }

    /**
     * 剪切
     */
    public boolean shearFile(File toFile, String filePath,String fileName) {
        if (copyFile(toFile, filePath, fileName)) {
            toFile.delete();
            return true;
        }
        return false;
    }


}

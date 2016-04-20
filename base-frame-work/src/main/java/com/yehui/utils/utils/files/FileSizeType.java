package com.yehui.utils.utils.files;

/**
 * Created by Luhao
 * on 2016/4/20.
 * 文件大小的枚举约束
 */
public enum FileSizeType {

    FILE_KB {
        public String getName() {
            return "KB";
        }
    },

    FILE_MB {
        public String getName() {
            return "MB";
        }
    },

    FILE_GB {
        public String getName() {
            return "GB";
        }
    },

    FILE_TB {
        public String getName() {
            return "TB";
        }
    },;

    public abstract String getName();
}

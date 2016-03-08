package com.yehui.utils.projectmodel;

/**
 * Created by yehuijifeng
 * on 2016/1/7.
 * 23中设计模式——单例模式
 */
public class Instance {
    /**
     * 将该类设置成单例模式
     * 当某个项目不用这个工具类则不会占用内存
     * 若用了该类，保证整个项目只有一个实例
     */

    /**
     * volatile的作用是： 作为指令关键字，确保本条指令不会因编译器的优化而省略，且要求每次直接读值.
     * 简单地说就是防止编译器对代码进行优化.比如如下程序：
     * XBYTE[2]=0x55;
     * XBYTE[2]=0x56;
     * XBYTE[2]=0x57;
     * XBYTE[2]=0x58;
     * 对外部硬件而言，上述四条语句分别表示不同的操作，会产生四种不同的动作，
     * 但是编译器却会对上述四条语句进行优化，认为只有XBYTE[2]=0x58（即忽略前三条语句，只产生一条机器代码）。
     * 如果键入volatile，则编译器会逐一的进行编译并产生相应的机器代码（产生四条代码）.
     */
    private static volatile Instance instance = null;

    private Instance() {
    }

    public synchronized static Instance getInstance() {
        if (instance == null) {
            synchronized (Instance.class) {
                if (instance == null) {
                    instance = new Instance();
                }
            }
        }
        return instance;
    }

}

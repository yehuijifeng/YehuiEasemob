package com.yehui.utils.db.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by yehuijifeng
 * on 2016/1/6.
 * 数据库帮助类代理类
 */
public class BaseDBHelper extends OrmLiteSqliteOpenHelper {

    private static BaseDBHelper instance;

    /**
     * 参数说明
     *
     * @param context 上下文
     *                2 数据库名称
     *                3 代理工厂，null
     *                4 数据库版本
     */
    private BaseDBHelper(Context context) {
        super(context, DBContact.DB_NAME, null, DBContact.DB_VERSION);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized BaseDBHelper getInstance(Context context, Class cla) {
        if (instance == null) {
            synchronized (BaseDBHelper.class) {
                if (instance == null) {
                    instance = new BaseDBHelper(context);
                    getClass = cla;
                }
            }
        }
        return instance;
    }


    private static Class getClass;
    /**
     * 数据库实例
     */
    protected SQLiteDatabase sqLiteDatabase;

    /**
     * 连接源
     */
    protected ConnectionSource connectionSource = getConnectionSource();

    /**
     * 创建数据库
     *
     * @param sqLiteDatabase   数据库
     * @param connectionSource 连接源
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            /**
             * 创建表
             * 1，连接源
             * 2，表信息
             */
            TableUtils.createTable(connectionSource, getClass);
            this.sqLiteDatabase = sqLiteDatabase;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 升级数据库
     *
     * @param sqLiteDatabase   数据库
     * @param connectionSource 连接源
     * @param i                原来的版本
     * @param i1               升级的版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            /**
             * 先删除表，然后再调用创建表
             * 1，连接源
             * 2，表信息
             * 3，是否忽略错误
             */
            TableUtils.dropTable(connectionSource, getClass, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动关闭数据库
     */
    public void closeDBHelper() {
        instance = null;
        getClass = null;
    }

    /**
     * 修改数据库的表结构
     */
    public void updateDB(Class cla) {
        /**
         * 先删除表，然后再调用创建表
         * 1，连接源
         * 2，表信息
         * 3，是否忽略错误
         */
        try {
            TableUtils.dropTable(getConnectionSource(), getClass, true);
            TableUtils.createTable(getConnectionSource(), cla);
            getClass=cla;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据库的表结构
     */
    public void deleteDB() {
        /**
         * 先删除表，然后再调用创建表
         * 1，连接源
         * 2，表信息
         * 3，是否忽略错误
         */
        try {
            TableUtils.dropTable(getConnectionSource(), getClass, true);
            getClass=null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重置数据库的表结构,无参数，则表示重置原来的表结构
     */
    public void resetDB() {
        /**
         * 先删除表，然后再调用创建表
         * 1，连接源
         * 2，表信息
         * 3，是否忽略错误
         */
        try {
            TableUtils.dropTable(getConnectionSource(), getClass, true);
            TableUtils.createTable(getConnectionSource(), getClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

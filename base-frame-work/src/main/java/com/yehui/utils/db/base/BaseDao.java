package com.yehui.utils.db.base;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by yehuijifeng
 * on 2016/1/6.
 * 数据库dao方法的代理类
 */
public abstract class BaseDao {

    /**
     * 条件查询QueryBuilder的使用：
     * 1、简单的where 条件
     * articleDaoOpe.queryBuilder().where().eq("user_id", userId).query();直接返回Article的列表
     *
     * 2,where and
     * QueryBuilder<Article, Integer> queryBuilder = articleDaoOpe
     .queryBuilder();
     Where<Article, Integer> where = queryBuilder.where();
     where.eq("user_id", 1);
     where.and();
     where.eq("name", "xxx");

     //或者
     articleDaoOpe.queryBuilder().//
     where().//
     eq("user_id", 1).and().//
     eq("name", "xxx");
     *
     *
     *上述两种都相当与：select * from tb_article where user_id = 1 and name = 'xxx' ;
     *
     *
     *3、更复杂的查询
     *
     * where.or(
     * where.and(
     * where.eq("user_id", 1),
     * where.eq("name", "xxx")),
     * where.and(where.eq("user_id", 2),
     * where.eq("name", "yyy")));
     *
     * 相当于：select * from tb_article where ( user_id = 1 and name = 'xxx' )  or ( user_id = 2 and name = 'yyy' )  ;
     *
     *
     * 4，事务操作
     TransactionManager.callInTransaction(helper.getConnectionSource(),
     new Callable<Void>()
     {

     @Override public Void call() throws Exception
     {
     return null;
     }
     });
     */

    /**
     * 传入上下文
     */
    protected abstract Context setContext();

    protected abstract Class setClass();

    protected abstract Dao setDbDao();

    /**
     * 删除数据库
     */
    public void deleteDB() {
        BaseDBHelper.getInstance(setContext(), setClass()).deleteDB();
    }

    /**
     * 修改数据库结构
     */
    public void updateDB(Class cla) {
        BaseDBHelper.getInstance(setContext(), setClass()).updateDB(cla);
    }

    /**
     * 重置数据库结构
     */
    public void resetDB() {
        BaseDBHelper.getInstance(setContext(), setClass()).resetDB();
    }

    /**
     * @param var 添加数据
     */
    public void addData(Object var) {
        try {
            setDbDao().create(var);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * @param var 更新数据
     */
    public void updateData(Object var) {
        try {
//            OrmLiteDemoTowBean ormLiteDemoTowBean = new OrmLiteDemoTowBean("testsd");
//            ormLiteDemoTowBean.setTest_id(1);
            setDbDao().update(var);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 更具id删除数据
     *
     * @param id
     */
    public void deleteById(int id) {
        try {
            setDbDao().deleteById(id);
            //dbDao.delete(ormLiteDemoTowBean);
        } catch (Exception e) {
            return;
        }
    }

    /**
     * 根据id查询单个信息
     *
     * @param id
     * @return
     */
    public Object queryById(int id) {
        try {
            return setDbDao().queryForId(id);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据id查询单个信息
     */
    public List queryByWhere(String key, String value) {
        try {
            QueryBuilder builder = setDbDao().queryBuilder();
            builder.where().eq(key, value);
            return builder.query();
            //return setDbDao().queryForEq(key, value);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 查询所有数据
     *
     * @return
     */
    public List queryAll() {
        try {
            return setDbDao().queryForAll();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 通过UserId获取所有的文章
     * 键值对查询全部
     */
    public List queryAllByWhere(String key, int valueById) {
        try {
            return setDbDao().queryBuilder().where().eq(key, valueById).query();
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * 释放资源
     */
    public void closeDao() {
        BaseDBHelper.getInstance(setContext(), setClass()).closeDBHelper();
    }
}

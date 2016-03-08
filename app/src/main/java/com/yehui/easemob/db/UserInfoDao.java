package com.yehui.easemob.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.yehui.easemob.bean.UserInfoBean;
import com.yehui.utils.db.base.BaseDBHelper;
import com.yehui.utils.db.base.BaseDao;

import java.sql.SQLException;

/**
 * Created by Luhao
 * on 2016/2/23.
 * 具体单张表的增删改查
 */
public class UserInfoDao extends BaseDao {

    private Context context;
    private Dao<UserInfoBean, Integer> dbDao;

    public UserInfoDao(Context context) {
        this.context = context;
        try {
            dbDao= BaseDBHelper.getInstance(setContext(),setClass()).getDao(UserInfoBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Context setContext() {
        return context;
    }

    @Override
    protected Class setClass() {
        return UserInfoBean.class;
    }

    @Override
    protected Dao setDbDao() {
        return dbDao;
    }
}

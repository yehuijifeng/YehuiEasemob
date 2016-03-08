package com.yehui.utils.bean.ormlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 添加数据添加测试实体类
 */
@DatabaseTable(tableName = "tab_demo")
public class OrmLiteDemoBean implements Parcelable {
    /**
     * 反射实例解释：
     * @DatabaseTable(tableName = "tab_demo")标明这是数据库中的一张表,表名为：tab_demo
     * @DatabaseField(generatedId = true) ，generatedId 表示id为主键且自动生成
     * @DatabaseField(columnName = "name")columnName的值为该字段在数据中的列名
     */
    public OrmLiteDemoBean() {
    }

    @DatabaseField(generatedId = true)
    private int sql_id;

    @DatabaseField(columnName = "sql_flast")
    private float sql_flast;

    @DatabaseField(columnName = "sql_double")
    private double sql_double;

    @DatabaseField(columnName = "sql_string")
    private String sql_string;

    @DatabaseField(columnName = "sql_boolean")
    private boolean sql_boolean;

    @DatabaseField(columnName = "sql_integer")
    private int sql_integer;

    @DatabaseField(columnName = "sql_list")
    private List<Integer> sql_list;

    @DatabaseField(columnName = "sql_long")
    private long sql_long;

    @DatabaseField(columnName = "sql_ints")
    private int[] sql_ints;

    @DatabaseField(columnName = "sql_obj")
    private Object sql_obj;

    @DatabaseField(columnName = "sqlLiteDemoTowBean")
    private OrmLiteDemoTowBean sqlLiteDemoTowBean;


    //超类
    public OrmLiteDemoBean(int sql_id, float sql_flast, double sql_double, String sql_string, boolean sql_boolean, int sql_integer, List<Integer> sql_list, long sql_long, int[] sql_ints, Object sql_obj, OrmLiteDemoTowBean sqlLiteDemoTowBean) {
        this.sql_id = sql_id;
        this.sql_flast = sql_flast;
        this.sql_double = sql_double;
        this.sql_string = sql_string;
        this.sql_boolean = sql_boolean;
        this.sql_integer = sql_integer;
        this.sql_list = sql_list;
        this.sql_long = sql_long;
        this.sql_ints = sql_ints;
        this.sql_obj = sql_obj;
        this.sqlLiteDemoTowBean = sqlLiteDemoTowBean;
    }
    public OrmLiteDemoBean( float sql_flast, double sql_double, String sql_string, boolean sql_boolean, int sql_integer, List<Integer> sql_list, long sql_long,  int[] sql_ints, Object sql_obj, OrmLiteDemoTowBean sqlLiteDemoTowBean) {
        this.sql_flast = sql_flast;
        this.sql_double = sql_double;
        this.sql_string = sql_string;
        this.sql_boolean = sql_boolean;
        this.sql_integer = sql_integer;
        this.sql_list = sql_list;
        this.sql_long = sql_long;
        this.sql_ints = sql_ints;
        this.sql_obj = sql_obj;
        this.sqlLiteDemoTowBean = sqlLiteDemoTowBean;
    }

    protected OrmLiteDemoBean(Parcel in) {
        sql_id = in.readInt();
        sql_flast = in.readFloat();
        sql_double = in.readDouble();
        sql_string = in.readString();
        sql_boolean = in.readByte() != 0;
        sql_integer = in.readInt();
        sql_long = in.readLong();
        sql_ints = in.createIntArray();
        sqlLiteDemoTowBean = in.readParcelable(OrmLiteDemoTowBean.class.getClassLoader());
    }

    public static final Creator<OrmLiteDemoBean> CREATOR = new Creator<OrmLiteDemoBean>() {
        @Override
        public OrmLiteDemoBean createFromParcel(Parcel in) {
            return new OrmLiteDemoBean(in);
        }

        @Override
        public OrmLiteDemoBean[] newArray(int size) {
            return new OrmLiteDemoBean[size];
        }
    };

    //get,set方法
    public int getSql_id() {
        return sql_id;
    }

    public void setSql_id(int sql_id) {
        this.sql_id = sql_id;
    }

    public float getSql_flast() {
        return sql_flast;
    }

    public void setSql_flast(float sql_flast) {
        this.sql_flast = sql_flast;
    }

    public double getSql_double() {
        return sql_double;
    }

    public void setSql_double(double sql_double) {
        this.sql_double = sql_double;
    }

    public String getSql_string() {
        return sql_string;
    }

    public void setSql_string(String sql_string) {
        this.sql_string = sql_string;
    }

    public boolean isSql_boolean() {
        return sql_boolean;
    }

    public void setSql_boolean(boolean sql_boolean) {
        this.sql_boolean = sql_boolean;
    }

    public int getSql_integer() {
        return sql_integer;
    }

    public void setSql_integer(int sql_integer) {
        this.sql_integer = sql_integer;
    }

    public List<Integer> getSql_list() {
        return sql_list;
    }

    public void setSql_list(List<Integer> sql_list) {
        this.sql_list = sql_list;
    }

    public long getSql_long() {
        return sql_long;
    }

    public void setSql_long(long sql_long) {
        this.sql_long = sql_long;
    }


    public int[] getSql_ints() {
        return sql_ints;
    }

    public void setSql_ints(int[] sql_ints) {
        this.sql_ints = sql_ints;
    }

    public Object getSql_obj() {
        return sql_obj;
    }

    public void setSql_obj(Object sql_obj) {
        this.sql_obj = sql_obj;
    }

    public OrmLiteDemoTowBean getSqlLiteDemoTowBean() {
        return sqlLiteDemoTowBean;
    }

    public void setSqlLiteDemoTowBean(OrmLiteDemoTowBean sqlLiteDemoTowBean) {
        this.sqlLiteDemoTowBean = sqlLiteDemoTowBean;
    }

    //序列化
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sql_id);
        dest.writeFloat(sql_flast);
        dest.writeDouble(sql_double);
        dest.writeString(sql_string);
        dest.writeByte((byte) (sql_boolean ? 1 : 0));
        dest.writeInt(sql_integer);
        dest.writeLong(sql_long);
        dest.writeIntArray(sql_ints);
    }
}

package com.yehui.utils.bean.ormlite;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Collection;


/**
 * Created by yehuijifeng
 * on 2016/1/5.
 * 测试数据库表实体类
 */
@DatabaseTable(tableName = "tab_tow_demo")
public class OrmLiteDemoTowBean extends BaseDaoEnabled<OrmLiteDemoTowBean, Integer> implements Parcelable {

    /**
     * 还有中更简洁的方法，将该bean直接继承BaseDaoEnabled<OrmLiteDemoTowBean, Integer>
     * OrmLiteDemoTowBean extends BaseDaoEnabled<OrmLiteDemoTowBean, Integer>
     Dao dao = DatabaseHelper.getHelper(getContext()).getDao(OrmLiteDemoTowBean.class);
     OrmLiteDemoTowBean ormLiteDemoTowBean = new OrmLiteDemoTowBean();
     ormLiteDemoTowBean.setDao(dao);
     ormLiteDemoTowBean.setTest_name("夜辉疾风");
     student.create();
     前提dao需要手动设置，如果dao为null会报错，尼玛，我觉得一点用没有。。。
     */
    public OrmLiteDemoTowBean() {
    }

    @DatabaseField(generatedId = true)
    private int test_id;

    @DatabaseField(columnName = "test_name")
    private String test_name;

    /**
     * 集合的正确赋值方式
     */
    @ForeignCollectionField
    private Collection<OrmLiteDemoThreeBean> ormLiteDemoThreeBeanList;


    public OrmLiteDemoTowBean(int test_id, String test_name) {
        this.test_id = test_id;
        this.test_name = test_name;
    }

    public OrmLiteDemoTowBean(String test_name) {
        this.test_name = test_name;
    }

    protected OrmLiteDemoTowBean(Parcel in) {
        test_id = in.readInt();
        test_name = in.readString();
    }

    public static final Creator<OrmLiteDemoTowBean> CREATOR = new Creator<OrmLiteDemoTowBean>() {
        @Override
        public OrmLiteDemoTowBean createFromParcel(Parcel in) {
            return new OrmLiteDemoTowBean(in);
        }

        @Override
        public OrmLiteDemoTowBean[] newArray(int size) {
            return new OrmLiteDemoTowBean[size];
        }
    };

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(test_id);
        dest.writeString(test_name);
    }
}

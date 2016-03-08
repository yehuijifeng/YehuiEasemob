package com.yehui.utils.bean.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by yehuijifeng
 * on 2016/1/6.
 * 测试外键实体类,three表中包含tow
 */
@DatabaseTable(tableName = "tab_three_demo")
public class OrmLiteDemoThreeBean {

    /**
     * 反射实例解释：
     * canBeNull  表示不能为null；
     * foreign=true 表示是一个外键;
     * columnName  列名；
     */
    public OrmLiteDemoThreeBean() {
    }

    @DatabaseField(generatedId = true)
    private int test_id;

    @DatabaseField(columnName = "test_float")
    private float test_float;

    /**
     * 定义一个User成员变量，表示本Article属于该User;
     * 并且，test_id为主表的主键
     */
    @DatabaseField(canBeNull = true, foreign = true, columnName = "test_id")
    private OrmLiteDemoTowBean ormLiteDemoTowBean;


    public OrmLiteDemoThreeBean(float test_float) {
        this.test_float = test_float;
    }

    public OrmLiteDemoThreeBean(float test_float, OrmLiteDemoTowBean ormLiteDemoTowBean) {
        this.test_float = test_float;
        this.ormLiteDemoTowBean = ormLiteDemoTowBean;
    }

    public int getTest_id() {
        return test_id;
    }

    public void setTest_id(int test_id) {
        this.test_id = test_id;
    }

    public float getTest_float() {
        return test_float;
    }

    public void setTest_float(float test_float) {
        this.test_float = test_float;
    }

    public OrmLiteDemoTowBean getOrmLiteDemoTowBean() {
        return ormLiteDemoTowBean;
    }

    public void setOrmLiteDemoTowBean(OrmLiteDemoTowBean ormLiteDemoTowBean) {
        this.ormLiteDemoTowBean = ormLiteDemoTowBean;
    }

    @Override
    public String toString() {
        return "OrmLiteDemoThreeBean{" +
                "test_id=" + test_id +
                ", test_float=" + test_float +
                ", ormLiteDemoTowBean=" + ormLiteDemoTowBean +
                '}';
    }
}

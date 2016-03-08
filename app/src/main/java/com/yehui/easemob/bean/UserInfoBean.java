package com.yehui.easemob.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Luhao
 * on 2016/2/23.
 * 用户信息表
 */
@DatabaseTable(tableName = "userinfo_table")
public class UserInfoBean extends BaseDaoEnabled<UserInfoBean, Integer> implements Parcelable {

    /**
     * 反射实例解释：
     * @DatabaseTable(tableName = "tab_demo")标明这是数据库中的一张表,表名为：tab_demo
     * @DatabaseField(generatedId = true) ，generatedId 表示id为主键且自动生成
     * @DatabaseField(columnName = "name")columnName的值为该字段在数据中的列名
     * /**
     * 反射实例解释：
     * @DatabaseField(canBeNull = true ),canBeNull  表示不能为null；
     *  @DatabaseField(generatedId = true) ,foreign=true 表示是一个外键;
     * columnName  列名；
     */

    /**外键的写法
     * /**
     * 定义一个User成员变量，表示本Article属于该User;
     * 并且，test_id为主表的主键

    @DatabaseField(canBeNull = true, foreign = true, columnName = "test_id")
    private OrmLiteDemoTowBean ormLiteDemoTowBean;
    **/

    /**
     * 集合的正确赋值方式
    @ForeignCollectionField
    private Collection<OrmLiteDemoThreeBean> ormLiteDemoThreeBeanList;
    **/
    @DatabaseField(columnName = "user_id", generatedId = true)
    private int userId;//用户id
    @DatabaseField(columnName = "user_name")
    private String userName;//用户名
    @DatabaseField(columnName = "user_pwd")
    private String userPwd;//用户密码
    @DatabaseField(columnName = "user_nickname")
    private String userNickname;//用户昵称
    @DatabaseField(columnName = "user_icon_path")
    private String userIconPath;//用户头像路径
    @DatabaseField(columnName = "user_introduce")
    private String userIntroduce;//用户个人说明
    @DatabaseField(columnName = "pwd_email")
    private String pwdEmail;//密保邮箱
    @DatabaseField(columnName = "pwd_issue")
    private String pwdIssue;//密保问题
    @DatabaseField(columnName = "pwd_answer")
    private String pwdAnswer;//密保答案

    protected UserInfoBean(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        userPwd = in.readString();
        userNickname = in.readString();
        userIconPath = in.readString();
        userIntroduce = in.readString();
        pwdEmail = in.readString();
        pwdIssue = in.readString();
        pwdAnswer = in.readString();
    }

    public static final Creator<UserInfoBean> CREATOR = new Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel in) {
            return new UserInfoBean(in);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserIconPath() {
        return userIconPath;
    }

    public void setUserIconPath(String userIconPath) {
        this.userIconPath = userIconPath;
    }

    public String getUserIntroduce() {
        return userIntroduce;
    }

    public void setUserIntroduce(String userIntroduce) {
        this.userIntroduce = userIntroduce;
    }

    public String getPwdEmail() {
        return pwdEmail;
    }

    public void setPwdEmail(String pwdEmail) {
        this.pwdEmail = pwdEmail;
    }

    public String getPwdIssue() {
        return pwdIssue;
    }

    public void setPwdIssue(String pwdIssue) {
        this.pwdIssue = pwdIssue;
    }

    public String getPwdAnswer() {
        return pwdAnswer;
    }

    public void setPwdAnswer(String pwdAnswer) {
        this.pwdAnswer = pwdAnswer;
    }

    public UserInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(userPwd);
        dest.writeString(userNickname);
        dest.writeString(userIconPath);
        dest.writeString(userIntroduce);
        dest.writeString(pwdEmail);
        dest.writeString(pwdIssue);
        dest.writeString(pwdAnswer);
    }
}

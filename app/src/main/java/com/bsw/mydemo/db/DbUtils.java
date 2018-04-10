package com.bsw.mydemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.bsw.mydemo.bean.UserAccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leiming on 2017/9/11.
 */

public class DbUtils {
    private final String TAG = "testDB";
    public static final String ACCOUNT_TABLE = "account_table";
    public static final String USER_ACCOUNT = "user_account";
    public static final String USER_PASSWORD = "user_password";


    private String dbName = "hzy.db";// 数据库名
    private int version = 1;// 版本号
    private DBManager mDBManager;
    private ArrayList<UserAccountBean> userAccountBeans = new ArrayList<>();

    public DbUtils(Context context) {
        mDBManager = new DBManager(context);
        String studentSql = mDBManager.addPrimaryKey().addText(USER_ACCOUNT).addText(USER_PASSWORD).getSql();
        String[] tables = {ACCOUNT_TABLE};
        String[] slqs = {studentSql};
        mDBManager.create(dbName, version, tables, slqs);
    }

    public static DbUtils init(Context context) {
        return new DbUtils(context);
    }

    /**
     * 增加新数据
     *
     * @param contentValues 数据源
     * @param tableName     表名
     * @param account       查重
     */
    public void addData(String tableName, ContentValues contentValues, String account) {
        if (mDBManager.hasThisData(tableName, USER_ACCOUNT, account)) {
            mDBManager.mUpdate(tableName, contentValues, USER_ACCOUNT + " = ?", new String[] {account});
        } else {
            mDBManager.mInsert(tableName, "", contentValues);
        }
        Log.i(TAG, "delData:增加了一条数据 ");
    }

    /**
     * 删除一条数据
     *
     * @param account   用户账号
     * @param tableName 表名
     */
    public void delData(String tableName, String account) {
        mDBManager.mDelete(tableName, DbUtils.USER_ACCOUNT + "=?", new String[] {account});
        Log.i(TAG, "delData:删除了一条数据 ");
    }

    /**
     * 清空表中的内容
     *
     * @param tableName 表名
     */
    public void delTable(String tableName) {
        mDBManager.mDeleteTable(tableName);
    }

    /**
     * 是否是空表
     *
     * @param tableName 表名
     * @return true 是空表
     */
    public boolean isEmptyTable(String tableName) {
        return mDBManager.getDataNum(tableName) <= 0;
    }

    public List<String> getAllHistoryAccount(String tableName) {
        return mDBManager.getAllHistoryAccount(tableName);
    }

    /**
     * 数据库是否存在要查询的这条数据
     *
     * @param columnName 查询的字段
     * @param data       查询的数据
     * @param tableName  表名
     * @return
     */
    public boolean hasThisData(String tableName, String columnName, String data) {
        return mDBManager.hasThisData(tableName, columnName, data);
    }

    /**
     * 本地数据库是否存在这条student_account
     *
     * @param data      查询的数据
     * @param tableName 表名
     * @return true 有这条数据
     */
    public boolean hasThisMenuID(String tableName, String data) {
        return mDBManager.hasThisData(tableName, "student_phone", data);
    }

    /**
     * 本地数据库是否存在这条账户
     *
     * @param data 查询的数据
     * @return studentBean 用户账号
     */
    public UserAccountBean hasAccount(String[] data) {
        return mDBManager.hasThisAccount(ACCOUNT_TABLE, new String[] {USER_ACCOUNT}, data);
    }

    /**
     * 修改一条数据的内容
     *
     * @param values      数据
     * @param whereclause 条件
     * @param tableName   表名
     */
    public void modifyData(String tableName, ContentValues values, String whereclause) {
        mDBManager.mUpdate(tableName, values, whereclause);
        Log.i(TAG, "modifyData: 修改了一条数据");
    }
}

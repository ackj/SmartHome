package com.meilun.security.smart.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.meilun.security.smart.App;
import com.meilun.security.smart.entity.bean.UserBean;

import cn.itsite.abase.log.ALog;

/**
 * Author：leguang on 2016/5/4 0009 15:49
 * Email：langmanleguang@qq.com
 */

public class UserHelper {
    public static final String TAG = UserHelper.class.getSimpleName();
    public static final String DEFAULT = "";
    public static final String TOKEN = "token";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String USER_INFO = "user_info";
    public static final String ISREMEMBER = "isRemember";
    public static String account = "";//账户、密码、是否记住密码，这三个值是记录在默认SP中的。
    public static String password = "";//同时账户和密码在各自的SP中也有一份。
    public static String FILE_NAME = "default";
    public static String token = "";
    public static final String DEVICE_ID = "device_id";
    public static UserBean.DataBean.MemberInfoBean userInfo;

    //判断是否登录。
    public static boolean isLogined() {
        return !TextUtils.isEmpty(token);
    }


    //退出登录或者token失效清除信息。
    public static void clear() {
        token = "";
        account = "";
        password = "";
        userInfo = null;
    }

    //更新Token。
    public static boolean setToken(String token) {
        UserHelper.token = token;
        SharedPreferences.Editor editor = getEditor();
        editor.putString(TOKEN, token);
        return editor.commit();
    }

    //更新用户信息。
    public static boolean setUserInfo(UserBean.DataBean.MemberInfoBean memberInfo) {
        setToken(memberInfo.getToken());
        UserHelper.userInfo = memberInfo;
        SharedPreferences.Editor editor = getEditor();
        String info = new Gson().toJson(memberInfo);
        editor.putString(USER_INFO, info);
        return editor.commit();
    }

    //获取用户信息。
    public static UserBean.DataBean.MemberInfoBean getUserInfo() {
        if (userInfo != null) {
            return userInfo;
        }
        String userInfo = getSp().getString(USER_INFO, "");
        if (TextUtils.isEmpty(userInfo)) {
            return null;
        }
        UserHelper.userInfo = new Gson().fromJson(userInfo, UserBean.DataBean.MemberInfoBean.class);
        return UserHelper.userInfo;
    }

    //更新账号密码，同时更改了SP文件名，作为用户数据的初始化入口。
    public static void setAccount(String account, String password) {
        ALog.e(TAG, "account-->" + account);
        ALog.e(TAG, "password-->" + password);

        //存到默认SP文件中，用于程序入口处获取默认账户。
        getDefaultEditor()
                .putString(ACCOUNT, account)
                .putString(PASSWORD, password)
                .commit();

        FILE_NAME = account;//此处更改SP文件名。
        getEditor()
                .putString(ACCOUNT, account)
                .putString(PASSWORD, password)
                .commit();
        initData();
    }

    public static String getAccount() {
        return getDefaultSp().getString(ACCOUNT, "");
    }

    public static String getPassword() {
        return getDefaultSp().getString(PASSWORD, "");
    }

    //用户数据的初始化入口。
    public static void init() {
        FILE_NAME = getDefaultSp().getString(ACCOUNT, "");
        initData();
    }

    public static String getDeviceID() {
        return getDefaultSp().getString(DEVICE_ID, "");
    }

    private static void initData() {
        SharedPreferences sp = getSp();
        token = sp.getString(TOKEN, "");
        account = sp.getString(ACCOUNT, "");
        password = sp.getString(PASSWORD, "");
        ALog.e(TAG, "account-->" + account);
        ALog.e(TAG, "password-->" + password);
        getUserInfo();
    }

    //更新是否记住密码。
    public static boolean setRemember(boolean isRemember) {
        return getDefaultEditor().putBoolean(ISREMEMBER, isRemember).commit();
    }

    public static boolean isRemember() {
        return getDefaultSp().getBoolean(ISREMEMBER, false);
    }

    private static SharedPreferences getSp() {
        return App.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor() {
        return getSp().edit();
    }

    private static SharedPreferences getDefaultSp() {
        return App.mContext.getSharedPreferences(DEFAULT, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getDefaultEditor() {
        return getDefaultSp().edit();
    }
}

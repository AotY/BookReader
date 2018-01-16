package com.xjtu.bookreader.util;

import android.content.Context;

import com.xjtu.bookreader.http.utils.CheckNetwork;

/**
 * 判断用户是否登录，获取用户名、头像等等。
 * Created by LeonTao on 2018/1/16.
 */

public class UserUtil {


    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_AVATAR = "user_avatar";


    /**
     * 设置用户id
     *
     * @param userId
     */
    public static void setUserId(long userId) {
        SharedPreferencesUtils.putLong(USER_ID, userId);
    }

    /**
     * 设置用户名
     *
     * @param userName
     */
    public static void setUserName(String userName) {
        SharedPreferencesUtils.putString(USER_NAME, userName);
    }

    /**
     * 设置用户头像地址
     *
     * @param userAvatar
     */
    public static void setUserAvatar(String userAvatar) {
        SharedPreferencesUtils.putString(USER_AVATAR, userAvatar);
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    public static long getUserId() {
        return SharedPreferencesUtils.getLong(USER_ID, 0);
    }


    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName() {
        return SharedPreferencesUtils.getString(USER_NAME, "");
    }

    /**
     * 获取用户头像地址
     *
     * @return
     */
    public static String getUserAvatar() {
        return SharedPreferencesUtils.getString(USER_AVATAR, "");
    }

    /**
     * 判断用户是否登录过，是否有保存信息
     */

    public static boolean isLogined() {
        long userId = getUserId();
        String userName = getUserName();

        if (userId == 0 || "".equals(userName)) {
            return false;
        }
        return true;
    }

    /**
     * 判断用户是否在线，（登录成功，并且当前有网络）
     */
    public static boolean isOnline(Context context) {
        long userId = getUserId();
        String userName = getUserName();

        if (userId == 0 || "".equals(userName)) {
            return false;
        }

        boolean isNetworkAvailable = CheckNetwork.isNetworkConnected(context);
        return isNetworkAvailable;
    }


}

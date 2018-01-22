package com.meilun.security.smart.utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.Player.web.response.ResponseCommon;
import com.Player.web.websocket.ClientCore;
import com.meilun.security.smart.App;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.sdk.Utility;

import cn.itsite.abase.log.ALog;

/**
 * Author: LiuJia on 2017/12/19 0019 15:27.
 * Email: liujia95me@126.com
 */

public class EyeCatHelper {

    private static String TAG = EyeCatHelper.class.getSimpleName();

    public interface OnRegisterListener {
        void registerSuccess();

        void registerFaild();
    }

    public interface OnLoginListener {
        void loginSuccess();

        void loginFaild();
    }

    @SuppressLint("HandlerLeak")
    public static void register(String account, OnRegisterListener listener) {
        String nickname = "nickname";
        String phoneNumber = "13800000000";
        String idCard = "idCard";
        int language = 1;
        ClientCore.getInstance().setupHost(App.mContext, Constants.SERVER, 0, Utility.getImsi(App.mContext),
                language, Constants.CUSTOM_NAME, Utility.getVersionName(App.mContext), "");
        ClientCore.getInstance().getCurrentBestServer(App.mContext, new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what != 200) {
                    ALog.e("服务获取失败");
                }
            }
        });
        ClientCore.getInstance().registeredUser("GC" + account, Constants.CATEYE_PASSWORD, Constants.EMAIL, nickname, phoneNumber, idCard, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                ALog.e(TAG, responseCommon);
                ALog.e(TAG, responseCommon.toString());
                ALog.e(TAG, responseCommon.toJsonString());
                ALog.e(TAG, responseCommon.h);
                ALog.e(TAG, responseCommon.h.toJsonString());
                ALog.e(TAG, responseCommon.h.toString());
                ALog.e(TAG, responseCommon.h.e);
                if ((responseCommon != null) && (responseCommon.h != null) && (responseCommon.h.e == 200)) {
                    listener.registerSuccess();
                } else {
                    listener.registerFaild();
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public static void login(String account,OnLoginListener listener) {
        int language = 1;
        ClientCore.getInstance().setupHost(App.mContext, Constants.SERVER, 0, Utility.getImsi(App.mContext),
                language, Constants.CUSTOM_NAME, Utility.getVersionName(App.mContext), "");
        ClientCore.getInstance().getCurrentBestServer(App.mContext, new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what != 200) {
                    ALog.e("服务获取失败");
                }
            }
        });
        ClientCore.getInstance().loginServerAtUserId(App.mContext, "GC" + account, Constants.CATEYE_PASSWORD, new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                ALog.e(TAG, "msg:" + msg);
                ResponseCommon responseCommon = (ResponseCommon) msg.obj;
                if ((responseCommon != null) && (responseCommon.h != null)) {
                    ALog.e("responseCommon.h.e-->" + responseCommon.h.e);
                    if (responseCommon.h.e == 200) {
                        listener.loginSuccess();
                    } else {
                        listener.loginFaild();
                    }
                } else {
                    //走这里说明还没注册过猫眼，但是登录亿社区会员中心却成功了，所以要将用户再注册一次猫眼的会员系统
                    register("GC" + account, new OnRegisterListener() {
                        @Override
                        public void registerSuccess() {
                            //注册成功后仍需走一遍猫眼的登录
                            login("GC" + account, new OnLoginListener() {
                                @Override
                                public void loginSuccess() {
                                    listener.loginSuccess();
                                }

                                @Override
                                public void loginFaild() {
                                    listener.loginFaild();
                                }
                            });
                        }

                        @Override
                        public void registerFaild() {
                            listener.loginFaild();
                        }
                    });
                }
            }
        });
    }

}

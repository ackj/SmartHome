package com.meilun.security.smart;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.Player.Core.PlayerClient;
import com.Player.web.response.DevItemInfo;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.meilun.security.smart.common.BoxingGlideLoader;
import com.meilun.security.smart.common.UserHelper;
import com.meilun.security.smart.common.sdk.WriteLogThread;
import com.p2p.core.P2PSpecial.P2PSpecial;

import java.util.Collections;
import java.util.List;

import cn.itsite.abase.BaseApp;
import cn.itsite.abase.log.ALog;
import cn.itsite.apush.PushHelper;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */
public class App extends BaseApp implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = App.class.getSimpleName();
    //    public final static String APPID="1e9a2c3ead108413e8218a639c540e44";
    public final static String APPID = "fe17b3e35717f4006a5d10a9106eb232";
    //    public final static String APPToken="7db7b2bff80a025a3dad546a4d5a6c3ee545568d4e0ce9609c0585c71c287d08";
    public final static String APPToken = "386294f10b00850ea94450cc335cbaefad3d615b68a73260e921b9c34bf39f3b";
    //前两位是客户APP唯一编号(00.00 由技威分配),后两位是APP版本号(客户自定义),此参数不可省略
    public final static String APPVersion = "05.72.00.00";
    public static String deviceID;
    public static PlayerClient mPlayerClient;

    public static List<DevItemInfo> devices = Collections.emptyList();

    @Override
    public void onCreate() {
        super.onCreate();
        initDate();
        initCatEye();
        initPush();//初始化推送。
        initBoxing();
        initP2P(this);
    }

    private void initDate() {
        UserHelper.init();
    }

    private void initCatEye() {
        mPlayerClient = new PlayerClient();
        WriteLogThread writeLogThread = new WriteLogThread(mPlayerClient);
        writeLogThread.start();
    }

    private void initBoxing() {
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);
    }

    private void initP2P(App app) {
        P2PSpecial.getInstance().init(app, APPID, APPToken, APPVersion);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ALog.e("onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        ALog.e("onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        ALog.e("onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        ALog.e("onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        ALog.e("onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        ALog.e("onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ALog.e("onActivityDestroyed");
    }

    /**
     * 初始化推送。
     */
    public void initPush() {
        PushHelper.init(this);
    }
}

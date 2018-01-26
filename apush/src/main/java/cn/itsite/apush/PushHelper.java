package cn.itsite.apush;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.PendingResult;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xiaomi.mipush.sdk.Logger;
import com.xiaomi.mipush.sdk.MiPushClient;

import cn.itsite.abase.BaseApp;
import cn.itsite.abase.cache.SPCache;
import cn.itsite.abase.log.ALog;

/**
 * Author：leguang on 2017/5/5 0009 10:49
 * Email：langmanleguang@qq.com
 * <p>
 * 推送管理类
 */

public class PushHelper {
    public static final String TAG = PushHelper.class.getSimpleName();
    public static final String XIAOMI = "xiaomi";
    public static final String HUAWEI = "huawei";
    public static final String OPPO = "oppo";
    public static final String VIVO = "vivo";
    public static final String ALIYUN = "";
    public static final String PREFIX = "and_";
    public static final String DEVICE_ID = "device_id";

    private static final String XIAOMI_APP_ID = "2882303761517678331";
    private static final String XIAOMI_APP_KEY = "5281767876331";
    private static HuaweiApiClient client;

    public static void init(Context mContext) {
        switch (Build.MANUFACTURER.toLowerCase()) {
            case XIAOMI:
                PushHelper.initXiaoMiPush(mContext);
                break;
            case HUAWEI:
                PushHelper.initHuaWeiPush(mContext);
                break;
            case OPPO:
            case VIVO:
            default:
                PushHelper.initAliPush(mContext);
                break;
        }
    }

    public static void initAliPush(final Context mContext) {
        ALog.e("initAliPush");
        BaseApp.PUSH_TYPE = ALIYUN;

        PushServiceFactory.init(mContext);
        final CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(mContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                ALog.e(TAG, "init cloudchannel success-->" + response);
                String deviceID = PushHelper.PREFIX + pushService.getDeviceId();
                SPCache.put(mContext, PushHelper.DEVICE_ID, deviceID);
                ALog.e(TAG, "deviceID-->" + deviceID);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                ALog.e(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
//        MiPushRegister.register(mContext, "2882303761517633954", "5551763357954");

        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
//        HuaWeiRegister.register(mContext);
    }

    public static void initXiaoMiPush(Context mContext) {
        BaseApp.PUSH_TYPE = XIAOMI;

        MiPushClient.registerPush(mContext, XIAOMI_APP_ID, XIAOMI_APP_KEY);
        LoggerInterface newLogger = new LoggerInterface() {
            @Override
            public void setTag(String tag) {
            }

            @Override
            public void log(String content, Throwable t) {
                Log.e(TAG, content, t);
            }

            @Override
            public void log(String content) {
                Log.e(TAG, content);
            }
        };
        Logger.setLogger(mContext, newLogger);
        Logger.disablePushFileLog(mContext);
    }

    public static void initHuaWeiPush(final Context mContext) {
        BaseApp.PUSH_TYPE = HUAWEI;

        //创建华为移动服务client实例用以使用华为push服务
        //需要指定api为HuaweiPush.PUSH_API
        //连接回调以及连接失败监听
        client = new HuaweiApiClient.Builder(mContext)
                .addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {
                        //华为移动服务client连接成功，在这边处理业务自己的事件
                        ALog.e("HuaweiApiClient 连接成功");
//                        ToastUtils.showToast(BaseApp.mContext, "HuaweiApiClient 连接成功");

                        getTokenAsyn(client);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        ALog.e("onConnectionSuspended");
//                        ToastUtils.showToast(BaseApp.mContext, "连接终止-->" + i);
                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        ALog.e("onConnectionFailed-->" + connectionResult.getErrorCode());
//                        ToastUtils.showToast(BaseApp.mContext, "连接失败-->" + connectionResult.getErrorCode());
                        PushHelper.initAliPush(mContext);
                    }
                }).build();
        //建议在oncreate的时候连接华为移动服务
        //业务可以根据自己业务的形态来确定client的连接和断开的时机，但是确保connect和disconnect必须成对出现
        client.connect();
    }

    /**
     * 使用异步接口来获取pushtoken
     * 结果通过广播的方式发送给应用，不通过标准接口的pendingResul返回
     * CP可以自行处理获取到token
     * 同步获取token和异步获取token的方法CP只要根据自身需要选取一种方式即可
     */
    private static void getTokenAsyn(HuaweiApiClient client) {
        if (!client.isConnected()) {
            Log.i(TAG, "获取token失败，原因：HuaweiApiClient未连接");
            client.connect();
            return;
        }

        Log.i(TAG, "异步接口获取push token");
        PendingResult<TokenResult> tokenResult = HuaweiPush.HuaweiPushApi.getToken(client);
        tokenResult.setResultCallback(new ResultCallback<TokenResult>() {

            @Override
            public void onResult(TokenResult result) {
                //这边的结果只表明接口调用成功，是否能收到响应结果只在广播中接收
            }
        });
    }
}

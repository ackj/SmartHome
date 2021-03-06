package com.meilun.security.smart.net.model;

import com.meilun.security.smart.net.contract.NetContract;
import com.tsvclient.ipc.WifiIpc;

import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.model.base.BaseModel;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */

public class NetModel extends BaseModel implements NetContract.Model {
    public static final String TAG = NetModel.class.getSimpleName();
    public static final String IP = "10.10.10.250";
    public static final int PORT = 12368;

    @Override
    public Observable command(int cmd, String params) {
        ALog.e("cmd-->" + cmd);
        ALog.e("params-->" + params);
        return Observable.create((Observable.OnSubscribe<String>) s -> {
            try {
                String result = WifiIpc.TSV_C_SendXmlCommand(IP, PORT, cmd, cmd, params);
                s.onNext(result);
            } catch (Exception e) {
                s.onError(e);
            }
            s.onCompleted();
        }).subscribeOn(Schedulers.io());
    }
}
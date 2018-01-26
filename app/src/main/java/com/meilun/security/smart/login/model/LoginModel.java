package com.meilun.security.smart.login.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.common.UserHelper;
import com.meilun.security.smart.entity.bean.UserBean;
import com.meilun.security.smart.login.contract.LoginContract;

import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */

public class LoginModel extends BaseModel implements LoginContract.Model {
    private static final String TAG = LoginModel.class.getSimpleName();

    @Override
    public Observable<UserBean> requestLogin(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestLogin(ApiService.requestLogin,
                        params.sc,
                        params.user,
                        params.pwd)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void registerPush() {
        HttpHelper.getService(ApiService.class)
                .registerDevice(ApiService.registerDevice, UserHelper.token, UserHelper.getDeviceID(), UserHelper.account, "userType")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseBean -> ALog.e(TAG, baseBean.getOther().getMessage()));
    }
}
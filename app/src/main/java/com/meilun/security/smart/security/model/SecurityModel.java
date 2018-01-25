package com.meilun.security.smart.security.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.security.contract.SecurityContract;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.SecurityBean;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Author: LiuJia on 2017/7/4 0004 09:19.
 * Email: liujia95me@126.com
 */

public class SecurityModel extends BaseModel implements SecurityContract.Model {
    public static final String TAG = SecurityModel.class.getSimpleName();

    @Override
    public void start(Object request) {

    }

    @Override
    public Observable<SecurityBean> requestSecurity(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestSecurity(ApiService.requestSecurity,
                        params.token)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<MainDeviceListBean> requestGateways(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestMainDeviceList(ApiService.requestMainDeviceList,
                        params.token,
                        Constants.SMART_GATEWAY + "," + Constants.SMART_GATEWAY_GSW3,
                        params.page,
                        params.pageSize)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseBean> requestSwichGateway(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestSwichGateway(ApiService.requestSwichGateway,
                        params.token,
                        params.gateway)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseBean> requestSwichState(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestSwichState(ApiService.requestSwichState,
                        params.token,
                        params.dstatus)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseBean> requestLeaveMassge(Params params) {
        // 创建 RequestBody，用于封装构建RequestBody
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("audio/amr"), params.file);
        builder.addFormDataPart("file", params.file.getName(), requestBody);
        builder.addFormDataPart("token", Params.token);

        return HttpHelper.getService(ApiService.class)
                .requestLeaveMassge(ApiService.requestLeaveMassge,
                        builder.build())
                .subscribeOn(Schedulers.io());
    }
}

package com.meilun.security.smart.host.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.host.contract.HostListContract;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import rx.Observable;
import rx.schedulers.Schedulers;

public class HostListModel extends BaseModel implements HostListContract.Model {
    @Override
    public void start(Object request) {

    }

    public Observable<MainDeviceListBean> requestGateways(Params params) {
//        return HttpHelper.getService(ApiService.class)
//                .requestGateways(ApiService.requestGateways,
//                        params.token,
//                        params.page,
//                        params.pageSize)
//                .subscribeOn(Schedulers.io());
        return HttpHelper.getService(ApiService.class)
                .requestMainDeviceList(ApiService.requestMainDeviceList,
                        params.token,
                        Constants.SMART_GATEWAY + "," + Constants.SMART_GATEWAY_GSW3,
                        params.page,
                        params.pageSize)
                .subscribeOn(Schedulers.io());
    }

}
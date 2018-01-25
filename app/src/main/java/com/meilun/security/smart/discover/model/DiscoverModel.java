package com.meilun.security.smart.discover.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.discover.contract.DiscoverContract;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceLogBean;
import com.meilun.security.smart.entity.bean.DiscoverBean;
import com.meilun.security.smart.entity.bean.FirstLevelBean;
import com.meilun.security.smart.entity.bean.SubCategoryBean;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Author: LiuJia on 2017/11/23 0023 10:45.
 * Email: liujia95me@126.com
 */

public class DiscoverModel extends BaseModel implements DiscoverContract.Model {
    @Override
    public Observable<FirstLevelBean> requestFirstLevel(Params params) {
        return HttpHelper.getService(ApiService.class).requestFirstLevel(ApiService.requestFirstLevel,params.keywords,Constants.payFrom)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SubCategoryBean> requestSubCategoryList(Params params) {
        return HttpHelper.getService(ApiService.class).requestSubCategoryLevel(
                ApiService.requestSubCategoryLevel,params.token,params.appType,params.id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<DeviceLogBean> requestDeviceLogs(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestDeviceLogs(ApiService.requestDeviceLogs,params.token,params.page,params.pageSize)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<DiscoverBean> requestDiscoverPage(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestDiscoverPage(ApiService.requestDiscoverPage,
                        params.token,
                        Constants.OPERATOR_CODE)
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
}

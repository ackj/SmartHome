package com.meilun.security.smart.smarthome.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.smarthome.contract.GoodsCategoryContract;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.FirstLevelBean;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Author: LiuJia on 2017/5/22 0022 10:22.
 * Email: liujia95me@126.com
 */

public class GoodsCategoryModel extends BaseModel implements GoodsCategoryContract.Model {

    @Override
    public void start(Object request) {

    }

    @Override
    public Observable<FirstLevelBean> requestFirstLevel(Params params) {
        return HttpHelper.getService(ApiService.class).requestFirstLevel(ApiService.requestFirstLevel,params.keywords, Constants.fromPoint)
                .subscribeOn(Schedulers.io());
    }
}

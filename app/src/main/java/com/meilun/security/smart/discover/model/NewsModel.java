package com.meilun.security.smart.discover.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.discover.contract.NewsContract;
import com.meilun.security.smart.entity.bean.NewsBean;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import rx.Observable;
import rx.schedulers.Schedulers;

public class NewsModel extends BaseModel implements NewsContract.Model {

    @Override
    public Observable<NewsBean> requestNewsList(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestNewsList(ApiService.requestNewsList,
                        params.token,
                        Constants.OPERATOR_CODE,
                        params.page,
                        params.pageSize)
                .subscribeOn(Schedulers.io());
    }
}
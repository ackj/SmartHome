package com.meilun.security.smart.security.model;

import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.SubDeviceDetBean;
import com.meilun.security.smart.security.contract.DetectorPropertyContract;

import cn.itsite.abase.mvp.model.base.BaseModel;
import cn.itsite.abase.network.http.HttpHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Author: LiuJia on 2017/7/4 0004 15:04.
 * Email: liujia95me@126.com
 */

public class DetectorPropertyModel extends BaseModel implements DetectorPropertyContract.Model {

    @Override
    public Observable<BaseBean> requestNotifProperty(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestModsensor(ApiService.requestModsensor,
                        params.token,
                        params.file,
                        params.index,
                        params.name,
                        params.defenseLevel,
                        params.alarmDelay)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseBean> requestDelsensor(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestDelsensor(ApiService.requestDelsensor,
                        params.token,
                        params.index)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<SubDeviceDetBean> requestSubDeviceDet(Params params) {
        return HttpHelper.getService(ApiService.class)
                .requestSubDeviceDet(ApiService.requestSubDeviceDet,
                        params.token,
                        params.category,
                        params.index)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<BaseBean> requestModsensor(Params params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params.file != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), params.file);
            builder.addFormDataPart("file", params.file.getName(), requestBody);
        }

        builder.addFormDataPart("token", params.token);
        builder.addFormDataPart("index", params.index + "");
        builder.addFormDataPart("name", params.name);
        builder.addFormDataPart("fc", Constants.FC);
        builder.addFormDataPart("defenseLevel", params.defenseLevel);
        builder.addFormDataPart("alarmDelay", params.alarmDelay + "");

        return HttpHelper.getService(ApiService.class)
                .requestModsensor(ApiService.requestModsensor,
                        builder.build())
                .subscribeOn(Schedulers.io());
    }


}

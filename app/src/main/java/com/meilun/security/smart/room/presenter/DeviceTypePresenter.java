package com.meilun.security.smart.room.presenter;

import android.support.annotation.NonNull;

import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.room.contract.DeviceTypeContract;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.room.model.DeviceTypeModel;

import cn.itsite.abase.mvp.presenter.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Author: LiuJia on 2017/8/30 0030 20:07.
 * Email: liujia95me@126.com
 */

public class DeviceTypePresenter extends BasePresenter<DeviceTypeContract.View,DeviceTypeContract.Model> implements DeviceTypeContract.Presenter {

    /**
     * 创建Presenter的时候就绑定View和创建model。
     *
     * @param mView 所要绑定的view层对象，一般在View层创建Presenter的时候通过this把自己传过来。
     */
    public DeviceTypePresenter(DeviceTypeContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected DeviceTypeContract.Model createModel() {
        return new DeviceTypeModel();
    }

    @Override
    public void requestDeviceType(Params params) {
        mRxManager.add(mModel.requestDeviceType(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseDeviceType(bean.getData().getDeviceTypeList());
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error));
    }

    @Override
    public void requestAddDevice(Params params) {
        mRxManager.add(mModel.requestAddDevice(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseAddDevice(bean);
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error));
    }

    @Override
    public void requestAddCamera(Params params) {
        mRxManager.add(mModel.requestAddCamera(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseAddDevice(bean);
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error));
    }
}

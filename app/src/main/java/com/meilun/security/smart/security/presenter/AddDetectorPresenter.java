package com.meilun.security.smart.security.presenter;

import android.support.annotation.NonNull;

import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.security.contract.AddDetectorContract;
import com.meilun.security.smart.security.model.AddDetectorModel;
import com.meilun.security.smart.common.Params;

import cn.itsite.abase.mvp.presenter.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;

public class AddDetectorPresenter extends BasePresenter<AddDetectorContract.View, AddDetectorContract.Model>
        implements AddDetectorContract.Presenter {
    private final String TAG = AddDetectorPresenter.class.getSimpleName();

    public AddDetectorPresenter(AddDetectorContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected AddDetectorContract.Model createModel() {
        return new AddDetectorModel();
    }

    @Override
    public void requestDetectorList(Params params) {
        mRxManager.add(mModel.requestDetectorList(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseDetectorList(bean.getData().getDeviceTypeList());
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error));
    }

    @Override
    public void requestAddDetector(Params params) {
        mRxManager.add(mModel.requestAddDetector(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseAddDetector(bean);
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error/*, this::complete, disposable -> start(null)*/)
        );
    }

    @Override
    public void reqeuestCancellationOfSensorLearning(Params params) {
        mRxManager.add(mModel.reqeuestCancellationOfSensorLearning(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bean -> {
                    if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseCancellationOfSensorLearning(bean);
                    } else {
                        getView().error(bean.getOther().getMessage());
                    }
                }, this::error/*, this::complete, disposable -> start(null)*/)
        );
    }
}
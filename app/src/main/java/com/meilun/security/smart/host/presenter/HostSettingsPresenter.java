package com.meilun.security.smart.host.presenter;

import android.support.annotation.NonNull;

import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.HostSettingsBean;
import com.meilun.security.smart.host.contract.HostSettingsContract;
import com.meilun.security.smart.host.model.HostSettingsModel;

import cn.itsite.abase.mvp.presenter.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Author：leguang on 2016/10/9 0009 10:35
 * Email：langmanleguang@qq.com
 * <p>
 * 负责添加主机模块Presenter层内容。
 */

public class HostSettingsPresenter extends BasePresenter<HostSettingsContract.View, HostSettingsContract.Model> implements HostSettingsContract.Presenter {
    private final String TAG = HostSettingsPresenter.class.getSimpleName();

    public HostSettingsPresenter(HostSettingsContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected HostSettingsContract.Model createModel() {
        return new HostSettingsModel();
    }

    @Override
    public void requestSetHost(Params params) {
        mRxManager.add(mModel.requestSetHost(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<BaseBean>() {
                    @Override
                    public void _onNext(BaseBean baseBean) {
                        if (baseBean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                            getView().responseSetHost(baseBean);
                        } else {
                            getView().error(baseBean.getOther().getMessage());
                        }
                    }
                }));
    }

    @Override
    public void requestHostSettings(Params params) {
        mRxManager.add(mModel.requestHostSettings(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<HostSettingsBean>() {
                    @Override
                    public void _onNext(HostSettingsBean baseBean) {
                        if (baseBean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                            getView().responseHostSettings(baseBean);
                        } else {
                            getView().error(baseBean.getOther().getMessage());
                        }
                    }
                }));
    }
}
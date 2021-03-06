package com.meilun.security.smart.more.presenter;

import android.support.annotation.NonNull;

import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.more.contract.MoreContract;
import com.meilun.security.smart.more.model.MoreModel;

import cn.itsite.abase.mvp.presenter.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Author：leguang on 2016/10/9 0009 10:35
 * Email：langmanleguang@qq.com
 * <p>
 * 负责邻里模块Presenter层内容。
 */

public class MorePresenter extends BasePresenter<MoreContract.View, MoreContract.Model> implements MoreContract.Presenter {
    private final String TAG = MorePresenter.class.getSimpleName();

    public MorePresenter(MoreContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected MoreContract.Model createModel() {
        return new MoreModel();
    }

    @Override
    public void start(Object request) {
    }

    @Override
    public void requestLogout(Params params) {
        mRxManager.add(mModel.requestLogout(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(baseBean -> {
                    if (baseBean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                        getView().responseLogout(baseBean.getOther().getMessage());
                    } else {
                        getView().error(baseBean.getOther().getMessage());
                    }
                }, this::error)
        );
    }
}
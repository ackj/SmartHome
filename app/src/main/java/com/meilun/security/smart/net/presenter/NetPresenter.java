package com.meilun.security.smart.net.presenter;

import android.support.annotation.NonNull;

import com.meilun.security.smart.net.contract.NetContract;
import com.meilun.security.smart.net.model.NetModel;

import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.presenter.base.BasePresenter;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */

public class NetPresenter extends BasePresenter<NetContract.View, NetContract.Model> implements NetContract.Presenter {
    private static final String TAG = NetPresenter.class.getSimpleName();

    public NetPresenter(NetContract.View mView) {
        super(mView);
    }

    @NonNull
    @Override
    protected NetContract.Model createModel() {
        return new NetModel();
    }

    @Override
    public void command(int cmd, String params) {
        mModel.command(cmd, params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxSubscriber<String>() {
                    @Override
                    public void _onNext(String s) {
                        ALog.e("指令返回-->" + s);
                        getView().command(cmd, s);
                    }
                });
    }
}
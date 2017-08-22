package com.aglhz.s1.security.contract;

import com.aglhz.s1.entity.bean.BaseBean;
import com.aglhz.s1.common.Params;
import com.aglhz.s1.entity.bean.SubDeviceDetBean;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import io.reactivex.Observable;

/**
 * Author: LiuJia on 2017/7/4 0004 15:04.
 * Email: liujia95me@126.com
 */

public interface DetectorPropertyContract {

    interface View extends BaseContract.View {
        void responseDetectorProperty(BaseBean baseBean);
        void responseNodifSuccess(BaseBean baseBean);
        void responseDelSuccess(BaseBean baseBean);
        void responseSubDeviceDet(SubDeviceDetBean bean);
        void responseModsensor(BaseBean bean);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestDetectorProperty(Params params);
        void requestNotifProperty(Params params);
        void requestDelsensor(Params params);
        void requestSubDeviceDet(Params params);
        void requestModsensor(Params params);

    }

    interface Model extends BaseContract.Model {
        Observable<BaseBean> requestDetectorProperty(Params params);
        Observable<BaseBean> requestNotifProperty(Params params);
        Observable<BaseBean> requestDelsensor(Params params);
        Observable<SubDeviceDetBean> requestSubDeviceDet(Params params);
        Observable<BaseBean> requestModsensor(Params params);
    }
}

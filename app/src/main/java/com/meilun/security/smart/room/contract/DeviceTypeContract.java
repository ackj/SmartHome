package com.meilun.security.smart.room.contract;

import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.DevicesBean;

import java.util.List;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;

/**
 * Author: LiuJia on 2017/8/30 0030 20:05.
 * Email: liujia95me@126.com
 */

public interface DeviceTypeContract {

    interface View extends BaseContract.View {
        void responseDeviceType(List<DevicesBean.DataBean.DeviceTypeListBean> bean);

        void responseAddDevice(BaseBean bean);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestDeviceType(Params params);

        void requestAddDevice(Params params);

        void requestAddCamera(Params params);
    }

    interface Model extends BaseContract.Model {
        Observable<DevicesBean> requestDeviceType(Params params);

        Observable<BaseBean> requestAddDevice(Params params);

        Observable<BaseBean> requestAddCamera(Params params);
    }

}

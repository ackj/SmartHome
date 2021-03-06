package com.meilun.security.smart.room.contract;

import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.entity.bean.RoomsBean;
import com.meilun.security.smart.common.Params;

import java.util.List;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;

/**
 * Author: LiuJia on 2017/8/20 0020 18:10.
 * Email: liujia95me@126.com
 */

public interface RoomDeviceListContract{


    interface View extends BaseContract.View {
        void responseDeviceList(List<DeviceListBean.DataBean.SubDevicesBean> data);
        void responseHouseList(List<RoomsBean.DataBean.RoomListBean> data);
        void responseDevicectrl(BaseBean bean);
        void responseNewDeviceConfirm(BaseBean bean);
        void responseDelSuccess(BaseBean bean);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestDeviceList(Params params);
        void requestHouseList(Params params);
        void requestDevicectrl(Params params);
        void requestNewDeviceConfirm(Params params);
        void requestDelDevice(Params params);
    }

    interface Model extends BaseContract.Model {
        Observable<DeviceListBean> requestDeviceList(Params params);
        Observable<RoomsBean> requestHouseList(Params params);
        Observable<BaseBean> requestDevicectrl(Params params);
        Observable<BaseBean> requestNewDeviceConfirm(Params params);
        Observable<BaseBean> requestDelDevice(Params params);

    }
}

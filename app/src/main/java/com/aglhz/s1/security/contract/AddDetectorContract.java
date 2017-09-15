package com.aglhz.s1.security.contract;

import com.aglhz.s1.common.Params;
import com.aglhz.s1.entity.bean.BaseBean;
import com.aglhz.s1.entity.bean.DevicesBean;

import java.util.List;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;

public interface AddDetectorContract {

    interface View extends BaseContract.View {
        void responseDetectorList(List<DevicesBean.DataBean.DeviceTypeListBean> bean);
        void responseAddDetector(BaseBean bean);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestDetectorList(Params params);
        void requestAddDetector(Params params);
    }

    interface Model extends BaseContract.Model {
        Observable<DevicesBean> requestDetectorList(Params params);
        Observable<BaseBean> requestAddDetector(Params params);
    }
}
package com.meilun.security.smart.smarthome.contract;

import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.FirstLevelBean;

import java.util.List;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;


/**
 * Author: LiuJia on 2017/5/22 0022 10:22.
 * Email: liujia95me@126.com
 */

public interface GoodsCategoryContract {

    interface View extends BaseContract.View {
        void responseFirstLevel(List<FirstLevelBean.DataBean> datas);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestFirstLevel(Params params);
    }

    interface Model extends BaseContract.Model {
        Observable<FirstLevelBean> requestFirstLevel(Params params);
    }

}

package com.aglhz.s1.smarthome.contract;


import com.aglhz.s1.common.Params;
import com.aglhz.s1.entity.bean.GoodsBean;
import com.aglhz.s1.entity.bean.SubCategoryBean;

import java.util.List;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;


/**
 * Author: LiuJia on 2017/5/22 0022 09:06.
 * Email: liujia95me@126.com
 */

public interface SmartHomeMallContract {
    interface View extends BaseContract.View {
        void responseSubCategoryList(List<SubCategoryBean.DataBean> datas);

        void responseGoodsList(List<GoodsBean.DataBean> datas);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestSubCategoryList(Params params);

        void requestGoodsList(Params params);
    }

    interface Model extends BaseContract.Model {
        Observable<SubCategoryBean> requestSubCategoryList(Params params);

        Observable<GoodsBean> requestGoodsList(Params params);
    }
}

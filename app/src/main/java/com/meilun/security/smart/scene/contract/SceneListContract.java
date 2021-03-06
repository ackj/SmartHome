package com.meilun.security.smart.scene.contract;

import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.SceneBean;
import com.meilun.security.smart.common.Params;

import cn.itsite.abase.mvp.contract.base.BaseContract;
import rx.Observable;

public interface SceneListContract {

    interface View extends BaseContract.View {

        void responseSceneList(SceneBean bean);

        void responseStartScene(BaseBean bean);

        void responseDeleteScene(BaseBean bean);
    }

    interface Presenter extends BaseContract.Presenter {

        void requestSceneList(Params params);

        void requestStartScene(Params params);


        void requestDeleteScene(Params params);
    }

    interface Model extends BaseContract.Model {

        Observable<SceneBean> requestSceneList(Params params);

        Observable<BaseBean> requestStartScene(Params params);

        Observable<BaseBean> requestDeleteScene(Params params);
    }
}
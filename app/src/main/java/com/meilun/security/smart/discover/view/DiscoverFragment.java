package com.meilun.security.smart.discover.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meilun.security.smart.R;
import com.meilun.security.smart.camera.CameraListFragment;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.discover.contract.DiscoverContract;
import com.meilun.security.smart.discover.presenter.DiscoverPresenter;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceLogBean;
import com.meilun.security.smart.entity.bean.DiscoverBean;
import com.meilun.security.smart.entity.bean.DiscoverHomeBean;
import com.meilun.security.smart.entity.bean.FirstLevelBean;
import com.meilun.security.smart.entity.bean.SubCategoryBean;
import com.meilun.security.smart.event.EventHostChanged;
import com.meilun.security.smart.event.EventLogin;
import com.meilun.security.smart.event.EventSwitchHost;
import com.meilun.security.smart.history.view.DeviceLogsFragment;
import com.meilun.security.smart.smarthome.view.SmartHomeMallFragment;
import com.meilun.security.smart.web.WebActivity;
import com.meilun.security.smart.widget.PtrHTFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;

/**
 * Author: LiuJia on 2017/11/21 0021 17:23.
 * Email: liujia95me@126.com
 * 发现页
 */
public class DiscoverFragment extends BaseFragment<DiscoverContract.Presenter> implements DiscoverContract.View {
    public static final String TAG = DiscoverFragment.class.getSimpleName();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    private Params params = Params.getInstance();
    private DiscoverRVAdapter adapter;
    Unbinder unbinder;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @NonNull
    @Override
    protected DiscoverContract.Presenter createPresenter() {
        return new DiscoverPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
        initPtrFrameLayout(ptrFrameLayout, recyclerView);
    }

    private void initData() {
        DiscoverHomeBean banners = new DiscoverHomeBean();
        banners.type = DiscoverHomeBean.TYPE_BANNER;
        DiscoverHomeBean notices = new DiscoverHomeBean();
        notices.type = DiscoverHomeBean.TYPE_NOTICE;
        DiscoverHomeBean buttons = new DiscoverHomeBean();
        buttons.type = DiscoverHomeBean.TYPE_BUTTONS;
        DiscoverHomeBean news = new DiscoverHomeBean();
        news.type = DiscoverHomeBean.TYPE_NEWS;
        DiscoverHomeBean stores = new DiscoverHomeBean();
        stores.type = DiscoverHomeBean.TYPE_STORE;

        List<DiscoverHomeBean> datas = new ArrayList<>();
        datas.add(banners);
        datas.add(notices);
        datas.add(buttons);
        datas.add(news);
        datas.add(stores);

        adapter = new DiscoverRVAdapter(datas);
        adapter.setFragment(this);
        //-----------初始化RecyclerView--------
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter1, View view, int position) {
                DiscoverHomeBean bean = adapter.getItem(position);
                switch (bean.type) {
                    case DiscoverHomeBean.TYPE_NOTICE:
                        _mActivity.start(DeviceLogsFragment.newInstance());
                        break;
                    case DiscoverHomeBean.TYPE_NEWS:
                        switch (view.getId()) {
                            case R.id.tv_more:
                                _mActivity.start(NewsListFragment.newInstance());
                                break;
                            default:
                        }
                        break;
                    case DiscoverHomeBean.TYPE_BUTTONS:
                        switch (view.getId()) {
                            case R.id.ll_camera:
                                _mActivity.start(CameraListFragment.newInstance());
                                break;
                            case R.id.ll_switch_gateway:
                                if (TextUtils.isEmpty(params.dstatus)) {
                                    params.dstatus = Constants.GATEWAY_STATE_HOME;
                                }
                                mPresenter.requestSwichState(params);
                                break;
                            case R.id.ll_company:
                                gotoWeb("公司简介", "http://www.aglhz.com/sub_property_ysq/m/html/company_profile.html");
                                break;
                            case R.id.ll_store:
                                _mActivity.start(SmartHomeMallFragment.newInstance(null, 0));
                                break;
                            default:
                        }
                        break;
                    default:
                }
            }
        });
    }

    private void gotoWeb(String title, String link) {
        if (TextUtils.isEmpty(link)) {
            return;
        }
        Intent intent = new Intent(_mActivity, WebActivity.class);
        intent.putExtra(Constants.KEY_TITLE, title);
        intent.putExtra(Constants.KEY_LINK, link);
        _mActivity.startActivity(intent);//点击一个商品跳WEB
    }

    @Override
    public void onRefresh() {
        params.pageSize = 10;
        params.page = 1;
        mPresenter.requestFirstLevel(params);
        mPresenter.requestDiscoverPage(params);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void error(String errorMessage) {
        ptrFrameLayout.refreshComplete();
        ALog.e(TAG, "error:" + errorMessage);
        DialogHelper.warningSnackbar(getView(), errorMessage);
        if (params.page == 1) {
            //为后面的pageState做准备
        } else if (params.page > 1) {
//            adapter.loadMoreFail();
            params.page--;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchHost(EventSwitchHost event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchHost(EventLogin event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangedSecurity(EventHostChanged event) {
        ALog.e(TAG, "onChangedSecurity:" + event.status);
        if (event.status.equals(Constants.GATEWAY_STATE_HOME)) {
            adapter.switchGatewayIsHome(true);
            params.dstatus = Constants.GATEWAY_STATE_FARAWAY;
        } else {
            adapter.switchGatewayIsHome(false);
            params.dstatus = Constants.GATEWAY_STATE_HOME;
        }
    }

    @Override
    public void responseFirstLevel(List<FirstLevelBean.DataBean> datas) {
        if (datas.size() > 0) {
            for (FirstLevelBean.DataBean bean : datas){
                if("智能家居".equals(bean.getName())){
                    params.id =  bean.getId();
                    mPresenter.requestSubCategoryList(params);
                    break;
                }
            }
        }
    }

    @Override
    public void responseSubCategoryList(List<SubCategoryBean.DataBean> datas) {
        if (datas.size() > 0) {
            ptrFrameLayout.refreshComplete();
            adapter.getData().get(4).stores = datas;
            adapter.notifyItemChanged(4);
        }
    }

    @Override
    public void responseDeviceLogs(List<DeviceLogBean.DataBean.LogsBean> data) {
        if (data.size() > 0) {
            ptrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void responseDiscoverPage(DiscoverBean bean) {
        ptrFrameLayout.refreshComplete();
        ALog.e(TAG, "responseDiscoverPage");

        if (bean == null || bean.getData() == null) {
            return;
        }

        adapter.getData().get(0).bannerss = bean.getData().getAdvs();
        adapter.getData().get(3).news = bean.getData().getNews();

        List<String> notices = new ArrayList<>();
        if (bean.getData().getNotices() == null
                || bean.getData().getNotices().isEmpty()) {
            notices.add("消息通知");
        } else {
            for (DiscoverBean.DataBean.NoticesBean noticesBean : bean.getData().getNotices()) {
                notices.add(noticesBean.getTitle());
            }
        }
        adapter.getData().get(1).notices = notices;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void responseSwichState(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
    }
}

package com.meilun.security.smart.security.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.GatewaysBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.entity.bean.SecurityBean;
import com.meilun.security.smart.security.contract.SecurityContract;
import com.meilun.security.smart.security.presenter.SecurityPresenter;

import cn.itsite.abase.mvp.view.base.BaseFragment;


/***
 * Author: LiuJia on 2017/12/25 0025 09:43.
 * Email: liujia95me@23456789-126.com
 */

public class SecurityDefenseFragment extends BaseFragment<SecurityPresenter> implements SecurityContract.View {

    public static final String TAG = SecurityDefenseFragment.class.getSimpleName();

    private Params params = Params.getInstance();
    private SecurityRVAdapter adapter;
    private RecyclerView recyclerView;
    private SecurityBean.DataBean.SubDevicesBean addIconDevice;


    public static SecurityDefenseFragment newInstance(String gateway) {
        SecurityDefenseFragment fragment = new SecurityDefenseFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gateway", gateway);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected SecurityPresenter createPresenter() {
        return new SecurityPresenter(this);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params.gateway = getArguments().getString("gateway", "");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        EventBus.getDefault().register(this);
        return recyclerView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initListener();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.requestSecurity(params);
        }
    }

    private void initData() {
        adapter = new SecurityRVAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        recyclerView.setAdapter(adapter);

        addIconDevice = new SecurityBean.DataBean.SubDevicesBean();
        addIconDevice.setIcon("add_icon");
        addIconDevice.setName("添加探测器");
        adapter.addData(addIconDevice);

        onRefresh();
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                if (position == adapter.getData().size() - 1 ||  adapter.getData().size() == 1) {
                    _mActivity.start(AddDetectorFragment.newInstance());
                } else {
                    SecurityBean.DataBean.SubDevicesBean bean = adapter.getItem(position);
                    _mActivity.start(DetectorPropertyFragment.newInstance(bean));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);
    }

    //----------------------------- 接口回调 ------------------------------

    public Security2Fragment.OnRefreshListener listener;

    public void setOnRefreshListener(Security2Fragment.OnRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void responseSecurity(SecurityBean securityBean) {
        listener.onRefreshScueess(securityBean.getData().getGateway());
        adapter.setNewData(securityBean.getData().getSubDevices());
        adapter.addData(addIconDevice);
    }

    @Override
    public void responseGateways(MainDeviceListBean bean) {

    }

    public void responseGateways(GatewaysBean gatewaysBean) {

    }

    @Override
    public void responseSwichGateway(BaseBean baseBean) {

    }

    @Override
    public void responseSwichState(BaseBean baseBean) {

    }

    @Override
    public void responseLeaveMassge(BaseBean baseBean) {

    }
}

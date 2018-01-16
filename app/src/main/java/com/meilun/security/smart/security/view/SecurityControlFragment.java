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
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.entity.bean.RoomsBean;
import com.meilun.security.smart.room.contract.RoomDeviceListContract;
import com.meilun.security.smart.room.presenter.RoomDeviceListPresenter;
import com.meilun.security.smart.room.view.DeviceGridRVAdapter;
import com.meilun.security.smart.room.view.DeviceOnOffFragment;
import com.meilun.security.smart.room.view.DeviceTypeFragment;

import java.util.List;

import cn.itsite.abase.mvp.view.base.BaseFragment;

/**
 * Author: LiuJia on 2017/12/25 0025 09:44.
 * Email: liujia95me@126.com
 */

public class SecurityControlFragment extends BaseFragment<RoomDeviceListPresenter> implements RoomDeviceListContract.View {


    public static final String TAG = SecurityControlFragment.class.getSimpleName();

    private Params params = Params.getInstance();
    private DeviceGridRVAdapter adapter;
    private RecyclerView recyclerView;
    private DeviceListBean.DataBean.SubDevicesBean addIconDevice;
    public static SecurityControlFragment newInstance(String gateway) {
        SecurityControlFragment fragment = new SecurityControlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gateway", gateway);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected RoomDeviceListPresenter createPresenter() {
        return new RoomDeviceListPresenter(this);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params.gateway = getArguments().getString("gateway", "G211EU6B1000300");
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

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                DeviceListBean.DataBean.SubDevicesBean bean = adapter.getItem(position);
                //点击最后一个跳添加页面
                if (position == adapter.getData().size() - 1||adapter.getData().size() == 1) {
//                    if (selectRoom == null) {
//                        DialogHelper.warningSnackbar(getView(), "请选择房间");
//                        return;
//                    }
                    _mActivity.start(DeviceTypeFragment.newInstance());
                } else if ("camera01".equals(bean.getDeviceType())) {
//                    Intent intent = new Intent(_mActivity, CameraPlay2Activity.class);
//                    intent.putExtra("bean", (Serializable) bean);
//                    startActivity(intent);
                } else {
                    _mActivity.start(DeviceOnOffFragment.newInstance(bean/*, selectRoom*/));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.requestDeviceList(params);
        }
    }

    private void initData() {
        params.roomId = -1;
        params.category = Constants.DEVICE_CTRL;

        adapter = new DeviceGridRVAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        recyclerView.setAdapter(adapter);

        addIconDevice = new DeviceListBean.DataBean.SubDevicesBean();
        addIconDevice.setIcon("add_icon");
        addIconDevice.setName("添加控制器");
        adapter.addData(addIconDevice);
        onRefresh();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBus.getDefault().unregister(this);
    }


    //------------------------------ 回调相关 ----------------------------
    public Security2Fragment.OnRefreshListener listener;

    public void setOnRefreshListener(Security2Fragment.OnRefreshListener listener) {
        this.listener = listener;
    }

    @Override
    public void responseDeviceList(List<DeviceListBean.DataBean.SubDevicesBean> data) {
        listener.onRefreshScueess(null);
        adapter.setNewData(data);
        adapter.addData(addIconDevice);
    }

    @Override
    public void responseHouseList(List<RoomsBean.DataBean.RoomListBean> data) {

    }

    @Override
    public void responseDevicectrl(BaseBean bean) {

    }

    @Override
    public void responseNewDeviceConfirm(BaseBean bean) {

    }

    @Override
    public void responseDelSuccess(BaseBean bean) {

    }
}

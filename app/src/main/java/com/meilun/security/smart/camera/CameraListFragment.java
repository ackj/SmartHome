package com.meilun.security.smart.camera;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meilun.security.smart.R;
import com.meilun.security.smart.camera.contract.CameraListContract;
import com.meilun.security.smart.camera.presenter.CameraListPresenter;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.event.EventCameraStatus;
import com.meilun.security.smart.widget.PtrHTFrameLayout;
import com.p2p.core.P2PHandler;
import com.p2p.core.global.P2PConstants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.statemanager.StateLayout;

/**
 * Author: LiuJia on 2017/11/22 0022 18:07.
 * Email: liujia95me@126.com
 */

public class CameraListFragment extends BaseFragment<CameraListContract.Presenter> implements CameraListContract.View {

    public static final String TAG = CameraListFragment.class.getSimpleName();

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;

    private Unbinder unbinder;
    private Params params = Params.getInstance();
    private CameraListRVAdapter adapter;

    public static CameraListFragment newInstance() {
        return new CameraListFragment();
    }

    @NonNull
    @Override
    protected CameraListContract.Presenter createPresenter() {
        return new CameraListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
        initListener();
        initPtrFrameLayout(ptrFrameLayout, recyclerView);
    }

    @Override
    public void onRefresh() {
        params.page = 1;
        params.pageSize = Constants.PAGE_SIZE;

        mPresenter.requestCameraList(params);
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("联防联动");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
    }

    private void initData() {
        adapter = new CameraListRVAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(adapter);

        params.category = Constants.DEVICE_CTRL;
        params.roomId = -1;
    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter1, View view, int position) {
                DeviceListBean.DataBean.SubDevicesBean bean = adapter.getItem(position);
//                Intent intent = new Intent(_mActivity, CameraPlay2Activity.class);
//                intent.putExtra("bean", (Serializable) bean);
//                _mActivity.startActivity(intent);
            }
        });
    }

    //摄像头是否在线的回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCameraStatusEvent(EventCameraStatus event) {
        for (int i = 0; i < event.contactIds.length; i++) {
            if (adapter.getItem(i).getDeviceId().equals(event.contactIds[i])) {
                adapter.getItem(i).setStatus(event.status[i]);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void start(Object response) {
    }

    @Override
    public void error(String errorMessage) {
        ptrFrameLayout.refreshComplete();
        DialogHelper.warningSnackbar(getView(), errorMessage);
    }

    @Override
    public void responseCameraList(List<DeviceListBean.DataBean.SubDevicesBean> data) {
        ptrFrameLayout.refreshComplete();
        List<DeviceListBean.DataBean.SubDevicesBean> newData = new ArrayList<>();
        //过滤非摄像头设备
        for (int i = 0; i < data.size(); i++) {
            if ("camera01".equals(data.get(i).getDeviceType())) {
                newData.add(data.get(i));
            }
        }

        if (newData.size() > 0) {
            adapter.setNewData(newData);
            //获取设备是否在线
            String[] contactIds = new String[newData.size()];
            for (int i = 0; i < newData.size(); i++) {
                contactIds[i] = newData.get(i).getDeviceId();
            }
            P2PHandler.getInstance().getFriendStatus(contactIds, P2PConstants.P2P_Server.SERVER_INDEX);
        }
    }

    @Override
    public void responseSuccess(BaseBean baseBean) {

    }

    @Override
    public void responseModSuccess(BaseBean baseBean) {

    }
}

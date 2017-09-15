package com.aglhz.s1.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aglhz.s1.R;
import com.aglhz.s1.widget.PtrHTFrameLayout;
import com.p2p.core.P2PHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.statemanager.StateManager;

/**
 * Author: LiuJia on 2017/9/12 0012 09:09.
 * Email: liujia95me@126.com
 */

public class CameraListFragment extends BaseFragment {

    private static final String TAG = CameraListFragment.class.getSimpleName();

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;

    Unbinder unbinder;
    private StateManager mStateManager;
    private String[] addSelectedArr = {"新设备配置网络", "添加已联网设备"};

    public static CameraListFragment newInstance() {
        CameraListFragment fragment = new CameraListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
        initStateManager();
        initListener();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("智能监控");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressedSupport();
            }
        });
    }

    private void initData() {
    }

    private void initStateManager() {
        mStateManager = StateManager.builder(_mActivity)
                .setContent(recyclerView)
                .setEmptyView(R.layout.state_empty)
                .setEmptyImage(R.drawable.bg_vidicon_360px)
                .setEmptyText("还没有添加摄像头哦,赶紧去添加吧~")
                .setErrorOnClickListener(v -> ptrFrameLayout.autoRefresh())
                .setEmptyOnClickListener(v -> showSelectedDialog())
                .setConvertListener((holder, stateLayout) ->
                        holder.setOnClickListener(R.id.bt_empty_state,
                                v -> showSelectedDialog())
                                .setText(R.id.bt_empty_state, "马上添加"))
                .build();

        mStateManager.showEmpty();
    }

    private void showSelectedDialog() {
        new AlertDialog.Builder(_mActivity)
                .setItems(addSelectedArr, (dialog, which) -> {
                    if (which == 0) {
                        _mActivity.start(CameraWifiInputFragment.newInstance());
                    } else {
                        //todo:
                        P2PHandler.getInstance().p2pInit(_mActivity, new P2PListener(), new SettingListener());

                        boolean connect = P2PHandler.getInstance().p2pConnect("011596016", 1682878398, 1951350739, 1437948591, 2059468656);
                        ALog.e(TAG, "connect:" + connect);
//                        _mActivity.start(CameraAddDeviceFragment.newInstance(null));
                        _mActivity.startActivity(new Intent(_mActivity,CameraPlayActivity.class));
                    }
                })
                .show();
    }

    private void initListener() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

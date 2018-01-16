package com.meilun.security.smart.room.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.camera.CameraPlay2Activity;
import com.meilun.security.smart.camera.CameraSettingFragment;
import com.meilun.security.smart.camera.CameraWifiInput2Fragment;
import com.meilun.security.smart.cateye.view.MonitorFragment;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.MainDeviceBean;
import com.meilun.security.smart.event.EventRefreshDeviceList;
import com.meilun.security.smart.room.presenter.DeviceListPresenter;
import com.meilun.security.smart.widget.PtrHTFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.adialog.dialogfragment.BaseDialogFragment;
import cn.itsite.statemanager.StateLayout;

/**
 * Author: LiuJia on 2018/1/3 0003 09:39.
 * Email: liujia95me@126.com
 */

public class DeviceListFragment extends BaseFragment<DeviceListPresenter> {

    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;

    Unbinder unbinder;
    private DeviceListRVAdaptper adaptper;
    private Params params = Params.getInstance();

    public static DeviceListFragment newInstance() {
        DeviceListFragment fragment = new DeviceListFragment();
        return fragment;
    }

    @NonNull
    @Override
    protected DeviceListPresenter createPresenter() {
        return new DeviceListPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        return view;
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
        mPresenter.requestCateyeList();
        mPresenter.requestCameraList(params);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventRefreshDeviceList event) {
        ptrFrameLayout.autoRefresh();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("设备");
        toolbarMenu.setText("添加设备");
        toolbarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(_mActivity)
                        .setItems(new String[]{"摄像头", "猫眼"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    new AlertDialog.Builder(_mActivity)
                                            .setItems(new String[]{"为摄像头配网", "添加已联网设备"}, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (which == 0) {
                                                        _mActivity.start(CameraWifiInput2Fragment.newInstance());
                                                    } else if (which == 1) {
                                                        showAddCameraDialog();
                                                    }
                                                }
                                            })
                                            .show();
                                } else if (which == 1) {
                                    _mActivity.start(com.meilun.security.smart.cateye.view.AddDeviceFragment.newInstance());
                                }
                            }
                        }).show();
            }
        });
    }

    private void initData() {
        params.type = Constants.SMART_CAMERA;

        adaptper = new DeviceListRVAdaptper();
        adaptper.setActivity(_mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        recyclerView.setAdapter(adaptper);
        List<MainDeviceBean> defaultData = new ArrayList<>();
        adaptper.addData(defaultData);
        adaptper.addData(defaultData);
    }

    private void initListener() {
        adaptper.setOnDeviceItemClickListener(new DeviceListRVAdaptper.OnDeviceItemClickListener() {
            @Override
            public void onClickCamera(MainDeviceBean bean) {
                Intent intent = new Intent(_mActivity, CameraPlay2Activity.class);
                intent.putExtra("bean", (Serializable) bean);
                _mActivity.startActivity(intent);
            }

            @Override
            public void onClickCateye(MainDeviceBean bean) {
                _mActivity.start(MonitorFragment.newInstance(bean));
            }

            @Override
            public void onClickLongCamera(MainDeviceBean bean) {
                new AlertDialog.Builder(_mActivity)
                        .setItems(new String[]{"删除","设置"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    new AlertDialog.Builder(_mActivity)
                                            .setTitle("提示")
                                            .setMessage("确定要删除该摄像头吗？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    params.no = bean.deviceId;
                                                    mPresenter.requestDeleteCamera(params);
                                                }
                                            })
                                            .setNegativeButton("取消",null)
                                            .show();
                                }else{
                                    _mActivity.start(CameraSettingFragment.newInstance(bean));
//                                    Intent intent = new Intent(_mActivity, CameraSettingActivity.class);
//                                    intent.putExtra("bean",  bean);
//                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void error(String errorMessage) {
        DialogHelper.errorSnackbar(getView(), errorMessage);
        ptrFrameLayout.refreshComplete();
    }

    public void responseCateyeDevices(List<MainDeviceBean> data) {
        ptrFrameLayout.refreshComplete();
        adaptper.setData(0, data);
    }

    public void responseCameraList(List<MainDeviceBean> data) {
        ptrFrameLayout.refreshComplete();
        adaptper.setData(1, data);
    }

    public void responseOperatorSuccess(String message) {
        DialogHelper.successSnackbar(getView(), message);
        ptrFrameLayout.autoRefresh();
        if(dialogAddCamera!=null){
            dialogAddCamera.dismiss();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    //----------------------------------------------------
    private BaseDialogFragment dialogAddCamera;

    /**
     * 弹出添加摄像头的输入框
     */
    private void showAddCameraDialog() {
        dialogAddCamera = new BaseDialogFragment()
                .setLayoutId(R.layout.fragment_input_video)
                .setConvertListener((holder, dialog) -> {
                    EditText etDeviceId = holder.getView(R.id.et_input_1);
                    EditText etNickname = holder.getView(R.id.et_input_2);
                    EditText etPassword = holder.getView(R.id.et_input_3);
                    holder.setText(R.id.tv_title, "添加设备")
                            .setText(R.id.et_input_2, params.name)
                            .setOnClickListener(R.id.tv_cancel, v -> {
                                dialog.dismiss();
                            })
                            .setOnClickListener(R.id.tv_comfirm, v -> {
                                params.no = etDeviceId.getText().toString().trim();
                                params.name = etNickname.getText().toString().trim();
                                params.password = etPassword.getText().toString().trim();
                                if (TextUtils.isEmpty(params.no)) {
                                    DialogHelper.warningSnackbar(getView(), "请输入摄像头ID");
                                    return;
                                }
                                if (TextUtils.isEmpty(params.name)) {
                                    DialogHelper.warningSnackbar(getView(), "请输入摄像头昵称");
                                    return;
                                }
                                if (TextUtils.isEmpty(params.password)) {
                                    DialogHelper.warningSnackbar(getView(), "请输入摄像头密码");
                                    return;
                                }
                                mPresenter.requestNewCamera(params);
                            });
                })
                .setMargin(40)
                .setDimAmount(0.3f)
                .setGravity(Gravity.CENTER)
                .show(getFragmentManager());
    }
}

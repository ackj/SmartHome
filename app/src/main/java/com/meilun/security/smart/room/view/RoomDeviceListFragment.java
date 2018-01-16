package com.meilun.security.smart.room.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.entity.bean.RoomsBean;
import com.meilun.security.smart.event.EventAddDevice;
import com.meilun.security.smart.event.EventCameraListRefresh;
import com.meilun.security.smart.event.EventDeviceChanged;
import com.meilun.security.smart.event.EventSelectedDeviceType;
import com.meilun.security.smart.event.EventSwitchHost;
import com.meilun.security.smart.room.contract.RoomDeviceListContract;
import com.meilun.security.smart.room.presenter.RoomDeviceListPresenter;
import com.meilun.security.smart.widget.PtrHTFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.adialog.dialogfragment.SelectorDialogFragment;

/**
 * Author： Administrator on 2017/8/18 0018.
 * Email： liujia95me@126.com
 */
public class RoomDeviceListFragment extends BaseFragment<RoomDeviceListContract.Presenter> implements RoomDeviceListContract.View {
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;
    Unbinder unbinder;
    private ImageView ivRoom;
    private ImageView ivHeader;
    private Params params = Params.getInstance();
    private RoomsBean.DataBean.RoomListBean selectRoom;
    private boolean isFirst = true;//是否是第一次进来
    private ImageView ivCamera;
    private DeviceGridRVAdapter adapter;
    private DeviceListBean.DataBean.SubDevicesBean addIconDevice;
    private String[] cameraArr = {"设置", "删除"};

    public static RoomDeviceListFragment newInstance() {
        return new RoomDeviceListFragment();
    }

    @NonNull
    @Override
    protected RoomDeviceListContract.Presenter createPresenter() {
        return new RoomDeviceListPresenter(this);
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
        params.page = 1;
        params.category = Constants.DEVICE_CTRL;
        mPresenter.requestDeviceList(params);
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("大厅");
        toolbarMenu.setText("切换");
        toolbarMenu.setOnClickListener(v -> mPresenter.requestHouseList(params));
    }

    private void initData() {
        recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        adapter = new DeviceGridRVAdapter();
        View viewHeader = LayoutInflater.from(_mActivity).inflate(R.layout.layout_room_header, null);
        ivRoom = (ImageView) viewHeader.findViewById(R.id.iv_room);
        ivCamera = (ImageView) viewHeader.findViewById(R.id.iv_camera);
        ivHeader = new ImageView(_mActivity);
        ivHeader.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(_mActivity)
                .load(R.drawable.room_cesuo_1242px_745px)
                .into(ivRoom);
        adapter.setHeaderView(viewHeader);
        recyclerView.setAdapter(adapter);
        mPresenter.requestHouseList(params);

        addIconDevice = new DeviceListBean.DataBean.SubDevicesBean();
        addIconDevice.setIcon("add_icon");
        addIconDevice.setName("添加控制器");
        adapter.addData(addIconDevice);
    }

    private void selectRoom(RoomsBean.DataBean.RoomListBean bean) {
        selectRoom = bean;
        ALog.e(TAG, "selectRoom--->:" + selectRoom);
        params.roomId = selectRoom.getIndex();
        params.category = Constants.DEVICE_CTRL;
        //请求设备
        mPresenter.requestDeviceList(params);
        int resId = R.drawable.room_room_1242px_745px;
        switch (bean.getName()) {
            case "大厅":
                resId = R.drawable.room_dating_1242px_745px;
                break;
            case "厨房":
                resId = R.drawable.room_chufang_1242px_745px;
                break;
            case "卧室":
                resId = R.drawable.room_room_1242px_745px;
                break;
            case "厕所":
            case "浴室":
                resId = R.drawable.room_cesuo_1242px_745px;
                break;
            default:
        }
        toolbarTitle.setText(bean.getName());
        Glide.with(_mActivity)
                .load(resId)
                .into(ivRoom);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventAddDevice(EventAddDevice event) {
        _mActivity.start(AddDeviceFragment.newInstance(null/*, selectRoom*/));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCameraListRefresh(EventCameraListRefresh event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSelectedDeviceType(EventSelectedDeviceType event) {
        new AlertDialog.Builder(_mActivity)
                .setTitle("学习中...")
                .setMessage("设备是否收到了正确的反馈？")
                .setNegativeButton("否", null)
                .setPositiveButton("是", (dialog, which) -> {
                    params.status = 1;
                    mPresenter.requestNewDeviceConfirm(params);
                }).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeDataEvent(EventDeviceChanged event) {
        onRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSwitchHost(EventSwitchHost event) {
        params.page = 1;
        params.roomFid = "0";
        params.category = Constants.DEVICE_CTRL;
        mPresenter.requestDeviceList(params);
    }

    private void initListener() {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            DeviceListBean.DataBean.SubDevicesBean bean = adapter.getItem(position);
            //点击最后一个跳添加页面
            if (position == adapter.getData().size() - 1) {
                if (selectRoom == null) {
                    DialogHelper.warningSnackbar(getView(), "请选择房间");
                    return;
                }
                _mActivity.start(DeviceTypeFragment.newInstance(selectRoom.getFid()));
            } else if ("camera01".equals(bean.getDeviceType())) {
//                Intent intent = new Intent(_mActivity, CameraPlay2Activity.class);
//                intent.putExtra("bean", (Serializable) bean);
//                startActivity(intent);
            } else {
                _mActivity.start(DeviceOnOffFragment.newInstance(bean/*, selectRoom*/));
            }
        });

        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter1, View view, int position) {
                DeviceListBean.DataBean.SubDevicesBean bean = adapter.getItem(position);
                if ("camera01".equals(bean.getDeviceType())) {
                    new AlertDialog.Builder(_mActivity)
                            .setItems(cameraArr, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
//                                        _mActivity.start(CameraSettingFragment.newInstance(bean));
                                    } else {

                                        showDeleteCameraDialog(bean);
                                    }
                                }
                            }).show();
                }
                return false;
            }
        });
    }

    private void showDeleteCameraDialog(DeviceListBean.DataBean.SubDevicesBean bean) {
        new AlertDialog.Builder(_mActivity)
                .setTitle("提示")
                .setMessage("你确定要删除吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        params.index = bean.getIndex();
                        mPresenter.requestDelDevice(params);
                    }
                }).setNegativeButton("取消", null).show();
    }

    private void changedRoom(String room) {
        toolbarTitle.setText(room);
        Glide.with(_mActivity)
                .load(R.drawable.room_dating_1242px_745px)
                .into(ivRoom);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void responseDeviceList(List<DeviceListBean.DataBean.SubDevicesBean> data) {
        ptrFrameLayout.refreshComplete();
        if (data.size() < Constants.PAGE_SIZE) {
            //如果加载数量小于个数，直接完成
            adapter.loadMoreEnd();
        } else {
            //否则，可继续加载
            adapter.loadMoreComplete();
        }
        if (params.page == 1) {
            data.add(addIconDevice);
            adapter.setNewData(data);
        } else {
            adapter.addData(data);
        }
        //如果个数为0，显示空
        if (adapter.getData().size() == 0) {
//            mStateManager.showEmpty();
            adapter.loadMoreEnd();
        } else {
//            mStateManager.showContent();
        }
    }


    @Override
    public void responseHouseList(List<RoomsBean.DataBean.RoomListBean> data) {
        if (isFirst) {
            selectRoom(data.get(0));//
            isFirst = false;
        } else {
            showRoomSelecotr(data);
        }
    }

    @Override
    public void error(String errorMessage) {
        super.error(errorMessage);
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void responseDevicectrl(BaseBean bean) {
        DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
    }

    @Override
    public void responseNewDeviceConfirm(BaseBean bean) {
        DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
        onRefresh();
    }

    @Override
    public void responseDelSuccess(BaseBean bean) {
        DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
        onRefresh();
    }

    private void showRoomSelecotr(List<RoomsBean.DataBean.RoomListBean> data) {
        new SelectorDialogFragment()
                .setTitle("切换房间")
                .setItemLayoutId(R.layout.item_rv_simple_selector)
                .setData(data)
                .setOnItemConvertListener((holder, which, dialog) ->
                        holder.setText(R.id.tv_item_rv_simple_selector, data.get(which).getName()))
                .setOnItemClickListener((view, baseViewHolder, which, dialog) -> {
                    dialog.dismiss();
                    selectRoom(data.get(which));
                })
                .setAnimStyle(R.style.SlideAnimation)
                .setGravity(Gravity.BOTTOM)
                .show(getChildFragmentManager());
    }
}

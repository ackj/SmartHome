package com.meilun.security.smart.security.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.meilun.security.smart.App;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.GatewaysBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.entity.bean.SecurityBean;
import com.meilun.security.smart.event.EventSwitchHost;
import com.meilun.security.smart.security.contract.SecurityContract;
import com.meilun.security.smart.security.presenter.SecurityPresenter;
import com.meilun.security.smart.widget.PtrHTFrameLayout;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.event.EventHostChanged;
import com.meilun.security.smart.event.NoMultiClickListener;
import com.meilun.security.smart.widget.RecordButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.abase.utils.DensityUtils;
import cn.itsite.adialog.dialogfragment.SelectorDialogFragment;
import cn.itsite.apush.event.EventRefreshSecurity;
import cn.itsite.statemanager.StateLayout;


/**
 * Author: LiuJia on 2017/4/26 0026 11:11.
 * Email: liujia95me@126.com
 */

public class SecurityFragment extends BaseFragment<SecurityContract.Presenter> implements SecurityContract.View {
    public static final String TAG = SecurityFragment.class.getSimpleName();
    static final int VOICE_REQUEST_CODE = 66;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    private SecurityRVAdapter adapter;
    private Params params = Params.getInstance();
    private TextView tvHome;
    private TextView tvCancel;
    private TextView tvFaraway;
    private TextView tvDes;
    private SecurityBean.DataBean.SubDevicesBean addIconDevice;
    private List<SecurityBean.DataBean.SubDevicesBean> subDevices;
    private RecordButton mRecord;

    public static SecurityFragment newInstance() {
        return new SecurityFragment();
    }

    @NonNull
    @Override
    protected SecurityContract.Presenter createPresenter() {
        return new SecurityPresenter(this);
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
        requestPermissions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("智能安防");
        toolbarMenu.setText("切换");
        toolbarMenu.setOnClickListener(v -> mPresenter.requestGateways(params));
    }

    private void initData() {
        recyclerView.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        addIconDevice = new SecurityBean.DataBean.SubDevicesBean();
        addIconDevice.setIcon("add_icon");
        addIconDevice.setName("添加探测器");
        adapter = new SecurityRVAdapter();
        adapter.setHeaderView(initHeaderView());
        recyclerView.setAdapter(adapter);
        List<SecurityBean.DataBean.SubDevicesBean> data = new ArrayList<>();
        data.add(addIconDevice);
        adapter.setNewData(data);
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.requestSecurity(params);
        }
    }

    private View initHeaderView() {
        View headerView = LayoutInflater.from(_mActivity).inflate(R.layout.item_security_header, null);
        headerView.setBackgroundResource(R.drawable.bg_security_header);
        //初始化撤防、布防等View。
        tvDes = (TextView) headerView.findViewById(R.id.tv_des_security_header);
        tvCancel = (TextView) headerView.findViewById(R.id.tv_cancel_item_security_header);
        tvHome = (TextView) headerView.findViewById(R.id.tv_home_item_security_header);
        tvFaraway = (TextView) headerView.findViewById(R.id.tv_faraway_item_security_header);
        mRecord = (RecordButton) headerView.findViewById(R.id.tv_message_item_security_header);
        //设置状态。
        tvCancel.setOnClickListener(new NoMultiClickListener() {
            @Override
            public void onNoMultiClick(View view) {
                params.dstatus = Constants.GATEWAY_STATE_CANCLE;
                mPresenter.requestSwichState(params);
            }
        });
        tvHome.setOnClickListener(new NoMultiClickListener() {
            @Override
            public void onNoMultiClick(View view) {
                params.dstatus = Constants.GATEWAY_STATE_HOME;
                mPresenter.requestSwichState(params);
            }
        });
        tvFaraway.setOnClickListener(new NoMultiClickListener() {
            @Override
            public void onNoMultiClick(View view) {
                params.dstatus = Constants.GATEWAY_STATE_FARAWAY;
                mPresenter.requestSwichState(params);
            }
        });

        mRecord.setSavePath(Constants.PATH_DATA + File.separator + "leavemassage.amr");
        mRecord.setOnFinishedRecordListener(audioPath -> {
            params.file = new File(audioPath);
            mPresenter.requestLeaveMassge(params);
        });
        return headerView;
    }

    private void initListener() {
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (position == adapter.getData().size() - 1) {
                _mActivity.start(AddDetectorFragment.newInstance());
            } else {
                if (subDevices == null || subDevices.size() == 0) {
                    return;
                }
                SecurityBean.DataBean.SubDevicesBean bean = subDevices.get(position);
                _mActivity.start(DetectorPropertyFragment.newInstance(bean));
            }
        });
    }

    private String getStringByStatus(String status) {
        switch (status) {
            case Constants.GATEWAY_STATE_CANCLE:
                return "撤防";
            case Constants.GATEWAY_STATE_HOME:
                return "在家";
            case Constants.GATEWAY_STATE_FARAWAY:
                return "离家";
            default:
                return "";
        }
    }

    @Override
    public void error(String errorMessage) {
        super.error(errorMessage);
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void responseSecurity(SecurityBean securityBean) {
        ptrFrameLayout.refreshComplete();
        subDevices = securityBean.getData().getSubDevices();
        SecurityBean.DataBean.GatewayBean gatewayBean = securityBean.getData().getGateway();
        TextView tv = (TextView) adapter.getHeaderLayout()
                .findViewWithTag(gatewayBean.getDefenseStatus());
        tvCancel.setSelected(false);
        tvHome.setSelected(false);
        tvFaraway.setSelected(false);
        EventBus.getDefault().post(new EventHostChanged(gatewayBean.getDefenseStatus()));
        if (tv != null) {//由于第一次安装，后台不知道主机的状态，所以defenseStatus这个字段为空，所以找不到这样的TextView。
            tv.setSelected(true);
        }

        String des = getStringByStatus(gatewayBean.getDefenseStatus()) + "：" + gatewayBean.getDefenseStatusDes();
        Spannable WordtoSpan = new SpannableString(des);
        WordtoSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(getContext(), 18)), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDes.setText(WordtoSpan);

        //设置toolbar
        StringBuilder title = new StringBuilder();
        if (securityBean.getData().getGateway().getIsOnline() == 0) {
            title.append("（离线）");
        } else {
            title.append("（在线）");
        }
        title.append(securityBean.getData().getGateway().getName());
        toolbarTitle.setText(title);

        if (securityBean.getData() != null
                || securityBean.getData().getSubDevices() != null) {
            adapter.setHostState(securityBean.getData().getGateway().getDefenseStatus());
            adapter.setNewData(securityBean.getData().getSubDevices());
        }
        adapter.addData(addIconDevice);
    }

    @Override
    public void responseGateways(MainDeviceListBean bean) {

    }

    public void responseGateways(GatewaysBean gateways) {
        if (gateways == null || gateways.getData() == null
                || gateways.getData().isEmpty()) {
            DialogHelper.warningSnackbar(getView(), "您尚未配置网关！");
            return;
        }
        new SelectorDialogFragment()
                .setTitle("请选择切换的主机")
                .setItemLayoutId(R.layout.item_rv_host_selector)
                .setData(gateways.getData())
                .setOnItemConvertListener((holder, position, dialog) -> {
                    GatewaysBean.DataBean item = gateways.getData().get(position);
                    holder.setText(R.id.tv_role_item_rv_host_selector, item.getIsManager() == 1 ? "管理员" : "成员")
                            .setText(R.id.tv_current_item_rv_host_selector, item.getIsCurrent() == 1 ? "当前主机" : "")
                            .setText(R.id.tv_name_item_rv_host_selector, "名称：" + item.getName() + (item.getIsOnline() == 1 ? "　(在线)" : "　(离线)"))
                            .setText(R.id.tv_code_item_rv_host_selector, "编号：" + item.getNo())
                            .setTextColor(R.id.tv_name_item_rv_host_selector,
                                    item.getIsOnline() == 1 ? Color.parseColor("#32E232") : Color.parseColor("#999999"));
                })
                .setOnItemClickListener((view, baseViewHolder, position, dialog) -> {
                    dialog.dismiss();
                    params.gateway = gateways.getData().get(position).getFid();
                    mPresenter.requestSwichGateway(params);
                })
                .setAnimStyle(R.style.SlideAnimation)
                .setGravity(Gravity.BOTTOM)
                .show(getChildFragmentManager());
    }

    @Override
    public void responseSwichGateway(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
        ptrFrameLayout.autoRefresh();
        EventBus.getDefault().post(new EventSwitchHost());
    }

    @Override
    public void responseSwichState(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
        switch (params.dstatus) {
            case Constants.GATEWAY_STATE_CANCLE:
                tvCancel.setSelected(true);
                tvHome.setSelected(false);
                tvFaraway.setSelected(false);
                break;
            case Constants.GATEWAY_STATE_HOME:
                tvHome.setSelected(true);
                tvCancel.setSelected(false);
                tvFaraway.setSelected(false);
                break;
            case Constants.GATEWAY_STATE_FARAWAY:
                tvFaraway.setSelected(true);
                tvHome.setSelected(false);
                tvCancel.setSelected(false);
                break;
            default:
        }
        adapter.setHostState(params.dstatus);
    }

    @Override
    public void responseLeaveMassge(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshSecurity(EventRefreshSecurity event) {
        onRefresh();
    }

    /**
     * 判断权限是否打开.
     */
    private void requestPermissions() {
        if ((ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)) {

        } else {
            ActivityCompat.requestPermissions(_mActivity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, VOICE_REQUEST_CODE);
        }
    }

    /**
     * 请求权限回调.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == VOICE_REQUEST_CODE) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

            } else {
                Toast.makeText(App.mContext, "已拒绝权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        onRefresh();
    }
}

package com.meilun.security.smart.security.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meilun.security.smart.App;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.entity.bean.SecurityBean;
import com.meilun.security.smart.event.EventHostChanged;
import com.meilun.security.smart.event.EventSwitchHost;
import com.meilun.security.smart.security.contract.SecurityContract;
import com.meilun.security.smart.security.presenter.SecurityPresenter;
import com.meilun.security.smart.widget.PtrHTFrameLayout;
import com.meilun.security.smart.widget.RecordButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.abase.utils.DensityUtils;
import cn.itsite.adialog.dialogfragment.SelectorDialogFragment;
import cn.itsite.apush.event.EventRefreshSecurity;

/**
 * Author: LiuJia on 2017/12/25 0025 09:20.
 * Email: liujia95me@126.com
 */

public class Security2Fragment extends BaseFragment<SecurityPresenter> implements SecurityContract.View {

    public static final String TAG = Security2Fragment.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_content_layout)
    LinearLayout llContentLayout;

    @BindView(R.id.ll_header_layout)
    LinearLayout llHeaderLayout;
    @BindView(R.id.tv_des_security_header)
    TextView tvDes;
    @BindView(R.id.tv_cancel_item_security_header)
    TextView tvCancel;
    @BindView(R.id.tv_home_item_security_header)
    TextView tvHome;
    @BindView(R.id.tv_faraway_item_security_header)
    TextView tvFaraway;
    @BindView(R.id.tv_message_item_security_header)
    RecordButton record;

    static final int VOICE_REQUEST_CODE = 66;
    private Params params = Params.getInstance();
    private SecurityDefenseFragment defenseFragment;
    private SecurityControlFragment controlFragment;
    private String gateway;

    public static Security2Fragment newInstance() {
        return new Security2Fragment();
    }

    @NonNull
    @Override
    protected SecurityPresenter createPresenter() {
        return new SecurityPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_security_2, container, false);
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
        initPtrFrameLayout(ptrFrameLayout, llContentLayout);
        requestPermissions();
    }

    private void initData() {
        llHeaderLayout.setBackgroundResource(R.drawable.bg_security_header);
        List<BaseFragment> fragments = new ArrayList<>();
        defenseFragment = SecurityDefenseFragment.newInstance(gateway);
        controlFragment = SecurityControlFragment.newInstance(gateway);
        //默认刷新的RecyclerView为控制
        setTopView(ptrFrameLayout, controlFragment.getRecyclerView());
        fragments.add(controlFragment);
        fragments.add(defenseFragment);
        viewpager.setAdapter(new SecurityVPAdapter(getChildFragmentManager(), fragments));
        tablayout.setupWithViewPager(viewpager);
    }

    private void initListener() {
        defenseFragment.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefreshScueess(SecurityBean.DataBean.GatewayBean gatewayBean) {
                refreshHeader(gatewayBean);
                ptrFrameLayout.refreshComplete();
            }
        });

        controlFragment.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefreshScueess(SecurityBean.DataBean.GatewayBean gatewayBean) {
                ptrFrameLayout.refreshComplete();
            }
        });

        record.setSavePath(Constants.PATH_DATA + File.separator + "leavemassage.amr");
        record.setOnFinishedRecordListener(audioPath -> {
            params.file = new File(audioPath);
            mPresenter.requestLeaveMassge(params);
        });


        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    isDefense = false;
                    setTopView(ptrFrameLayout, controlFragment.getRecyclerView());
                } else {
                    isDefense = true;
                    setTopView(ptrFrameLayout, defenseFragment.getRecyclerView());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshSecurity(EventRefreshSecurity event) {
        onRefresh();
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
        params.type = Constants.SMART_GATEWAY;
        toolbarMenu.setOnClickListener(v -> mPresenter.requestGateways(params));
    }

    private void refreshHeader(SecurityBean.DataBean.GatewayBean gatewayBean) {
        gateway = gatewayBean.getFid();
        TextView tv = (TextView) llHeaderLayout
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
        if (gatewayBean.getIsOnline() == 0) {
            title.append("（离线）");
        } else {
            title.append("（在线）");
        }
        title.append(gatewayBean.getName());
        toolbarTitle.setText(title);
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

    private boolean isDefense = true;

    @Override
    public void onRefresh() {
        if (isDefense) {
            defenseFragment.onRefresh();
        } else {
            controlFragment.onRefresh();
        }
    }

    @OnClick({R.id.tv_cancel_item_security_header, R.id.tv_home_item_security_header, R.id.tv_faraway_item_security_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_item_security_header:
                params.dstatus = Constants.GATEWAY_STATE_CANCLE;
                mPresenter.requestSwichState(params);
                break;
            case R.id.tv_home_item_security_header:
                params.dstatus = Constants.GATEWAY_STATE_HOME;
                mPresenter.requestSwichState(params);
                break;
            case R.id.tv_faraway_item_security_header:
                params.dstatus = Constants.GATEWAY_STATE_FARAWAY;
                mPresenter.requestSwichState(params);
                break;
            default:
        }
    }

    public interface OnRefreshListener {
        void onRefreshScueess(SecurityBean.DataBean.GatewayBean gatewayBean);
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

    //------------------------- 回调相关 --------------------------


    @Override
    public void error(String errorMessage) {
        super.error(errorMessage);
        ptrFrameLayout.refreshComplete();
    }

    @Override
    public void responseSecurity(SecurityBean securityBean) {

    }

    @Override
    public void responseGateways(MainDeviceListBean bean) {
        if (bean == null || bean.getData() == null
                || bean.getData().isEmpty()) {
            DialogHelper.warningSnackbar(getView(), "您尚未配置网关！");
            return;
        }
        new SelectorDialogFragment()
                .setTitle("请选择切换的主机")
                .setItemLayoutId(R.layout.item_rv_host_selector)
                .setData(bean.getData())
                .setOnItemConvertListener((holder, position, dialog) -> {
                    MainDeviceListBean.DataBean item = bean.getData().get(position);
                    holder.setText(R.id.tv_role_item_rv_host_selector, item.getIsManager() == 1 ? "管理员" : "成员")
                            .setText(R.id.tv_current_item_rv_host_selector, item.getIsCurrent() == 1 ? "当前主机" : "")
                            .setText(R.id.tv_name_item_rv_host_selector, "名称：" + item.getName() + (item.getIsOnline() == 1 ? "　(在线)" : "　(离线)"))
                            .setText(R.id.tv_code_item_rv_host_selector, "编号：" + item.getNo())
                            .setTextColor(R.id.tv_name_item_rv_host_selector,
                                    item.getIsOnline() == 1 ? Color.parseColor("#32E232") : Color.parseColor("#999999"));
                })
                .setOnItemClickListener((view, baseViewHolder, position, dialog) -> {
                    dialog.dismiss();
                    params.gateway = bean.getData().get(position).getFid();
                    mPresenter.requestSwichGateway(params);
                })
                .setAnimStyle(R.style.SlideAnimation)
                .setGravity(Gravity.BOTTOM)
                .show(getChildFragmentManager());
    }

    @Override
    public void responseSwichGateway(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
//        ptrFrameLayout.autoRefresh();
        defenseFragment.onRefresh();
        controlFragment.onRefresh();
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
    }

    @Override
    public void responseLeaveMassge(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
    }


}

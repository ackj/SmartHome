package com.meilun.security.smart.host.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.UserHelper;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.common.RxManager;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.abase.network.http.HttpHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */

public class HostSettingsFragment extends BaseFragment {
    public static final String TAG = HostSettingsFragment.class.getSimpleName();
    public static final int RESULT_HOST_SETTINGS = 1234;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_host_name_host_setting_fragment)
    TextView tvHostName;
    @BindView(R.id.tv_location_host_setting_fragment)
    TextView tvLocation;
    @BindView(R.id.tv_alert_sms_host_setting_fragment)
    TextView tvAlertMessage;
    @BindView(R.id.tv_push_host_setting_fragment)
    TextView tvPush;
    @BindView(R.id.tv_volume_host_setting_fragment)
    TextView tvVolume;
    @BindView(R.id.tv_accredit_host_setting_fragment)
    TextView tvAccredit;
    @BindView(R.id.ll_host_name_host_setting_fragment)
    LinearLayout llHostName;
    @BindView(R.id.tv_unbind_host_setting_fragment)
    TextView tvUnbind;
    private Unbinder unbinder;
    private MainDeviceListBean.DataBean hostBean;
    private RxManager mRxManager = new RxManager();

    public static HostSettingsFragment newInstance(MainDeviceListBean.DataBean hostBean) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_HOST, hostBean);
        HostSettingsFragment fragment = new HostSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            hostBean = (MainDeviceListBean.DataBean) args.getSerializable(Constants.KEY_HOST);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
    }

    private void initData() {
        tvHostName.setText(hostBean.getName());
        if (hostBean.getIsManager() != 1) {
            tvAccredit.setVisibility(View.GONE);
        }
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("主机设置");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mRxManager.clear();
    }

    @OnClick({R.id.ll_host_name_host_setting_fragment,
            R.id.tv_location_host_setting_fragment,
            R.id.tv_alert_sms_host_setting_fragment,
            R.id.tv_push_host_setting_fragment,
            R.id.tv_volume_host_setting_fragment,
            R.id.tv_unbind_host_setting_fragment,
            R.id.tv_accredit_host_setting_fragment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_host_name_host_setting_fragment:
                startForResult(EditHostFragment.newInstance(hostBean), RESULT_HOST_SETTINGS);
                break;
            case R.id.tv_location_host_setting_fragment:
                startForResult(AddHostFragment.newInstance("", hostBean),RESULT_HOST_SETTINGS);
                break;
            case R.id.tv_alert_sms_host_setting_fragment:
                start(AlertSmsFragment.newInstance(hostBean));
                break;
            case R.id.tv_push_host_setting_fragment:
                start(PushSettingsFragment.newInstance(hostBean));
                break;
            case R.id.tv_volume_host_setting_fragment:
                start(VolumeSettingsFragment.newInstance(hostBean));
                break;
            case R.id.tv_unbind_host_setting_fragment:
                new AlertDialog.Builder(_mActivity)
                        .setTitle("提醒")
                        .setMessage("确定要解除绑定当前主机吗？")
                        .setPositiveButton("确定", (dialog, which) ->
                                mRxManager.add(HttpHelper.getService(ApiService.class)
                                        .requestUnbindHost(ApiService.requestUnbindHost, UserHelper.token, hostBean.getNo())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(bean -> {
                                            if (bean.getOther().getCode() == Constants.RESPONSE_CODE_SUCCESS) {
                                                DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
                                            } else {
                                                error(bean.getOther().getMessage());
                                            }
                                        }, this::error/*, () -> complete(""), disposable -> start("")*/)))
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.tv_accredit_host_setting_fragment:
                start(AuthorizeFragment.newInstance(hostBean));
                break;
            default:
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        ALog.e("requestCode-->" + requestCode);
        ALog.e("resultCode-->" + resultCode);
        if (data != null) {
            hostBean = (MainDeviceListBean.DataBean) data.getSerializable(Constants.KEY_HOST);
            tvHostName.setText(hostBean.getName());
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.KEY_HOST, hostBean);
            setFragmentResult(HostSettingsFragment.RESULT_HOST_SETTINGS, bundle);
        }
    }
}

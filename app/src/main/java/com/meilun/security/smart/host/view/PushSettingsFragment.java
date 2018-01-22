package com.meilun.security.smart.host.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.HostSettingsBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.host.contract.HostSettingsContract;
import com.meilun.security.smart.host.presenter.HostSettingsPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */

public class PushSettingsFragment extends BaseFragment<HostSettingsContract.Presenter> implements HostSettingsContract.View {
    public static final String TAG = PushSettingsFragment.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sb_waijiedianyuandiaodian)
    SwitchButton sbWaijiedianyuandiaodian;
    @BindView(R.id.ll_waijiedianyuandiaodian)
    LinearLayout llWaijiedianyuandiaodian;
    @BindView(R.id.sb_huifuwaijiedianyuan)
    SwitchButton sbHuifuwaijiedianyuan;
    @BindView(R.id.ll_huifuwaijiedianyuan)
    LinearLayout llHuifuwaijiedianyuan;
    @BindView(R.id.sb_bufangchefang)
    SwitchButton sbBufangchefang;
    @BindView(R.id.ll_bufangchefang)
    LinearLayout llBufangchefang;
    @BindView(R.id.sb_zhujidianchidianliangdi)
    SwitchButton sbZhujidianchidianliangdi;
    @BindView(R.id.ll_zhujidianchidianliangdi)
    LinearLayout llZhujidianchidianliangdi;
    @BindView(R.id.sb_chuanganqidianliangdi)
    SwitchButton sbChuanganqidianliangdi;
    @BindView(R.id.ll_chuanganqidianliangdi)
    LinearLayout llChuanganqidianliangdi;
    @BindView(R.id.sb_wifilianjei)
    SwitchButton sbWifilianjei;
    @BindView(R.id.ll_wifilianjei)
    LinearLayout llWifilianjei;
    @BindView(R.id.sb_wifiduankai)
    SwitchButton sbWifiduankai;
    @BindView(R.id.ll_wifiduankai)
    LinearLayout llWifiduankai;
    @BindView(R.id.sb_duanxintuisong)
    SwitchButton sbDuanxintuisong;
    @BindView(R.id.ll_duanxintuisong)
    LinearLayout llDuanxintuisong;
    private Unbinder unbinder;
    private MainDeviceListBean.DataBean hostBean;
    private Params params = Params.getInstance();
    private SwitchButton sbCurrent;

    public static PushSettingsFragment newInstance(MainDeviceListBean.DataBean hostBean) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.KEY_HOST, hostBean);
        PushSettingsFragment fragment = new PushSettingsFragment();
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

    @NonNull
    @Override
    protected HostSettingsContract.Presenter createPresenter() {
        return new HostSettingsPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_settings, container, false);
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
        params.gateway = hostBean.getNo();
        params.type = Constants.PUSH;
        mPresenter.requestHostSettings(params);
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("推送设置");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void responseSetHost(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
        sbCurrent.setChecked(params.val.equals("1"));
    }

    @Override
    public void responseHostSettings(HostSettingsBean baseBean) {
        HostSettingsBean.DataBean bean = baseBean.getData();
        sbWaijiedianyuandiaodian.setChecked(bean.getPower_fail() == 1);
        sbHuifuwaijiedianyuan.setChecked(bean.getPower_recover() == 1);
        sbBufangchefang.setChecked(bean.getDefense_chg() == 1);
        sbZhujidianchidianliangdi.setChecked(bean.getHost_power_low() == 1);
        sbChuanganqidianliangdi.setChecked(bean.getSensor_power_low() == 1);
        sbWifilianjei.setChecked(bean.getWifi_connect() == 1);
        sbWifiduankai.setChecked(bean.getWifi_disconnect() == 1);
        sbDuanxintuisong.setChecked(bean.getSms_tophone() == 1);
    }

    @OnClick({R.id.ll_waijiedianyuandiaodian,
            R.id.ll_huifuwaijiedianyuan,
            R.id.ll_bufangchefang,
            R.id.ll_zhujidianchidianliangdi,
            R.id.ll_chuanganqidianliangdi,
            R.id.ll_wifilianjei,
            R.id.ll_wifiduankai,
            R.id.ll_duanxintuisong})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_waijiedianyuandiaodian:
                params.subType = Constants.PS_POWER_FAIL;
                sbCurrent = sbWaijiedianyuandiaodian;
                break;
            case R.id.ll_huifuwaijiedianyuan:
                params.subType = Constants.PS_POWER_RECOVER;
                sbCurrent = sbHuifuwaijiedianyuan;
                break;
            case R.id.ll_bufangchefang:
                params.subType = Constants.PS_DEFENSE_CHG;
                sbCurrent = sbBufangchefang;
                break;
            case R.id.ll_zhujidianchidianliangdi:
                params.subType = Constants.PS_HOST_POWER_LOW;
                sbCurrent = sbZhujidianchidianliangdi;
                break;
            case R.id.ll_chuanganqidianliangdi:
                params.subType = Constants.PS_SENSOR_POWER_LOW;
                sbCurrent = sbChuanganqidianliangdi;
                break;
            case R.id.ll_wifilianjei:
                params.subType = Constants.PS_WIFI_CONNECT;
                sbCurrent = sbWifilianjei;
                break;
            case R.id.ll_wifiduankai:
                params.subType = Constants.PS_WIFI_DISCONNECT;
                sbCurrent = sbWifiduankai;
                break;
            case R.id.ll_duanxintuisong:
                params.subType = Constants.PS_SMS_TOPHONE;
                sbCurrent = sbDuanxintuisong;
                break;
        }
        //这里之所以这么做是因为不想看到当网络出错的时候，而开关却显示已经切换了，所以为了避免这种情况，在网络访问成功的时候才切换。
        params.val = sbCurrent.isChecked() ? "0" : "1";//是相反的，这里需要注意一下。
        mPresenter.requestSetHost(params);
    }
}

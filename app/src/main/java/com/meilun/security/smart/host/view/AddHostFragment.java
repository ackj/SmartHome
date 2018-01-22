package com.meilun.security.smart.host.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.LbsManager;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.host.contract.AddHostContract;
import com.meilun.security.smart.host.presenter.AddHostPresenter;
import com.meilun.security.smart.location.LoacationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.abase.utils.KeyBoardUtils;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */

public class AddHostFragment extends BaseFragment<AddHostContract.Presenter> implements AddHostContract.View {
    public static final String TAG = AddHostFragment.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_device_code_add_host_fragment)
    EditText etDeviceCode;
    @BindView(R.id.et_name_add_host_fragment)
    EditText etName;
    @BindView(R.id.tv_location_add_host_fragment)
    TextView tvLocation;
    @BindView(R.id.ll_location_add_host_fragment)
    LinearLayout llLocation;
    @BindView(R.id.et_address_add_host_fragment)
    EditText etAddress;
    @BindView(R.id.bt_save_add_host_fragment)
    Button btSave;
    @BindView(R.id.ll_device_code_add_host_fragment)
    LinearLayout llDeviceCode;
    @BindView(R.id.ll_name_add_host_fragment)
    LinearLayout llName;
    private Unbinder unbinder;
    private Params params = Params.getInstance();
    private MainDeviceListBean.DataBean hostBean;

    public static AddHostFragment newInstance(String hostCode, MainDeviceListBean.DataBean hostBean) {
        Bundle args = new Bundle();
        args.putString(Constants.KEY_HOST_NUMBER, hostCode);
        args.putSerializable(Constants.KEY_HOST, hostBean);
        AddHostFragment fragment = new AddHostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            params.no = args.getString(Constants.KEY_HOST_NUMBER);
            hostBean = (MainDeviceListBean.DataBean) args.getSerializable(Constants.KEY_HOST);
        }
    }

    @NonNull
    @Override
    protected AddHostContract.Presenter createPresenter() {
        return new AddHostPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_host, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
        initLocation();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        if (hostBean != null) {
            toolbarTitle.setText("地理位置");
        } else {
            toolbarTitle.setText("添加主机");
        }
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
    }

    private void initData() {
        etDeviceCode.setText(params.no);
        if (hostBean != null) {
            llDeviceCode.setVisibility(View.GONE);
            llName.setVisibility(View.GONE);
            tvLocation.setText(hostBean.getResidence().getAddr());
            etAddress.setText(hostBean.getResidence().getAddrDet());
            params.lng = hostBean.getResidence().getLng();
            params.lat = hostBean.getResidence().getLat();
        }
    }

    private void initLocation() {
        LbsManager.getInstance().startLocation(aMapLocation -> {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                LbsManager.getInstance().stopLocation();
                if (hostBean == null) {
                    tvLocation.setText(aMapLocation.getAddress());
                    params.lng = aMapLocation.getLongitude() + "";
                    params.lat = aMapLocation.getLatitude() + "";
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        KeyBoardUtils.hideKeybord(etAddress, _mActivity);
        unbinder.unbind();
        LbsManager.getInstance().stopLocation();
    }

    @Override
    public void responseAddHost(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
        pop();
    }

    @Override
    public void responseEditHostLocation(BaseBean baseBean) {
        DialogHelper.successSnackbar(getView(), baseBean.getOther().getMessage());
        hostBean.getResidence().setAddr(params.addr);
        hostBean.getResidence().setAddrDet(params.addrDet);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.KEY_HOST, hostBean);
        setFragmentResult(HostSettingsFragment.RESULT_HOST_SETTINGS, bundle);
        pop();
    }

    @OnClick({R.id.ll_location_add_host_fragment, R.id.bt_save_add_host_fragment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_location_add_host_fragment:
                startForResult(LoacationFragment.newInstance(), SupportFragment.RESULT_OK);
                break;
            case R.id.bt_save_add_host_fragment:

                if (hostBean == null) {
                    if (TextUtils.isEmpty(etDeviceCode.getText().toString())) {
                        DialogHelper.warningSnackbar(getView(), "主机编码不能为空！");
                        return;
                    }
                    if (TextUtils.isEmpty(etName.getText().toString())) {
                        DialogHelper.warningSnackbar(getView(), "主机名称不能为空！");
                        return;
                    }
                }

                if (TextUtils.isEmpty(tvLocation.getText().toString())) {
                    DialogHelper.warningSnackbar(getView(), "所在地区不能为空！");
                    return;
                }
                if (TextUtils.isEmpty(etAddress.getText().toString())) {
                    DialogHelper.warningSnackbar(getView(), "详细地址不能为空！");
                    return;
                }
                params.gateway = etDeviceCode.getText().toString().trim();
                params.name = etName.getText().toString();
                params.roomDir = "";
                params.addr = tvLocation.getText().toString();
                params.addrDet = etAddress.getText().toString().trim();
                if (hostBean == null) {
                    mPresenter.requestAddHost(params);
                } else {
                    params.gateway = hostBean.getNo();
                    mPresenter.requestEditHostLocation(params);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (data != null) {
            PoiItem poiItem = data.getParcelable(LoacationFragment.POI);
            if (poiItem != null) {
                StringBuilder sb = new StringBuilder()
                        .append(TextUtils.isEmpty(poiItem.getProvinceName()) ? "" : poiItem.getProvinceName())
                        .append(TextUtils.isEmpty(poiItem.getCityName()) ? "" : poiItem.getCityName())
                        .append(TextUtils.isEmpty(poiItem.getAdName()) ? "" : poiItem.getAdName())
                        .append(TextUtils.isEmpty(poiItem.getSnippet()) ? "" : poiItem.getSnippet())
                        .append(TextUtils.isEmpty(poiItem.getTitle()) ? "" : poiItem.getTitle());

                tvLocation.setText(sb.toString());
                params.lng = poiItem.getLatLonPoint().getLongitude() + "";
                params.lat = poiItem.getLatLonPoint().getLatitude() + "";
            }
        }
    }
}

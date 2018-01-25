package com.meilun.security.smart.host.view;

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
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.AuthorizationBean;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.host.contract.AuthorizationContract;
import com.meilun.security.smart.host.presenter.AuthorizationPresenter;
import com.meilun.security.smart.qrcode.ScanQRCodeFragment;
import com.meilun.security.smart.widget.PtrHTFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.adialog.dialog.BaseDialog;
import cn.itsite.adialog.dialogfragment.SelectorDialogFragment;
import cn.itsite.statemanager.StateManager;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */

public class AuthorizeFragment extends BaseFragment<AuthorizationContract.Presenter> implements AuthorizationContract.View {
    public static final String TAG = AuthorizeFragment.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;
    @BindView(R.id.ptrFrameLayout)
    PtrHTFrameLayout ptrFrameLayout;
    private Unbinder unbinder;
    private AuthorizeAdapter adapter;
    private Params params = Params.getInstance();
    private MainDeviceListBean.DataBean hostBean;
    private StateManager mStateManager;
    private List<String> addHostTypes;

    public static AuthorizeFragment newInstance(MainDeviceListBean.DataBean hostBean) {
        AuthorizeFragment fragment = new AuthorizeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", hostBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected AuthorizationContract.Presenter createPresenter() {
        return new AuthorizationPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        hostBean = (MainDeviceListBean.DataBean) getArguments().getSerializable("bean");
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
        initListener();
        initStateManager();
        initPtrFrameLayout(ptrFrameLayout, recyclerView);
    }

    private void initToolbar() {
        initStateBar(toolbar);
        if (hostBean == null) {
            DialogHelper.warningSnackbar(getView(), "主机为空");
            return;
        }
        toolbarTitle.setText(hostBean.getName());
        toolbarMenu.setText("授权");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
    }

    private void initData() {
        params.gateway = hostBean.getNo();
        adapter = new AuthorizeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(() -> {
            params.page++;
            mPresenter.requestgatewayAuthList(params);
        }, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        params.page = 1;
        params.pageSize = Constants.PAGE_SIZE;
        mPresenter.requestgatewayAuthList(params);
    }

    private void initListener() {
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            switch (view.getId()) {
                case R.id.tv_unbound:
                    new AlertDialog.Builder(_mActivity)
                            .setTitle("提醒")
                            .setMessage("是否解除对该用户授权")
                            .setNegativeButton("否", null)
                            .setPositiveButton("是", (dialog, which) -> {
                                params.fid = adapter.getItem(position).getFid();
                                mPresenter.requestGatewayUnAuth(params);
                            }).show();
                    break;
                default:
            }
        });
    }

    private void initStateManager() {
        mStateManager = StateManager.builder(_mActivity)
                .setContent(recyclerView)
                .setEmptyView(R.layout.state_empty)
                .setEmptyText("还没有主机哦，赶紧添加吧！")
                .setErrorOnClickListener(v -> ptrFrameLayout.autoRefresh())
                .setEmptyOnClickListener(v -> showAddHostSelecotr())
                .setConvertListener((holder, stateLayout) ->
                        holder.setOnClickListener(R.id.bt_empty_state, v -> showAddHostSelecotr())
                                .setText(R.id.bt_empty_state, "点击添加"))
                .build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void responsegatewayAuthList(List<AuthorizationBean.DataBean> data) {
        ptrFrameLayout.refreshComplete();
        if (data == null || data.isEmpty()) {
            if (params.page == 1) {
                mStateManager.showEmpty();
            }
            adapter.loadMoreEnd();
            return;
        }
        if (params.page == 1) {
            mStateManager.showContent();
            adapter.setNewData(data);
            adapter.disableLoadMoreIfNotFullPage(recyclerView);
        } else {
            adapter.addData(data);
            adapter.setEnableLoadMore(true);
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void responseGatewayAuthSuccesst(BaseBean bean) {
        DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
        onRefresh();
    }

    @Override
    public void responseGatewayUnAuthSuccesst(BaseBean bean) {
        DialogHelper.successSnackbar(getView(), bean.getOther().getMessage());
        onRefresh();
    }

    @OnClick(R.id.toolbar_menu)
    public void onViewClicked() {
        new BaseDialog(_mActivity)
                .setLayoutId(R.layout.dialog_add_authorization)//传入你的xml布局。
                .setConvertListener((holder, dialog) -> {
                    //通过ViewHolder对View进行一些定制化。
                    EditText etInputPhone = holder.getView(R.id.et_input_phone);
                    holder.setOnClickListener(R.id.tv_comfirm, v -> {
                        if (TextUtils.isEmpty(etInputPhone.getText().toString())) {
                            DialogHelper.warningSnackbar(getView(), "请输入电话号码");
                            return;
                        }
                        params.gateway = hostBean.getNo();
                        params.mobile = etInputPhone.getText().toString();
                        mPresenter.requestGatewayAuth(params);
                        //确定
                        dialog.dismiss();
                    }).setOnClickListener(R.id.tv_cancel, v -> dialog.dismiss());
                })
                .show();//显示。
    }

    private void showAddHostSelecotr() {
        if (addHostTypes == null) {
            addHostTypes = new ArrayList<>();
            addHostTypes.add(0, "扫码添加");
            addHostTypes.add(1, "手动输入添加");
        }
        new SelectorDialogFragment()
                .setTitle("请选择添加方式")
                .setItemLayoutId(R.layout.item_rv_simple_selector)
                .setData(addHostTypes)
                .setOnItemConvertListener((holder, position, dialog) ->
                        holder.setText(R.id.tv_item_rv_simple_selector, addHostTypes.get(position)))
                .setOnItemClickListener((view, baseViewHolder, position, dialog) -> {
                    dialog.dismiss();
                    switch (position) {
                        case 0:
                            _mActivity.start(ScanQRCodeFragment.newInstance());
                            break;
                        case 1:
                            _mActivity.start(AddHostFragment.newInstance("", null));
                            break;
                    }
                })
                .setAnimStyle(R.style.SlideAnimation)
                .setGravity(Gravity.BOTTOM)
                .show(getChildFragmentManager());
    }


    @Override
    public void error(String errorMessage) {
        super.error(errorMessage);
        ptrFrameLayout.refreshComplete();
        if (params.page == 1) {
//            mStateManager.showError();
        } else if (params.page > 1) {
            adapter.loadMoreFail();
            params.page--;
        }
    }
}

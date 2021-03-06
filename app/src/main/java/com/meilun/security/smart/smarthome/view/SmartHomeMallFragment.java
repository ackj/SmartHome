package com.meilun.security.smart.smarthome.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.FirstLevelBean;
import com.meilun.security.smart.entity.bean.GoodsBean;
import com.meilun.security.smart.entity.bean.SubCategoryBean;
import com.meilun.security.smart.smarthome.contract.SmartHomeMallContract;
import com.meilun.security.smart.smarthome.presenter.SmartHomeMallPresenter;
import com.meilun.security.smart.web.WebActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.mvp.view.base.BaseFragment;

/**
 * Author: LiuJia on 2017/5/22 0022 08:58.
 * Email: liujia95me@126.com
 * [智能家居商城]的View层
 * 打开方式：StartApp-->管家-->智能家居商城
 */

public class SmartHomeMallFragment extends BaseFragment<SmartHomeMallContract.Presenter> implements SmartHomeMallContract.View {
    public static final String TAG = SmartHomeMallFragment.class.getSimpleName();

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView_menu)
    RecyclerView recyclerViewMenu;
    @BindView(R.id.recyclerView_commodity)
    RecyclerView recyclerViewGoods;

    private Unbinder unbinder;
    private Params params = Params.getInstance();
    private SmartHomeMenuRVAdapter menuAdapter;
    private SmartHomeGoodsRVAdapter goodsAdapter;
    private List<SubCategoryBean.DataBean> data;
    private int position;

    public static SmartHomeMallFragment newInstance(List<SubCategoryBean.DataBean> data, int position) {
        SmartHomeMallFragment fragment = new SmartHomeMallFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) data);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * SmartHomeMallFragment 的创建入口
     *
     * @param id 一级菜单项的id，用于获取二级菜单的数据
     * @return
     */
    public static SmartHomeMallFragment newInstance(String id) {
        SmartHomeMallFragment fragment = new SmartHomeMallFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_ID, id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    protected SmartHomeMallContract.Presenter createPresenter() {
        return new SmartHomeMallPresenter(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        params.id = getArguments().getString(Constants.KEY_ID);
        data = getArguments().getParcelableArrayList("data");
        position = getArguments().getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_smart_home_mall, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initData();
        initListener();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText("智能家居商城");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> {
            onBackPressedSupport();
            _mActivity.onBackPressedSupport();
        });
    }

    private void initData() {
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(_mActivity));
        menuAdapter = new SmartHomeMenuRVAdapter();
        recyclerViewMenu.setAdapter(menuAdapter);

        recyclerViewGoods.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        goodsAdapter = new SmartHomeGoodsRVAdapter();
        recyclerViewGoods.setAdapter(goodsAdapter);

        if (data != null && data.size() > 0) {
            responseSubCategoryList(data);
        } else {
            mPresenter.requestFirstLevel(params);
        }
    }

    private void initListener() {
        menuAdapter.setOnItemClickListener((adapter, view, position) -> {
            SubCategoryBean.DataBean bean = (SubCategoryBean.DataBean) adapter.getData().get(position);
            menuAdapter.setSelectItem(bean);
            params.secondCategoryId = bean.getId();
            mPresenter.requestGoodsList(params);//点击一个类别后请求商品列表
        });

        goodsAdapter.setOnItemClickListener((adapter, view, position) -> {
            GoodsBean.DataBean bean = (GoodsBean.DataBean) adapter.getData().get(position);
            Intent intent = new Intent(_mActivity, WebActivity.class);
            intent.putExtra(Constants.KEY_TITLE, bean.getName());
            intent.putExtra(Constants.KEY_LINK, bean.getLink()+String.format("&token=%1$s&fromPoint=%2$s&appType=2",Params.token,Constants.fromPoint));
            _mActivity.startActivity(intent);//点击一个商品跳WEB
        });
    }

    @Override
    public void responseFirstLevel(List<FirstLevelBean.DataBean> datas) {
        if (datas.size() > 0) {
            for (FirstLevelBean.DataBean bean : datas){
                if("智能家居".equals(bean.getName())){
                    params.id =  bean.getId();
                    mPresenter.requestSubCategoryList(params);
                    break;
                }
            }
        }
    }

    /**
     * 响应请求商品类型（左侧列表）
     *
     * @param datas
     */
    @Override
    public void responseSubCategoryList(List<SubCategoryBean.DataBean> datas) {
        menuAdapter.setNewData(datas);
        if (datas.size() > 0) {
            SubCategoryBean.DataBean bean = datas.get(position);
            menuAdapter.setSelectItem(bean);
            params.secondCategoryId = bean.getId();
            mPresenter.requestGoodsList(params);
        }
    }

    /**
     * 响应请求商品列表（右侧列表）
     *
     * @param datas
     */
    @Override
    public void responseGoodsList(List<GoodsBean.DataBean> datas) {
        goodsAdapter.setNewData(datas);
    }

    @Override
    public void start(Object response) {

    }

    @Override
    public void error(String errorMessage) {
        DialogHelper.warningSnackbar(getView(), errorMessage);
    }

    @Override
    public boolean onBackPressedSupport() {
        setFragmentResult(RESULT_OK, null);
        return super.onBackPressedSupport();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

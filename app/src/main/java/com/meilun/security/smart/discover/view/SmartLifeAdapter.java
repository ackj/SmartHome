package com.meilun.security.smart.discover.view;

import android.support.v7.widget.CardView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.entity.bean.SubCategoryBean;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Author: LiuJia on 2017/9/11 0011 10:53.
 * Email: liujia95me@126.com
 */

public class SmartLifeAdapter extends BaseRecyclerViewAdapter<SubCategoryBean.DataBean, BaseViewHolder> {
    public static final String TAG = SmartLifeAdapter.class.getSimpleName();

    public SmartLifeAdapter(List<SubCategoryBean.DataBean> wisdomLife) {
        super(R.layout.item_discover_fragment_rv_smart_life, wisdomLife);
    }

    @Override
    protected void convert(BaseViewHolder helper, SubCategoryBean.DataBean item) {
        helper.setText(R.id.tv_name_item_smart_life, item.getName());
        CardView frameLayout = helper.getView(R.id.cv_smart_life);
        switch (helper.getLayoutPosition()) {
            case 0:
                frameLayout.setBackgroundResource(R.drawable.bg_a01_420px_300px);
                break;
            case 1:
                frameLayout.setBackgroundResource(R.drawable.bg_mensuo_420px_300px);
                break;
            case 2:
                frameLayout.setBackgroundResource(R.drawable.bg_peijian_420px_300px);
                break;
            default:
        }
    }
}

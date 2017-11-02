package com.aglhz.s1.host.view;

import com.aglhz.s1.R;
import com.aglhz.s1.entity.bean.AuthorizationBean;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Created by leguang on 2017/6/22 0022.
 * Email：langmanleguang@qq.com
 */

public class AuthorizeAdapter extends BaseRecyclerViewAdapter<AuthorizationBean.DataBean,BaseViewHolder> {

    public AuthorizeAdapter() {
        super(R.layout.item_authorization);
    }

    @Override
    protected void convert(BaseViewHolder helper, AuthorizationBean.DataBean item) {
        helper.setText(R.id.tv_phone,item.getMobile())
                .addOnClickListener(R.id.tv_unbound);
    }
}

package com.meilun.security.smart.linkage.view;

import com.meilun.security.smart.R;
import com.meilun.security.smart.entity.bean.LinkageBean;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Author: leguang on 2017/5/31 0031 09:50.
 * Email: langmanleguang@qq.com
 */

public class LinkageRVAdapter extends BaseRecyclerViewAdapter<LinkageBean.DataBean, BaseViewHolder> {

    public LinkageRVAdapter() {
        super(R.layout.item_rv_linkage);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkageBean.DataBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .addOnClickListener(R.id.tv_delete)
                .setChecked(R.id.switch_button, item.getStatus() == 1)
                .addOnClickListener(R.id.ll_item_intelligence_linkage)
                .addOnClickListener(R.id.switch_button);
//        helper.setText(R.id.tv_scene_item_scene, item.scene)
//                .addOnClickListener(R.id.ll_item_intelligence_linkage)
//                .addOnClickListener(R.id.tv_delete_item_scene);
    }
}

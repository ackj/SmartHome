package com.meilun.security.smart.room.view;

import com.chad.library.adapter.base.BaseViewHolder;
import com.meilun.security.smart.R;
import com.meilun.security.smart.entity.bean.MainDeviceBean;

import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Author: LiuJia on 2018/1/3 0003 10:05.
 * Email: liujia95me@126.com
 */

public class DeviceListItemRVAdapter extends BaseRecyclerViewAdapter<MainDeviceBean, BaseViewHolder> {
    public DeviceListItemRVAdapter() {
        super(R.layout.item_item_cateye);
    }

    @Override
    protected void convert(BaseViewHolder helper, MainDeviceBean item) {
        helper.setText(R.id.tv_name, item.name)
                .setText(R.id.tv_id, item.deviceId)
                .setImageResource(R.id.iv_image,item.cateyeBean==null?R.drawable.ic_jiankong_220px:R.drawable.ic_equipment_cateye_220px);
    }
}

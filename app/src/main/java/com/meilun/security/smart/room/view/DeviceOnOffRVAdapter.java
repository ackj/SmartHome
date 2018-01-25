package com.meilun.security.smart.room.view;

import com.meilun.security.smart.R;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Author: LiuJia on 2017/8/30 0030 09:50.
 * Email: liujia95me@126.com
 */

public class DeviceOnOffRVAdapter extends BaseRecyclerViewAdapter<Integer, BaseViewHolder> {

    public DeviceOnOffRVAdapter() {
        super(R.layout.item_device_on_off_2);
    }

    private boolean isCurtains = false;

    public void setIsCurtains(boolean isCurtains) {
        this.isCurtains = isCurtains;
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_way_no, isCurtains ? "窗帘控制按钮" : "第 " + item + " 路")
                .setVisible(R.id.ll_stop, isCurtains)
                .addOnClickListener(R.id.ll_stop)
                .addOnClickListener(R.id.ll_open)
                .addOnClickListener(R.id.ll_close);

    }
}

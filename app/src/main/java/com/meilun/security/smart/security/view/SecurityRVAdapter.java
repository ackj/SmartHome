package com.meilun.security.smart.security.view;

import android.text.TextUtils;
import android.widget.ImageView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.entity.bean.SecurityBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.itsite.abase.BaseApp;
import cn.itsite.abase.mvp.view.base.BaseRecyclerViewAdapter;

/**
 * Author: LiuJia on 2017/8/14 0014 15:48.
 * Email: liujia95me@126.com
 */
public class SecurityRVAdapter extends BaseRecyclerViewAdapter<SecurityBean.DataBean.SubDevicesBean, BaseViewHolder> {

    public SecurityRVAdapter() {
        super(R.layout.item_security);
    }

    private String dstatus = Constants.GATEWAY_STATE_CANCLE;

    //更新主机状态
    public void setHostState(String dstatus) {
        this.dstatus = dstatus;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, SecurityBean.DataBean.SubDevicesBean item) {
        //默认防御状态
        int pointState = R.drawable.ic_state_green_24px;
        if(!TextUtils.isEmpty(item.getWorkStatus())){
            switch (item.getWorkStatus()) {
                case "defense_off":
                    pointState = R.drawable.ic_state_orange_24px;
                    break;
                case "line_off":
                    pointState = R.drawable.ic_state_red_24px;
                    break;
                case "not_found":
                    pointState = R.drawable.ic_state_gray_24px;
                    break;
                default:
            }
        }

        helper.setText(R.id.tv_name_item_security, item.getName())
                .setVisible(R.id.iv_state, !"add_icon".equals(item.getIcon()))
                .setImageResource(R.id.iv_state, pointState);
        ImageView ivSecurity = helper.getView(R.id.iv_icon_item_security);
        Glide.with(BaseApp.mContext)
                .load("add_icon".equals(item.getIcon()) ? R.drawable.ic_add_security_140px : item.getIcon())
                .error(R.mipmap.ic_launcher)
                .into(ivSecurity);
    }
}

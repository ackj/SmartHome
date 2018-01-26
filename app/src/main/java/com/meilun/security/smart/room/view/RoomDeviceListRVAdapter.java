package com.meilun.security.smart.room.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.meilun.security.smart.App;
import com.meilun.security.smart.R;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.entity.bean.DeviceOnOffBean;
import com.meilun.security.smart.event.OnDeviceOnOffListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.itsite.abase.BaseApp;

/**
 * Author： Administrator on 2017/8/18 0018.
 * Email： liujia95me@126.com
 */
public class RoomDeviceListRVAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    public static final int TYPE_DEVICE = 100;
    public static final int TYPE_ON_OFF = 101;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RoomDeviceListRVAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_DEVICE, R.layout.item_device);
        addItemType(TYPE_ON_OFF, R.layout.item_device_on_off_container);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_DEVICE:
                final DeviceListBean.DataBean.SubDevicesBean bean = (DeviceListBean.DataBean.SubDevicesBean) item;
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = helper.getAdapterPosition();
//                        if (bean.isExpanded()) {
//                            collapse(pos);
//                        } else {
//                            expand(pos);
//                        }
                    }
                });
                helper.addOnClickListener(R.id.iv_setting)
                        .setText(R.id.tv_device_name, bean.getName());
                ImageView iv = helper.getView(R.id.iv_icon);
                Glide.with(App.mContext)
                        .load(bean.getIcon())
                        .into(iv);
                break;
            case TYPE_ON_OFF:
                DeviceOnOffBean onOff = (DeviceOnOffBean) item;
                LinearLayout llContainer = helper.getView(R.id.ll_container);
                llContainer.removeAllViews();
                for (int i = 0; i < onOff.node; i++) {
                    View view = LayoutInflater.from(BaseApp.mContext).inflate(R.layout.item_device_on_off, null);
                    view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                    llContainer.addView(view);
                    int finalI = i;
                    view.findViewById(R.id.iv_switch).setOnClickListener(v -> listener.onOff(onOff.deviceIndex, finalI, 1));
                    TextView tvIndex = (TextView) view.findViewById(R.id.tv_index);
                    tvIndex.setText("第" + (i + 1) + "路");
                }
                break;
            default:
        }
    }

    private OnDeviceOnOffListener listener;

    public void onOff(OnDeviceOnOffListener listener) {
        this.listener = listener;
    }
}

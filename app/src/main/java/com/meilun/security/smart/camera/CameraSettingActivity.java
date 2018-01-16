package com.meilun.security.smart.camera;

import android.os.Bundle;

import com.meilun.security.smart.R;
import com.meilun.security.smart.entity.bean.MainDeviceBean;

import cn.itsite.abase.mvp.view.base.BaseActivity;

/**
 * Author: LiuJia on 2017/11/16 0016 09:00.
 * Email: liujia95me@126.com
 */

public class CameraSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            MainDeviceBean bean = (MainDeviceBean) getIntent().getSerializableExtra("bean");
            loadRootFragment(R.id.fl_main_activity, CameraSettingFragment.newInstance(bean));
        }
    }

//    @Override
//    public boolean swipeBackPriority() {
//        return false;
//    }
}

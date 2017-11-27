package com.meilun.security.smart.launch;

import android.os.Bundle;

import com.meilun.security.smart.R;
import com.meilun.security.smart.launch.view.SplashFragment;

import cn.itsite.abase.mvp.view.base.BaseActivity;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */
public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_launch_activity, SplashFragment.newInstance());
        }
    }
}

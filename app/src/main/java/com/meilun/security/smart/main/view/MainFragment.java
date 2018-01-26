package com.meilun.security.smart.main.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.gson.Gson;
import com.meilun.security.smart.App;
import com.meilun.security.smart.R;
import com.meilun.security.smart.common.ApiService;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.common.appupdate.UpdateAppHttpUtils;
import com.meilun.security.smart.discover.view.DiscoverFragment;
import com.meilun.security.smart.entity.bean.AppUpdateBean;
import com.meilun.security.smart.more.view.MoreFragment;
import com.meilun.security.smart.net.NetActivity;
import com.meilun.security.smart.net.view.SetWifiFragment;
import com.meilun.security.smart.room.view.DeviceListFragment;
import com.meilun.security.smart.scene.view.SceneFragment;
import com.meilun.security.smart.security.view.Security2Fragment;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.itsite.abase.common.ActivityManager;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import cn.itsite.abase.utils.AppUtils;
import cn.itsite.abase.utils.ToastUtils;
import me.yokeyword.fragmentation.SupportFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Author: LiuJia on 2017/4/27 0027 11:12.
 * Email: liujia95me@126.com
 */

public class MainFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    public static final String TAG = MainFragment.class.getSimpleName();
    private static final long WAIT_TIME = 2000L;// 再点一次退出程序时间设置
    private static final int CAMERA_LOCATION = 100;
    private long TOUCH_TIME = 0;
    private AHBottomNavigation ahbn;
    private SupportFragment[] fragments = new SupportFragment[5];
    private int prePosition;
    private static final String KEY_CURR_POSITION = "prePosition";

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ahbn = (AHBottomNavigation) view.findViewById(R.id.ahbn_main_fragment);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState == null) {
            fragments[0] = DiscoverFragment.newInstance();
            fragments[1] = Security2Fragment.newInstance();
            fragments[2] = DeviceListFragment.newInstance();
            fragments[3] = SceneFragment.newInstance();
            fragments[4] = MoreFragment.newInstance();
            loadMultipleRootFragment(R.id.fl_container_main_fragment, 0, fragments[0], fragments[1], fragments[2], fragments[3], fragments[4]);
        } else {
            fragments[0] = findChildFragment(DiscoverFragment.class);
            fragments[1] = findChildFragment(Security2Fragment.class);
            fragments[2] = findChildFragment(DeviceListFragment.class);
            fragments[3] = findChildFragment(SceneFragment.class);
            fragments[4] = findChildFragment(MoreFragment.class);
            prePosition = savedInstanceState.getInt(KEY_CURR_POSITION);
        }
        initData();
        requiresPermissions();
    }


    private void initData() {
//        updateApp();//检测App的更新。

        AHBottomNavigationItem item0 = new AHBottomNavigationItem(R.string.discover, R.drawable.ic_find_black_78px, R.color.white);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.security, R.drawable.ic_navigationsecurity_black_78px, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.control, R.drawable.ic_navigationroom_black_78px, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.scene, R.drawable.ic_navigationscenes_black_78px, R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.more, R.drawable.ic_navigationmore_black_78px, R.color.white);
        List<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
        bottomNavigationItems.add(item0);
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);
        ahbn.addItems(bottomNavigationItems);
        ahbn.setDefaultBackgroundColor(getResources().getColor(R.color.white));
        ahbn.setBehaviorTranslationEnabled(false);
        ahbn.setColored(true);
        ahbn.setForceTint(false);
        ahbn.setAccentColor(getResources().getColor(R.color.base_color));
        ahbn.setInactiveColor(getResources().getColor(R.color.black));
        ahbn.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ahbn.setOnTabSelectedListener((position, wasSelected) -> {
            showHideFragment(fragments[position], fragments[prePosition]);
            prePosition = position;
            return true;
        });
        ahbn.setCurrentItem(prePosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CURR_POSITION, prePosition);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            //退到桌面，而不是退出应用，让用户以为退出应用，尽量保活。
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            ToastUtils.showToast(App.mContext, Constants.PRESS_AGAIN);
        }
        return true;
    }

    /**
     * 检测是否有新版本需要下载更新。
     */
    private void updateApp() {
        String random = System.currentTimeMillis() + "";
        String accessKey = Constants.SYS_ACCESS_PREFIX + random + Constants.SYS_ACCESS_KEY;
        ALog.e("random-->" + random);
        ALog.e("accessKey-->" + accessKey);
        ALog.e("getMd5(accessKey)-->" + getMd5(accessKey));

        Map<String, String> params = new HashMap<>();
        params.put("accessKey", getMd5(accessKey));
        params.put("random", random);
        params.put("sc", Constants.SC);
        params.put("appType", Constants.APP_TYPE);

        new UpdateAppManager
                .Builder()
                .setActivity(_mActivity)
                .setHttpManager(new UpdateAppHttpUtils())
                .setPost(true)
                .setParams(params)
                .setUpdateUrl(ApiService.requestAppUpdatae)
                .hideDialogOnDownloading(false)
                .build()
                .checkNewApp(new UpdateCallback() {
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        ALog.e("requestAppUpdatae-->" + json);
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        AppUpdateBean mAppUpdateBean = new Gson().fromJson(json, AppUpdateBean.class);

                        updateAppBean
                                //（必须）是否更新Yes,No
                                .setUpdate(AppUtils.getVersionCode(_mActivity) < mAppUpdateBean.getData().getVersionCode() ? "Yes" : "No")
                                //（必须）新版本号，
                                .setNewVersion(mAppUpdateBean.getData().getVersionName())
                                //（必须）下载地址
                                .setApkFileUrl(mAppUpdateBean.getData().getUrl())
                                //（必须）更新内容
                                .setUpdateLog(mAppUpdateBean.getData().getDescription())
                                //大小，不设置不显示大小，可以不设置
                                .setTargetSize(mAppUpdateBean.getData().getSize())
                                //是否强制更新，可以不设置
                                .setConstraint(mAppUpdateBean.getData().isIsForce());

                        return updateAppBean;
                    }

                    @Override
                    protected void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
                        updateAppManager.showDialogFragment();
                    }
                });
    }

    @AfterPermissionGranted(CAMERA_LOCATION)
    private void requiresPermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(_mActivity, perms)) {
        } else {
            EasyPermissions.requestPermissions(this, "需要定位权限", CAMERA_LOCATION, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //这里需要重新设置Rationale和title，否则默认是英文格式
        new AppSettingsDialog.Builder(this)
                .setRationale("没有该权限，此应用程序可能无法正常工作。打开应用设置屏幕以修改应用权限")
                .setTitle("必需权限")
                .build()
                .show();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        WifiManager wifiManager = (WifiManager) App.mContext.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo.getSSID().contains(SetWifiFragment.WIFI_NAME)) {
            startActivity(new Intent(_mActivity, NetActivity.class));
        } else {
            ActivityManager.getInstance().finishActivity(NetActivity.class);
        }
    }

    private String getMd5(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            //获取加密方式为md5的算法对象
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(str.getBytes());
            for (byte b : digest) {
                String hexString = Integer.toHexString(b & 0xff);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb = sb.append(hexString);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}

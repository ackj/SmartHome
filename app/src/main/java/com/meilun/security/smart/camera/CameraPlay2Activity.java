package com.meilun.security.smart.camera;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meilun.security.smart.R;
import com.meilun.security.smart.camera.contract.CameraSettingContract;
import com.meilun.security.smart.camera.presenter.CameraSettingPresenter;
import com.meilun.security.smart.common.Params;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.MainDeviceBean;
import com.meilun.security.smart.event.EventCameraListRefresh;
import com.meilun.security.smart.utils.CameraHelper;
import com.p2p.core.BaseMonitorActivity;
import com.p2p.core.P2PHandler;
import com.p2p.core.P2PValue;
import com.p2p.core.P2PView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.utils.ToastUtils;
import cn.itsite.adialog.dialogfragment.BaseDialogFragment;

/**
 * Author: LiuJia on 2017/11/9 0009 11:16.
 * Email: liujia95me@126.com
 */

public class CameraPlay2Activity extends BaseMonitorActivity implements CameraSettingContract.View {

    private static final String TAG = CameraPlay2Activity.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_red_pointer)
    View viewRECPointer;
    @BindView(R.id.ll_rec)
    LinearLayout llREC;

    public static String P2P_ACCEPT = "com.meilun.security.smart.P2P_ACCEPT";
    public static String P2P_READY = "com.meilun.security.smart.P2P_READY";
    public static String P2P_REJECT = "com.meilun.security.smart.P2P_REJECT";
    private static final int VIDEO_MODE_SD = 0;
    private static final int VIDEO_MODE_HD = 1;
    private static final int VIDEO_MODE_LD = 2;

    @BindView(R.id.iv_microphone)
    ImageView ivMicrophone;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.iv_photograph)
    ImageView ivPhotograph;
    @BindView(R.id.tv_quality)
    TextView tvQuality;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;

    private MainDeviceBean cameraBean;
    private String cameraUserId;
    private String cameraPassword;
    private String cameraCallId;
    private String pathName;//录像保存地址
    private Params params = Params.getInstance();
    private CameraSettingPresenter presenter = new CameraSettingPresenter(this);
    private String[] qualityArr = {"标清", "高清", "流畅"};

    //辅助变量
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_play2);
        ButterKnife.bind(this);
        //7是设备类型(技威定义的)
        pView = (P2PView) findViewById(R.id.p2pview);
        initP2PView(7, P2PView.LAYOUTTYPE_TOGGEDER);
        initToolbar();
        initAnimator();
        initData();
        initListener();
    }

    private void initListener() {
        ivMicrophone.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ivMicrophone.setSelected(true);
                    setMute(false);
                    return true;
                case MotionEvent.ACTION_UP:
                    ivMicrophone.setSelected(false);
                    setMute(true);
                    return true;
                default:
            }
            return false;
        });
    }

    private void initToolbar() {
        toolbarTitle.setText("智能监控");
//        toolbarMenu.setText("设置");
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        registerCameraReceiver();
        //获取数据
        cameraBean = (MainDeviceBean) getIntent().getSerializableExtra("bean");
        SharedPreferences sp = getSharedPreferences("Account", MODE_PRIVATE);
        cameraUserId = sp.getString("userId", "");
        cameraPassword = P2PHandler.getInstance().EntryPassword(cameraBean.userPwd);
        cameraCallId = cameraBean.deviceId;

        ALog.e(TAG, "id:" + cameraCallId + " -- password:" + cameraBean.userPwd + " -- userId:" + cameraUserId + " -- pwd:" + cameraPassword);
        //首次连接
        connectDevice();
        loadingShow();
    }

    private void registerCameraReceiver() {
        ALog.e(TAG, "注册广播");
        //注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(P2P_REJECT);
        filter.addAction(P2P_ACCEPT);
        filter.addAction(P2P_READY);
        registerReceiver(mReceiver, filter);
    }

    private void initAnimator() {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(500);
        animation.setRepeatMode(ValueAnimator.INFINITE);
        animation.setRepeatCount(-1);
        viewRECPointer.startAnimation(animation);
    }

    private void loadingShow() {
        isLoading = true;
        tvLoading.setText("加载中...");
        progressBar.setVisibility(View.VISIBLE);
        llLoading.setVisibility(View.VISIBLE);
        setControlEnable(false);
    }

    private void loadingError(String message) {
        isLoading = false;
        progressBar.setVisibility(View.GONE);
        tvLoading.setText(message + "\n点击重试");
        llLoading.setVisibility(View.VISIBLE);
        setControlEnable(false);
    }

    private void loadingSuccess() {
        isLoading = false;
        tvLoading.setText("加载成功");
        llLoading.setVisibility(View.GONE);
        setControlEnable(true);
    }

    //控制按钮
    private void setControlEnable(boolean enable) {
        ivMute.setEnabled(enable);
        ivVideo.setEnabled(enable);
        ivMicrophone.setEnabled(enable);
        ivPhotograph.setEnabled(enable);
        tvQuality.setEnabled(enable);
    }

    private void connectDevice() {
        ALog.e(TAG, "cameraUserId:" + cameraUserId + " cameraPassword:" + cameraPassword);
        boolean call = P2PHandler.getInstance().call(cameraUserId, cameraPassword, true, 1, cameraCallId, "", "", 2, cameraCallId);
        if (!call) {
            CameraHelper.cameraLogin();
        }
        ALog.e(TAG, "正在连接:" + call);
        loadingShow();
    }

    @OnClick({R.id.ll_loading, R.id.iv_mute, R.id.iv_video, R.id.iv_photograph, R.id.tv_quality, R.id.toolbar_menu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_loading://重新加载
                if (!isLoading) {
                    connectDevice();
                }
                break;
            case R.id.iv_mute://静音
                clickMute();
                break;
            case R.id.iv_video://录像
                clickVideo();
                break;
            case R.id.iv_photograph://拍照
                clickPhotograph();
                break;
            case R.id.tv_quality:
                clickQuality();
                break;
            case R.id.toolbar_menu:
                Intent intent = new Intent(this, CameraSettingActivity.class);
                intent.putExtra("bean", (Serializable) cameraBean);
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    //控制画面画质
    private void clickQuality() {
        new AlertDialog.Builder(this)
                .setItems(qualityArr, (dialog, which) -> {
                    switch (which) {
                        case VIDEO_MODE_SD:
                            P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_SD);
                            break;
                        case VIDEO_MODE_HD:
                            P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_HD);
                            break;
                        case VIDEO_MODE_LD:
                            P2PHandler.getInstance().setVideoMode(P2PValue.VideoMode.VIDEO_MODE_LD);
                            break;
                        default:
                    }
                    tvQuality.setText(qualityArr[which]);
                }).show();
    }

    private void clickMute() {
        AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (manager != null) {
            if (ivMute.isSelected()) {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
                ivMute.setSelected(false);
            } else {
                manager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                ivMute.setSelected(true);
            }
        }
    }

    private void clickVideo() {
        if (ivVideo.isSelected()) {
            //停止录像
            stopMoniterReocding();
            ivVideo.setSelected(false);
            llREC.setVisibility(View.GONE);
        } else {
            //开始录像
            startMoniterRecoding();
            ivVideo.setSelected(true);
            llREC.setVisibility(View.VISIBLE);
        }
    }

    private void clickPhotograph() {
        int d = P2PHandler.getInstance().setScreenShotpath(Environment.getExternalStorageDirectory().getPath(), System.currentTimeMillis() + ".jpg");
        ALog.e(TAG, "clickPhotograph：" + d);
        captureScreen(-1);
    }

    private void showUpdatePasswordDialog() {
        new BaseDialogFragment()
                .setLayoutId(R.layout.dialog_add_authorization)
                .setConvertListener((holder, dialog) -> {
                    EditText etInput = holder.getView(R.id.et_input_phone);
                    holder.setText(R.id.tv_title, "密码错误，请输入正确密码")
                            .setOnClickListener(R.id.tv_cancel, v -> {
                                dialog.dismiss();
                                finish();
                            })
                            .setOnClickListener(R.id.tv_comfirm, v -> {
                                String result = etInput.getText().toString().trim();
                                if (TextUtils.isEmpty(result)) {
                                    DialogHelper.warningSnackbar(toolbar, "请输入内容");
                                } else {
//                                    params.index = cameraBean.getIndex();
//                                    params.deviceName = cameraBean.getName();
//                                    params.deviceType = cameraBean.getDeviceType();
                                    params.devicePassword = result;
                                    params.deviceId = cameraBean.deviceId;
                                    presenter.requestModCamera(params);
                                    dialog.dismiss();
                                }
                            });
                })
                .setMargin(40)
                .setDimAmount(0.3f)
                .setGravity(Gravity.CENTER)
                .show(getSupportFragmentManager());
    }

    //开始录像
    public void startMoniterRecoding() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                    Environment.getExternalStorageState().equals(Environment.MEDIA_SHARED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + File.separator + "gwellvideorec" + File.separator + cameraCallId;
                File file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }
                long time = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());// 制定日期的显示格式
                String name = "gwell" + "_" + sdf.format(new Date(time));
                pathName = file.getPath() + File.separator + name + ".mp4";
            } else {
                throw new NoSuchFieldException("sd卡");
            }
        } catch (NoSuchFieldException | NullPointerException e) {
            ToastUtils.showToast(this, " 没有内存卡");
            e.printStackTrace();
        }
        ALog.e(TAG, "pathName:" + pathName);
        if (P2PHandler.getInstance().starRecoding(pathName)) {
            ToastUtils.showToast(this, " 正在录像");
        } else {
            //录像初始化失败
            ToastUtils.showToast(this, " 初始化录像失败");
        }
    }

    //停止录像
    public void stopMoniterReocding() {
        if (P2PHandler.getInstance().stopRecoding() == 0) {
            //录像不正常
            ToastUtils.showToast(CameraPlay2Activity.this, " 视频片段时间太短了");
        } else {
            //正常停止
            ToastUtils.showToast(CameraPlay2Activity.this, " 停止录像，已保存到" + pathName);
        }
    }

    //todo(高亮------------------------- 生命周期 ----------------------------
    @Override
    protected void onRestart() {
        super.onRestart();
        connectDevice();
        registerCameraReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.e(TAG, "解除广播");
        P2PHandler.getInstance().finish();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroy() {
//        P2PHandler.getInstance().finish();
        super.onDestroy();
    }

    //todo(高亮) ------------------------- 以下是设备的广播接收 ----------------------------

    /**
     * reson_code相关参阅：http://doc.cloud-links.net/SDK/Android/P2PCore/com/p2p/core/global/P2PConstants.HangupCode.html
     */
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ALog.e(TAG, "onReceive action:" + intent.getAction());
            if (intent.getAction().equals(P2P_ACCEPT)) {
                int[] type = intent.getIntArrayExtra("type");
                P2PView.type = type[0];
                P2PView.scale = type[1];
                ALog.e(TAG, "监控数据接收 type:" + P2PView.type + " scale:" + P2PView.scale);
                ALog.e(TAG, "监控数据接收:");
                P2PHandler.getInstance().openAudioAndStartPlaying(1);//打开音频并准备播放，calllType与call时type一致
            } else if (intent.getAction().equals(P2P_READY)) {
                ALog.e(TAG, "监控准备,开始监控");
                pView.sendStartBrod();
                loadingSuccess();
            } else if (intent.getAction().equals(P2P_REJECT)) {
                int reason_code = intent.getIntExtra("reason_code", -1);
                int code1 = intent.getIntExtra("exCode1", -1);//806363145
                int code2 = intent.getIntExtra("exCode2", -1);
                ALog.e(TAG, String.format("监控挂断(reson:%d,code1:%d,code2:%d)", reason_code, code1, code2));

                //分两类：1类是有用的信息(给出对应提示)，1类是与用户无关信息(只提示失败)
                String reasonMessage = "";
                switch (reason_code) {
                    case 0: // 密码错误(password_incrrect)
                        reasonMessage = "密码错误";
                        showUpdatePasswordDialog();
                        break;
                    case 1: //忙线(busy)
                        reasonMessage = "设备忙线";
                        break;
                    case 6://离线(offline)
                        reasonMessage = "设备离线";
                        break;
                    case 10://连接超时(timeout)
                        reasonMessage = "连接超时";
                        break;
                    case 9://设备挂断(hungup)
                        reasonMessage = "设备挂断";
                        break;
                    case 2://无原因(none)
                    case 3://userid不可用(id_disabled)
                    case 4://userid过期(id_overdate)
                    case 5://userid不可用(id_inactived)
                    case 7://无(powerdown)
                    case 8://无(nohelper)
                    case 11://无(nobody)
                    case 12://内部错误(internal_error)
                    case 13://链接失败(conn_fail)
                    case 14://设备不支持(not_support)
                    case 15://无视频帧(noframe)
                    default:
                        reasonMessage = "连接失败";
                        break;
                }
                loadingError(reasonMessage);
            }
        }
    };

    //todo(高亮) ------------------------- 以下是网络回调 ----------------------------

    @Override
    public void start(Object response) {

    }

    @Override
    public void error(String errorMessage) {
        loadingError("修改密码失败");
    }

    @Override
    public void complete(Object response) {

    }

    @Override
    public void responseSuccess(BaseBean baseBean) {
        ToastUtils.showToast(this, "修改密码成功");
        cameraBean.userPwd = params.devicePassword;
        EventBus.getDefault().post(new EventCameraListRefresh());
        cameraPassword = P2PHandler.getInstance().EntryPassword(params.devicePassword);
        connectDevice();
    }

    //todo(高亮) ------------------------- 以下是设备重载方法 ----------------------------

    @Override
    protected void onP2PViewSingleTap() {

    }

    @Override
    protected void onP2PViewFilling() {

    }

    @Override
    protected void onCaptureScreenResult(boolean isSuccess, int prePoint) {
        ToastUtils.showToast(this, isSuccess ? "截图成功，照片已保存到系统相册" : "截图失败");
    }

    @Override
    protected void onVideoPTS(long videoPTS) {

    }

    @Override
    public int getActivityInfo() {
        return 0;
    }

    @Override
    protected void onGoBack() {

    }

    @Override
    protected void onGoFront() {

    }

    @Override
    protected void onExit() {

    }

}

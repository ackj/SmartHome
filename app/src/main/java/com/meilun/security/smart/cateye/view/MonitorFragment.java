package com.meilun.security.smart.cateye.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.Player.Core.PlayerCore;
import com.Player.Source.LogOut;
import com.Player.Source.SDKError;
import com.Player.Source.TAlarmFrame;
import com.alibaba.fastjson.JSON;
import com.meilun.security.smart.App;
import com.meilun.security.smart.R;
import com.meilun.security.smart.cateye.presenter.MonitorPresenter;
import com.meilun.security.smart.common.Constants;
import com.meilun.security.smart.entity.bean.MainDeviceBean;
import com.meilun.security.smart.entity.bean.ResultBean;
import com.meilun.security.smart.event.EventRefreshDeviceList;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.itsite.abase.common.DialogHelper;
import cn.itsite.abase.common.RxManager;
import cn.itsite.abase.log.ALog;
import cn.itsite.abase.mvp.view.base.BaseFragment;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.itsite.abase.BaseApp.mContext;

/**
 * Author: LiuJia on 2017/5/4 0004 15:38.
 * Email: liujia95me@126.com
 */

public class MonitorFragment extends BaseFragment<MonitorPresenter> {
    public static final String TAG = MonitorFragment.class.getSimpleName();
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_video_monitoring_fragment)
    ImageView ivVideo;
    @BindView(R.id.tv_play_monitoring_fragment)
    TextView tvPlay;
    @BindView(R.id.tv_picture_monitoring_fragment)
    TextView tvPicture;
    @BindView(R.id.tv_video_monitoring_fragment)
    TextView tvVideo;
    @BindView(R.id.tv_sound_monitoring_fragment)
    TextView tvSound;
    @BindView(R.id.tv_police_monitoring_fragment)
    TextView tvPolice;
    @BindView(R.id.tv_openkey_monitoring_fragment)
    TextView tvOpenkey;
    @BindView(R.id.bt_talk_monitoring_fragment)
    Button btTalk;
    @BindView(R.id.tv_rec_monitoring_fragment)
    TextView tvRec;
    @BindView(R.id.pb_loading_monitoring_fragment)
    ProgressBar pbLoading;
    @BindView(R.id.tv_state_monitoring_fragment)
    TextView tvState;
    @BindView(R.id.ll_state_monitoring_fragment)
    LinearLayout llState;
    @BindView(R.id.iv_voice_animation_monitoring_fragment)
    ImageView ivVoiceAnimation;
    @BindView(R.id.toolbar_menu)
    TextView toolbarMenu;
    private PlayerCore mPlayerCore;
    Unbinder unbinder;
    private RxManager mRxManager = new RxManager();
//    private EyeCatBean device = new EyeCatBean();
    MainDeviceBean device;

    private String[] selectDialog = {"记录查询","开锁密码设置","设备连接密码","设置WiFi","删除设备"};

    @NonNull
    @Override
    protected MonitorPresenter createPresenter() {
        return new MonitorPresenter(this);
    }

    public static MonitorFragment newInstance(MainDeviceBean device) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.DEVICE, device);
        MonitorFragment fragment = new MonitorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static MonitorFragment newInstance() {
        return new MonitorFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            device = (MainDeviceBean) args.getSerializable(Constants.DEVICE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);
        unbinder = ButterKnife.bind(this, view);
        return attachToSwipeBack(view);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initPlayCore();
        initData();
        initListener();
    }

    private void initToolbar() {
        initStateBar(toolbar);
        toolbarTitle.setText(device.name);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_24dp);
        toolbar.setNavigationOnClickListener(v -> _mActivity.onBackPressedSupport());
        toolbarMenu.setText("更多");
        toolbarMenu.setOnClickListener(v -> {
//            if (mPlayerCore.PlayCoreGetCameraPlayerState() == 2) {
////            说明正在播放中，表明当前密码是正确的，即可以让他重新设置连接密码。
//                start(UpdatePasswordFragment.newInstance(device.cateyeBean));
//            }
            new AlertDialog.Builder(_mActivity)
                    .setItems(selectDialog, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0://记录查询
                                    _mActivity.start(QueryFragment.newInstance());
                                    break;
                                case 1://开锁密码设置
                                    unLock();
                                    break;
                                case 2://设备连接密码
                                    _mActivity.start(UpdatePasswordFragment.newInstance(device.cateyeBean));
                                    break;
                                case 3://设置WIFI
                                    _mActivity.start(GenerateWifiFragment.newInstance());
                                    break;
                                case 4://删除设备
                                    showDeleteDeviceDialog();
                                    break;
                                default:
                            }
                        }
                    })
                    .show();
        });
    }

    private void showDeleteDeviceDialog() {
        new AlertDialog.Builder(_mActivity)
                .setTitle("警告")
                .setMessage("确定要删除该设备吗？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showLoading();
                        mPresenter.requestDeleteCatEye(device);
                    }
                }).setNegativeButton("取消",null)
                .show();
    }

    public void responseDeleteSuccess(){
        dismissLoading();
        DialogHelper.successSnackbar(getView(), "删除成功！");
        EventBus.getDefault().post(new EventRefreshDeviceList());
        pop();
    }

    @Override
    public void error(String errorMessage) {
        dismissLoading();
        DialogHelper.successSnackbar(getView(), "删除失败！");
    }

    public void initPlayCore() {
        mPlayerCore = new PlayerCore(App.mContext);
        mPlayerCore.InitParam("", -1, ivVideo);
        mPlayerCore.SetPPtMode(false);
    }

    private void initData() {
        paly();
    }

    private void initListener() {
        btTalk.setOnTouchListener((v, event) -> {
            if (isPlaying()) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivVoiceAnimation.setVisibility(View.VISIBLE);
                        if (!mPlayerCore.GetIsPPT()) {
                            mPlayerCore.StartPPTAudio();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        ivVoiceAnimation.setVisibility(View.GONE);
                        if (mPlayerCore.GetIsPPT()) {
                            mPlayerCore.StopPPTAudio();
                        }
                        break;
                    default:
                }
            } else {
                DialogHelper.warningSnackbar(getView(), "未连接设备播放");
            }
            return true;
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRxManager.clear();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPlayerCore.GetIsPPT()) {
            mPlayerCore.StartPPTAudio();
        }
        mPlayerCore.Resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerCore.GetIsPPT()) {
            mPlayerCore.StopPPTAudio();
        }
        mPlayerCore.Pause();
    }

    @OnClick({R.id.tv_play_monitoring_fragment
            , R.id.tv_picture_monitoring_fragment
            , R.id.tv_video_monitoring_fragment
            , R.id.tv_sound_monitoring_fragment
            , R.id.tv_police_monitoring_fragment
            , R.id.tv_openkey_monitoring_fragment
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_play_monitoring_fragment:
                if (isPlaying()) {
                    stop();
                } else {
                    paly();
                }
                break;
            case R.id.tv_picture_monitoring_fragment:
                snapshot();
                break;
            case R.id.tv_video_monitoring_fragment:
                video();
                break;
            case R.id.tv_sound_monitoring_fragment:
                voice();
                break;
            case R.id.tv_police_monitoring_fragment:
                break;
            case R.id.tv_openkey_monitoring_fragment:
                unLock();
                break;
            default:
        }
    }

    private void voice() {
        if (isPlaying()) {
            if (mPlayerCore.GetIsVoicePause()) {
                mPlayerCore.OpenAudio();
                Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_shengyinquan_blue_180px);
                drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
                tvSound.setCompoundDrawables(null, drawableTop, null, null);
                tvSound.setText("静音");
            } else {
                mPlayerCore.CloseAudio();
                Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_labaquan_blue_180px);
                drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
                tvSound.setCompoundDrawables(null, drawableTop, null, null);
                tvSound.setText("扬声");
            }
        } else {
            DialogHelper.warningSnackbar(getView(), "未连接设备播放");
        }
    }

    private void video() {
        if (isPlaying()) {
            if (mPlayerCore.GetIsSnapVideo()) {
                mPlayerCore.SetSnapVideo(false);
                tvRec.setVisibility(View.GONE);
                Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_videoquan_180px);
                drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
                tvVideo.setCompoundDrawables(null, drawableTop, null, null);
                tvVideo.setText("录像");
                DialogHelper.successSnackbar(getView(), "录像放在！" + Constants.UserVideoDir);
            } else {
                mPlayerCore.SetSnapVideo(true);
                tvRec.setVisibility(View.VISIBLE);
                Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_stop_play_brown_150_150);
                drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
                tvVideo.setCompoundDrawables(null, drawableTop, null, null);
                tvVideo.setText("录像中…");
            }
        } else {
            DialogHelper.warningSnackbar(getView(), "未连接设备播放");
        }
    }

    private void snapshot() {
        if (isPlaying()) {
            mPlayerCore.SetSnapPicture(true);
            DialogHelper.successSnackbar(getView(), "拍照放在！" + Constants.UserImageDir);
        } else {
            DialogHelper.warningSnackbar(getView(), "未连接设备播放");
        }
    }

    private void stop() {
        Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_play_black_150px);
        drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        tvPlay.setCompoundDrawables(null, drawableTop, null, null);
        tvPlay.setText("播放");

        if (mPlayerCore.GetIsPPT()) {
            mPlayerCore.StopPPTAudio();
        }
        mPlayerCore.Stop();
        mRxManager.clear();
    }

    public void paly() {
        Drawable drawableTop = ContextCompat.getDrawable(_mActivity, R.drawable.ic_stop_play_brown_150_150);
        drawableTop.setBounds(0, 0, drawableTop.getMinimumWidth(), drawableTop.getMinimumHeight());
        tvPlay.setCompoundDrawables(null, drawableTop, null, null);
        tvPlay.setText("暂停");

        updateState();
        mPlayerCore.PlayP2P(device.deviceId, device.userName, device.userPwd, 0, 1);
    }

    public void vibrate() {
        Vibrator vib = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(1000);
    }

    //启动线程更新状态。
    private void updateState() {
        mRxManager.add(Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()) // 由于interval默认在新线程，所以我们应该切回主线程。
                        .subscribe(aLong -> {
                            tvRec.setVisibility(mPlayerCore.GetIsSnapVideo() ? View.VISIBLE : View.GONE);

                            int intState = mPlayerCore.PlayCoreGetCameraPlayerState();
                            setDescription(intState);

                            ALog.e("intState: " + intState);

                            TAlarmFrame tAlarmFrame = mPlayerCore.CameraGetAlarmInfo();

                            if (tAlarmFrame != null) {
                                LogOut.d("tAlarmFrame", "tAlarmFrame:"
                                        + tAlarmFrame.AlarmInfo + ",nAlarmType:"
                                        + tAlarmFrame.nAlarmType);
//                        Toast.makeText(mContext, "tAlarmFrame:" + tAlarmFrame.AlarmInfo, Toast.LENGTH_LONG).show();
                                vibrate();
                            }
                        })
        );
    }

    /**
     * private static final int NPC_D_MPI_MON_ERROR_USERID_ERROR = -101; //用户ID或用户名错误
     * private static final int NPC_D_MPI_MON_ERROR_USERPWD_ERROR = -102; //用户密码错误
     * private static final int NPC_D_MPI_MON_ERROR_REJECT_ACCESS = -111;// 权限不够
     */
    public void setDescription(int state) {
        ALog.e("setDescription:" + state);
        String des;
        switch (state) {
            case 0:
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                des = App.mContext.getString(R.string.ready);
                tvState.setText(des);
                break;
            case 1:
                if (llState.getVisibility() != View.VISIBLE) {
                    llState.setVisibility(View.VISIBLE);
                }
                des = App.mContext.getString(R.string.connecting);
                tvState.setText(des);
                break;
            case 2:
                des = App.mContext.getString(R.string.playing);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                break;
            case 3:
                des = App.mContext.getString(R.string.connect_fail);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            case 4:
                des = App.mContext.getString(R.string.stop);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            case 7:
                des = App.mContext.getString(R.string.stop);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            case SDKError.NPC_D_MPI_MON_ERROR_USERID_ERROR:
                des = App.mContext.getString(R.string.usererro);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            case SDKError.NPC_D_MPI_MON_ERROR_USERPWD_ERROR:
                des = App.mContext.getString(R.string.passworderro);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                showHint();
                break;
            case 10:
                if (llState.getVisibility() != View.VISIBLE) {
                    llState.setVisibility(View.VISIBLE);
                }
                des = App.mContext.getString(R.string.buffering);
                tvState.setText(des);
                break;
            case SDKError.NPC_D_MPI_MON_ERROR_REJECT_ACCESS:
                des = App.mContext.getString(R.string.NPC_D_MPI_MON_ERROR_REJECT_ACCESS);
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            case -112:
                des = "设备离线";
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
            default:
                des = "连接失败";
                if (llState.getVisibility() != View.GONE) {
                    llState.setVisibility(View.GONE);
                }
                DialogHelper.errorSnackbar(getView(), des);
                mRxManager.clear();
                break;
        }
    }

    private void showHint() {
        new AlertDialog.Builder(_mActivity)
                .setTitle("温馨提示")
                .setMessage("您当前连接密码错误，是否修改？")
                .setPositiveButton("是", (dialog, which) ->
                        start(UpdateParamsFragment.newInstance(device.cateyeBean)))
                .setNegativeButton("否", null)
                .show();
    }

    /**
     * 判断是否播放中
     *
     * @return
     */
    public boolean isPlaying() {
        return mPlayerCore.PlayCoreGetCameraPlayerState() == 2;
    }

    /**
     * “ztca”开头umid的设备 解锁
     *
     * @param password
     */
    public void openLock1(final String password) {
        new Thread() {
            @Override
            public void run() {
                /**
                 * 开锁 参数是8位数的密码
                 */
                int result = mPlayerCore.SendOpenLockCmd(password);
                btTalk.post(() -> {

                    if (result == 0 || result == 1) {
                        DialogHelper.errorSnackbar(getView(), "抱歉，开锁失败！");

                    } else {
                        DialogHelper.successSnackbar(getView(), "恭喜，开锁成功！");

                    }
                });
            }
        }.start();
    }

    /**
     * “ztca”开头umid以外 的设备 解锁
     *
     * @param password
     */
    public void openLock2(final String password) {
        showLoading();
        new Thread() {

            @Override
            public void run() {
                String passwordBase64 = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
                String requestStr = "{\"Operation\":3,\"Request_Type\":0,\"Value\":{\"Lock_Psw\":\""
                        + passwordBase64 + "\"}}";
                byte[] request = requestStr.getBytes();
                ALog.e("openLock-->" + requestStr);
                byte[] result = App.mPlayerClient.CallCustomFunc(device.deviceId, "admin", "admin", 66052, request);
                dismissLoading();
                if (null != result) {
                    String sResult = new String(result);

                    ALog.e("openLock-->" + sResult);
                    final ResultBean bean = JSON.parseObject(sResult,
                            ResultBean.class);
                    btTalk.post(() -> {
                        if (bean.getResult() == 1) {
                            DialogHelper.successSnackbar(getView(), "恭喜，开锁成功！");
                        } else {
                            DialogHelper.errorSnackbar(getView(), "抱歉，开锁失败！");
                        }
                    });

                    Log.e("openLock", sResult);
                } else {
                    DialogHelper.errorSnackbar(getView(), "抱歉，开锁失败！");

                }
            }
        }.start();

    }

    /**
     * 输入密码解锁
     */
    public void unLock() {
        EditText passEditText = new EditText(_mActivity);
        new AlertDialog.Builder(_mActivity)
                .setTitle("请输入密码")
                .setView(passEditText)
                .setPositiveButton("确定", (dialog, which) -> {
                    String inputText = passEditText.getText().toString();
                    if (TextUtils.isEmpty(inputText)) {
                        DialogHelper.warningSnackbar(getView(), "密码不能为空！");
                        return;
                    }
                    if (device.deviceId.toLowerCase().startsWith("ztca")) {
                        if (isPlaying()) {
                            openLock1(inputText);
                        } else {
                            DialogHelper.warningSnackbar(getView(), "未连接设备播放");
                        }
                    } else {
                        openLock2(inputText);
                    }
                    dialog.cancel();
                }).setNegativeButton(R.string.negative, null)
                .show();
    }
}

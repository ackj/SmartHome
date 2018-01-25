package com.meilun.security.smart.common;


import com.meilun.security.smart.BuildConfig;

import cn.itsite.abase.common.BaseConstants;

/**
 * Author：leguang on 2017/4/12 0009 14:23
 * Email：langmanleguang@qq.com
 */
public class Constants extends BaseConstants {
    public static final String TAG = Constants.class.getSimpleName();

    //--------------------以下是区分debug版和非debug版的baseurl-----------

    public static String BASE_USER = "";//用户系统
    public static String BASE_URL = "";

    static {
        if (BuildConfig.DEBUG) {
            //调试可以改这里的地址。
//            BASE_USER = "http://120.77.83.45:8076/gasMember/";//用户系统
//            BASE_URL = "http://120.77.83.45:8096/gas";
            BASE_USER = "http://www.aglhz.com:8076/memberSYS-m";//用户系统
            BASE_URL = "http://119.23.129.133:8090/gcsmart";
        } else {
            //这里的是正式版的基础地址，永远不要动。
            BASE_USER = "http://www.aglhz.com:8076/memberSYS-m";//用户系统
            BASE_URL = "http://119.23.129.133:8090/gcsmart";
        }
    }

    //-------------------猫眼相关-----------------

    public static String SERVER = "v0.api.zhongjiaan.com";//
    public static String CUSTOM_NAME = "";
    public static String CATEYE_PASSWORD = "GCAGLF2";
    public static String EMAIL = "yunshan_support@sina.com";
    public static String UserImageDir = "/storage/emulated/0/snapshot";
    public static String UserVideoDir = "/storage/emulated/0/videorecord";
    public static String SERIAL_NUMBER = "Serial_Number";
    //-------------------------------------------
    //smart_gateway/smart_gateway_gsw3/smart_camera/smart_cateye
    public static String SMART_GATEWAY = "smart_gateway";//S1网关
    public static String SMART_GATEWAY_GSW3 = "smart_gateway_gsw3";//安全小卫士
    public static String SMART_CAMERA = "smart_camera";//摄像头
    public static String SMART_CATEYE = "smart_cateye";//猫眼

    public static final int RESPONSE_CODE_SUCCESS = 200;

    public static final String PRESS_AGAIN = "再按一次退出";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LINK = "link";

    public static final String IS_FIRST_ENTER = "is_first_enter";

    //配网模块
    public static final String WIFI_NAME = "wifi_name";
    public static final String WIFI_PASSWORD = "wifi_password";

    //安全模块
    public static final String GATEWAY_STATE_CANCLE = "cancel";
    public static final String GATEWAY_STATE_HOME = "home";
    public static final String GATEWAY_STATE_FARAWAY = "faraway";

    //key
    public static final String KEY_HOST_NUMBER = "host_number";
    public static final String KEY_HOST = "key_host";
    public static final String KEY_HOST_NAME = "key_host_name";

    public static final int PAGE_SIZE = 20;

    //设备分类：sensor/device_ctrl 控制类或者探测器类。默认控制类
    public static final String DEVICE_CTRL = "device_ctrl";
    public static final String SENSOR = "sensor";
    public static final String TIME = "time";
    public static final String SCENE = "scene";
    public static final String DEVICE = "device";

    public static final String KEY_SELECTOR = "key_SELECTOR";

    public static final String FC = "FAglSmart";
    public static String OPERATOR_CODE="AglHz";

    /**
     * 主机配置类型
     */
    public static final String VOLUME = "vol";//音量
    public static final String PHONE = "phones";//手机号
    public static final String PUSH = "pushs";//推送

    /**
     * 主机设置子类型
     */
    public static final String V_GSM = "v_gsm";//留言音量
    public static final String V_SPEECH = "v_speech";//通话音量大小
    public static final String V_ALARM = "v_alarm";//报警音量大小
    public static final String V_TONE = "v_tone";//按键音量+语音提示音量

    public static final String P_MAIN = "p_main";//主卡电话(网关sim卡)
    public static final String P_ALARM = "p_alarm";//报警电话
    public static final String P_PUSH = "p_push";//推送电话

    public static final String PS_DEFENSE_CHG = "p_defense_chg";///** 网关开启布防撤防的推送 */
    public static final String PS_DOOR_CLOSE = "p_door_close";///** 网关开启门关闭的推送 */
    public static final String PS_DOOR_OPEN = "p_door_open";///** 网关开启门打开的推送 */
    public static final String PS_HOST_POWER_LOW = "p_host_power_low";///** 网关开启主机电量低的推送 */
    public static final String PS_POWER_FAIL = "p_power_fail";///** 网关开启外界电源掉电的推送 */
    public static final String PS_POWER_RECOVER = "p_power_recover";///** 网关开启外界电源恢复的推送 */
    public static final String PS_SENSOR_POWER_LOW = "p_sensor_power_low";///** 网关开启传感器电量低的推送 */
    public static final String PS_SMS_TOPHONE = "p_sms_tophone";///** 网关开启指定手机短信的推送 */
    public static final String PS_WIFI_CONNECT = "p_wifi_connect";///** 网关开启WiFi连接的推送 */
    public static final String PS_WIFI_DISCONNECT = "p_wifi_disconnect";///** 网关开启WiFi断开的推送 */

    /**
     * 探测器类型，现在只有8种。
     */
    public static final String TYPE_GAS_DETECTOR = "gas_detector";//煤气探测器。
    public static final String TYPE_SMOKE_DETECTOR = "smoke_detector";//烟雾探测器。
    public static final String TYPE_EMERGENCY_BUTTON = "emergency_button";//紧急按钮。
    public static final String TYPE_REMOTE_CONTROL = "remote_control";//遥控器。
    public static final String TYPE_DOOR_BELL = "door_bell";//门铃。
    public static final String TYPE_DOOR_MAGNET = "door_magnet";//门磁。
    public static final String TYPE_INFRARED_08_SENSOR = "infrared_08_sensor";//人体红外08。
    public static final String TYPE_WINDOWRED_SENSOR = "windowred_sensor";//幕帘红外。

    //App更新
    public static final String APP_UPDATE_URL = "http://119.23.129.133:8066/appstore/client/info/checkVersion";
    public static final String SYS_ACCESS_PREFIX = "centro";
    public static final String SYS_ACCESS_KEY = "aglapps@0752";
    public static String SC = "AglhzCateye";
    public static String APP_TYPE = "1";//1为android。2为iOS。
}

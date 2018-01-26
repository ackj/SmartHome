package com.meilun.security.smart.common;

import com.meilun.security.smart.entity.bean.AuthorizationBean;
import com.meilun.security.smart.entity.bean.BaseBean;
import com.meilun.security.smart.entity.bean.CameraBean;
import com.meilun.security.smart.entity.bean.CheckTokenBean;
import com.meilun.security.smart.entity.bean.DeviceListBean;
import com.meilun.security.smart.entity.bean.DeviceLogBean;
import com.meilun.security.smart.entity.bean.DevicesBean;
import com.meilun.security.smart.entity.bean.DiscoverBean;
import com.meilun.security.smart.entity.bean.FirstLevelBean;
import com.meilun.security.smart.entity.bean.GatewaysBean;
import com.meilun.security.smart.entity.bean.GoodsBean;
import com.meilun.security.smart.entity.bean.HostSettingsBean;
import com.meilun.security.smart.entity.bean.LinkageBean;
import com.meilun.security.smart.entity.bean.MainDeviceListBean;
import com.meilun.security.smart.entity.bean.NewsBean;
import com.meilun.security.smart.entity.bean.RoomTypesBean;
import com.meilun.security.smart.entity.bean.RoomsBean;
import com.meilun.security.smart.entity.bean.SceneBean;
import com.meilun.security.smart.entity.bean.SecurityBean;
import com.meilun.security.smart.entity.bean.SubCategoryBean;
import com.meilun.security.smart.entity.bean.SubDeviceDetBean;
import com.meilun.security.smart.entity.bean.UserBean;

import java.io.File;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by leguang on 2017/7/4 0022.
 * Email：langmanleguang@qq.com
 */

public interface ApiService {

    String BASE_USER = Constants.BASE_USER;           //用户

    String BASE_URL = Constants.BASE_URL;

    //*************以上基础路径*******************

    //-----------------以下为推送相关---------------------
    //阿里云deviceID登记接口
    String registerDevice = BASE_USER + "/client/logUMengParams.do";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> registerDevice(@Url String url,
                                        @Field("token") String token,
                                        @Field("deviceToken") String deviceToken,
                                        @Field("alias") String alias,
                                        @Field("aliasType") String aliasType);

    //-----------------以上为推送相关---------------------

    //----------以下为Launch模块--------------
    //登录
    String requestLogin = BASE_USER + "/client/login.do";

    @FormUrlEncoded
    @POST
    Observable<UserBean> requestLogin(@Url String url,
                                      @Field("sc") String sc,
                                      @Field("user") String user,
                                      @Field("pwd") String pwd);

    //注册
    String requestRegister = BASE_USER + "/client/register.do";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestRegister(@Url String url,
                                         @Field("sc") String sc,
                                         @Field("account") String account,
                                         @Field("code") String code,
                                         @Field("Password1") String password1,
                                         @Field("Password2") String password2);

    //获取验证码
    String requestVerifyCode = BASE_USER + "/client/validCode.do";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestVerifyCode(@Url String url,
                                           @Field("sc") String sc,
                                           @Field("phone") String phone,
                                           @Field("type") String type);

    //重置密码
    String requestResetPassword = BASE_USER + "/client/renewMemberPwd.do";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestResetPassword(@Url String url,
                                              @Field("sc") String sc,
                                              @Field("account") String account,
                                              @Field("code") String code,
                                              @Field("pwd1") String password1,
                                              @Field("pwd2") String password2);

    //登录验证
    String requestCheckToken = BASE_USER + "/client/checkIfTokenInvalid.do";

    @FormUrlEncoded
    @POST
    Observable<CheckTokenBean> requestCheckToken(@Url String url,
                                                 @Field("token") String token);

    //登出
    String requestLogout = BASE_USER + "/client/logout.do";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestLogout(@Url String url,
                                       @Field("token") String token);

    //----------以上为Launch模块--------------


    //----------------------------- 以下为主机操作相关 ---------------------------------
    //获取主机列表
    String requestGateways = BASE_URL + "/client/info/gatewayList";

    @FormUrlEncoded
    @POST
    Observable<GatewaysBean> requestGateways(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
    );

    //-----------------------------探测器 相关 ---------------------------------
    //获取探测器列表
    String requestSensorTypeList = BASE_URL + "/client/info/sensorTypeList";

    @FormUrlEncoded
    @POST
    Observable<DevicesBean> requestSensorTypeList(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
    );

    //添加探测器
    String reqeuestNewsensor = BASE_URL + "/ctrl/client/newsensor";


    @POST
    Observable<BaseBean> reqeuestNewsensor(@Url String url
            , @Body MultipartBody file);

    //取消传感器的学习。
    String reqeuestCancellationOfSensorLearning = BASE_URL + "/ctrl/client/sensorLearnCancle";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> reqeuestCancellationOfSensorLearning(@Url String url
            , @Field("token") String token);

    // 修改探测器
    String requestModsensor = BASE_URL + "/ctrl/client/modsensor";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestModsensor(@Url String url
            , @Field("token") String token
            , @Field("file") File file
            , @Field("index") int index
            , @Field("name") String name
            , @Field("defenseLevel") String defenseLevel
            , @Field("alarmDelay") int alarmDelay);


    @POST
    Observable<BaseBean> requestModsensor(@Url String url
            , @Body MultipartBody file
    );

    // 删除探测器
    String requestDelsensor = BASE_URL + "/ctrl/client/delsensor";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDelsensor(@Url String url,
                                          @Field("token") String token,
                                          @Field("index") int index);

    //获取探测器/设备详情
    String requestSubDeviceDet = BASE_URL + "/client/info/subDeviceDet";

    @FormUrlEncoded
    @POST
    Observable<SubDeviceDetBean> requestSubDeviceDet(@Url String url,
                                                     @Field("token") String token,
                                                     @Field("category") String category,
                                                     @Field("index") int index);

    //----------------------------- 设备相关 ---------------------------------
    //添加设备
    String requestNewDevice = BASE_URL + "/ctrl/client/newdevice24";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewDevice(@Url String url,
                                          @Field("token") String token,
                                          @Field("deviceType") String deviceType,
                                          @Field("name") String name,
                                          @Field("roomFid") String roomFid);

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewCamera(@Url String url,
                                          @Field("token") String token,
                                          @Field("deviceType") String deviceType,
                                          @Field("name") String name,
                                          @Field("roomFid") String roomFid,
                                          @Field("deviceId") String deviceId,
                                          @Field("password") String password);

    //修改设备
    String requestModDevice = BASE_URL + "/ctrl/client/moddevice";

    @POST
    Observable<BaseBean> requestModDevice(@Url String url
            , @Body MultipartBody file);

    //删除设备
    String requestDelDevice = BASE_URL + "/ctrl/client/deldevice";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDelDevice(@Url String url
            , @Field("token") String token
            , @Field("index") int index);

    String requestSubDeviceList = BASE_URL + "/client/info/subDeviceList";

    //设备列表
    @FormUrlEncoded
    @POST
    Observable<DeviceListBean> requestSubDeviceList(@Url String url,
                                                    @Field("token") String token,
                                                    @Field("page") int page,
                                                    @Field("pageSize") int pageSize,
                                                    @Field("roomId") int roomId,
                                                    @Field("category") String category);

    //设备列表
    @FormUrlEncoded
    @POST
    Observable<DeviceListBean> requestSubDeviceList(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
            , @Field("category") String category);

    //设备控制
    String requestDevicectrl = BASE_URL + "/ctrl/client/devicectrl";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDevicectrl(@Url String url
            , @Field("token") String token
            , @Field("index") int index
            , @Field("nodeId") String nodeId
            , @Field("status") int status);

    //设备消息记录
    String requestDeviceLogs = BASE_URL + "/client/info/deviceLogs";

    @FormUrlEncoded
    @POST
    Observable<DeviceLogBean> requestDeviceLogs(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
    );

    //设备类型
    String requestCtrlSDeviceTypeList = BASE_URL + "/client/info/ctrlSDeviceTypeList";

    @FormUrlEncoded
    @POST
    Observable<DevicesBean> requestCtrlSDeviceTypeList(@Url String url, @Field("A") String a);

    String requestNewDeviceConfirm = BASE_URL + "/ctrl/client/newDeviceConfirm";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewDeviceConfirm(@Url String url
            , @Field("token") String token
            , @Field("status") int status);


    //----------------------------- 房间相关 ---------------------------------
    //获取房间列表
    String requestRoomList = BASE_URL + "/client/info/roomList";

    @FormUrlEncoded
    @POST
    Observable<RoomsBean> requestRoomList(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
    );

    //添加房间
    String requestNewroom = BASE_URL + "/ctrl/client/newroom";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewroom(@Url String url
            , @Field("token") String token
            , @Field("name") String roomName
            , @Field("roomTypeFid") String roomTypeFid
    );

    //房间类型列表
    String requestRoomTypeList = BASE_URL + "/client/info/roomTypeList";

    @FormUrlEncoded
    @POST
    Observable<RoomTypesBean> requestRoomTypeList(@Url String url, @Field("A") String a);

    //删除房间
    String requestDelroom = BASE_URL + "/ctrl/client/delroom";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDelroom(@Url String url,
                                        @Field("token") String token,
                                        @Field("fid") String fid
    );

    //----------------------------- 联动相关 ---------------------------------
    //添加联动
    String requestNewlinkage = BASE_URL + "/ctrl/client/newlinkage";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewlinkage(@Url String url
            , @Field("token") String token
            , @Field("name") String roomName
            , @Field("triggerType") String triggerType
            , @Field("cdt_sensorId") int cdt_sensorId
            , @Field("cdt_sensorAct") String cdt_sensorAct
            , @Field("cdt_day") String cdt_day
            , @Field("cdt_time") String cdt_time
            , @Field("targetType") String targetType
            , @Field("targetId") String targetId
            , @Field("nodeId") String nodeId
            , @Field("act1") String act1
            , @Field("delay") String delay
            , @Field("act2") String act2
    );

    //修改联动
    String requestModLinkage = BASE_URL + "/ctrl/client/modlinkage";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestModLinkage(@Url String url
            , @Field("token") String token
            , @Field("index") int index
            , @Field("name") String name
            , @Field("triggerType") String triggerType
            , @Field("cdt_sensorId") int cdt_sensorId
            , @Field("cdt_sensorAct") String cdt_sensorAct
            , @Field("cdt_day") String cdt_day
            , @Field("cdt_time") String cdt_time
            , @Field("targetType") String targetType
            , @Field("targetId") String targetId
            , @Field("nodeId") String nodeId
            , @Field("act1") String act1
            , @Field("delay") String delay
            , @Field("act2") String act2
            , @Field("status") int status);


    //联动列表
    String requestLinkageList = BASE_URL + "/client/info/linkageList";

    @FormUrlEncoded
    @POST
    Observable<LinkageBean> requestLinkageList(@Url String url
            , @Field("token") String token
            , @Field("page") int page
            , @Field("pageSize") int pageSize
    );

    //删除联动
    String requestDellinkage = BASE_URL + "/ctrl/client/dellinkage";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDellinkage(@Url String url
            , @Field("token") String token
            , @Field("index") int index
    );
    //----------------------------- 授权相关 ---------------------------------

    //会员主机授权列表
    String requestGatewayAuthList = BASE_URL + "/client/info/gatewayAuthList";

    @FormUrlEncoded
    @POST
    Observable<AuthorizationBean> requestGatewayAuthList(@Url String url,
                                                         @Field("token") String token,
                                                         @Field("gateway") String gateway,
                                                         @Field("page") int page,
                                                         @Field("pageSize") int pageSize);

    //解除主机授权
    String requestGatewayUnAuth = BASE_URL + "/ctrl/client/gatewayUnAuth";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestGatewayUnAuth(@Url String url,
                                              @Field("token") String token,
                                              @Field("gateway") String gateway,
                                              @Field("fid") String fid);

    //选择一个主机，授权给用户
    String requestGatewayAuth = BASE_URL + "/ctrl/client/gatewayAuth";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestGatewayAuth(@Url String url
            , @Field("token") String token
            , @Field("gateway") String gateway
            , @Field("mobile") String mobile);

    //解绑当前主机
    String requestUnbindHost = BASE_URL + "/ctrl/client/delgateway";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestUnbindHost(@Url String url
            , @Field("token") String token
            , @Field("gateway") String gateway);

    //----------------------------- 关于 ---------------------------------
    String requestFeedback = BASE_URL + "/client/feedback";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestFeedback(@Url String url,
                                         @Field("token") String token,
                                         @Field("des") String des,
                                         @Field("contact") String contact
    );

    //-----------------以下为主机操作相关---------------------
    //切换主机
    String requestSwichGateway = BASE_URL + "/ctrl/client/gatewaySW";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestSwichGateway(@Url String url,
                                             @Field("token") String token,
                                             @Field("gateway") String gateway);

    //切换主机状态
    String requestSwichState = BASE_URL + "/ctrl/client/gatewayDSS";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestSwichState(@Url String url,
                                           @Field("token") String token,
                                           @Field("dstatus") String dstatus);

    //主页显示内容
    String requestSecurity = BASE_URL + "/client/info/homePage ";

    @FormUrlEncoded
    @POST
    Observable<SecurityBean> requestSecurity(@Url String url,
                                             @Field("token") String token);

    //添加主机
    String requestAddHost = BASE_URL + "/ctrl/client/newgateway";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestAddHost(@Url String url,
                                        @Field("token") String token,
                                        @Field("gateway") String gateway,
                                        @Field("name") String name,
                                        @Field("roomDir") String roomDir,
                                        @Field("addr") String addr,
                                        @Field("addrDet") String addrDet,
                                        @Field("lng") String lng,
                                        @Field("lat") String lat);

    //修改主机定位
    String requestEditHostLocation = BASE_URL + "/ctrl/client/gatewayAddrSet";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestEditHostLocation(@Url String url,
                                                 @Field("token") String token,
                                                 @Field("addr") String addr,
                                                 @Field("addrDet") String addrDet,
                                                 @Field("lng") String lng,
                                                 @Field("lat") String lat,
                                                 @Field("gateway") String gateway);

    //主机配置都归为这一个借口。如：音量，短信，推送等。
    String requestHostConfig = BASE_URL + "/ctrl/client/gatewayConfig";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestHostConfig(@Url String url,
                                           @Field("gateway") String gateway,
                                           @Field("token") String token,
                                           @Field("type") String type,
                                           @Field("subType") String subType,
                                           @Field("val") String val);


    //获取主机设置信息。
    String requestHostSettings = BASE_URL + "/client/info/gatewayConfigInfo";

    @FormUrlEncoded
    @POST
    Observable<HostSettingsBean> requestHostSettings(@Url String url,
                                                     @Field("gateway") String gateway,
                                                     @Field("token") String token,
                                                     @Field("type") String type);

    //主机配置都归为这一个借口。如：音量，短信，推送等。
    String requestUpdateHostName = BASE_URL + "/ctrl/client/modgateway";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestUpdateHostName(@Url String url,
                                               @Field("token") String token,
                                               @Field("gateway") String gateway,
                                               @Field("name") String name);

    //主机留言。
    String requestLeaveMassge = BASE_URL + "/ctrl/client/gatewayMsgLeave";


    @POST
    Observable<BaseBean> requestLeaveMassge(@Url String url,
                                            @Body MultipartBody file);

    //----------------------------- 以上为主机操作相关 ---------------------------------

    //----------------------------- 以下为场景 ----------------------------------

    //场景列表
    String requestSceneList = BASE_URL + "/client/info/sceneList";

    @FormUrlEncoded
    @POST
    Observable<SceneBean> requestSceneList(@Url String url,
                                           @Field("pageSize") int pageSize,
                                           @Field("page") int page,
                                           @Field("token") String token);

    //启动场景
    String requestStartScene = BASE_URL + "/ctrl/client/scenectrl";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestStartScene(@Url String url,
                                           @Field("token") String token,
                                           @Field("index") int index);

    //删除场景
    String requestDeleteScene = BASE_URL + "/ctrl/client/delscene";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDeleteScene(@Url String url,
                                            @Field("token") String token,
                                            @Field("index") int index);


    // 添加场景
    String requestAddScene = BASE_URL + "/ctrl/client/newscene";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestAddScene(@Url String url,
                                         @Field("token") String token,
                                         @Field("name") String name,
                                         @Field("paramJson") String paramJson);

    //----------------------------- 以上为场景 ----------------------------------


    //********************以下为更新App接口*******************************
    String requestAppUpdatae = BASE_URL + "/client/info/checkVersion";

    //********************以上为更新App接口*******************************


    //----------------------------- 摄像头相关 ----------------------------------

    //获取摄像头列表
    String requestCameraList = BASE_URL + "/client/info/cameraList";

    @FormUrlEncoded
    @POST
    Observable<CameraBean> requestCameraList(@Url String url,
                                             @Field("token") String token,
                                             @Field("page") int page,
                                             @Field("pageSize") int pageSize);

    @FormUrlEncoded
    @POST
    Observable<CameraBean> requestCameraList(@Url String url,
                                             @Field("token") String token,
                                             @Field("gateway") String gateway);

    //删除摄像头
    String requestDelcamera = BASE_URL + "/ctrl/client/delcamera";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDelcamera(@Url String url,
                                          @Field("token") String token,
                                          @Field("camera") String camera);

    //修改摄像头信息
    String requestModCamera = BASE_URL + "/ctrl/client/modcamera";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestModCamera(@Url String url,
                                          @Field("token") String token,
                                          @Field("camera") String camera,
                                          @Field("type") String type,
                                          @Field("name") String name,
                                          @Field("password") String password);

    //添加摄像头
    String requestNewcamera = BASE_URL + "/ctrl/client/newcamera";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewcamera(@Url String url,
                                          @Field("token") String token,
                                          @Field("deviceId") String deviceId,
                                          @Field("deviceName") String deviceName,
                                          @Field("devicePassword") String devicePassword);


    //************************* 智能商城模块 **************************
    //一级列表同时是判断是否是跳转一级列表还是二级列表的入口
    String requestFirstLevel = "http://www.aglhz.com/mall/member/goodscategory/firstLevelList.do";

    @FormUrlEncoded
    @POST
    Observable<FirstLevelBean> requestFirstLevel(@Url String url,
                                                 @Field("keywords") String keywords,
                                                 @Field("fromPoint") String fromPoint);

    //二级列表
    String requestSubCategoryLevel = "http://www.aglhz.com/mall/member/goodscategory/subCategoryLevelList.do";

    @FormUrlEncoded
    @POST
    Observable<SubCategoryBean> requestSubCategoryLevel(@Url String url,
                                                        @Field("token") String token,
                                                        @Field("appType") int appType,
                                                        @Field("id") String id);

    //三级列表
    String requestGoodsList = "http://www.aglhz.com/mall/member/goodscategory/findGoodsListByCategoryId.do";

    @FormUrlEncoded
    @POST
    Observable<GoodsBean> requestGoodsList(@Url String url,
                                           @Field("token") String token,
                                           @Field("appType") int appType,
                                           @Field("secondCategoryId") String id,
                                           @Field("fromPoint")String fromPoint);

    String requestDeviceNotAlone = BASE_URL + "/client/info/subDevicesNotAlone";

    @FormUrlEncoded
    @POST
    Observable<DeviceListBean> requestDeviceNotAlone(@Url String url,
                                                     @Field("token") String token);

    //todo(高亮)--------------------- 发现模块新增or修改 ----------------------

    String requestDiscoverPage = BASE_URL + "/client/discovery/homepage";

    @GET
    Observable<DiscoverBean> requestDiscoverPage(@Url String url,
                                                 @Query("token") String token,
                                                 @Query("code")String code);

    String requestNewsList = BASE_URL + "/client/discovery/homepage/news";

    @GET
    Observable<NewsBean> requestNewsList(@Url String url,
                                         @Query("token") String token,
                                         @Query("code") String code,
                                         @Query("page") int page,
                                         @Query("pageSize") int pageSize);

    //(todo)--------------------- 2018.1.3新增（单独获取摄像头）----------------------
    String requestMainDeviceList = BASE_URL + "/client/info/mainDeviceList";

    @FormUrlEncoded
    @POST
    Observable<MainDeviceListBean> requestMainDeviceList(@Url String url,
                                                         @Field("token") String token,
                                                         @Field("type") String type,
                                                         @Field("page") int page,
                                                         @Field("pageSize") int pageSize);

    String requestNewMainDevice = BASE_URL + "/ctrl/client/newMainDevice";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestNewMainDevice(@Url String url,
                                              @Field("token") String token,
                                              @Field("type") String type,
                                              @Field("serialNO") String serialNO,
                                              @Field("name") String name,
                                              @Field("password") String password);

    String requestDelMainDevice = BASE_URL + "/ctrl/client/delMainDevice";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestDelMainDevice(@Url String url,
                                              @Field("token") String token,
                                              @Field("type") String type,
                                              @Field("serialNO") String serialNO);

    String requestCatEyeUpdate = BASE_URL + "/cateye/device/from-client/device-update";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestCatEyeUpdate(@Url String url,
                                             @Field("account") String account,
                                             @Field("token") String token,
                                             @Field("catEyeSerialNo") String serialNO);

    String requestCatEyeDelete = BASE_URL+"/cateye/device/from-client/device-delete";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestCatEyeDelete(@Url String url,
                                             @Field("token") String token,
                                             @Field("catEyeSerialNo") String catEyeSerialNo);

    String requestModMainDevice = BASE_URL+"/ctrl/client/modMainDevice";

    @FormUrlEncoded
    @POST
    Observable<BaseBean> requestModMainDevice(@Url String url,
                                              @Field("token") String token,
                                              @Field("type") String type,
                                              @Field("serialNO") String serialNO,
                                              @Field("name") String name,
                                              @Field("password") String password
    );
}

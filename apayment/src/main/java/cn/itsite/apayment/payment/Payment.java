package cn.itsite.apayment.payment;

import android.app.Activity;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;

import cn.itsite.apayment.payment.network.INetworkClient;
import cn.itsite.apayment.payment.network.NetworkClient;
import cn.itsite.apayment.payment.pay.IPayable;
import cn.itsite.apayment.payment.pay.alipay.ALiAppPay;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 支付SDK封装工具类.
 */

public final class Payment {
    public static final String TAG = Payment.class.getSimpleName();
    public static final String ALIPAY = "支付宝支付";
    public static final String WXPAY = "微信支付";
    //支付方式
    public static final int PAYTYPE_WECHAT_APP = 202;
    public static final int PAYTYPE_WECHAT_H5 = 203;
    public static final int PAYTYPE_WECHAT_H5X = 203;//本来是207的，后台暂时未更改，跟H5支付一样。
    public static final int PAYTYPE_ALI_APP = 102;
    public static final int PAYTYPE_ALI_H5 = 103;
    public static final int PAYTYPE_UNI = 5;
    //请求方式
    public static final int HTTP_GET = 1;
    public static final int HTTP_POST = 2;

    //请求错误
    public static final int REQUEST_ERROR = -100;
    //解析错误
    public static final int PARSE_ERROR = -200;
    // 通用结果码
    public static final int PAY_OK = 1;
    public static final int PAY_CACELED = 0;
    public static final int PAY_ERROR = -1;
    public static final int VERIFY_ERROR = -2;
    public static final int PAY_REPAY = -3;

    // 微信结果码
    public static final int WECHAT_SENT_FAILED_ERROR = -3;
    public static final int WECHAT_AUTH_DENIED_ERROR = -4;
    public static final int WECHAT_UNSUPPORT_ERROR = -5;
    public static final int WECHAT_BAN_ERROR = -6;
    public static final int WECHAT_NOT_INSTALLED_ERROR = -7;
    // 支付宝结果码
    public static final int ALIPAY_WAIT_CONFIRM_ERROR = 8000;
    public static final int ALIPAY_NET_ERROR = 6002;
    public static final int ALIPAY_UNKNOW_ERROR = 6004;
    public static final int ALIPAY_OTHER_ERROR = 6005;
    // 银联结果码
    public static final int UPPAY_PLUGIN_NOT_INSTALLED = -10;
    public static final int UPPAy_PLUGIN_NEED_UPGRADE = -11;
    //完成支付所需对象
    private PaymentListener.OnRequestListener onRequestListener;
    private PaymentListener.OnParseListener onParseListener;
    private PaymentListener.OnPayListener onPayListener;
    private PaymentListener.OnVerifyListener onVerifyListener;
    private Map<String, String> params;
    private IPayable pay;
    private INetworkClient client = NetworkClient.httpUrlConnection();
    @HttpType
    private int httpType;
    private String url;
    private Activity activity;

    @IntDef({PAYTYPE_WECHAT_APP, PAYTYPE_WECHAT_H5, PAYTYPE_WECHAT_H5X, PAYTYPE_ALI_APP, PAYTYPE_ALI_H5, PAYTYPE_UNI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PayType {
    }

    @IntDef({HTTP_GET, HTTP_POST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface HttpType {
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Payment setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public IPayable getPay() {
        return pay;
    }

    public Payment setPay(@NonNull IPayable pay) {
        this.pay = pay;
        return this;
    }

    public INetworkClient getClient() {
        return client;
    }

    public Payment setClient(@NonNull INetworkClient client) {
        this.client = client;
        return this;
    }

    @HttpType
    public int getHttpType() {
        return httpType;
    }

    public Payment setHttpType(@HttpType int httpType) {
        this.httpType = httpType;
        return this;
    }

    public PaymentListener.OnRequestListener getOnRequestListener() {
        return onRequestListener;
    }

    public Payment setOnRequestListener(PaymentListener.OnRequestListener onRequestListener) {
        this.onRequestListener = onRequestListener;
        return this;
    }

    public PaymentListener.OnParseListener getOnParseListener() {
        return onParseListener;
    }

    public Payment setOnParseListener(PaymentListener.OnParseListener onParseListener) {
        this.onParseListener = onParseListener;
        return this;
    }

    public PaymentListener.OnPayListener getOnPayListener() {
        return onPayListener;
    }

    public Payment setOnPayListener(PaymentListener.OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
        return this;
    }

    public PaymentListener.OnVerifyListener getOnVerifyListener() {
        return onVerifyListener;
    }

    public Payment setOnVerifyListener(PaymentListener.OnVerifyListener onVerifyListener) {
        this.onVerifyListener = onVerifyListener;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Payment setUrl(String url) {
        this.url = url;
        return this;
    }

    public Activity getActivity() {
        return activity;
    }

    public Payment setActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    /**
     * 支付从这个函数开始：1.请求支付参数（requestPayParams()）。2.获取参数成功后就开始解析参数。3.解析得到订单后就开始支付了pay()。
     *
     * @return
     */
    public Payment start() {
        requestPayParams();
        return this;
    }

    /**
     * 从这个函数开始的支付是已经从后台获取统一订单的。
     *
     * @param result
     * @return
     */
    public Payment pay(final String result) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //        parse(result);
                parse4JiGe(result);//临时方案。
            }
        });
        return this;
    }

    /**
     * 临时方法：对请求返回的统一订单进行解析，由于基哥返回的json中的部分字段的key与大叔返回的不一样，所以另立一套解析方案。
     *
     * @param result
     */
    private void parse4JiGe(String result) {
        getFragment(activity).setPayment(this);

        if (onParseListener != null) {
            onParseListener.onStart(result);
        }

        if (TextUtils.isEmpty(result)) {
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
            }
            clear();
            return;
        }

        JSONObject resultObject = null;
        try {
            resultObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
            }
            clear();
            return;
        }

        JSONObject jsonOther = resultObject.optJSONObject("other");
        if (jsonOther != null && jsonOther.optInt("code") == 500) {
            if (onPayListener != null) {
                onPayListener.onFailure(pay.getPayType(), PAY_REPAY);
            }
            clear();
            return;
        }

        PayParams payParams = null;
        try {
            if (pay instanceof ALiAppPay) {
                resultObject = new JSONObject(result);
                JSONObject jsonData = resultObject.optJSONObject("data");
                payParams = new PayParams.Builder()
                        .orderInfo(jsonData.optString("body"))
                        .build();
            } else {
                resultObject = new JSONObject(result);
                JSONObject jsonData = resultObject.optJSONObject("data");
                payParams = new PayParams.Builder()
                        .appID(jsonData.optString("appid"))
                        .partnerId(jsonData.optString("partnerid"))
                        .prePayId(jsonData.optString("prepayid"))
                        .packageValue("Sign=WXPay")
                        .nonceStr(jsonData.optString("noncestr"))
                        .timeStamp(jsonData.optString("timestamp"))
                        .sign(jsonData.optString("sign"))
                        .outTradeNo(jsonData.optString("out_trade_no"))
                        .build();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
                clear();
                return;
            }
        }

        if (onParseListener != null) {
            onParseListener.onSuccess(payParams);
        }

        if (pay != null) {
            pay.pay(activity, payParams, new PaymentListener.OnPayListener() {
                @Override
                public void onStart(@Payment.PayType int payType) {
                    if (onPayListener != null) {
                        onPayListener.onStart(payType);
                    }
                }

                @Override
                public void onSuccess(@Payment.PayType int payType) {
                    if (onPayListener != null) {
                        onPayListener.onSuccess(payType);
                    }
                    clear();
                }

                @Override
                public void onFailure(@Payment.PayType int payType, int errorCode) {
                    if (onPayListener != null) {
                        onPayListener.onFailure(payType, errorCode);
                    }
                    clear();
                }
            });
        }
    }

    /**
     * 请求APP服务器获取预支付信息：微信，支付宝，银联都需要此步骤。
     */
    private void requestPayParams() {
        if (onRequestListener != null) {
            onRequestListener.onStart();
        }
        INetworkClient.CallBack callBack = new INetworkClient.CallBack<String>() {
            @Override
            public void onSuccess(final String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (onRequestListener != null) {
                            onRequestListener.onSuccess(result);
                        }
                        parse(result);
                    }
                });
            }

            @Override
            public void onFailure() {
                if (onRequestListener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onRequestListener.onError(REQUEST_ERROR);
                        }
                    });
                }
                clear();
            }
        };

        if (client != null) {
            switch (httpType) {
                case HTTP_GET:
                    client.get(url, params, callBack);
                    break;
                case HTTP_POST:
                default:
                    client.post(url, params, callBack);
                    break;
            }
        }
    }

    /**
     * 对请求的统一订单进行解析。
     *
     * @param result
     */
    private void parse(String result) {
        getFragment(activity).setPayment(this);

        if (onParseListener != null) {
            onParseListener.onStart(result);
        }

        if (TextUtils.isEmpty(result)) {
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
            }
            clear();
            return;
        }

        JSONObject resultObject = null;
        try {
            resultObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
            }
            clear();
            return;

        }
        JSONObject jsonOther = resultObject.optJSONObject("other");
        if (jsonOther != null && jsonOther.optInt("code") == 500) {
            if (onPayListener != null) {
                onPayListener.onFailure(pay.getPayType(), PAY_REPAY);
            }
            clear();
            return;
        }

        PayParams payParams = null;
        try {
            if (pay != null) {
                payParams = pay.parse(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onParseListener != null) {
                onParseListener.onError(PARSE_ERROR);
                clear();
                return;
            }
        }

        if (onParseListener != null) {
            onParseListener.onSuccess(payParams);
        }

        if (pay != null) {
            pay.pay(activity, payParams, new PaymentListener.OnPayListener() {
                @Override
                public void onStart(@Payment.PayType int payType) {
                    if (onPayListener != null) {
                        onPayListener.onStart(payType);
                    }
                }

                @Override
                public void onSuccess(@Payment.PayType int payType) {
                    if (onPayListener != null) {
                        onPayListener.onSuccess(payType);
                    }
                    clear();
                }

                @Override
                public void onFailure(@Payment.PayType int payType, int errorCode) {
                    if (onPayListener != null) {
                        onPayListener.onFailure(payType, errorCode);
                    }
                    clear();
                }
            });
        }
    }

    private PayFragment getFragment(Activity activity) {
        PayFragment payFragment = findPayFragment(activity);
        if (payFragment == null) {
            payFragment = new PayFragment();
            FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(payFragment, PayFragment.TAG)
                    .commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        }
        return payFragment;
    }

    private PayFragment findPayFragment(Activity activity) {
        return (PayFragment) ((FragmentActivity) activity)
                .getSupportFragmentManager().findFragmentByTag(PayFragment.TAG);
    }

    public void clear() {
        if (params != null && activity != null && !activity.isDestroyed()) {
            //还要记得remove掉这个fragment
            if (findPayFragment(activity) != null) {
                FragmentManager fragmentManager = ((FragmentActivity) activity).getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .remove(findPayFragment(activity))
                        .commitAllowingStateLoss();
            }
            activity = null;
            params = null;
        }
    }

    public static class Builder {
        Map<String, String> params;
        IPayable pay;
        INetworkClient client;
        @HttpType
        int httpType;
        String url;
        Activity activity;
        PaymentListener.OnRequestListener onRequestListener;
        PaymentListener.OnParseListener onParseListener;
        PaymentListener.OnPayListener onPayListener;
        PaymentListener.OnVerifyListener onVerifyListener;

        public Builder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder setPay(@NonNull IPayable pay) {
            this.pay = pay;
            return this;
        }

        public Builder setClient(@NonNull INetworkClient client) {
            this.client = client;
            return this;
        }

        public Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        public Builder setActivity(@NonNull Activity activity) {
            this.activity = activity;
            return this;
        }

        public Builder setHttpType(@NonNull @HttpType int httpType) {
            this.httpType = httpType;
            return this;
        }

        public Builder setOnRequestListener(PaymentListener.OnRequestListener onRequestListener) {
            this.onRequestListener = onRequestListener;
            return this;
        }

        public Builder setOnParseListener(PaymentListener.OnParseListener onParseListener) {
            this.onParseListener = onParseListener;
            return this;
        }

        public Builder setOnPayListener(PaymentListener.OnPayListener onPayListener) {
            this.onPayListener = onPayListener;
            return this;
        }

        public Builder setOnVerifyListener(PaymentListener.OnVerifyListener onVerifyListener) {
            this.onVerifyListener = onVerifyListener;
            return this;
        }

        public Payment build() {
            return new Payment()
                    .setPay(pay)
                    .setParams(params)
                    .setClient(client)
                    .setUrl(url)
                    .setHttpType(httpType)
                    .setActivity(activity)
                    .setOnVerifyListener(onVerifyListener)
                    .setOnRequestListener(onRequestListener)
                    .setOnPayListener(onPayListener)
                    .setOnParseListener(onParseListener);
        }

        public Payment start() {
            return build().start();
        }

        public Payment pay(String result) {
            return build().pay(result);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}

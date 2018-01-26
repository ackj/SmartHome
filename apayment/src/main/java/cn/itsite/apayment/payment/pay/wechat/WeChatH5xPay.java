package cn.itsite.apayment.payment.pay.wechat;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import org.json.JSONException;
import org.json.JSONObject;

import cn.itsite.abase.log.ALog;
import cn.itsite.apayment.payment.PayFragment;
import cn.itsite.apayment.payment.PayParams;
import cn.itsite.apayment.payment.Payment;
import cn.itsite.apayment.payment.PaymentListener;
import cn.itsite.apayment.payment.pay.IPayable;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 微信H5定制版支付策略.
 */

public class WeChatH5xPay implements IPayable {
    private PaymentListener.OnPayListener onPayListener;

    @Override
    public PayParams parse(@NonNull String result) throws JSONException {
        ALog.e("ATG", "微信支付-->" + result);
        JSONObject resultObject = new JSONObject(result);
        JSONObject jsonData = resultObject.optJSONObject("data");
        return new PayParams.Builder()
                .appID(jsonData.optString("appId"))
                .partnerId(jsonData.optString("partnerId"))
                .prePayId(jsonData.optString("prePayId"))
                .packageValue("Sign=WXPay")
                .nonceStr(jsonData.optString("nonceStr"))
                .timeStamp(jsonData.optString("timestamp"))
                .sign(jsonData.optString("sign"))
                .outTradeNo(jsonData.optString("outTradeNo"))
                .webUrl(jsonData.optString("mwebUrl"))
                .build();
    }

    @Override
    public void pay(@NonNull Activity activity, @NonNull PayParams params, PaymentListener.OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
        if (onPayListener != null) {
            onPayListener.onStart(getPayType());
        }
        h5xPay(activity, params);
    }

    @Override
    public int getPayType() {
        return Payment.PAYTYPE_WECHAT_H5X;
    }

    private void h5xPay(@NonNull Activity activity, @NonNull PayParams params) {
        getFragment(activity).pay(params);
    }

    private PayFragment getFragment(Activity activity) {
        if (activity == null) {
            return null;
        }
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
        if (activity == null) {
            return null;
        }
        return (PayFragment) ((FragmentActivity) activity)
                .getSupportFragmentManager().findFragmentByTag(PayFragment.TAG);
    }
}

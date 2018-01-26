package cn.itsite.apayment.payment.pay;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.json.JSONException;

import cn.itsite.apayment.payment.PayParams;
import cn.itsite.apayment.payment.Payment;
import cn.itsite.apayment.payment.PaymentListener;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 支付策略接口
 */

public interface IPayable {

    PayParams parse(@NonNull String result) throws JSONException;

    void pay(@NonNull Activity activity, @NonNull PayParams params, PaymentListener.OnPayListener onPayListener);

    @Payment.PayType
    int getPayType();
}

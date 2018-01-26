package cn.itsite.apayment.payment.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import cn.itsite.apayment.payment.PayParams;
import cn.itsite.apayment.payment.Payment;
import cn.itsite.apayment.payment.PaymentListener;
import cn.itsite.apayment.payment.network.ThreadManager;
import cn.itsite.apayment.payment.pay.IPayable;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 支付宝策略.
 */

public class ALiAppPay implements IPayable {
    public static final int PAY_RESULT = 0;
    public static final String PAY_OK_STATUS = "9000";// 支付成功
    public static final String PAY_WAIT_CONFIRM_STATUS = "8000";// 交易待确认
    public static final String PAY_FAILED_STATUS = "4000";// 交易失败
    public static final String PAY_CANCLE_STATUS = "6001";// 交易取消
    public static final String PAY_NET_ERR_STATUS = "6002";// 网络出错
    public static final String PAY_UNKNOWN_ERR_STATUS = "6004";// 结果未知
    private PaymentListener.OnPayListener onPayListener;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != PAY_RESULT) {
                return;
            }
            ThreadManager.shutdown();
            Map<String, String> result = (Map<String, String>) msg.obj;
            if (TextUtils.equals(PAY_OK_STATUS, result.get("resultStatus"))) {
                if (onPayListener != null) {
                    onPayListener.onSuccess(getPayType());
                }
            } else {
                if (onPayListener != null) {
                    onPayListener.onFailure(getPayType(), Integer.parseInt(result.get("resultStatus")));
                }
            }
            mHandler.removeCallbacksAndMessages(null);
        }
    };


    @Override
    public PayParams parse(@NonNull String result) throws JSONException {
        Log.e("支付宝支付-->", result);
        JSONObject resultObject = new JSONObject(result);
        JSONObject jsonData = resultObject.optJSONObject("data");
        return new PayParams.Builder()
                .orderInfo(jsonData.optString("body"))
                .outTradeNo(jsonData.optString("outTradeNo"))
                .build();
    }

    @Override
    public void pay(@NonNull final Activity activity, @NonNull final PayParams params, PaymentListener.OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
        if (onPayListener != null) {
            onPayListener.onStart(getPayType());
        }

        Runnable payRun = new Runnable() {
            @Override
            public void run() {
                PayTask task = new PayTask(activity);
                // TODO 请根据自身需求解析mPrePayinfo，最终的字符串值应该为一连串key=value形式
                Map<String, String> result = task.payV2(params.getOrderInfo(), true);
                Message message = Message.obtain();
                message.what = PAY_RESULT;
                message.obj = result;
                mHandler.sendMessage(message);
            }
        };
        ThreadManager.execute(payRun);
    }

    @Override
    public int getPayType() {
        return Payment.PAYTYPE_ALI_APP;
    }
}

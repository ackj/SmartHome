package cn.itsite.apayment.payment;


/**
 * @author leguang
 * @version v0.0.0
 * @E-mail langmanleguang@qq.com
 * @time 2018/1/12 0012 9:00
 */
public interface PaymentListener {
    interface OnRequestListener {
        void onStart();

        void onSuccess(String result);

        void onError(int errorCode);
    }

    interface OnParseListener {
        void onStart(String result);

        void onSuccess(PayParams params);

        void onError(int errorCode);
    }

    interface OnPayListener {
        void onStart(@Payment.PayType int payType);

        void onSuccess(@Payment.PayType int payType);

        void onFailure(@Payment.PayType int payType, int errorCode);
    }

    interface OnVerifyListener {
        void onStart();

        void onSuccess();

        void onFailure(int errorCode);
    }
}

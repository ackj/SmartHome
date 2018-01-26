package cn.itsite.apayment.payment.network;


import java.util.Map;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description 网络访问接口.
 */

public interface INetworkClient {
    interface CallBack<R> {
        void onSuccess(R result);

        void onFailure();
    }

    void get(String url, Map<String, String> params, CallBack callBack);

    void post(String url, Map<String, String> params, CallBack callBack);
}

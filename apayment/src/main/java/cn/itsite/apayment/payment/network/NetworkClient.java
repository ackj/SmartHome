package cn.itsite.apayment.payment.network;


/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description Description: 网络访问接口简单工厂.
 */

public class NetworkClient {

    public static INetworkClient httpUrlConnection() {
        return new HttpUrlConnectionClient();
    }

    public static INetworkClient volley() {
        return new VolleyClient();
    }

    public static INetworkClient retrofit() {
        return new RetrofitClient();
    }

    public static INetworkClient okhttp() {
        return new OkHttpClient();
    }
}

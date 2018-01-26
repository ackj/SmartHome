package cn.itsite.abase.network.http;


import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.itsite.abase.BaseApp;
import cn.itsite.abase.log.ALog;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Author：leguang on 2016/10/9 0009 15:49
 * Email：langmanleguang@qq.com
 */
public class LogInterceptor implements Interceptor {
    private static final String TAG = LogInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        if (request.method().equals("POST")) {
            if (request.body() instanceof FormBody) {
                FormBody.Builder bodyBuilder = new FormBody.Builder();
                FormBody formBody = (FormBody) request.body();

                //把原来的参数添加到新的构造器，（因为没找到直接添加，所以就new新的）
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }

                formBody = bodyBuilder
                        .addEncoded("fc", "FSmartMeilun")
                        .build();
                request = request.newBuilder().post(formBody).build();
            }
        }

        Buffer buffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(buffer);
        }

        ALog.e(String.format("Sending request %s on %s%n%sRequest Params: %s",
                request.url(), chain.connection(), request.headers(), buffer.clone().readUtf8()));
        buffer.close();

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE);
        buffer = source.buffer().clone();
        try {
            JSONObject jsonObject = new JSONObject(buffer.readUtf8());
            JSONObject jsonOther = jsonObject.optJSONObject("other");
            ALog.e(String.format("Received response for %s%nin %.1fms%n%sResponse Json: %s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers(),
                    jsonObject.toString()));

            ALog.json(jsonObject.toString());

            int code = jsonOther.optInt("code");
            if (code == 123) {
                ALog.e("code-->" + code);
                Intent intent = new Intent("com.meilun.security.smart.login.LoginActivity");
                //不添加这个Flag则会报如下错误：Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                BaseApp.mContext.startActivity(intent);
                EventBus.getDefault().post(new LogInterceptor());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        buffer.close();
        return response;
    }
}

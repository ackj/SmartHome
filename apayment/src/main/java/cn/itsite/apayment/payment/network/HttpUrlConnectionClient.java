package cn.itsite.apayment.payment.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import cn.itsite.abase.log.ALog;


/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:15
 */

public class HttpUrlConnectionClient implements INetworkClient {
    public static final int CONNECTTIMEOUT = 20 * 1000;
    public static final int READTIMEOUT = 10 * 1000;

    public void get(final String url, final Map<String, String> params, final CallBack callBack) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                try {
                    StringBuffer sbUrl = new StringBuffer(url).append("?");

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sbUrl.append(entry.getKey())
                                .append("=")
                                .append(entry.getValue())
                                .append("&");
                    }

                    ALog.e("sbUrl.toString()-->" + sbUrl.toString());

                    sbUrl.deleteCharAt(sbUrl.length() - 1);
                    ALog.e("sbUrl.toString()-->" + sbUrl.toString());

//                    sbUrl.append("?")
//                            .append("token=").append(payParams.getToken())
//                            .append("&")
//                            .append("billFids=").append(payParams.getBillFids())
//                            .append("&")
//                            .append("payMethod=").append(payParams.getPayMethod())
//                            .append("&")
//                            .append("jumpUrl=").append(payParams.getJumpUrl())
//                            .append("&")
//                            .append("quitUrl=").append(payParams.getQuitUrl())
//                            .append("&")
//                            .append("payUrl=").append(payParams.getPayMethod())
//                            .append("&")
//                            .append("code=").append(payParams.getCode());


                    connection = (HttpURLConnection) new URL(sbUrl.toString()).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(CONNECTTIMEOUT);
                    connection.setReadTimeout(READTIMEOUT);
                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = connection.getInputStream();
                        byte[] data = new byte[512];
                        int len = 0;
                        StringBuffer sb = new StringBuffer();
                        while ((len = inputStream.read(data)) > 0) {
                            sb.append(new String(data, 0, len));
                        }
                        callBack.onSuccess(sb.toString());
                    } else {
                        callBack.onFailure();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onFailure();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        ThreadManager.execute(command);
    }

    public void post(final String url, final Map<String, String> params, final CallBack callBack) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(CONNECTTIMEOUT);
                    connection.setReadTimeout(READTIMEOUT);
                    connection.setDoOutput(true);

                    outputStream = connection.getOutputStream();
                    StringBuffer sbUrl = new StringBuffer();

                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        sbUrl.append(entry.getKey())
                                .append("=")
                                .append(entry.getValue())
                                .append("&");
                    }

                    ALog.e("sbUrl.toString()-->" + sbUrl.toString());

                    sbUrl.deleteCharAt(sbUrl.length() - 1);
                    ALog.e("sbUrl.toString()-->" + sbUrl.toString());


                    outputStream.write(sbUrl.toString().getBytes());
                    outputStream.flush();
                    connection.connect();
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = connection.getInputStream();
                        byte[] data = new byte[512];
                        int len = 0;
                        StringBuffer sb = new StringBuffer();
                        while ((len = inputStream.read(data)) > 0) {
                            sb.append(new String(data, 0, len));
                        }
                        callBack.onSuccess(sb.toString());
                    } else {
                        callBack.onFailure();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callBack.onFailure();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        ThreadManager.execute(command);
    }
}

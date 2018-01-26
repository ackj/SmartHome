package cn.itsite.apayment.payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.itsite.abase.log.ALog;
import cn.itsite.apayment.payment.network.INetworkClient;
import cn.itsite.apayment.payment.network.PayService;

/**
 * @author leguang
 * @version v0.0.0
 * @E-mail langmanleguang@qq.com
 * @time 2017/5/20 0020 10:56
 * 支付返回结果Bean。
 */

public class PayFragment extends Fragment {
    public static final String TAG = PayFragment.class.getSimpleName();
    private PayParams payParams;
    private WebView webView;
    private Payment payment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        ALog.e("fragment onCreate ——----------------------");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ALog.e("fragment onCreateView ——----------------------");
        webView = new WebView(getContext());
        webView.setLayoutParams(new ViewGroup.LayoutParams(1, 1));
        webView.setVisibility(View.INVISIBLE);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                ALog.e("url-->" + url);

                // 如下方案可在非微信内部WebView的H5页面中调出微信支付
                if (url.startsWith("weixin://wap/pay?")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                return false;
            }
        };
        webView.setWebViewClient(webViewClient);
        return webView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.e("fragment onViewCreated ——----------------------");

    }

    public void setPayment(Payment payment) {
        ALog.e("payment-->" + payment);

        this.payment = payment;
    }

    public void setParams(PayParams payParams) {
        this.payParams = payParams;
    }

    @Override
    public void onStart() {
        super.onStart();

        ALog.e("fragment onStart—----------------------");

    }


    @Override
    public void onResume() {
        super.onResume();
        ALog.e("fragment onResume—----------------------");
        ALog.e("payment-->" + payment);
        if (payParams != null && payment != null) {
            PaymentListener.OnVerifyListener onVerifyListener = payment.getOnVerifyListener();
            PaymentListener.OnPayListener onPayListener = payment.getOnPayListener();
            if (onVerifyListener != null) {
                onVerifyListener.onStart();
                requestVerify(onVerifyListener, onPayListener);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        ALog.e("fragment onPause—----------------------");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ALog.e("fragment onDestroyView—----------------------");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.e("fragment onDestroy—----------------------");

    }

    public void pay(PayParams params) {
        this.payParams = params;
        ALog.e("params.getWebUrl()-->" + params.getWebUrl());
        Map<String, String> extraHeaders = new HashMap<>();
        extraHeaders.put("Referer", "http://www.aglhz.com");
        webView.loadUrl(params.getWebUrl(), extraHeaders);
    }

    private void requestVerify(final PaymentListener.OnVerifyListener onVerifyListener, final PaymentListener.OnPayListener onPayListener) {
        HashMap<String, String> params = new HashMap<>();
        params.put("outTradeNo", payParams.getOutTradeNo());
        payment.getClient().post(PayService.requestPayResult, params, new INetworkClient.CallBack<String>() {
            @Override
            public void onSuccess(String result) {
                ALog.e("result-->" + result);
                JSONObject jsonObject;
                boolean isPayed = false;
                try {
                    jsonObject = new JSONObject(result);
                    isPayed = jsonObject.optJSONObject("data").optBoolean("tradeState");
                } catch (Exception e) {
                    e.printStackTrace();
                    onVerifyListener.onFailure(Payment.VERIFY_ERROR);
                }
                onVerifyListener.onSuccess();
                if (isPayed) {
                    onPayListener.onSuccess(payment.getPay().getPayType());
                } else {
                    onPayListener.onFailure(payment.getPay().getPayType(), Payment.VERIFY_ERROR);
                }
            }

            @Override
            public void onFailure() {
                ALog.e("确认失败-->");
                onVerifyListener.onFailure(Payment.VERIFY_ERROR);
            }
        });
    }
}

package cn.itsite.apayment.payment.network;

import java.util.Map;

import cn.itsite.apayment.payment.PayParams;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:17
 * @Description Description: volley网络请求封装.
 */

public class VolleyClient implements INetworkClient {

    @Override
    public void post(String url, Map<String, String> params, CallBack callBack) {
//        RequestQueue queue = Volley.newRequestQueue(payParams.getActivity());
//        String baseUrl = payParams.getApiUrl();
//        StringBuffer sburl = new StringBuffer();
//        sburl.append(baseUrl)
//                .append("?")
//                .append("pay_way=").append(payParams.getPayWay())
//                .append("&")
//                .append("price=").append(payParams.getGoodsPrice())
//                .append("&")
//                .append("goods_name=").append(payParams.getGoodsName())
//                .append("&")
//                .append("goods_introduction=").append(payParams.getGoodsIntroduction());
//        StringRequest request = new StringRequest(Request.Method.GET, sburl.toString(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        c.onSuccess(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                c.onFailure();
//            }
//        });
//        queue.add(request);
    }

    @Override
    public void get(String url, Map<String, String> params, CallBack callBack) {
//        RequestQueue queue = Volley.newRequestQueue(payParams.getActivity());
//        StringRequest request = new StringRequest(Request.Method.POST, payParams.getApiUrl(),
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        c.onSuccess(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                c.onFailure();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("pay_way", payParams.getPayWay().toString());
//                params.put("price", String.valueOf(payParams.getGoodsPrice()));
//                params.put("goods_name", payParams.getGoodsName());
//                params.put("goods_introduction", payParams.getGoodsIntroduction());
//
//                return params;
//            }
//        };
//
//        queue.add(request);
    }
}

package cn.itsite.apayment.payment.pay;

import cn.itsite.apayment.payment.pay.alipay.ALiAppPay;
import cn.itsite.apayment.payment.pay.union.UpiAppPay;
import cn.itsite.apayment.payment.pay.wechat.WeChatAppPay;
import cn.itsite.apayment.payment.pay.wechat.WeChatH5xPay;

/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 15:18
 * @Description
 */
public class Pay {

    /******************以下为支付宝支付***********************/
    public static IPayable aliAppPay() {
        return new ALiAppPay();
    }
    /******************以上为支付宝支付***********************/


    /******************以下为微信支付***********************/
    public static IPayable weChatAppPay() {
        return new WeChatAppPay();

    }

    public static IPayable weChatH5xPay() {
        return new WeChatH5xPay();
    }
    /******************以上为微信支付***********************/


    /******************以下为银联支付***********************/
    public static IPayable upiAppPay() {
        return new UpiAppPay();

    }
    /******************以上为银联支付*************************/

}

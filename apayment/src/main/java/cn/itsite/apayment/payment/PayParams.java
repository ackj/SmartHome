package cn.itsite.apayment.payment;


/**
 * @version v0.0.0
 * @Author leguang
 * @E-mail langmanleguang@qq.com
 * @Blog https://github.com/leguang
 * @Time 2018/1/12 0012 11:15
 * Description: 支付相关参数.
 */

public class PayParams {
    private String appID;
    private String partnerId;
    private String prePayId;
    private String packageValue;
    private String nonceStr;
    private String timeStamp;
    private String sign;
    private String outTradeNo;
    private String orderInfo;
    private String webUrl;

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrePayId() {
        return prePayId;
    }

    public void setPrePayId(String prePayId) {
        this.prePayId = prePayId;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Override
    public String toString() {
        return "PayParams{" +
                "appID='" + appID + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prePayId='" + prePayId + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", nonceStr='" + nonceStr + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", sign='" + sign + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", orderInfo='" + orderInfo + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }

    public static class Builder {
        String appID;
        String partnerId;
        String prePayId;
        String packageValue;
        String nonceStr;
        String timeStamp;
        String sign;
        String outTradeNo;
        String orderInfo;
        String webUrl;

        public Builder appID(String appid) {
            this.appID = appid;
            return this;
        }

        public Builder partnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public Builder prePayId(String prePayId) {
            this.prePayId = prePayId;
            return this;
        }

        public Builder packageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }

        public Builder nonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        public Builder timeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public Builder sign(String sign) {
            this.sign = sign;
            return this;
        }

        public Builder outTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
            return this;
        }

        public Builder orderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
            return this;
        }

        public Builder webUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        public PayParams build() {
            PayParams params = new PayParams();
            params.setAppID(appID);
            params.setPartnerId(partnerId);
            params.setPrePayId(prePayId);
            params.setPackageValue(packageValue);
            params.setNonceStr(nonceStr);
            params.setTimeStamp(timeStamp);
            params.setSign(sign);
            params.setOutTradeNo(outTradeNo);
            params.setOrderInfo(orderInfo);
            params.setWebUrl(webUrl);
            return params;
        }
    }
}
